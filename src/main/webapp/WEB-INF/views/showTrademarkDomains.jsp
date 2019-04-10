<%@ page import="top.atzlt.cons.CommonConstant" %>
<%@ page import="top.atzlt.domain.TrademarkTempDomain" %>
<%@ page import="top.atzlt.domain.TrademarkListDomain" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: AXD
  Date: 2018/12/9
  Time: 11:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>商标信息表</title>
</head>
<body>

<%
  String stateCode = (String) request.getAttribute("stateCode");
  TrademarkTempDomain trademarkTempDomain = (TrademarkTempDomain) request.getAttribute("trademarkTempDomain");
  if (stateCode != null && !stateCode.equals(""))  {
      if (stateCode.equals(CommonConstant.OPERATE_SUCCESS)) {
          out.print("添加成功！   " + "[" + trademarkTempDomain.getTrademarkId() + ", " + trademarkTempDomain.getTrademarkName() + "]");
      } else {
          out.print("添加商标  [" + trademarkTempDomain.getTrademarkName() + "]  失败!");
      }
      out.print("<br><br>");
  }

    List<TrademarkListDomain> trademarkListDomains = (List<TrademarkListDomain>)request.getAttribute("trademarkListDomains");
%>
<h4>----------------------商标列表-----------------------------</h4>
<table border="1" >
    <thead>
        <tr><td>id</td><td>name</td><td>查看操作</td></tr>
    </thead>
    <tbody>
        <%
            for (TrademarkListDomain trademarkListDomain : trademarkListDomains){
                out.print("<tr>");
                out.print("<td>" + trademarkListDomain.getTrademarkId() + "</td>");
                out.print("<td>" + trademarkListDomain.getTrademarkName() + "</td>");
                out.print("<td><a href='http://atzlt.top:8080/trademark/dataManagement/showTrademarkDomain.do?trademarkId=" + trademarkListDomain.getTrademarkId() + "'>查看</a></td>");
                out.print("</tr>");
            }
        %>
    </tbody>
</table>
<br>
<a href="http://atzlt.top:8080/trademark/">返回主页</a>
</body>
</html>
