<%@page import="org.todomap.o29.utils.URLUtil"%><%@ page language="java" contentType="text/xml; charset=UTF-8"
    pageEncoding="UTF-8"%>
<?xml version="1.0" encoding="UTF-8"?>
<%@page import="org.todomap.o29.beans.BaseBean"%>
<%@page import="java.util.List"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.util.Date"%><urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
<%
final List<Date> dates = (List<Date>)request.getAttribute("dates");
final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
for(final Date date : dates) {
%>
   <url>
      <loc><%= URLUtil.getApplicationRoot(request) + "sitemaps/"+ format.format(date) %>.txt</loc>
      <lastmod><%=format.format(date)%></lastmod>
   </url>
<%
}
%>
</urlset> 
    