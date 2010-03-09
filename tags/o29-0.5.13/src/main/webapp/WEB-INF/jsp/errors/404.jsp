<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="org.todomap.o29.utils.URLUtil"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>404</title>
</head>
<body>
	<img alt="error" src="<%= URLUtil.getApplicationRoot(request) %>img/search.png" style="float: left;"> 
	Sorry, it was not found.<br/>
	<ul>
		<li>
		You can <a href="<%= URLUtil.getApplicationRoot(request) %>">get back to todomap</a>
		</li>
		<li>
		Please <a href="http://code.google.com/p/todomap/issues/entry?template=Defect%20report%20from%20user">file a bug</a> if this resource should exist!
		</li>
		<li>
		Update your bookmarks if you have broken links!
		</li>
	</ul>
</body>
</html>