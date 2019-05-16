<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta >
    <title>删除结果</title>
</head>
<body>
<#import "common.ftl" as com>
<#if sign>
    <h2>删除成功</h2>
<#else >
    <h2>删除失败！</h2>
</#if>
<script>
    setTimeout('location.href="${com.showTrademarkDomainsUrl}}"', 5000);
</script>
</body>
</html>