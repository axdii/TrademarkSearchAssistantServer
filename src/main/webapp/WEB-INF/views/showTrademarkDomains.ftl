<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>商标大全</title>
</head>
<body>
<#import "common.ftl" as com>
<#if stateCode??>
    <#if stateCode == "OPERATE_SUCCESS">
        添加成功！ [${trademarkTempDomain.trademarkId}，${trademarkTempDomain.trademarkName}]
    <#else >
        添加商标 [${trademarkTempDomain.trademarkName} 失败！]
    </#if>
    <br><br>
</#if>
<table border="1">
    <thead>
    <tr><td>id</td><td>name</td><td>查看操作</td></tr>
    </thead>
    <tbody>
    <#list trademarkListDomains as trademarkListDomain>
        <tr>
            <td>${trademarkListDomain.trademarkId}</td>
            <td>${trademarkListDomain.trademarkName}</td>
            <td><a href='${com.showTrademarkDomainUrl}?trademarkId=${trademarkListDomain.trademarkId}'>查看</a></td></td>
        </tr>
    </#list>
    </tbody>
</table>
<br>
<a href="${com.homePage}">${com.gotoHomePageText}</a>
</body>
</html>