<#import "/spring.ftl" as spring/>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
    <link rel="stylesheet" href="/css/reset.css">
    <link rel="stylesheet" href="/css/base.css">
    <link rel="stylesheet" href="/css/board.css">
    <link rel="stylesheet" href="/fontawesome/css/all.min.css">
</head>
<body id="body" data-csrf="${_csrf.token}" data-id="${board.getId()}" data-user="${userId}">
<div class="wrapper">
    <div class="header-wrapper">
        <div class="header-small">
            <div class="app-name"><a href="/" class="white">Trello</a></div>
            <div class="current-language">
                <div id="language">
                <@spring.message code='lang' />
                </div>
                <div class="languages shadow" id="languages">
                    <a href="?lang=ru" class="language">Русский</a>
                    <a href="?lang=en" class="language">English</a>
                </div>
            </div>
            <div class="link"><a href="/logout" class="white"><i
                    class="fas fa-sign-out-alt"></i> logout</a></div>
        </div>
        <div class="header-chat">CHAT</div>
    </div>
    <div class="main">
        <div class="board">
            <div class="board-header shadow">
                <div class="board-title">${board.getTitle()}</div>
                <div class="add-member" id="add-member">
                    <i class="fas fa-user-plus"></i>
                    Add member
                </div>
                <div class="add-member">
                    <i class="fas fa-users"></i>
                    Members
                </div>
                <div class="add-member">
                    <i class="fas fa-archive"></i>
                    Archive
                </div>
            </div>
            <div class="search-box hidden" id="search-box">
                <div class="search-wrapper shadow">
                    <input type="text" id="query" class="search" placeholder="Enter name..">
                    <i class="fas fa-search" id="search"></i>
                </div>
                <div class="result">
                    <div class="users shadow hidden" id="users">
                    </div>
                </div>
            </div>
            <div class="members-list-wrapper">
                <div class="members-list shadow">
                    <div class="member">
                        <div class="name">Ivan</div>
                        <div class="email">ivan@gmail.com</div>
                    </div>
                    <div class="member">
                        <div class="name">Ivan</div>
                        <div class="email">ivan@gmail.com</div>
                    </div>
                    <div class="member">
                        <div class="name">Ivan</div>
                        <div class="email">ivan@gmail.com</div>
                    </div>
                    <div class="member">
                        <div class="name">Ivan</div>
                        <div class="email">ivan@gmail.com</div>
                    </div>
                </div>
            </div>
            <div class="members-list-wrapper">
                <div class="members-list shadow">
                    <div class="member">
                        <div class="name">Ivan</div>
                        <div class="email">ivan@gmail.com</div>
                    </div>
                    <div class="member">
                        <div class="name">Ivan</div>
                        <div class="email">ivan@gmail.com</div>
                    </div>
                    <div class="member">
                        <div class="name">Ivan</div>
                        <div class="email">ivan@gmail.com</div>
                    </div>
                    <div class="member">
                        <div class="name">Ivan</div>
                        <div class="email">ivan@gmail.com</div>
                    </div>
                </div>
            </div>
            <div class="archive-list-wrapper">
                <div class="archive-list shadow">
                    <div class="archive-card">
                        <div class="archive-card-title">Title of card</div>
                    </div>
                    <div class="archive-card">
                        <div class="archive-card-title">Title of card</div>
                    </div>
                    <div class="archive-card">
                        <div class="archive-card-title">Title of card</div>
                    </div>
                    <div class="archive-card">
                        <div class="archive-card-title">Title of card</div>
                    </div>
                </div>
            </div>
            <div class="lists" id="lists">
                <div id="first-list" class="list-wrapper first-list">
                    <div class="new-list">
                        <div class="new-list-form">
                            <input type="text" class="input" placeholder="Title.." id="stack-title">
                            <i class="fas fa-check" id="add-stack"></i>
                        </div>
                    </div>
                </div>
                <#list stacks as stack>
                     <div class="list-wrapper <#if !stack?has_next>last-list</#if>" id="lists">
                         <div class="list">
                             <div class="list-title">${stack.getTitle()}</div>
                             <div class="cards" data-container-stack-id="${stack.getId()}" ondragover="allowDrag(event)"
                                  ondrop="drop(event, this)">
                                 <div data-stack-id="${stack.getId()}" class="card shadow last-card new-card">
                                     <input type="text" placeholder="Title" class="input"
                                            data-title-stack-id="${stack.getId()}">
                                     <i class="fas fa-check" data-add-stack-id="${stack.getId()}"></i>
                                 </div>
                                 <#list stack.getCards() as card>
                                     <div onclick="openCard(this)" draggable="true" data-card-id="${card.getId()}"
                                          ondragstart="drag(event)"
                                          class="card shadow last-card">
                                         <div class="card-title">${card.getTitle()}</div>
                                     </div>
                                 </#list>
                             </div>
                         </div>
                     </div>
                </#list>
            </div>
        </div>
        <div class="chat">
            <div class="messages" id="messages">
                <#list messages as message>
                <#if message.getUserDto().getId() == userId>
                    <div class="blue-message">${message.getMessage()}</div>
                <#else>
                <div class="light-message">
                    <div class="name">${message.getUserDto().getName()}</div>
                    ${message.getMessage()}
                </div>
                </#if>
                </#list>
            </div>
            <div class="message-box">
                <input type="text" class="text" placeholder="Message.." id="message">
                <div class="button"><i class="fas fa-paper-plane" id="send"></i></div>
            </div>
        </div>
    </div>
