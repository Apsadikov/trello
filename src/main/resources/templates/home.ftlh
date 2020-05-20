<#import "base.ftlh" as base>
<@base.extends css="home" link="/logout" linkTitle="logout" icon="fa-sign-out-alt">
    <#import "/spring.ftl" as spring/>
<div class="boards">
    <div class="board">
        <@spring.bind "addBoardForm" />
        <form action="/add" class="new-board" method="post">
                <@spring.bind 'addBoardForm.title'/>
                <@spring.formInput path="addBoardForm.title" attributes="class=input placeholder=Title"/>
                <input type="hidden" id="csrf" value="${_csrf.token}" name="${_csrf.parameterName}">
                <button><i class="fas fa-check" id="save"></i></button>
            <input type="hidden" id="csrf" value="${_csrf.token}" name="${_csrf.parameterName}">
        </form>
        <div class="error"><@spring.showErrors "<br>"/></div>
    </div>
    <#list boards as board>
    <div class="board">
        <div><a href="/boards/${board.id}" class="title">${board.title}</a></div>
    </div>
    </#list>
</div>
<div class="pagination-wrapper">
    <div class="pagination">
        <#assign x=pages>
        <#list 1..x as i>
                <div class="page <#if page == i>selected</#if>"><a href="/?page=${i}">${i}</a></div>
        </#list>
    </div>
</div>
</@base.extends>