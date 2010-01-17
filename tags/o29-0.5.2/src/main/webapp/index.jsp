<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix='c' uri='http://java.sun.com/jstl/core_rt' %>

<%@ page import="org.springframework.security.ui.AbstractProcessingFilter" %>
<%@ page import="org.springframework.security.ui.webapp.AuthenticationProcessingFilter" %>
<%@ page import="org.springframework.security.AuthenticationException" %>

<%@page import="org.todomap.o29.logic.Configuration"%>
<%@page import="org.todomap.o29.utils.VersionUtil"%>
<%
final Configuration configuration = (Configuration)WebApplicationContextUtils
	.getRequiredWebApplicationContext(config.getServletContext()).getBean("config");

%>

<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.todomap.o29.utils.URLUtil"%><html>
<head>
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<meta name="google-site-verification" content="<%= configuration.getGoogleWebmastersVerification() %>" />
<meta name="y_key" content="<%= configuration.getYahooSiteExplorerVerification() %>">
<meta name="description" content="Keeping track of issues on the map. A direct-democracy tool for town and country-size communities." lang="english"/>

<jsp:include page="WEB-INF/jsp/includes/sitemeta.jsp"/>

<title>todomap</title>

<link rel="stylesheet" type="text/css"
	href="style/jquery-ui-1.7.2.custom.css" media="all" />

<link rel="stylesheet" type="text/css"
	href="style/default.css" media="all" />
<link type="text/css" rel="stylesheet" href="style/jquery.tooltip.css" />

<script type="text/javascript" src="http://www.google.com/jsapi">
</script>
<script type="text/javascript" src="scripts/json.js">
</script>
<script type="text/javascript" src="scripts/jquery-1.3.2.min.js">
</script>
<script type="text/javascript" src="scripts/jquery-ui-1.7.2.js">
</script>
<script type="text/javascript" src="scripts/jquery.dimensions.min.js">
</script>
<script type="text/javascript" src="scripts/jquery.tooltip.min.js">
</script>
<script type="text/javascript"
	src="http://maps.google.com/maps/api/js?sensor=false">
</script>

<script type="text/javascript" src="scripts/o29.js">
</script>

