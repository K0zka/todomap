<%@page import="java.util.Locale"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="i18n" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
final Locale locale = (Locale)request.getAttribute("locale");
%>
<i18n:setLocale value="<%= locale %>"/>
<i18n:setBundle basename="Messages"/>
<script type="text/javascript">
$(document).ready(function(){
	$('#userDetailsNotifyWindow').dialog({
    	autoOpen : false,
        width: 240,
        height: 120,
        title:	'<i18n:message key="window.userDetailsNotify.title"/>'
	});
	setTimeout(function() {
		$.get('services/rest/home/auth', function(data) {
			if(data == 'true') {
				debug('signed in');
				$.getJSON('services/rest/home/user/get', function(data) {
					if(data.user.email == '' && data.user.displayName == '') {
						$('#userDetailsNotifyWindow').dialog('open');
					}
				});
			}
		});
		
	}, 1000);
});

</script>

<div id="userDetailsNotifyWindow">
	<i18n:message key="window.userDetailsNotify.question"/> <br/>
	<button><i18n:message key="etc.no"/></button> <button onclick="$('#userDetailsWindow').dialog('open'); getUserDetails(); $('#userDetailsNotifyWindow').dialog('close');"><i18n:message key="etc.yes"/></button>
</div>