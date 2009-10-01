<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix='c' uri='http://java.sun.com/jstl/core_rt' %>
<%@ page import="org.springframework.security.ui.AbstractProcessingFilter" %>
<%@ page import="org.springframework.security.ui.webapp.AuthenticationProcessingFilter" %>

<%@ page import="org.springframework.security.AuthenticationException" %>

<html>
<head>
<link REL="SHORTCUT ICON" HREF="img/earth.ico" />
<link rel="stylesheet" type="text/css"
	href="style/default.css" media="all" />
<title>OpenID</title>
</head>

<body>

<c:if test="${not empty param.login_error}">
  <font color="red">

    Your login attempt was not successful, try again.<br/><br/>
    Reason: <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>.
  </font>
</c:if>


<form name="f" action="<c:url value='j_spring_openid_security_check'/>" method="POST">

<div style="width: 100%; height: 20%">
	<img src="img/logo_openid.png"/>
</div>

<div style="width: 100%; height: 80%">

    <label for="j_username">Your OpenID Identity:</label> <input id="" type='text' name='j_username' value='<c:if test="${not empty param.login_error}"><c:out value="${SPRING_SECURITY_LAST_USERNAME}"/></c:if>'/>

    <input name="submit" type="submit">
    <input name="reset" type="reset">
</div>

</form>

</body>
</html>