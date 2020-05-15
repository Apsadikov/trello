<#import "base.ftlh" as base>
<#import "/spring.ftl" as spring/>
<@base.extends css="${css}" link="${link}" linkTitle="${linkTitle}" icon="fa-user-plus">
<div class="wrapper">
    <div class="sign-in shadow">
        <div class="sign-in-header"><@spring.message code='sign-in' /></div>
        <form action="/sign-in" class="sign-in-form" method="post">
            <div class="hint"><@spring.message code='email' /></div>
            <input type="email" name="email" placeholder="<@spring.message code='email' />" class="input"
                   autocomplete="email">
            <div class="hint"><@spring.message code='password' /></div>
            <input type="password" name="password" placeholder="<@spring.message code='password' />" class="input"
                   autocomplete="password">
            <#if isAuthError == 'true'>
                <div class="error"><@spring.message code='error.auth' /></div>
            </#if>
            <input type="submit" class="send" value="<@spring.message code='sign-in' />">
            <label for="remember-me" class="label-remember-me">
                <input type="checkbox" id="remember-me" name="remember-me">
                <span class="remember-me"></span>
                <span class="remember-me-hint">Remember me</span>
            </label>
            <input type="hidden" value="${_csrf.token}" name="${_csrf.parameterName}">
        </form>
    </div>
</div>
</@base.extends>