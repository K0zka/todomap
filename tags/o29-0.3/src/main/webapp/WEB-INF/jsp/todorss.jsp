<%@ page language="java" contentType="application/rss+xml; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@page import="java.util.List"%>
<%@page import="net.anzix.o29.beans.Todo"%>
<%@page import="net.anzix.o29.utils.VersionUtil"%>
<%
SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss ZZZ");
final List<Todo> todos = (List<Todo>)request.getAttribute("todos");
%>

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%><rss version="2.0">
  <channel>
    <title>Todomap RSS feed for area</title>
    <description>Todomap RSS feed for area</description>
    <lastBuildDate><%= sdf.format(new Date())%></lastBuildDate>
	<% for(Todo todo : todos)  {%>
    <item>
      <title><%= todo.getShortDescr() %></title>
      <link><%= "../"+todo.getId() + "-" + todo.getShortDescr() + ".html" %></link>
      <description> <%= todo.getDescription() %> </description>
      <pubDate><%= sdf.format(todo.getCreated()) %></pubDate>
      <guid><%= todo.getId() + "-" + todo.getShortDescr() + ".html" %></guid>
    </item>
	<% } %>
  </channel>
</rss>