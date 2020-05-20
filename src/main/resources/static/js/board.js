let body = $("#body");
let boardId = body.data("id");
let token = body.data("csrf");

function ajax(url, type, body, success) {
    $.ajax({
        url: url,
        headers: {"X-CSRF-TOKEN": token},
        type: type,
        data: body,
        success: function (data) {
            if (success !== undefined) {
                success(data);
            }
        }
    });
}

let webSocket = new SockJS("http://localhost:8080/chat");
webSocket.onopen = function () {
    webSocket.send(JSON.stringify({
        header: "room",
        payload: {
            boardId: boardId
        }
    }));
};

webSocket.onmessage = function (data) {
    let message = JSON.parse(data.data).payload;
    let html = "<div class=\"light-message\">" +
        "<div class=\"name\">" + message.name + "</div>" + message.text +
        "</div>";
    messages.append(html);
};


let query = $("#query");
let search = $("#search");
let users = $("#users");

search.on("click", function () {
    if (query.val().length !== 0) {
        ajax('/api/users?name=' + query.val(), 'GET', {}, function (data) {
            users.empty();
            users.attr("class", "users shadow");
            for (let i = 0; i < data.length; i++) {
                users.append(
                    "<div class='user'>" +
                    "<div class='name-small' onclick='addUserToBoard(" + data[i].id + ', \"' + data[i].name + "\")'>" + data[i].name + "</div>" +
                    "</div>"
                );
            }
        });
    } else {
        query.val("");
        users.attr("class", "users shadow hidden");
        users.empty();
    }
});

function addUserToBoard(userId, userName) {
    users.empty();
    query.val("");
    users.attr("class", "users shadow hidden");
    ajax('/api/boards/' + boardId + "/members", "POST", {
        invited_user_id: userId
    }, function (data) {
        let attr = "[data-member-id=" + userId + "]";
        let isNotExist = $(attr).length === 0;
        if (isNotExist) {
            let html =
                "<div class=\"member\" data-container-member-id='" + userId + " '>" +
                "     <div class=\"member-info\">" +
                "         <div class=\"name\">" + userName + "</div>" +
                "     </div>" +
                "     <div class=\"delete\">" +
                "         <i class=\"fas fa-times\"\n" +
                "            data-member-id='" + userId + "' onclick='deleteMember(" + userId + ")'></i>" +
                "     </div>" +
                "</div>";
            memberList.append(html);
        }
    });
}

let searchBox = $("#search-box");
let addUser = $("#add-member");
let toggle = false;
addUser.on("click", function () {
    if (!toggle) {
        searchBox.show();
    } else {
        searchBox.hide();
    }
    toggle = !toggle;
});

let send = $("#send");
let message = $("#message");
let messages = $("#messages");
send.on("click", function () {
    let text = message.val().trim();
    if (text.length === 0) return;
    webSocket.send(JSON.stringify({
        header: "message",
        payload: {
            text: text,
            roomId: boardId
        }
    }));
    message.val("");
    messages.append("<div class=\"blue-message\">" + text + "</div>");
});

let addStack = $("#add-stack");
let stackTitle = $("#stack-title");
let lists = $("#lists");
addStack.on("click", function () {
    let title = stackTitle.val();
    if (title.length === 0) return;
    ajax('/api/boards/' + boardId + "/stacks", "POST", {
        title: title
    }, function (stack) {
        stackTitle.val("");
        let html =
            "<div class=\"list-wrapper " + (lists.children().length === 1 ? "last-list" : "") + "\">" +
            "   <div class=\"list\">" +
            "       <div class=\"list-title\">" + stack.title + "</div>" +
            "       <div class=\"cards\" data-container-stack-id=\"" + stack.id + "\" ondragover=\"allowDrag(event)\" ondrop=\"drop(event, this)\">" +
            "           <div data-stack-id=\"" + stack.id + "\" class=\"card shadow last-card new-card\">" +
            "               <input type=\"text\" placeholder=\"Title\" class=\"input\" data-title-stack-id=\"" + stack.id + "\">" +
            "               <i class=\"fas fa-check\" data-add-stack-id=\"" + stack.id + "\"></i>" +
            "           </div>" +
            "       </div>" +
            "    </div>" +
            "</div>";
        $("#first-list").after(html);
    });
});

lists.on("click", function (event) {
    if (event.target.dataset.addStackId !== undefined) {
        let attr = "[data-title-stack-id=" + event.target.dataset.addStackId + "]";
        let title = $(attr);
        ajax('/api/stacks/' + event.target.dataset.addStackId + '/cards', "POST", {
            title: title.val(),
        }, function (card) {
            let attrFirstCard = "[data-stack-id=" + event.target.dataset.addStackId + "]";
            let firstCard = $(attrFirstCard);
            firstCard.after(
                "<div draggable=\"true\" onclick=\"openCard(this)\" ondragstart=\"drag(event)\" class=\"card shadow last-card\" data-card-id=\"" + card.id + "\">" +
                "   <div class=\"card-title\">" + card.title + "</div>" +
                "</div>");
            title.val("");
        });
    }
});

function allowDrag(ev) {
    ev.preventDefault();
}

function drag(ev) {
    ev.dataTransfer.setData("card-id", ev.target.dataset.cardId);
    ev.dataTransfer.setData("card-title", ev.target.getElementsByClassName("card-title")[0].innerHTML);
}

function drop(ev, block) {
    ev.preventDefault();
    let cardId = ev.dataTransfer.getData("card-id");
    let cardTitle = ev.dataTransfer.getData("card-title");
    ajax('/api/boards/' + boardId + '/stacks/' + block.dataset.containerStackId + '/cards/' + cardId, "PUT", {},
        function () {
            let attr = "[data-card-id=" + cardId + "]";
            $(attr).remove();
            block.innerHTML += "<div draggable=\"true\" onclick='openCard(this)' ondragstart=\"drag(event)\" class=\"card shadow last-card\" data-card-id=\"" + cardId + "\">" +
                "   <div class=\"card-title\">" + cardTitle + "</div>" +
                "</div>";
        });
}

let members = $("#members");
let memberList = $("#members-list");
let membersToggle = false;
members.on("click", function () {
    if (!membersToggle) {
        memberList.show();
    } else {
        memberList.hide();
    }
    membersToggle = !membersToggle;
});

function deleteMember(userId) {
    ajax("/api/boards/" + boardId + "/members", "DELETE", {
        deleted_user_id: userId
    }, function () {
        let attr = "[data-container-member-id=" + userId + "]";
        let memberContainer = $(attr);
        memberContainer.remove();
    });
}

let archive = $("#archive");
let archiveToggle = false;
let archiveList = $("#archivedCardList");
archive.on("click", function () {
    if (!archiveToggle) {
        archiveList.show();
    } else {
        archiveList.hide();
    }
    archiveToggle = !archiveToggle;
});