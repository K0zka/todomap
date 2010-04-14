<%@page import="org.todomap.o29.utils.URLUtil"%><%@ page language="java" contentType="text/txt; charset=UTF-8"
    pageEncoding="UTF-8"%>
User-agent: *
Allow: /
Allow: /*.html
Disallow: /services/
Disallow: /embed/
Sitemap: <%=URLUtil.getApplicationRoot(request) %>sitemaps/date_breakdown.xml
