<%@ page language="java" contentType="text/txt; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="org.todomap.o29.beans.BaseBean"%>
<%@page import="java.util.List"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.util.Date"%>
<%@page import="org.todomap.o29.utils.URLUtil"%>
<%
final List<BaseBean> beans = (List<BaseBean>)request.getAttribute("beans");
for(final BaseBean bean : beans) {
%><%=URLUtil.getApplicationRoot(request)+bean.getId()+"-"+URLEncoder.encode(bean.getName()+".html","UTF-8")%>
<%
}
%>
    