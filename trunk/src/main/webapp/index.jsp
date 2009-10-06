<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<%@page import="net.anzix.o29.utils.VersionUtil"%><html>
<head>
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<link REL="SHORTCUT ICON" HREF="img/earth.ico"/>
<title>O29</title>

<link rel="stylesheet" type="text/css"
	href="style/jquery-ui-1.7.2.custom.css" media="all" />

<link rel="stylesheet" type="text/css"
	href="style/default.css" media="all" />

<script type="text/javascript" src="http://www.google.com/jsapi">
</script>
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

	var pageLocation = '<%= request.getRequestURL() %>';

    var map;
    var myLatlng;
    var zoomL;

	function isDefined(variable) {
		return (typeof(window[variable]) != 'undefined');
	}

	function debug(message) {
		try {
			//the good old firebug console
			console.log(message)
		} catch (fubar) {
			//do nothing. No console, no log.
		}
	}

    (function () {
    	google.maps.Marker.prototype.todoId = -1;

    	google.maps.Marker.prototype.getTodoId = function() {
        	return this.todoId;
        }
    	google.maps.Marker.prototype.setTodoId = function(todoId) {
        	this.todoId = todoId;
        }
    })();

    (function () {

    	  google.maps.Map.prototype.markers = new Array();

    	  google.maps.Map.prototype.addMarker = function(marker) {
    		  debug('add'+this.markers.length);
    	    this.markers[this.markers.length] = marker;
    	  };

    	  google.maps.Map.prototype.getMarkers = function() {
    	    return this.markers
    	  };
    	    
    	  google.maps.Map.prototype.clearMarkers = function() {
    	    for(var i=0; i<this.markers.length; i++){
    	      this.markers[i].set_map(null);
    	    }
    	    this.markers = new Array();
    	  };
    	})();


	function ipBasedLocation() {
		try {
			return new google.maps.LatLng(google.loader.ClientLocation['latitude'], google.loader.ClientLocation['longitude']);
		} catch (fubar) {
			debug(fubar);
			return new google.maps.LatLng(0, 0);
		}
	}

	function zoomLevel() {
		try {
			if(isDefined(google.loader.ClientLocation)) {
				return 4;
			}
		} catch (fubar) {
			debug(fubar);
		} 
		return 8;

	}
    
    function initialize() {
<%
if(request.getParameter("lat") == null) {
%>
        myLatlng = ipBasedLocation();
        zoomL = zoomLevel();
<%
} else {
%>
		myLatlng = new google.maps.LatLng(<%=request.getParameter("lat")%>,<%=request.getParameter("lng")%>);
		zoomL = <%= request.getParameter("zoom") %>
<% 
}
%>

        var myOptions = {
          zoom: zoomL,
          center: new google.maps.LatLng(myLatlng.lat(), myLatlng.lng()),
          mapTypeId: google.maps.MapTypeId.ROADMAP
        }
        
        map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);

		google.maps.event.addListener(map, "rightclick", function(event) {
			document.getElementById("newTodoLat").value = event.latLng.lat();
			document.getElementById("newTodoLng").value = event.latLng.lng();
			debug('lng: '+$("#newTodoLat").val());
			debug('lat: '+$("#newTodoLng").val());
			$("#newTodo").dialog('open');
        });
	    
		google.maps.event.addListener(map, "bounds_changed", function() {

			var center = map.getCenter();
			var bounds = map.get_bounds();
			var sw = bounds.getSouthWest();
			var ne = bounds.getNorthEast();

			//update the link content
			$('#linkToThisMap').val(pageLocation+'?lat='+center.lat()+'&lng='+center.lng()+"&zoom="+map.getZoom());
			$('#rssLinkToThisMap').val(pageLocation.replace(/index.jsp/,'')+'rss.xml/' + ne.lat() + ',' + ne.lng() + ',' + sw.lat() + ',' + sw.lng() );

			//update the todos on the map
			$.get("services/todos/area.sht/"+sw.lat() + "," + sw.lng() + "," + ne.lat() + "," + ne.lng(), function(data){
					var response = eval("("+data+")");
					$.each(response['todo-sum'], function(i, val) {

						debug('find the marker by ID on the map');
						markers = map.getMarkers();
						found = false;
						for(i = 0; i < markers.length; i++) {
							if(markers[i].getTodoId() == val['id']) {
								debug('todo '+val['id']+' found on the map');
								found = true;
								break;
							}
						}
						
						if(!found) {
							var mLatlng = new google.maps.LatLng(val['location']['latitude'], val['location']['longitude']);
						    var marker = new google.maps.Marker({
						        position: mLatlng, 
						        map: map,
						        title:val['descr']
						    });
						    marker.setTodoId(val['id']);
						    map.addMarker(marker);
						    google.maps.event.addListener(marker, 'click', function() {
							    $.get("services/todos/byid/"+val['id'], function(data){
								    var todo = eval("("+data+")");
							    	var infowindow = new google.maps.InfoWindow({
							            content: '<h2>'+todo['todo']['shortDescr'] + '</h2><div>' + todo['todo']['description'] + '</div>'
							            	+ '<img src="img/bookmark32.png" onclick="javascript:postRating('+val['id']+',1)"/>'
							            	+ '<img src="img/bookmark32.png" onclick="javascript:postRating('+val['id']+',2)"/>'
							            	+ '<img src="img/bookmark32.png" onclick="javascript:postRating('+val['id']+',3)"/>'
							            	+ '<img src="img/bookmark32.png" onclick="javascript:postRating('+val['id']+',4)"/>'
							            	+ '<img src="img/bookmark32.png" onclick="javascript:postRating('+val['id']+',5)"/> <br/>'
							            	+ '<a target="new" href="' + todo['todo']['id'] + '-' + todo['todo']['shortDescr'] + '.html">'
					            			+ 'more...</a>'
							        });
							        infowindow.open(map,marker);
								})
					        });

						}
					});
				});
        });
        
    }
    $(document).ready(function(){
        $("#toolsAccordion").accordion({
           });
        $("#helpWindow").dialog({
            autoOpen : false
           });
        $("#newTodo").dialog({
            modal: true,
            autoOpen : false
           });
        $("#productInfoWindow").dialog({
            modal: true,
            width: 600,
            height: 600,
            show: 'slide',
            autoOpen : false
           });
        $("#loginWindow").dialog({
        	modal: true,
            autoOpen : false
            });
        $("#linksWindow").dialog({
            autoOpen : false
            });

    });

	function submitNewTodo() {

		debug('submit new todo');
		
		var submitData = {todo:{
			shortDescr: $('#newTodoShortDescr').val(),
			description: $('#newTodoDescription').val(),
			location: {
				longitude : $('#newTodoLng').val(),
				latitude :  $('#newTodoLat').val()
				}
			}};
		var strData = JSON.stringify(submitData);
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
		map.panTo(myLatlng);
		map.setZoom(zoomL);
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
			<button id="homeButton"  onclick="goHome()"><img src="img/gohome32.png"/> Go Home </button>
			<button id="embedButton" onclick="$(linksWindow).dialog('open')"><img src="img/gear32.png"/> Link to this map </button>
			</p>
			</div>
			<h3><a href="#">Info</a></h3>
			<div>
			<p>
			<button id="infoButton" onclick="$(productInfoWindow).dialog('open')"><img src="img/info32.png"/> About todomap </button>
			<button id="statisticsButton"><img src="img/math32.png"/> Statistics </button>
			<button id="helpButton" onclick="$(helpWindow).dialog('open')"><img src="img/help32.png"/> Help </button>
			</p>
			</div>
			<h3><a href="#">Search</a></h3>
			<div>
			</div>
			<h3><a href="#">Bookmarks</a></h3>
			<div>
			</div>
		</div>
		<div class="versionInfo">
			<span>
			<img alt="logo" src="img/todomap-logo.jpg" width="80%"><br/>
			Version: <%= VersionUtil.getVersionNumber() %></span>
		</div>
	</span>
	<span style="width: 80%; height: 100%; position: absolute; right: 0px;">
		<div id="map_canvas" style="width: 100%; height: 100%"></div>
	</span>
</div>


<div id="helpWindow" title="Help">
	TODO.
<!-- 
	<iframe src="help.html" style="width: 100%; height: 100%"></iframe>
	 -->
</div>

<div id="linksWindow" title="Links to this map">
	<label for="linkToThisPage" >Link to this map</label>
	<input type="text" id="linkToThisMap" value=""/>
	<label for="rssLinkToThisMap" >RSS to this map</label>
	<input type="text" id="rssLinkToThisMap" value=""/>
</div>


<div id="loginWindow" title="Log in">
	<iframe src="openidlogin.jsp" style="width: 100%; height: 100%"></iframe>
</div>

<div id="productInfoWindow" title="About todomap">
	<h2>
	Todomap - development version
	</h2>
	<p>
	This site is runngin todomap version <%= VersionUtil.getVersionNumber() %>.
	</p>
	<p>
	Please note that this is an <strong>early beta</strong> version of the application under active development since Q3 2009.
	</p>
	<p>
	To find out more about the software, please visit the project page at 
	<a target="new" href="http://code.google.com/p/todomap/">google code</a>! Your contribution 
	to todomap's success is highly appreciated!
	</p>
	<p>
		<img alt="todomap logo" src="img/todomap-logo.jpg">
	</p>
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