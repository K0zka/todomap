<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.Locale"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="i18n" %>
<%
final Locale locale = (Locale)request.getAttribute("locale");
%>
<i18n:setLocale value="<%= locale %>"/>
<i18n:setBundle basename="Messages"/>

<meta name="keywords" content="<i18n:message key="etc.keywords">democracy, direct democracy, map, public infrastructure, issues</i18n:message>" />
<link REL="SHORTCUT ICON" HREF="img/earth.ico"/>
