<%@page import="org.todomap.o29.utils.URLUtil"%><%@ page language="java" contentType="text/xml; charset=UTF-8"
    pageEncoding="UTF-8"%>
<?xml version="1.0" encoding="UTF-8"?>
<%@page import="org.todomap.o29.beans.BaseBean"%>
<%@page import="java.util.List"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.util.Date"%>
<%@page import="org.todomap.o29.beans.Attachment"%><urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9" xmlns:image="http://www.google.com/schemas/sitemap-image/1.1">
<%
final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
final List<BaseBean> beans = (List<BaseBean>)request.getAttribute("beans");
for(final BaseBean bean : beans) {
%>
   <url>
      <loc><%= URLUtil.getUrl(request, bean) %></loc>
      <lastmod><%=format.format(bean.getCreated())%></lastmod>
      <%
      for(Attachment attachment : bean.getAttachments()) {
      %>
      <image:image>
      	<image:loc><%= URLUtil.getApplicationRoot(request) + "download/" + attachment.getId() %></image:loc>
      </image:image>
      <% } %>
   </url>
<%
}
%>
</urlset>