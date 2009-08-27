<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
<meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
<title>O29</title>

<link rel="stylesheet" type="text/css" href="style/jquery-ui-1.7.2.custom.css"
	media="all" />

<script type="text/javascript" src="scripts/jquery-1.3.2.js">
</script>
<script type="text/javascript" src="scripts/jquery-ui-1.7.2.js">
</script>
<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false">
</script>
<script type="text/javascript">
    
    var map;
    
    function initialize() {
        var myLatlng = new google.maps.LatLng(33.956461,118.396225);
        var myOptions = {
          zoom: 4,
          center: myLatlng,
          mapTypeId: google.maps.MapTypeId.ROADMAP
        }
        
        map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);

		google.maps.event.addListener(map, "bounds_changed", function() {
			$.get()
        });
        
    }
    $(document).ready(function(){
        $("#dialog").dialog();
      });
        
</script>


</head>
<body onload="initialize()" style="margin:0px; padding:0px;">
<div style="width:100%; height:100%">
	<div id="map_canvas" style="width:100%; height:100%"></div>
</div>

<div id="dialog" title="Tools">
	<button id="homeButton"><img src="img/gohome32.png"></button>
	<button id="searchCriteriasButton"><img src="img/search32.png"></button>
	<button id="infoButton"><img src="img/info32.png"></button>
	<button id="statisticsButton"><img src="img/math32.png"></button>
	<button id="helpButton"><img src="img/help32.png"></button>
</div>

</body>
</html>