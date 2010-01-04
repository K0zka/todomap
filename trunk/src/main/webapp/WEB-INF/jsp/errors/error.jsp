<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="org.todomap.o29.utils.URLUtil"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Error</title>
</head>
<body>
	<img alt="error" src="<%= URLUtil.getApplicationRoot(request) %>img/core.png" style="float: left;"> There was an error processing your request. 
	Please send a <a href="http://code.google.com/p/todomap/issues/entry?template=Defect%20report%20from%20user">bugreport</a>!
</body>
</html>