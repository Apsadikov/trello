let popup = $("#popup");
let currentCardId;
let currentCard;
let archivedCardList = $("#archivedCardList");
let membersArr = [];

function openCard(card) {
    cardMembers.empty();
    membersHint.empty();
    comments.empty();
    commentText.val("");
    files.empty();
    membersArr = [];
    findMembers();
    currentCard = card;
    currentCardId = card.dataset.cardId;
    ajax("/api/cards/" + currentCardId, "GET", {}, function (data) {
        setData(data);
    });
}

function closeCard() {
    popup.hide();
    mapWrapper.hide();
}

let title = $("#title");
let comments = $("#comments");

function setData(data) {
    console.log(data);
    title.text(() => data.title);
    description.val(data.description);
    if (data.deadline !== null) {
        deadline.val(data.deadline.substring(0, 10));
    }
    if (data.map == null) {
        mapWrapper.hide();
    } else {
        marker.setPosition({lat: data.map.latitude, lng: data.map.longitude});
    }

    for (let i = 0; i < data.files.length; i++) {
        files
            .append("<div><a href='http://localhost:8080/api/files?file=" + data.files[i].file + "'>File: " + data.files[i].file + "</a></div>");
    }

    for (let i = 0; i < membersArr.length; i++) {
        let user = data.cardMembers.filter(member => member.userId == membersArr[i].id)[0];
        let html =
            "<div class='member' onclick='addCardMember(this)' data-card-member-id='" + membersArr[i].id + "'>" +
            "    <div class='member-info'>" +
            "        <div class='name'>" + membersArr[i].name + "</div>" +
            "    </div>" +
            "</div>";
        if (user !== undefined) {
            html =
                "<div class='member'>" +
                "    <div class='member-info'>" +
                "        <div class='name'>" + membersArr[i].name + "</div>" +
                "    </div>" +
                "    <div class='delete'>" +
                "        <i class='fas fa-check'></i>" +
                "    </div>" +
                "</div>";
        }
        cardMembers.append(html);
    }

    for (let i = 0; i < membersArr.length; i++) {
        membersHint.append("<div class='member-id'><div>ID: " + membersArr[i].id + ", " + membersArr[i].name + "</div></div>");
    }

    for (let i = 0; i < data.comments.length; i++) {
        let comment = resolveMentions(data.comments[i]);
        let html =
            "<div class='light-comment-wrapper'>" +
            "<div class='light-comment'>" +
            "    <div class='name'>" + comment.name + "</div>" + comment.message + "</div>" +
            "</div>";
        comments.append(html);
    }
    popup.show();
}

function addToArchive() {
    ajax("/api/archive", "PUT", {
        card_id: currentCardId
    }, function () {
        closeCard();
        currentCard.remove();
        let title = currentCard.childNodes[1].innerHTML;
        let html =
            "<div class='archive-card'>" +
            "    <div data-card-id='" + currentCard.dataset.cardId + "' onclick='openCard(this)'" +
            "         class='archive-card-title'>" + title + "</div>" +
            "</div>";
        archivedCardList.append(html);
    });
}

let deadline = $("#deadline");
let description = $("#description");
description.bind('input propertychange', function () {
    ajax("/api/cards/" + currentCardId + "/description", "PUT", {
        description: description.val()
    });
});

deadline.bind('input propertychange', function () {
    ajax("/api/cards/" + currentCardId + "/deadline", "PUT", {
        deadline: deadline.val()
    });
});

let map = document.getElementById("map");
let mapWrapper = $("#map-wrapper");
let marker;

function openMap() {
    mapWrapper.show();
}

function initMap() {
    let cord = {lat: 41, lng: 1};
    map = new google.maps.Map(map, {
        center: cord,
        zoom: 16
    });

    marker = new google.maps.Marker({
        position: cord,
        map: map,
        icon: "http://localhost:8080/image/online.png"
    });

    map.addListener('click', function (data) {
        let lat = data.latLng.lat();
        let lng = data.latLng.lng();
        marker.setPosition({lat: lat, lng: lng});
        ajax("/api/cards/" + currentCardId + "/map", "PUT", {
            longitude: lng,
            latitude: lat
        });
    });
}

let commentText = $("#comment-text");
let sendComment = $("#send-comment");
let membersHint = $("#members-hint");
commentText.bind('input propertychange', function () {
    if (commentText.val().slice(-1) == "@") {
        membersHint.show();
    } else {
        membersHint.hide();
    }
});

sendComment.on("click", function () {
    console.log(commentText.val().trim().length);
    if (commentText.val().trim().length !== 0) {
        ajax("/api/cards/" + currentCardId + "/comment", "POST", {
            comment: commentText.val().trim()
        }, function success() {
            let comment = resolveMentions({
                userId: document.getElementById("body").dataset.userId,
                message: commentText.val().trim()
            });
            let html =
                "<div class='light-comment-wrapper'>" +
                "<div class='light-comment'>" +
                "    <div class='name'>" + comment.name + "</div>" + comment.message + "</div>" +
                "</div>";
            comments.append(html);
            commentText.val("");
        });
    }
});

let cardMembers = $("#card-members");

function findMembers() {
    let list = document.getElementById("members-list");
    let memberNames = list.getElementsByClassName("name");
    let ids = list.getElementsByClassName("fa-times");
    for (let i = 0; i < memberNames.length; i++) {
        membersArr.push({
            name: memberNames[i].innerHTML,
            id: ids[i].dataset.memberId
        })
    }
}

function addCardMember(block) {
    ajax("/api/cards/" + currentCardId + "/members", "POST", {
        invited_user_id: block.dataset.cardMemberId
    }, function () {
        block.innerHTML += "<div class='delete'><i class='fas fa-check'></i></div>";
    });
}

function resolveMentions(text) {
    for (let i = 0; i < membersArr.length; i++) {
        if (text.userId == membersArr[i].id) {
            text.name = membersArr[i].name;
        }
        text.message = text.message.replace("@id" + membersArr[i].id, "<span class='mention'>" + membersArr[i].name + "</span>");
    }
    return text;
}

let file = $("#file");
let upload = $("#upload");
upload.on("click", function () {
    file.trigger("click");
});

let files = $("#files-list");
file.on("change", function () {
    let formdata = new FormData();
    formdata.append("file", file.prop("files")[0]);
    formdata.append("card_id", currentCardId);
    $.ajax({
        url: '/api/files',
        type: 'POST',
        headers: {"X-CSRF-TOKEN": token},
        data: formdata,
        contentType: false,
        cache: false,
        processData: false,
        success: function (response) {
            files.append("<div><a href='http://localhost:8080/api/files?file=" + response + "'>File: " + response + "</a></div>");
        }
    });
});