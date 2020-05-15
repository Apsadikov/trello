<#import "base.ftlh" as base>
<#import "/spring.ftl" as spring/>
<@base.extends css="${css}" link="${link}" linkTitle="${linkTitle}" icon="fa-sign-out-alt">
<div class="wrapper">
    <div class="hello shadow">
        If you see this text, then you are the administrator
    </div>
</div>
</@base.extends>