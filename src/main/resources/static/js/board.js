let body = $("#body");
let boardId = body.data("id");
let token = body.data("csrf");

function ajax(url, type, body = {}, success) {
    $.ajax({
        url: url,
        headers: {"X-CSRF-TOKEN": token},
        type: type,
        body: body,
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
                    "<div class=\"user\">" +
                    "<div class=\"name-small\" data-id=\"" + data[i].id + "\">" + data[i].name + "</div>" +
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

users.on("click", function (ev) {
    let element = ev.target;
    if (element.dataset.id !== undefined) {
        let userId = element.dataset.id;
        users.empty();
        query.val("");
        users.attr("class", "users shadow hidden");
        ajax('/api/boards/' + boardId + "/members", "POST", {
            invited_user_id: userId
        });
    }
});

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
    let title = stackTitle.val().trim();
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
                "<div draggable=\"true\" ondragstart=\"drag(event)\" class=\"card shadow last-card\" data-card-id=\"" + card.id + "\">" +
                "   <div class=\"card-title\">" + card.title + "</div>" +
                "</div>");
            title.val("");
        });
    }
});

let card = $("#card");

function openCard(card) {
    card.attr("class", "popup");
    console.log(card.dataset.cardId);
}

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
    ajax('/api/cards/' + cardId + "/action=move", "PUT", {
            stack_id: block.dataset.containerStackId,
            board_id: boardId,
        },
        function () {
            let attr = "[data-card-id=" + cardId + "]";
            $(attr).remove();
            block.innerHTML += "<div draggable=\"true\" onclick='openCard(this)' ondragstart=\"drag(event)\" class=\"card shadow last-card\" data-card-id=\"" + cardId + "\">" +
                "   <div class=\"card-title\">" + cardTitle + "</div>" +
                "</div>";
        });
}

let map = document.getElementById("map"),
    lat = 56,
    lng = 58;

function initMap() {
    let coordinates = {lat: lat, lng: lng};
    map = new google.maps.Map(map, {
        center: coordinates,
        zoom: 16
    });
    let marker = new google.maps.Marker({
        position: coordinates,
        map: map,
        icon: "http://localhost:8080/image/online.png"
    });

    map.addListener('click', function (data) {
        let lat = data.latLng.lat();
        let lng = data.latLng.lng();
        marker.setPosition({lat: lat, lng: lng});
    });
}