<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>添加商标</title>
</head>
<body>
<#import "common.ftl" as com>
<form action="addTrademarkDomain.do" method="post" enctype="multipart/form-data">
    商标名称：<input type="text" name="trademarkName" id="trademarkName" value=""><br>
    国际分类：<input type="text" name="trademarkType" id="trademarkType" value=""><br>
    类似群：<input type="text" name="similarId" id="similarId" value=""><br>
    简要描述 ：<input type="text" name="briefDescription" id="briefDescription" value=""><br>
    注册时间：<input type="text" name="timeOfApplication" id="timeOfApplication" value=""><br>
    注册地址：<input type="text" name="registeredAddress" id="registeredAddress" value=""><br>
    申请人名称：<input type="text" name="petitioner" id="petitioner" value=""><br>
    专用权期限：<input type="text" name="useTime" id="useTime" value=""><br>
    颜色：<input type="text" name="color" id="color" value="">（暂时先不添加）<br>
    形状：<input type="text" name="shape" id="shape" value="">（暂时先不添加）<br>
    原图：<input type="file" name="trademarkImg" id="trademarkImg" accept="image/jpeg" ><br>
    缩略图：<input type="file" name="trademarkTbImg" id="trademarkTbImg" accept="image/jpeg"><br><br>
    <button type="submit">提交</button>
</form>
<br><a href="${com.homePage}">${com.gotoHomePageText}</a>
</body>
</html>