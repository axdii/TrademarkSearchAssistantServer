<%--
  Created by IntelliJ IDEA.
  User: AXD
  Date: 2018/10/21
  Time: 11:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="java.lang.Thread" %>
<%@ page import="top.atzlt.domain.User" %>
<%@ page import="java.util.UUID" %>
<%@ page import="top.atzlt.web.UserSessionManager" %>
<%@ page import="top.atzlt.service.UserService" %>
<%@ page import="org.springframework.beans.factory.annotation.Autowired" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
${user.statusCode}
<br>
${user.sessionID}
</body>
</html>
