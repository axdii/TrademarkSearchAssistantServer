<%--
  Created by IntelliJ IDEA.
  User: AXD
  Date: 2018/12/10
  Time: 21:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>删除结果</title>
</head>
<body>
<%
    response.setHeader("Refresh","5;URL=http://atzlt.top:8080/trademark/dataManagement/showTrademarkDomains.do");
    boolean sign = (boolean)request.getAttribute("sign");
    if (sign) {
        out.print("<h2>删除成功</h2>");
    } else {
        out.print("<h2>删除失败！</h2>");
    }

%>

<br>
<a href="http://atzlt.top:8080/trademark/dataManagement/showTrademarkDomains.do">返回</a>
</body>
</html>
