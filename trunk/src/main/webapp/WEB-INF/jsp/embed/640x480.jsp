
<%@page import="org.todomap.o29.utils.URLUtil"%>
<%@page import="org.todomap.o29.beans.Todo"%>
<%@page import="org.todomap.o29.beans.BaseBean"%>
<%@page import="java.util.Locale"%>
<%@page import="org.todomap.o29.beans.Locatable"%>
<%@page import="org.todomap.o29.beans.Coordinate"%>
<%@page import="org.todomap.geocoder.Address"%><html>
<%
	final BaseBean item = (BaseBean) request.getAttribute("data");
	final Locale locale = (Locale) request.getAttribute("locale");
%>

<head>
<base href="<%= URLUtil.getApplicationRoot(request) %>"/>
<style type="text/css">
img.control {
	cursor: pointer;
	position: absolute;
	bottom: 60px;
}

#voteUp {
	left: 120px;
}

#voteDown {
	right: 120px;
}

body {
	font-size: small;
	font-family: sans-serif;
	font-style: normal;
	overflow: hidden;
	margin: 20px;
}

h1 {
	font-size: medium;
}

#desc {
	text-align: justify;
	font-size: small;
	width: 100%;
	height: 100%;
	overflow: hidden;
}

#map {
	color: gray;
	float: left;
	margin-right: 10px;
	margin-bottom: 10px;
	font-size: x-small;
	width: 240;
}

#logo {
	position: absolute;
	bottom: 0px;
	left: 0px;
	height: 40px;
}

p.intro {
	padding-top: 0px;
	margin-top: 0px;
}

</style>

<script type="text/javascript" src="script/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="scripts/anonrating.js"></script>

</head>
<body lang="hungarian">
<h1><%= item.getName() %></h1>

<div id="desc">
<% if(item instanceof Locatable) { 
	final Coordinate location = ((Locatable)item).getLocation();
	final Address address = ((Locatable)item).getAddr();
%>

<script type="text/javascript" src="scripts/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="scripts/anonrating.js"></script>

<span id="map">
<img
	src="http://maps.google.com/maps/api/staticmap?center=<%= location.getLatitude() %>,<%= location.getLongitude() %>&zoom=14&size=240x180&markers=color:red|label:X|<%= location.getLatitude() %>,<%= location.getLongitude() %>&sensor=false"><br/>
<%= address.getTown() %>, <%= address.getAddress() %>
</span>
<% } %>
<p class="intro"><%= ((Todo)item).getShortDescr() %>
</div>

<img id="voteUp" src="img/Add.png" alt="vote up" class="control"
	onclick="anonRate(<%= item.getId() %>, +10, showRatings)" />
<img id="voteDown" src="img/Remove.png" alt="vote down" class="control"
	onclick="anonRate(<%= item.getId() %>, -10, showRatings)" />
<img id="logo" alt="todomap logo" src="img/todomap-logo.jpg">
</body>
</html>