<#import "base.ftlh" as base>
<@base.extends css="${css}" link="${link}" linkTitle="${linkTitle}" icon="fa-sign-out-alt">
    <#import "/spring.ftl" as spring/>
<div class="boards">
    <div class="board">
        <@spring.bind "addBoardForm" />
        <form action="/add" class="new-board" method="post">
            <input type="text" placeholder="Title" name="title" class="input">
            <input type="hidden" id="csrf" value="${_csrf.token}" name="${_csrf.parameterName}">
            <button><i class="fas fa-check" id="save"></i></button>
        </form>
    </div>
    <#list boards as board>
    <div class="board">
        <div><a href="/boards/${board.id}" class="title">${board.title}</a></div>
    </div>
    </#list>
</div>
</@base.extends>