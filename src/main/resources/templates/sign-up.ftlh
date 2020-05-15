<#import "base.ftlh" as base>
<#import "/spring.ftl" as spring/>
<@base.extends css="${css}" link="${link}" linkTitle="${linkTitle}" icon="fa-sign-in-alt">
<div class="wrapper">
    <div class="sign-in shadow">
        <div class="sign-up-header"><@spring.message code='sign-up-title' /></div>
        <@spring.bind "signUpForm" />
        <form action="/sign-up" class="sign-up-form" method="post">
            <div class="hint"><@spring.message code='name' /></div>
            <@spring.bind 'signUpForm.name'/>
            <#assign name><@spring.message code='name' /></#assign>
            <@spring.formInput path="signUpForm.name" attributes="class=input autocomplete=email placeholder=${name}"/>
            <div class="error"><@spring.showErrors "<br>"/></div>
            <div class="hint"><@spring.message code='email' /></div>
            <@spring.bind 'signUpForm.email'/>
            <#assign email><@spring.message code='email' /></#assign>
            <@spring.formInput path="signUpForm.email" attributes="class=input autocomplete=email placeholder=${email}"/>
            <div class="error"><@spring.showErrors "<br>"/></div>
            <div class="hint"><@spring.message code='password' /></div>
            <@spring.bind 'signUpForm.password'/>
            <#assign password><@spring.message code='password' /></#assign>
            <@spring.formPasswordInput path="signUpForm.password" attributes="class=input autocomplete=email placeholder=${password}"/>
            <div class="error"><@spring.showErrors "<br>"/></div>
            <input type="submit" class="send" value="<@spring.message code='sign-up' />">
            <input type="hidden" value="${_csrf.token}" name="${_csrf.parameterName}">
        </form>
    </div>
</div>
</@base.extends>