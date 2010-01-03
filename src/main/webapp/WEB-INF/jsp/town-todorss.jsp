<%@ page language="java" contentType="application/rss+xml; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="i18n" %>
<%@page import="java.util.List"%>
<%@page import="java.util.Locale"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="org.todomap.o29.beans.Todo"%>
<%@page import="org.todomap.o29.utils.VersionUtil"%>
<%@page import="org.todomap.o29.utils.URLUtil"%>
<%@page import="org.todomap.o29.utils.HtmlUtil"%>
<%
final SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss ZZZ");
final List<Todo> todos = (List<Todo>)request.getAttribute("todos");
final String town = (String)request.getAttribute("town");
final String state = (String)request.getAttribute("state");
final String countryCode = (String)request.getAttribute("countryCode");
final String chanelLink = (String)request.getAttribute("chanelLink");
%>

<i18n:setLocale value="<%= (Locale)request.getAttribute("locale") %>"/>
<i18n:setBundle basename="Messages"/>
<rss version="2.0" xmlns:atom="http://www.w3.org/2005/Atom" xmlns:georss="http://www.georss.org/georss">
  <channel>
    <title><i18n:message key="rss.town.title">Todomap RSS feed for </i18n:message><%= town == null ? (state == null ? countryCode: state) : town  %> </title>
    <description><%= town == null ? (state == null ? countryCode: state) : town  %></description>
    <lastBuildDate><%=sdf.format(new Date())%></lastBuildDate>
    <generator>Todomap <%= VersionUtil.getVersionNumber() %></generator>
    <link><%= chanelLink %></link>
    <atom:link href="<%= chanelLink %>" rel="self" type="application/rss+xml" />
	<% for(Todo todo : todos)  {%>
    <item>
      <title><%= todo.getShortDescr() %></title>
      <link><%= URLUtil.getUrl(request, todo) %></link>
      <description> <%= HtmlUtil.getFirstParagraph(todo.getDescription()) %> </description>
      <pubDate><%= sdf.format(todo.getCreated()) %></pubDate>
      <guid><%= URLUtil.getUrl(request, todo) %></guid>
      <georss:point><%= todo.getLocation().getLatitude() %> <%= todo.getLocation().getLongitude() %></georss:point>
    </item>
	<% } %>
  </channel>
</rss>