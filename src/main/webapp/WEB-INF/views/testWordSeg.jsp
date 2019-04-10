<%--
  Created by IntelliJ IDEA.
  User: AXD
  Date: 2019/2/19
  Time: 21:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="segTest.do" method="post" enctype="multipart/form-data">
    分词测试：<input type="text" name="rawText" id="rawText" placeholder="请输入测试用文本" value=""><br>
    <button type="submit">提交</button>
</form>
<br>
<br>
<br>
<%
    String parseText = null;
    if ((parseText = (String)request.getAttribute("result")) != null) {


%>
    <p>原句：<%out.println((String)request.getAttribute("rawText"));%></p>
    <p>分词结果：<%out.println(parseText);%></p>
<%
    }
%>
<br>
<br>
<a href="http://atzlt.top:8080/trademark/">返回主页</a>
</body>
</html>
