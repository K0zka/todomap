<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.todomap.o29.beans.RatingSummary"%>
<%@page import="org.todomap.o29.logic.RatingService"%>
<%@page import="org.todomap.o29.utils.URLUtil"%>
<%@page import="org.todomap.o29.beans.BaseBean"%>
<%@page import="java.util.Locale"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="i18n" %>
<%
	final BaseBean item = (BaseBean) request.getAttribute("data");
	final Locale locale = (Locale) request.getAttribute("locale");
	final RatingService ratingService = (RatingService) WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext()).getBean("ratings");
	final RatingSummary ratings = ratingService.getRatingSummary(item.getId());
%>
<i18n:setLocale value="<%= locale %>"/>
<i18n:setBundle basename="Messages"/>

<script type="text/javascript">
function replaceWithListenerList() {
	$.getJSON('services/rest/bookmarks/listeners/<%=item.getId()%>', function(data){
		debug(data);
		$('#nrOfListeners').empty();
		$('#nrOfListeners').append('<ul></ul><br/>');
		debug('single user');
		$.each(data.user, function(i, user) {
			$('#nrOfListeners ul').append('<li><a target="_blank" href="<%=URLUtil.getApplicationRoot(request)%>'+user.id+'-'+user.name+'.html">'+user.name+'</a></li>');
		});
	});
}
</script>

	<h3><i18n:message key="todo.ratingdetails">Ratings details</i18n:message></h3>
	<ul>
		<li><i18n:message key="ratings.bookmarkedby">Bookmarked by:</i18n:message> <span id="nrOfListeners" onclick="replaceWithListenerList();"><%= ratings.getBookmarked() %></span></li>
		<li><i18n:message key="ratings.authenticated">Authenticated users:</i18n:message> <%= ratings.getNrOfRatings() %></li>
		<li><i18n:message key="ratings.authenticated_avg">Authenticated users average:</i18n:message> <%= ratings.getAverage() == null ? "-" :  ratings.getAverage() %></li>
		<li><i18n:message key="ratings.anon">Anon users:</i18n:message> <%= ratings.getNrOfAnonRatings() %></li>
		<li><i18n:message key="ratings.anon_avg">Anon users average:</i18n:message> <%= ratings.getAnonAverage() == null ? "-" : ratings.getAnonAverage() %></li>
	</ul>
