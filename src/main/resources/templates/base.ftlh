<#macro extends css link linkTitle icon>
    <#import "/spring.ftl" as spring/>
<!DOCTYPE HTML>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
    <link rel="stylesheet" href="/css/reset.css">
    <link rel="stylesheet" href="/css/base.css">
    <link rel="stylesheet" href="/css/${css}.css">
    <link rel="stylesheet" href="/fontawesome/css/all.min.css">
</head>
<body>
<div class="header-wrapper">
    <div class="header">
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
        <div class="link"><a href="${link}" class="white"><i
                class="fas ${icon}"></i> <@spring.message code='${linkTitle}' /></a></div>
    </div>
</div>
<#nested>
<script>
    let language = document.getElementById("language");
    let languages = document.getElementById("languages");
    let toggle = false;
    language.onclick = function () {
        languages.style.display = !toggle ? "block" : "none";
        toggle = !toggle;
    }
</script>
</body>
</html>
</#macro>