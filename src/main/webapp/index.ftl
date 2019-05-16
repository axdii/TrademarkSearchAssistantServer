<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${title!"商标检索系统"}</title>
</head>
<body>
<h2>简易后台:FTL</h2>
<a href=${addTrademarkUrl}!"http://atzlt.top:8080/trademark/dataManagement/addTrademarkDomain.do">添加商标信息</a><br>
<a href=${showAllTrademark}!"http://atzlt.top:8080/trademark/dataManagement/showTrademarkDomains.do">查看所有商标信息</a><br>
<a href=${testSegWord}!"http://atzlt.top:8080/trademark/segTest.do">分词测试</a>
</body>
</html>