<script type="text/javascript">

	var pageLocation = '<%= request.getRequestURL() %>';

    var map;
    var myLatlng;
    var zoomL;

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
    	    this.markers[this.markers.length] = marker;
    	  };

    	  google.maps.Map.prototype.getMarkers = function() {
    	    return this.markers
    	  };
    	    
    	  google.maps.Map.prototype.clearMarkers = function() {
    	    for(var i=0; i<this.markers.length; i++){
    	      this.markers[i].setMap(null);
    	    }
    	    this.markers = new Array();
    	  };

    	  google.maps.Map.prototype.removeMarker = function(marker) {
          //remove from the map
	    	marker.setMap(null);
      	  //clear from the markers array
      	  var pos = 0;
      	  var found = false;
      	  while(!found && pos < this.markers.length) {
			if(this.markers[pos] == marker) {
				found = true;
				break;
			}
			pos++;
          }
          for(var i = pos; i < this.markers.length - 1; i++) {
        	  this.markers[i] = this.markers[i+1];
          }
          this.markers.pop();
    	  };
    	})();


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
			$("#newTodo").dialog('open');
        });
	    
		google.maps.event.addListener(map, "bounds_changed", refreshMarkers);
        
    }
    $(document).ready(function(){
        $("#toolsAccordion").accordion({
           });
        $("#userDetailsAccordion").accordion({});
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
            height: 400,
            show: 'slide',
            autoOpen : false
           });
        $("#loginWindow").dialog({
        	modal: true,
            autoOpen : false,
            show: 'slide',
            width: 600,
            height: 600,
            position: 'top'
            });
        $("#linksWindow").dialog({
            autoOpen : false
            });
        $('#wheretogoWindow').dialog({
        	autoOpen : false
            });
        $('#userDetailsWindow').dialog({
        	autoOpen : false,
            width: 400,
            height: 400
            });
        $('#loginTabs').tabs();

		var tooltipOptions = {
	            delay : 1000,
	            bodyHandler: function() {
	            	return $('#'+this.id+"-tooltip").html();
	        	}
	    };
        
        //Button tooltips
        $('button').tooltip(tooltipOptions);
        //Link tooltips
        $('a').tooltip(tooltipOptions);
        //div tooltips
		$('div').tooltip({
            delay : 2000,
            bodyHandler: function() {
            	//TODO: return different tooltip for authenticated users and anons.
           		return $('#'+this.id+"-tooltip").html();
        	}
		});
        
    });

	var flagmode = 'none';
    
	function refreshMarkers() {
		
		var center = map.getCenter();
		var bounds = map.getBounds();
		var zoomLevel = map.getZoom();
		debug('map level:'+zoomLevel);
		var sw = bounds.getSouthWest();
		var ne = bounds.getNorthEast();
		//update the link content
		$('#linkToThisMap').val(pageLocation+'?lat='+center.lat()+'&lng='+center.lng()+"&zoom="+map.getZoom());
		$('#rssLinkToThisMap').val(pageLocation.replace(/index.jsp/,'')+'rss.xml/' + ne.lat() + ',' + ne.lng() + ',' + sw.lat() + ',' + sw.lng() );


		if( zoomLevel > 12) {

			prepareForLevel('all');

			//update the todos on the map
			$.get("services/todos/area.sht/"+sw.lat() + "," + sw.lng() + "," + ne.lat() + "," + ne.lng(), function(data){
					var response = eval("("+data+")");
					$.each(response['todo-sum'], function(i, val) {

						markers = map.getMarkers();
						found = false;
						for(i = 0; i < markers.length; i++) {
							if(markers[i].getTodoId() == val['id']) {
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
						    marker.setIcon('img/flag.png');
						    map.addMarker(marker);
						    google.maps.event.addListener(marker, 'click', function() {
							    $.get("services/todos/shortbyid/"+val['id'], function(data){
								    var todo = eval("("+data+")");
								    var shortDescr = todo['todo']['shortDescr'];
								    var itemId = todo['todo']['id'];
							    	var infowindow = new google.maps.InfoWindow({
							    		content: '<div style="width: 200px; height: 200px;">'
							    			+ '<h3 style="margin: 5px; font-size: 15px; width: 160px;">'+shortDescr+'</h3>'
							    			+ '<div style="overflow: hidden; margin: 10px; text-align: justify; font-size: 12px; width: 160px; height: 100px;">'
							    			+ todo['todo']['description']
								    		+ '</div>'
							    		    + '<a href="'+ encodeURI(itemId + '-' + todo['todo']['shortDescr']) + '.html" style="position: absolute; bottom: 10px; font-style: italic; font-size: 10px;" target="new">more...</a>'
							    		    + '<img src="img/bookmark32.png" style="position: absolute; top: 0px; right: 0px; cursor: pointer;" onclick="bookmarkItem('+itemId+')"/>'
							    			+ '<img src="img/up32.png" style="position: absolute; top: 32px; right: 0px; cursor: pointer;"/>'
							    			+ '<img src="img/down32.png" style="position: absolute; bottom: 10px; right: 0px; cursor: pointer;"/>'
							    			+ '</div>'
							        });
							        infowindow.open(map,marker);
								})
					        });

						}
					});
				});
				
			
		} else {
			var fn = function(data){
				var response = eval("("+data+")");
				$.each(response['group-sum'], function(i, val) {

					markers = map.getMarkers();
					found = false;
					for(i = 0; i < markers.length; i++) {
						if(markers[i].getTodoId() == val['address']) {
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
					    marker.setTodoId(val['address']);
					    marker.setIcon('img/flags.png');
					    map.addMarker(marker);
					    google.maps.event.addListener(marker, 'click', function() {
					    	var infowindow = new google.maps.InfoWindow({
					    		content: '<div style="width: 200px; height: 100px;">'
					    			+ '<h3 style="margin: 5px; font-size: 15px; width: 160px;">'+getAddress(val['address'])+'</h3>'
					    			+ '<div style="overflow: hidden; margin: 10px; text-align: justify; font-size: 12px; width: 160px; height: 50px;">'
					    			+ 'Open issues: '+  val['nrOfIssues']
						    		+ '</div>'
					    		    + '<a style="color: #FFFFFF;" href="'+getRssUrlForAddr(val['address'])+'" target="_new"><img src="img/feed32.png" style="position: absolute; top: 0px; right: 0px; cursor: pointer;"/>'
//					    		    + '<img src="img/search32.png" style="position: absolute; top: 32px; right: 0px; cursor: pointer; onclick="map.panTo(new google.maps.LatLng('+1+','+1+'))"/>'
					    			+ '</div>'
					        });
					        infowindow.open(map,marker);
				        });

					}
				});
			};
		
			if (zoomLevel >= 10) {
				prepareForLevel('town');
				$.get("services/todos/area/town/"+sw.lat() + "," + sw.lng() + "," + ne.lat() + "," + ne.lng(), fn);
			} else if (zoomLevel > 5) {
				prepareForLevel('state');
				$.get("services/todos/area/state/"+sw.lat() + "," + sw.lng() + "," + ne.lat() + "," + ne.lng(), fn);
			} else {
				prepareForLevel('country');
				$.get("services/todos/area/country/"+sw.lat() + "," + sw.lng() + "," + ne.lat() + "," + ne.lng(), fn);
			}
		}

	
	}

	function prepareForLevel(level) {
		if(flagmode != level) {
			map.clearMarkers();
		}
		flagmode = level;
	}
	
	function getAddress(addr) {
		ret = '';
		try {
			if(''+addr['country'] != 'undefined') {
				ret = ret + addr['country'];
			}
		} finally {};
		try {
			if(''+addr['state'] != 'undefined') {
				ret = ret + " > " + addr['state'];
			}
		} finally {};
		try {
			if(''+addr['town'] != 'undefined') {
				ret = ret + " > " + addr['town'];
			}
		} finally {};
		return ret;
	}

	function getRssUrlForAddr(addr) {
		ret = '<%= URLUtil.getApplicationRoot(request,"index.jsp") + "rss.xml/region/" %>';
		try {
			if(''+addr['country'] != 'undefined') {
				ret = ret + addr['country'];
			}
		} finally {};
		try {
			if(''+addr['state'] != 'undefined') {
				ret = ret + "/" + addr['state'];
			}
		} finally {};
		try {
			if(''+addr['town'] != 'undefined') {
				ret = ret + "/" + addr['town'];
			}
		} finally {};
		return ret;

	}
	
	function submitNewTodo() {

		debug('submit new todo');

		$('#submitNewTodoButton').attr('disabled','disabled');
		
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
			type : 'POST',
			url : 'services/todos/new',
			data: strData,
			success: function(msg){
				refreshMarkers();
				$("#newTodo").dialog('close');
				$('#submitNewTodoButton').removeAttr('disabled');
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
			type	: 'POST',
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


	function saveUserDetails() {
		var user = {'user':
			{
			'displayName'	:$('#userDetailsDisplayName').val(),
			'email'			:$('#userDetailsEmail').val(),
			'homeLoc'		: {
				'longitude' 	: $('#userDetailsHomeLocationLat').val(),
				'latitude'		: $('#userDetailsHomeLocationLng').val()
				}
			}
		};
		$.ajax({
			url		: 'services/home/user/set',
			type	: 'POST',
			data	: JSON.stringify(user),
			success	:$('#userDetailsWindow').dialog('close'),
			contentType : 'application/json',
			dataType : 'json'
		});
	}

	function getUserDetails() {
		$.get("services/home/user/get", function(data) {
			debug(data);
			var userData = eval("("+data+")");
			$('#userDetailsDisplayName').val(userData['user']['displayName']);
			$('#userDetailsEmail').val(userData['user']['email']);
		});
	}


	function gotoLocation() {
		geocoder = new google.maps.Geocoder();
		geocoder.geocode( { 'address': $('#wheretogoLocation').val()}, function(results, status) {
	        if (status == google.maps.GeocoderStatus.OK) {
	          map.setCenter(results[0].geometry.location);
	        } else {
	        	$('#wheretogoErrors').val(status);
	        }
	      });
			
	}

	function cleanupMarkers() {
		markers = map.getMarkers();
		bounds = map.getBounds();
		for(i = 0; i < markers.length; i++) {
			if(!bounds.contains(markers[i].getPosition())) {
				map.removeMarker(markers[i]);
			}
		}

		setTimeout("cleanupMarkers()", 20000);
	}

	setTimeout("cleanupMarkers()", 20000);
	
</script>


</head>
<body onload="initialize()" style="margin: 0px; padding: 0px;">

<div style="width: 100%; height: 100%;">
	<span id="sidebar" style="width: 20%; height: 100%; position: absolute; left: 0px;">
		<div id="toolsAccordion">
			<h3><a href="#">Tools</a></h3>
			<div class="sidebarControls">
				<span class="authOnly">
				<button id="homeButton"  onclick="goHome()"> Go Home </button>
				<button id="logoutButton"  onclick="logOut()"> Log out </button>
				<button id="yourDetailsButton"  onclick="$('#userDetailsWindow').dialog('open'); getUserDetails();"> Your details </button>
				</span>
				<span class="noAuthOnly">
				<button id="loginButton" onclick="$(loginWindow).dialog('open')"> Log in / Register </button>
				</span>
				<button id="embedButton" onclick="$(linksWindow).dialog('open')"> Link to this map </button>
				<button id="gotoButton" onclick="$('#wheretogoWindow').dialog('open')">Enter address</button>
			</div>
			<h3><a href="#">Info</a></h3>
			<div class="sidebarControls">
				<button id="infoButton" onclick="$(productInfoWindow).dialog('open')"> About todomap </button>
				<button id="statisticsButton"> Statistics </button>
				<button id="helpButton" onclick="$(helpWindow).dialog('open')"> Help </button>
			</div>
			<h3><a href="#">Search</a></h3>
			<div class="sidebarControls">
			</div>
			<h3><a href="#">Bookmarks</a></h3>
			<div class="sidebarControls">
			</div>
		</div>
		<div class="versionInfo">
			<span>
			<img alt="logo" src="img/todomap-logo.jpg" width="80%"><br/>
			Version: <%= VersionUtil.getVersionNumber() %></span>
		</div>
		<div class="licenseInfo">
			<a href="http://creativecommons.org/licenses/by/3.0/">
				<img alt="creative commons logo" src="img/cc88x31.png"/>
			</a>
			The content of the site is published under creative commons license.<br/>
			The map is copyrighted by <a href="http://www.google.com/">google<a/>.
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
	<div style="width: 100%" onclick="javascript:linkToThisMap.select()">
		<label for="linkToThisPage" style="width: 40%">Link to this map</label>
		<input type="text" id="linkToThisMap" value="" style="width: 40%" />
	</div>
	<div style="width: 100%" onclick="javascript:rssLinkToThisMap.select()">
		<label for="rssLinkToThisMap" style="width:40%" >RSS to this map</label>
		<input type="text" id="rssLinkToThisMap" value="" style="width: 40%"/>
	</div>
</div>


<div id="loginWindow" title="Log in">
	<c:if test="${not empty param.login_error}">
	  <font color="red">
	
	    Your login attempt was not successful, try again.<br/><br/>
	    Reason: <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>.
	  </font>
	</c:if>
	
	
	<form name="loginForm" action="<c:url value='j_spring_openid_security_check'/>" method="POST">
	
	<div id="loginTabs">
		<ul>
			<li><a href="#logintab-openid">OpenId</a></li>
			<li><a href="#logintab-google">Google</a></li>
			<li><a href="#logintab-yahooid">Yahoo! ID</a></li>
			<li><a href="#logintab-bloggercom">Blogger.com</a></li>
		</ul>
		<div id="logintab-openid">
			<div>
				<img src="img/logo_openid.png"/>
			</div>
			<div>
			    <label for="j_username">Your <a id="openIdLink" href="https://openid.org/home">OpenID</a> Identity:</label> 
			    <input id="openidUrl" type='text' name='j_username' value='<c:if test="${not empty param.login_error}"><c:out value="${SPRING_SECURITY_LAST_USERNAME}"/></c:if>'/>
			</div>
			<div>
			    <button onclick="document.loginForm.submit()">Log in</button>
		    </div>
		</div>
		<div id="logintab-google">
			<div>
				<p><img src="img/google-logo.png" style="float: right;"/> Please click on the button below to log in with your google account!
				</p>
			</div>
			<div>
				<button id="googleLoginButton" onclick="$('#openidUrl').val('https://www.google.com/accounts/o8/id'); document.loginForm.submit()">Log in</button>
			</div>
		</div>
		<div id="logintab-yahooid">
			<div>
				Enter your Yahoo! ID
			</div>
			<div>
				<input 
					id="yahooIdInput" 
					onkeyup="$('#openidUrl').val('https://me.yahoo.com/'+$('#yahooIdInput').val())"/>
			</div>
			<div>
			    <button onclick="document.loginForm.submit()">Log in</button>
		    </div>
		</div>
		<div id="logintab-bloggercom">
			<div>
				Enter your Blogger blog URL
			</div>
			<div >
				<input 
					id="bloggerInput" 
					onkeyup="$('#openidUrl').val($('#bloggerInput').val())"/>
			</div>
			<div>
			    <button onclick="document.loginForm.submit()">Log in</button>
		    </div>
		</div>
	</div>
	
	</form>
</div>

<div id="productInfoWindow" title="About todomap">
	<h2>
	Todomap - development version
	</h2>
	<p>
	This site is running todomap version <%= VersionUtil.getVersionNumber() %>.
	</p>
	<p>
	Please note that this is an <strong>early beta</strong> version of the application under active development since Q3 2009.
	</p>
	<p>
	To find out more about the software, please visit the project page at 
	<a id="googleCodeLink" target="new" href="http://code.google.com/p/todomap/">google code</a>! Your contribution 
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
	<button id="submitNewTodoButton" onclick="submitNewTodo()">save</button>
	<button id="cancelNewTodoButton" onclick="$('#newTodo').dialog('close')">close</button>
</div>

<div id="wheretogoWindow" title="Where do you want to go?">
	<div>
		<!-- GeoIP is racism. -->
		<div style="width: 100%">
		<label for="wheretogoLocation">Location:</label>
		<input id="wheretogoLocation" onchange="gotoLocation()"/>
		<button id="wheretogoButton" onclick="gotoLocation()">Go!</button>
		<button id="nogoButton" onclick="$('#wheretogoWindow').dialog('close')">I like it here</button>
		<span id="wheretogoErrors"></span>
		</div>
	</div>
</div>

<div id="userDetailsWindow" title="Your details">
	<script type="text/javascript">
	function updateLocation() {
		$('#userDetailsHomeLocationLat').val(map.getCenter().lat());
		$('#userDetailsHomeLocationLng').val(map.getCenter().lng());
	}
	</script>
	<div id="userDetailsAccordion">
		<h3><a href="#userDetails">Your data</a></h3>
		<div>
			<div>
			<label for="userDetailsDisplayName">Display name</label>
			<input id="userDetailsDisplayName"/>
			</div>
			<div>
			<label for="userDetailsEmail">Email address (never displayed)</label>
			<input id="userDetailsEmail"/>
			</div>
		</div>
		<h3><a href="#userDetails">Your home location</a></h3>
		<div>
			<div>
			<label for="userDetailsEmail">Home location</label>
			<input id="userDetailsHomeLocationLat"/>,<input id="userDetailsHomeLocationLng"/>
			<button id="userDetailsUseCurrentLocation" onclick="updateLocation()">Use current</button>
			</div>
			<div>
				
			</div>
		</div>
	</div>
	<button id="saveUserDetailsButton" onclick="saveUserDetails()">Save</button>
</div>

<!-- tooltips here -->
<div id="tooltips" style="visibility: hidden; overflow: hidden; display: none;">
	<div id="homeButton-tooltip">
		<p><img src="img/gohome.png"/>Position the map to your home location</p>
	</div>
	<div id="loginButton-tooltip">
		<p><img src="img/keys.png"/>Log in with your OpenID account.<br/>You can use your <strong>Yahoo! ID</strong>, your <strong>google/blogger</strong> account, or <strong>any OpenID provider</strong>.</p>
	</div>
	<div id="logoutButton-tooltip">
		<p><img src="img/lock.png"/>Close your session to protect your data.</p>
	</div>
	<div id="yourDetailsButton-tooltip">
		<p><img src="img/user.png"/>Edit your personal data, like home location, email address, etc</p>
	</div>
	<div id="embedButton-tooltip">
		<p> <img src="img/feed.png"> Link and RSS feed URL to this map. </p>
	</div>
	<div id="infoButton-tooltip">
		<p> <img src="img/info.png"/> Information about the software and todomap.org </p>
	</div>
	<div id="statisticsButton-tooltip">
		<p> <img src="img/math.png"> Service statistics </p>
	</div>
	<div id="helpButton-tooltip">
		<p> <img src="img/help.png"> Todomap help </p>
	</div>
	<div id="gotoButton-tooltip">
		<p> <img src="img/earth.png"> Enter an address to jump to. </p>
	</div>
	<div id="openIdLink-tooltip">
		<p> Find your OpenID provider at openid.org! </p>
	</div>
	<div id="map_canvas-tooltip">
		<p> <img src="img/earth.png"/> Right click to add a new TODO! </p>
	</div>
	<div id="googleCodeLink-tooltip" class="tooltipable">
		<p> <img src="img/gear.png"/> This link will take you to the developer site where you can find the
		<ul>
			<li>bugtracker</li>
			<li>wiki</li>
			<li>and all the source code</li>
		</ul>
		</p>
	</div>
</div>

</body>
</html>