</div>
<div class="popup hidden" id="card">
    <div class="card-detailed">
        <div class="card-header">
            <div class="title-wrapper">
                <div class="title">
                    Title
                </div>
                <div class="edit">
                    <i class="fas fa-pen"></i>
                </div>
            </div>
            <div class="comments-header">
                Comments
            </div>
        </div>
        <div class="main">
            <div class="card-details-wrapper">
                <div class="card-details">
                    <div class="description-title">Description</div>
                    <div class="description">
                        Lorem ipsum dolor sit amet, consectetur adipiscing elit. In ac velit purus. Morbi et odio dui.
                        Aliquam erat volutpat. Pellentesque vel quam id nisl volutpat tincidunt bibendum nec ipsum.
                        Interdum et malesuada fames ac ante ipsum primis in faucibus. Nullam euismod blandit rhoncus.
                        Duis vehicula, enim vel ullamcorper pretium, neque augue porta magna, a finibus nisl felis in
                        orci. Donec at pulvinar justo.
                    </div>
                </div>
                <div id="map" class="map"></div>
            <#--<div class="tools">-->
            <#--<div class="tool"><i class="far fa-clock"></i> Deadline</div>-->
            <#--<div class="tool"><i class="fas fa-user"></i> Member</div>-->
            <#--<div class="tool"><i class="fas fa-folder-open"></i> Attachments</div>-->
            <#--<div class="tool"><i class="fas fa-list"></i> Checklists</div>-->
            <#--</div>-->
            <#--<div class="members-wrapper">-->
            <#--<div class="members-title">Members</div>-->
            <#--</div>-->
            </div>
            <div class="comments-wrapper">
                <div class="comments">
                    <div class="light-comment-wrapper">
                        <div class="light-comment">
                            <div class="name">User</div>
                            Lorem ipsum dolor sit amet, consectetur adipiscing elit.
                        </div>
                    </div>
                    <div class="light-comment-wrapper">
                        <div class="light-comment">
                            <div class="name">User</div>
                            Lorem ipsum dolor sit amet, consectetur adipiscing elit.
                        </div>
                    </div>
                    <div class="light-comment-wrapper">
                        <div class="light-comment">
                            <div class="name">User</div>
                            Lorem ipsum dolor sit amet, consectetur adipiscing elit.
                        </div>
                    </div>
                    <div class="light-comment-wrapper">
                        <div class="light-comment">
                            <div class="name">User</div>
                            Lorem ipsum dolor sit amet, consectetur adipiscing elit.
                        </div>
                    </div>
                    <div class="light-comment-wrapper">
                        <div class="light-comment">
                            <div class="name">User</div>
                            Lorem ipsum dolor sit amet, consectetur adipiscing elit.
                        </div>
                    </div>
                    <div class="light-comment-wrapper">
                        <div class="light-comment">
                            <div class="name">User</div>
                            Lorem ipsum dolor sit amet, consectetur adipiscing elit.
                        </div>
                    </div>
                </div>
                <div class="comment-box">
                    <input type="text" class="text" placeholder="Comment..">
                    <div class="button"><i class="fas fa-paper-plane"></i></div>
                </div>
            </div>
        </div>
    </div>
</div>
<script
        src="https://code.jquery.com/jquery-3.4.1.min.js"
        integrity="sha256-CSXorXvZcTkaix6Yvo6HppcZGetbYMGWSFlBw8HfCJo="
        crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
<script src="/js/board.js"></script>
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaGECOR7M1N3AAXwLVc-nI&callback=initMap"></script>
</body>
</html>