
<%@page import="org.springframework.security.Authentication"%>
<%@page import="org.springframework.security.context.SecurityContextHolder"%><%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%

Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

if(authentication == null) {
	response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
	response.setHeader("Location", "index.jsp");
} else {
	%>
	Wellcome!
	<%
}
%>