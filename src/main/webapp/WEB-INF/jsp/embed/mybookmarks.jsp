<%@page import="org.todomap.o29.utils.URLUtil"%>
<%@page import="org.todomap.o29.beans.User"%>
<%@page import="org.todomap.o29.beans.BaseBean"%>
<%@page import="java.util.Locale"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<%@ taglib prefix="i18n" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
	final Locale locale = (Locale) request.getAttribute("locale");
	final User user = (User)request.getAttribute("user");
%>
<i18n:setLocale value="<%= locale %>"/>
<i18n:setBundle basename="Messages"/>

<html>
	<head>
		<title><%= user.getDisplayName() %></title>
		<script type="text/javascript"
			src="http://maps.google.com/maps/api/js?sensor=false&language=<%= locale.getLanguage() %>">
		</script>
		<script type="text/javascript">
			function initialize() {
				var latlng = new google.maps.LatLng(0, 0);
				var mapOptions = {
						zoom: 8,
						center: latlng,
						mapTypeId: google.maps.MapTypeId.ROADMAP
						};
				var map = new google.maps.Map(document.getElementById("map_canvas"),
				        mapOptions);
		        var kmlOptions = {
		        		map : map,
		        		preserveViewport : true
				        }
				var kmlLayer = new google.maps.KmlLayer('<%= URLUtil.getApplicationRoot(request) + "rss.xml/user/"+user.getId() %>', kmlOptions);
		        kmlLayer.setMap(map);
			}
		</script>
	</head>
	<body onload="initialize()">
		<div id="map_canvas" style="width:100%; height:100%"></div>
	</body>
</html>