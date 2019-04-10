<%@ page import="top.atzlt.domain.InitDataDomain" %>
<%@ page import="java.util.List" %>
<%@ page import="top.atzlt.domain.TrademarkListDomain" %>
<%@ page import="top.atzlt.domain.TrademarkDomain" %><%--
  Created by IntelliJ IDEA.
  User: AXD
  Date: 2018/12/6
  Time: 14:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%
    TrademarkDomain trademarkDomain = (TrademarkDomain) request.getAttribute("trademark");
    out.print(trademarkDomain.getTrademarkName());
%>

${trademark.toString()}
${trademark.statusCode}
</body>
</html>
