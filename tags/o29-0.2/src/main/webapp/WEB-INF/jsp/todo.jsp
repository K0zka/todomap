<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="net.anzix.o29.beans.Todo"%>

<%
Todo todo = (Todo)request.getAttribute("todo");
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link REL="SHORTCUT ICON" HREF="img/earth.ico"/>

<script type="text/javascript"
	src="http://maps.google.com/maps/api/js?sensor=false">
</script>

<script type="text/javascript" src="scripts/json.js">
</script>


<title><%= todo.getShortDescr() %></title>

<script type="text/javascript">
function initialize() {
	var map;
	var mapCenter = new google.maps.LatLng(<%= todo.getLocation().getLatitude() %>, <%= todo.getLocation().getLongitude() %>);
	var mapOptions = {
          zoom: 15,
          center: mapCenter,
          mapTypeId: google.maps.MapTypeId.ROADMAP
        };
    map = new google.maps.Map(document.getElementById("map_canvas"), mapOptions);
    var marker = new google.maps.Marker({
        position: mapCenter, 
        map: map,
        title:'-'
    });

}
</script>

</head>
<body onload="initialize()">

<div class="todoDescription">
	<%= todo.getDescription() %>
</div>


<div id="map_canvas" style="width: 300px; height: 300px"></div>

</body>
</html>