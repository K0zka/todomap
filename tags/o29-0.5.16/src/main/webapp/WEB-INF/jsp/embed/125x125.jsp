<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<%@ taglib prefix="i18n" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
	final BaseBean item = (BaseBean) request.getAttribute("data");
	final Locale locale = (Locale) request.getAttribute("locale");
%>
<i18n:setLocale value="<%= locale %>"/>
<i18n:setBundle basename="Messages"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="org.todomap.o29.beans.BaseBean"%>
<%@page import="java.util.Locale"%>
<%@page import="org.todomap.o29.beans.Todo"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="org.todomap.o29.utils.URLUtil"%>
<%@page import="org.todomap.o29.beans.Locatable"%><html>

<head>
<base href="<%= URLUtil.getApplicationRoot(request) %>"/>
<style type="text/css">
img.control {
	cursor: pointer;
	position: absolute;
	right: 0px;
}

#voteUp {
	top: 0px;
}

#voteDown {
	bottom: 0px;
}

body {
	font-size: small;
	font-family: sans-serif;
	font-style: normal;
	overflow: hidden;
}

h1 {
	font-size: x-small;
}

#desc {
	text-align: justify;
	font-size: x-small;
	width: 115px;
	height: 100%;
	overflow: hidden;
	cursor: pointer;
}

#thx {
	text-align: justify;
	font-size: x-small;
	width: 115px;
	height: 100%;
	overflow: hidden;
	cursor: pointer;
}

#logo {
	position: absolute;
	bottom: 0px;
	left: 0px;
	height: 20px;
}

#addr {
	font-size: xx-small;
	position: absolute;
	right: 0px;
	bottom: 0px;
}
</style>

<script type="text/javascript" src="scripts/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="scripts/anonrating.js"></script>

<script type="text/javascript">
function popup() {
	window.open('embed/640x480/<%= URLEncoder.encode(item.getId() + "-" + item.getName(), "UTF-8") %>.html','_blank','height=640, width=480, location=no, toolbar=no, menubar=no, titlebar=no, status=no');
}

</script>

</head>
<body lang="hungarian">
<h1><%= item.getName() %></h1>
<span id="question">
<div id="desc" onclick="popup()"><%= ((Todo)item).getDescription() %></div>
<div id="addr"><%= ((Locatable)item).getAddr().getTown() %><br/><%= ((Locatable)item).getAddr().getAddress() %></div>
<img id="voteUp" src="img/Add.png" alt="vote up" class="control"
	onclick="anonRate(<%= item.getId() %>, 10, function() {showRatings(<%=item.getId() %>);})" />
<img id="voteDown" src="img/Remove.png" alt="vote down" class="control"
	onclick="anonRate(<%= item.getId() %>, -10, function() {showRatings(<%=item.getId() %>);})" />
</span>
<span id="thx" style="display: none">
	<img id="voteResults" src="img/todomap-logo.jpg"/>
</span>
<img id="logo" alt="todomap logo" src="img/todomap-logo.jpg"/>
</body>
</html>