<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">  
    <title>商标检索系统</title>
  </head>
  
  <body>
    <h2>简易后台:JSP</h2>
    <a href="http://atzlt.top:8080/trademark/dataManagement/addTrademarkDomain.do">添加商标信息</a><br>
    <a href="http://atzlt.top:8080/trademark/dataManagement/showTrademarkDomains.do">查看所有商标信息</a><br>
    <a href="http://atzlt.top:8080/trademark/segTest.do">分词测试</a>
  </body>
</html>
