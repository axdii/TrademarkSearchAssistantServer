<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>分词测试</title>
</head>
<body>
<#import "common.ftl" as com>
<form action="${com.segTestUrl}" method="post" enctype="multipart/form-data">
    分词测试：<input type="text" name="rawText" id="rawText" placeholder="请输入测试用文本" value=""><br>
    <button type="submit">提交</button>
</form>
<br>
<br>
<br>
<#if result??>
    <p>原句：${rawText}</p>
    <p>分词结果：${result}</p>
</#if>
<br>
<br>
<a href="${com.homePage}">返回主页</a>

</body>
</html>