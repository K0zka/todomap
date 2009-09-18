<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix='c' uri='http://java.sun.com/jstl/core_rt' %>

<html>
<head>
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<link REL="SHORTCUT ICON" HREF="img/earth.ico"/>
<title>O29</title>

<link rel="stylesheet" type="text/css"
	href="style/jquery-ui-1.7.2.custom.css" media="all" />

<script type="text/javascript" src="scripts/json.js">
</script>
<script type="text/javascript" src="scripts/jquery-1.3.2.js">
</script>
<script type="text/javascript" src="scripts/jquery-ui-1.7.2.js">
</script>
<script type="text/javascript"
	src="http://maps.google.com/maps/api/js?sensor=false">
</script>
<script type="text/javascript">

    var map;

    (function () {

    	  google.maps.Map.prototype.markers = new Array();
    	    
    	  google.maps.Map.prototype.addMarker = function(marker) {
    	    this.markers[this.markers.length] = marker;
    	  };
    	    
    	  google.maps.Map.prototype.getMarkers = function() {
    	    return this.markers
    	  };
    	    
    	  google.maps.Map.prototype.clearMarkers = function() {
    	    
    	    for(var i=0; i<this.markers.length; i++){
    	      this.markers[i].set_map(null);
    	    }
    	  };
    	})();

    
    function initialize() {
        var myLatlng = new google.maps.LatLng(0,0);
        var myOptions = {
          zoom: 4,
          center: myLatlng,
          mapTypeId: google.maps.MapTypeId.ROADMAP
        }
        
        map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);

		google.maps.event.addListener(map, "rightclick", function(event) {
			document.getElementById("newTodoLat").value = event.latLng.lat();
			document.getElementById("newTodoLng").value = event.latLng.lng();
//			console.log('lng: '+$("#newTodoLat").val());
//			console.log('lat: '+$("#newTodoLng").val());
			$("#newTodo").dialog('open');
        });
	    
		google.maps.event.addListener(map, "bounds_changed", function() {
			var bounds = map.get_bounds();
			var sw = bounds.getSouthWest();
			var ne = bounds.getNorthEast();
			$.get("services/todos/area.sht/"+sw.lat() + "," + sw.lng() + "," + ne.lat() + "," + ne.lng(), function(data){
					var response = eval("("+data+")");
					map.clearMarkers();
					$.each(response['todo-sum'], function(i, val) {
						var mLatlng = new google.maps.LatLng(val['location']['latitude'], val['location']['longitude']);
					    var marker = new google.maps.Marker({
					        position: mLatlng, 
					        map: map,
					        title:val['descr']
					    });
					    google.maps.event.addListener(marker, 'click', function() {
						    $.get("services/todos/byid/"+val['id'], function(data){
							    var todo = eval("("+data+")");
						    	var infowindow = new google.maps.InfoWindow({
						            content: '<h2>'+todo['todo']['shortDescr'] + '</h2><div>' + todo['todo']['description'] + '</div>'
						            	+ '<img src="img/bookmark32.png" onclick="javascript:postRating('+val['id']+',1)"/>'
						            	+ '<img src="img/bookmark32.png" onclick="javascript:postRating('+val['id']+',2)"/>'
						            	+ '<img src="img/bookmark32.png" onclick="javascript:postRating('+val['id']+',3)"/>'
						            	+ '<img src="img/bookmark32.png" onclick="javascript:postRating('+val['id']+',4)"/>'
						            	+ '<img src="img/bookmark32.png" onclick="javascript:postRating('+val['id']+',5)"/>'
						        });
						        infowindow.open(map,marker);
							})
				        });

					});
				});
        });
        
    }
    $(document).ready(function(){
        $("#toolsAccordion").accordion({
           });
        $("#helpWindow").dialog({
           });
        $("#helpWindow").dialog('close');
        $("#newTodo").dialog({
            modal: true
           });
        $("#newTodo").dialog('close');
        $("#loginWindow").dialog({
        });
      });

	function submitNewTodo() {

//		console.log('submit new todo');
//		console.log('latitude : ' + $('#newTodoLat').val());
//		console.log('longitude : ' + $('#newTodoLng').val());

		
		var submitData = {todo:{
			shortDescr: $('#newTodoShortDescr').val(),
			description: $('#newTodoDescription').val(),
			location: {
				longitude : $('#newTodoLng').val(),
				latitude :  $('#newTodoLat').val()
				}
			}};
		var strData = JSON.stringify(submitData);
//		console.log('submit new todo');
		$.ajax({
			type : 'PUT',
			url : 'services/todos/new',
			data: strData,
			success: function(msg){
				$("#newTodo").dialog('close');
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				handleErrors(XMLHttpRequest);
			},
			processData : false,
			contentType : 'application/json',
			dataType : 'json'
			});
	}

	function goHome() {
//		console.log('go home');
	}

	function handleErrors(XMLHttpRequest) {
		if(XMLHttpRequest.status == 401) {
			$("#loginWindow").dialog('open');
		}
	}

	function postRating(todoId, ratingValue) {
		var data = {rating:{
			rate : ratingValue,
			comment : ''
			}};
		var strData = JSON.stringify(data);
		$.ajax({
			type	: 'PUT',
			url		: 'services/rating/add/' + todoId,
			data	: strData,
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				handleErrors(XMLHttpRequest);
			},
			processData : false,
			contentType : 'application/json',
			dataType : 'json'
			});
	}
	
</script>


</head>
<body onload="initialize()" style="margin: 0px; padding: 0px;">

<div style="width: 100%; height: 100%;">
	<span style="width: 20%; height: 100%; position: absolute; left: 0px;">
		<div id="toolsAccordion">
			<h3><a href="#">Tools</a></h3>
			<div>
			<p>
			<button id="homeButton"><img src="img/gohome32.png"/></button>
			<button id="searchCriteriasButton"><img src="img/search32.png"/></button>
			</p>
			</div>
			<h3><a href="#">Info</a></h3>
			<div>
			<p>
			<button id="infoButton"><img src="img/info32.png"/></button>
			<button id="statisticsButton"><img src="img/math32.png"/></button>
			<button id="helpButton" onclick="$(helpWindow).dialog('open')"><img src="img/help32.png"/></button>
			</p>
			</div>
		</div>
	</span>
	<span style="width: 80%; height: 100%; position: absolute; right: 0px;">
		<div id="map_canvas" style="width: 100%; height: 100%"></div>
	</span>
</div>


<div id="helpWindow" title="Help">
	<iframe src="help.html" style="width: 100%; height: 100%"></iframe>
</div>

<div id="loginWindow" title="Log in">
	<iframe src="openidlogin.jsp" style="width: 100%; height: 100%"></iframe>
</div>

<div id="newTodo" title="New Todo">
	<form>
		<div>
			<label for="newTodoShortDescr">Short description</label> <input type="text" name="newTodoShortDescr" id="newTodoShortDescr"/>
			</div>
		<div>
			<label for="newTodoDescription">Details</label>
			<textarea id="newTodoDescription" name="newTodoDesription" style="width: 100%; height: 50%"></textarea>
		</div>
		<input type="hidden" id="newTodoLat" name="newTodoLat"/>
		<input type="hidden" id="newTodoLng" name="newTodoLng"/>
	</form>
	<button id="submitNewTodoButton" onclick="submitNewTodo()"><img src="img/gear32.png"/></button>
</div>

</body>
</html>