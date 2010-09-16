<%@page import="org.todomap.o29.utils.URLUtil"%>
<%@page import="org.todomap.o29.beans.BaseBean"%>
<%@page import="java.util.Locale"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="i18n" %>
<%
	final BaseBean item = (BaseBean) request.getAttribute("data");
	final Locale locale = (Locale) request.getAttribute("locale");
%>
<i18n:setLocale value="<%= locale %>"/>
<i18n:setBundle basename="Messages"/>

<script type="text/javascript">
function replaceWithListenerList() {
	$.getJSON('services/rest/bookmarks/listeners/<%=item.getId()%>', function(data){
		debug(data);
		$('#nrOfListeners').empty();
		$('#nrOfListeners').append('<ul></ul>');
		debug('single user');
		$.each(data.user, function(i, user) {
			$('#nrOfListeners ul').append('<li><a target="_blank" href="<%=URLUtil.getApplicationRoot(request)%>'+user.id+'-'+user.name+'.html">'+user.name+'</a></li>');
		});
	});
}
</script>

	<h3><i18n:message key="todo.nrofListeners">Number of listeners</i18n:message></h3>
	<span id="nrOfListeners" onclick="replaceWithListenerList();"><%= request.getAttribute("numberOfListeners") %></span>
