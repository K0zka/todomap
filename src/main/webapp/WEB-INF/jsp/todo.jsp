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

<link rel="stylesheet" type="text/css"
	href="style/jquery-ui-1.7.2.custom.css" media="all" />
<link rel="stylesheet" type="text/css"
	href="style/default.css" media="all" />

<script type="text/javascript" src="scripts/json.js">
</script>
<script type="text/javascript" src="scripts/jquery-1.3.2.js">
</script>
<script type="text/javascript" src="scripts/jquery-ui-1.7.2.js">
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

    $(document).ready(function(){
        $("#todoDetails").accordion({
           });
    });
	
    
}
</script>

</head>
<body onload="initialize()">

<div id="todoDetails">

	<h3><a href="#">Details</a></h3>
	<div>
		<h1><%= todo.getShortDescr() %></h1>
		
		<div id="map_canvas" style="width: 300px; height: 300px"></div>
		
		<span class="todoDescription">
			<%= todo.getDescription() %>
		</span>
	</div>
	<h3><a href="#">Attachments</a></h3>
	<div>
	</div>
	<h3><a href="#">Comments</a></h3>
	<div>
	</div>
	<h3><a href="#">Ratings details</a></h3>
	<div>
	</div>

</div>

</body>
</html>