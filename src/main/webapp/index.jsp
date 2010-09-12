<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<%@ taglib prefix="i18n" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<%@page import="java.util.Locale"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="org.springframework.security.core.AuthenticationException" %>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>

<%@page import="org.todomap.o29.logic.Configuration"%>
<%@page import="org.todomap.o29.utils.VersionUtil"%>
<%@page import="org.todomap.o29.utils.URLUtil"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<%
final Configuration configuration = (Configuration)WebApplicationContextUtils
	.getRequiredWebApplicationContext(config.getServletContext()).getBean("config");
final Locale locale = (Locale)request.getAttribute("locale");
%>

<i18n:setLocale value="<%= locale %>"/>
<i18n:setBundle basename="Messages"/>
<head>
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<meta name="google-site-verification" content="<%= configuration.getGoogleWebmastersVerification() %>" />
<meta name="y_key" content="<%= configuration.getYahooSiteExplorerVerification() %>">
<meta name="description" content="<i18n:message key="etc.description"/>" lang="<i18n:message key="etc.language"/>"/>
<meta name="keywords" content="<i18n:message key="etc.keywords">democracy, direct democracy, map, public infrastructure, issues</i18n:message>" />

<link rel="license" href="http://creativecommons.org/licenses/by/3.0/"/>
<link REL="SHORTCUT ICON" HREF="img/earth.ico"/>
<link rel="icon" href="img/earth.ico"/>

<title><i18n:message key="title">todomap</i18n:message></title>

<link rel="stylesheet" type="text/css"
	href="style/jquery-ui-1.7.2.custom.css" media="all" />

<link rel="stylesheet" type="text/css"
	href="style/default.css" media="all" />
<script type="text/javascript" src="http://www.google.com/jsapi">
</script>
<script type="text/javascript" src="geoip.js">
</script>
<script type="text/javascript" src="scripts/json.js">
</script>
<script type="text/javascript" src="scripts/jquery-1.3.2.min.js">
</script>
<script type="text/javascript" src="scripts/jquery-ui-1.7.2.js">
</script>
<script type="text/javascript"
	src="http://maps.google.com/maps/api/js?sensor=false&language=<%= locale.getLanguage() %>">
</script>
<script type="text/javascript" src="scripts/jquery.heavylogic.autocomplete-1.0.js">
</script>
<script type="text/javascript" src="scripts/jquery.tagcloud.js">
</script>
<script type="text/javascript" src="scripts/jquery.cookie.js">
</script>
<script type="text/javascript" src="scripts/o29.js">
</script>
<script type="text/javascript" src="scripts/trolltip.js">
</script>
<script type="text/javascript" src="scripts/ajaxfileupload.js">
</script>

<script type="text/javascript">

	var pageLocation = '<%= request.getRequestURL() %>';

	var isAuthenticated = null;
    var map;
    var myLatlng;
    var zoomL;
	var infoWindows = new Array();

    (function () {
    	google.maps.Marker.prototype.todoId = -1;

    	google.maps.Marker.prototype.getTodoId = function() {
        	return this.todoId;
        }
    	google.maps.Marker.prototype.setTodoId = function(todoId) {
        	this.todoId = todoId;
        }
    })();

	(function() {
		google.maps.InfoWindow.prototype._open = function (map,marker) {
			this.open(map, marker);
			infoWindows[infoWindows.length] = this;
		}
	})();

	function closeAllInfoWindow() {
		for(var i=0; i<infoWindows.length; i++){
			infoWindows[i].close();
		}
		infoWindows = new Array();
	}

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
if(request.getSession(false) != null && request.getSession().getAttribute("returnToLat") != null){
%>
		myLatlng = new google.maps.LatLng(<%=request.getSession().getAttribute("returnToLat") %>,<%=request.getSession().getAttribute("returnToLng") %>);
		zoomL = <%=request.getSession().getAttribute("returnToZoom") %>;
<%
} else if(request.getParameter("lat") == null) {
%>
        myLatlng = ipBasedLocation();
        //locale-based location
        if(myLatlng == null) {
        	myLatlng = new google.maps.LatLng(<i18n:message key="etc.initloc.lat"/>,<i18n:message key="etc.initloc.lng"/>);
        	if(todomap.geoip != 'Unknown') {
        		geocoder = new google.maps.Geocoder();
        		geocoder.geocode(todomap.geoip , function(results, status) {
        	        if (status == google.maps.GeocoderStatus.OK) {
        	          map.setCenter(results[0].geometry.location);
        	        }
       	      });
            }
        }
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
			map.setOptions({draggableCursor:undefined});
		});
        
		google.maps.event.addListener(map, "click", function(event) {
			debug(map.draggableCursor);
			if(map.draggableCursor == 'crosshair') {
				popupAddTodoWindow(event);
				map.setOptions({draggableCursor:undefined});
			}
		});

		
		google.maps.event.addListener(map, "bounds_changed", refreshMarkers);
        
    }
    $(document).ready(function(){
        $("#toolsAccordion").accordion({
           });
        $("#userDetailsAccordion").accordion({});
        $('#newTodoAccordion').accordion({});
        $('#todoWindow').dialog({
            autoOpen : false,
            width: 600,
            height: 400
        });
        $("#helpWindow").dialog({
            autoOpen : false,
            title	:	'<i18n:message key="window.help"/>'
           });
        $('#searchWindow').dialog({
            autoOpen : false,
            title	:	'<i18n:message key="window.search"/>',
            position: 'left',
            stack: false,
            maxHeight : 400,
            maxWidth : 400
        });
        $("#newTodo").dialog({
            modal: true,
            autoOpen : false,
            width: 600,
            height: 400,
            title: '<i18n:message key="window.newTodo.title"/>'
           });
        
        $("#productInfoWindow").dialog({
            modal: true,
            width: 600,
            height: 400,
            show: 'slide',
            autoOpen : ($.cookie('todomap-intro-cookie') == undefined),
            title: '<i18n:message key="window.productInfoWindow.title">About todomap</i18n:message>'
           });
        $.cookie('todomap-intro-cookie', 'true', { path: '/', expires: 65535 });
        $("#loginWindow").dialog({
        	modal: true,
            autoOpen : <%= request.getParameter("error") != null %>,
            show: 'slide',
            width: 650,
            height: 400,
            position: 'top',
            title: '<i18n:message key="window.login.title"/>'
            });
        $('#todoExtraWindow').dialog({
        	modal: false,
            autoOpen : false,
            show: 'slide',
            width: 650,
            height: 400,
            title: '<i18n:message key="todo.extraWindow.title"/>'
            });
        $("#linksWindow").dialog({
            autoOpen : false,
            title	: '<i18n:message key="window.links.title"/>'
            });
        $('#wheretogoWindow').dialog({
        	autoOpen : false,
        	width: 400,
        	title	: '<i18n:message key="window.wheretogoWindow.title"/>'
            });
        $('#userDetailsWindow').dialog({
        	autoOpen : false,
            width: 500,
            height: 450,
            title:	'<i18n:message key="window.userDetailsWindow.title"/>'
            });
        $('#loginTabs').tabs( { cookie: { name: 'todomap-login-tab', expires: 30 } } );
        $('#infoTabs').tabs( { cookie: { name: 'todomap-info-tab', expires: 30 } } );
        $('#todoExtraTabs').tabs();
        $('#addTags').autocomplete(
                'addtags-autocompletelist',
                'autocomplete',
                'services/rest/tags/list/<%=locale.getLanguage()%>/', 
                function(){ debug('click me babe, I am not afraid'); },
                'lastTodoId',
                '<%=locale.getLanguage()%>'
         );

        refreshTagClouds('<%=locale.getLanguage()%>');
        refreshBookmarks();

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
			$.get("services/rest/todos/area.sht/"+sw.lat() + "," + sw.lng() + "," + ne.lat() + "," + ne.lng(), function(data){
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
						    if(val['status'] == 'Closed') {
						    	marker.setIcon('img/fixed.png');
						    } else if(val['status'] == 'Pending') {
							    	marker.setIcon('img/pending.png');
							} else {
								marker.setIcon('img/flag.png');
							}
						    map.addMarker(marker);
						    google.maps.event.addListener(marker, 'click', function() {
							    openInfoBubble(val['id'], marker);
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
						var latitude = val['location']['latitude'];
						var longitude = val['location']['longitude'];
						var zoomLevel = map.getZoom();
						var zoom = 1;
						if(zoomLevel >= 10) {
							zoom = 13;
						} else if (zoomLevel >= 5) {
							zoom = 10;
						}
						var mLatlng = new google.maps.LatLng(latitude, longitude);
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
					    		content: '<div style="width: 200px; height: 100px; overflow: hidden;">'
					    			+ '<h3 style="margin: 5px; font-size: 15px; width: 160px;">'+getAddress(val['address'])+'</h3>'
					    			+ '<div style="overflow: hidden; margin: 10px; text-align: justify; font-size: 12px; width: 160px; height: 50px;">'
					    			+ '<i18n:message key="etc.openIssues"/> : '+  val['nrOfIssues'] + '<br>'
					    			+ '<i18n:message key="etc.pendingIssues"/> : '+  val['nrOfPendingIssues'] + '<br>'
					    			+ '<i18n:message key="etc.closedIssues"/> : '+  val['nrOfClosedIssues'] + '<br>'
						    		+ '</div>'
					    		    + '<img src="img/search32.png" style="position: absolute; top: 0px; right: 0px; cursor: pointer;" onclick="map.panTo(new google.maps.LatLng('+latitude+','+longitude+')); closeAllInfoWindow(); map.setZoom('+zoom+');"/>'
					    		    + '<a style="color: #FFFFFF;" href="'+getRssUrlForAddr(val['address'])+'" target="_blank"><img src="img/feed32.png" style="position: absolute; top: 32px; right: 0px; cursor: pointer;"/></a>'
					    			+ '</div>'
					        });
					        infowindow._open(map,marker);
				        });

					}
				});
			};
		
			if (zoomLevel >= 10) {
				prepareForLevel('town');
				$.get("services/rest/todos/area/town/"+sw.lat() + "," + sw.lng() + "," + ne.lat() + "," + ne.lng(), fn);
			} else if (zoomLevel > 5) {
				prepareForLevel('state');
				$.get("services/rest/todos/area/state/"+sw.lat() + "," + sw.lng() + "," + ne.lat() + "," + ne.lng(), fn);
			} else {
				prepareForLevel('country');
				$.get("services/rest/todos/area/country/"+sw.lat() + "," + sw.lng() + "," + ne.lat() + "," + ne.lng(), fn);
			}
		}
	}

	/**
	 * Opens an infowindow for a Todo filled with the data it requests from the server.
	 * id: the todo ID
	 * marker: optionally the marker of the todo
	 */
	function openInfoBubble(id, marker) {
	    $.get("services/rest/todofacade/get/"+id, function(data){
		    var todo_rel = eval("("+data+")");
		    var todo = todo_rel['todo-rel'];
		    var shortDescr = todo['todo']['shortDescr'];
		    var rating = todo['rating'];
		    var ratingSum = todo['ratingSummary'];
		    var itemId = todo['todo']['id'];
		    var fullLink = encodeURI(itemId + '-' + todo['todo']['shortDescr'] + '.html');
	    	var infowindow = new google.maps.InfoWindow({
	    		content: '<div style="overflow:hidden; width: 200px; height: 200px;">'
	    			+ '<h3 style="margin: 5px; font-size: 15px; width: 160px;">'+shortDescr+'</h3>'
	    			+ '<div class="infowindow">'
	    			+ todo['todo']['description']
		    		+ '</div>'
		    		+ '<span class="morelink">'
		    		+ '<a onclick="openInTodoWindow(\''+fullLink+'\')"><i18n:message key="etc.more"/></a>'
	    		    + '<a href="'+ fullLink + '" target="_blank"><img src="img/external-link.png"/></a>'
	    		    + '</span>'
	    		    + '<div id="bookmark_togle_'+itemId+'" class="starTogle_'+(todo['bookmarked'] ? '' : 'in')+'active" style="position: absolute; top: 0px; right: 0px;" onclick="togle(\'bookmark_togle_'+itemId+'\',function(t,isAdd){ if(isAdd) {bookmarkItem('+itemId+');} else {unbookmarkItem('+itemId+');}})"></div>'
	    		    
	    			+ '<div id="voteup_'+itemId+'" class="' + (rating && rating['rate'] >= 0 ? 'voteUp_selected' : 'voteUp_unselected') + '" style="position: absolute; top: 32px; right: 0px;" ' 
	    			+ (rating ? '' : 'onclick="voteUp('+itemId+', function(){updateVoted('+itemId+', true)})"' ) + '></div>'
	    			
	    			+ '<div id="votedown_'+itemId+'" class="' + (rating && rating['rate'] < 0 ? 'voteDown_selected' : 'voteDown_unselected') + '" style="position: absolute; bottom: 30px; right: 0px;" ' 
	    			+ (rating ? '' : 'onclick="voteDown('+itemId+', function(){updateVoted('+itemId+', false)})"') + '></div>'

					+ '<div class="ratingsummary" id="rating-'+itemId+'">'
	    			+ getRatingSum(ratingSum)
					+ '</div>'
	    			+ '</div>'
	        });
	    	closeAllInfoWindow();
	        infowindow._open(map,marker);
	        if(marker == null) {
		        debug(todo.todo.location.latitude);
		        debug(todo.todo.location.longitude);
		        infowindow.setPosition(new google.maps.LatLng(todo.todo.location.longitude, todo.todo.location.latitude));
		    }
		})
	}
	
	function getRatingSum(ratingSum) {
		return ' '
		+ ((ratingSum['average'] && ratingSum['nrOfRatings']) ? ('<i18n:message key="etc.average"/>: ' + ratingSum['average'] + ' <i18n:message key="etc.votes"/>: ' + ratingSum['nrOfRatings']) : ('<i18n:message key="etc.novotes"/>'));
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
			url : 'services/rest/todos/new',
			data: strData,
			success: function(newTodo){
				refreshMarkers();
				$("#newTodo").dialog('close');
				$('#submitNewTodoButton').removeAttr('disabled');
				$('#tagList').empty();
				$('#attachments').empty();
				$('#todoExtraWindow').dialog('open');
				$('#todoExtraWindow_thanks').show();
				$('#lastTodoId').val(newTodo.todo.id);
				setTimeout(function(){$('#todoExtraWindow_thanks').hide(1000);}, 4000);
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
			url		: 'services/rest/rating/add/' + todoId,
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
				'latitude' 	: $('#userDetailsHomeLocationLat').val(),
				'longitude'		: $('#userDetailsHomeLocationLng').val()
				}
			}
		};
		$.ajax({
			url		: 'services/rest/home/user/set',
			type	: 'POST',
			data	: JSON.stringify(user),
			success	:$('#userDetailsWindow').dialog('close'),
			contentType : 'application/json',
			dataType : 'json'
		});
	}

	function getUserDetails() {
		$.get("services/rest/home/user/get", function(data) {
			debug(data);
			var userData = eval("("+data+")");
			$('#userDetailsDisplayName').val(userData['user']['displayName']);
			$('#userDetailsEmail').val(userData['user']['email']);
			try {
				$('#userDetailsHomeLocationLat').val(userData['user']['homeLoc']['latitude']);
				$('#userDetailsHomeLocationLng').val(userData['user']['homeLoc']['longitude']);
				$.each(userData.user.userLinks, function() {debug('x')});
				updateAddr('userDetailsHomeLocationReverseGeo','userDetailsHomeLocationLat','userDetailsHomeLocationLng');
				//TODO: this code only handles n > 1 case for user links
				//I wonder why cxf does not serialize an empty or single-element array instead.
				//but aanyway this needs work.
				$('#userLinks').empty();
				var links = '<ul>';
				if($.isArray(userData['user']['userLinks'])) {
					for(i = 0; i < userData['user']['userLinks'].length; i++) {
						links = links + 
							'<li><a target="_blank" href="' + userData['user']['userLinks'][i]['url'] + '">' 
							+ userData['user']['userLinks'][i]['desc'] 
							+ '</a> <a class="removeitemlink" onclick="removeUserLink('+userData['user']['userLinks'][i]['id']+')">x</a></li>';
					}
				} else {
					console.log('not array');
				}
				links = links + '<ul>';
				debug(links);
				$('#userLinks').html(links);
			} catch (fubar) {
				debug(fubar);
			}
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
	
	if(isAuthenticated) {
		$(".authOnly").show(1000);
		$(".noAuthOnly").hide(1000);
	} else {
		$(".authOnly").hide(1000);
		$(".noAuthOnly").show(1000);
	}

function updateMapReturnValues() {
	$('#returnToLat').val(map.getCenter().lat());
	$('#returnToLng').val(map.getCenter().lng());
	$('#returnToZoom').val(map.getZoom());
}

function updateVoted(id, votedUp) {
	$('votedown_'+id).attr('onclick','');
	$('voteup_'+id).attr('onclick','');
//	$('votedown_'+id).addClass((votedUp ? 'voteDown_unselected' : 'voteDown_selected'));
//	$('voteup_'+id).addClass((votedUp ? 'voteUp_selected' : 'voteUp_unselected'));
}

function attachFile() {
	$.ajaxFileUpload
	(
		{
			url: 'upload/'+ $('#lastTodoId').val(),
			secureuri:false,
			fileElementId:'fileToUpload',
			success: function (data, status)
			{
				debug('success');
				updateAttachments($('#lastTodoId').val());
			},
			error: function (data, status, e)
			{
				alert(e);
			}
		}
	);
}

function openInTodoWindow(url) {
	$('#todoWindow').dialog('open');
	$('#todoWindow_contentFrame').attr('src',url);
}

function enterAddIssue() {
	map.setOptions({
		draggableCursor: 'crosshair'
      });
}

function popupAddTodoWindow(event) {
	$("#newTodoLat").val(event.latLng.lat());
	$("#newTodoLng").val(event.latLng.lng());
	updateAddr('todoReverseGeo','newTodoLat','newTodoLng');
	if(isAuthenticated) {
		$("#newTodo").dialog('open');
	} else {
		$("#loginWindow").dialog('open');
	}
}

function search() {
	var text = $('#searchtext').val();
	if(text != ''){
		$.ajax({
			  type: 'GET',
			  url: 'services/rest/base/search/'+text,
			  success: function(data) {
			  	var content = '';
			  	$.each(data.base, function(i, item){
				  		debug(item.todo);
				  		content = content + '<div class="searchresultrow" onclick="openInTodoWindow(\''+item.todo.id+'.html\')">'+item.todo.shortDescr+'</div>'
				  	});
			  	$('#searchWindowContent').html(content);
			  	$('#searchWindow').dialog('open');
			  },
			contentType : 'application/json',
			dataType : 'json'
			});
	}
}

</script>


</head>
<body onload="initialize()" style="margin: 0px; padding: 0px;">

<div style="width: 100%; height: 100%;">
	<div id="sidebar" style="width: 20%; height: 100%; position: absolute; left: 0px;">
		<!-- search -->
		<div class="search">
			<input type="text" onchange="search()" id="searchtext" style="width: 80%; float: left"> <button style="width: 32px" onclick="search()" class="tooltipable" id="searchButton"><img src="img/search32.png" style="width: 20px; height: 20px"/></button>
		</div>
		<!-- //search -->
		<div id="toolsAccordion">
			<h3><a href="#" id="toolsAccordionLink" class="tooltipable"><i18n:message key="sidebar.accordion.tools"> Tools </i18n:message></a></h3>
			<div class="sidebarControls">
				<span class="authOnly">
				<button id="logoutButton" class="tooltipable" onclick="logOut()"> <i18n:message key="sidebar.button.logout"> Log out </i18n:message> </button><br/>
				<button id="yourDetailsButton" class="tooltipable" onclick="$('#userDetailsWindow').dialog('open'); getUserDetails();"> <i18n:message key="sidebar.button.yourdetails"> Your details </i18n:message> </button><br/>
				</span>
				<span class="noAuthOnly">
				<button id="loginButton" class="tooltipable" onclick="$('#loginWindow').dialog('open')"> <i18n:message key="sidebar.button.login"> Log in / Register </i18n:message> </button><br/>
				</span>
				<button id="embedButton" class="tooltipable" onclick="$('#linksWindow').dialog('open')"> <i18n:message key="sidebar.button.links"> Link to this map </i18n:message> </button><br/>
				<button id="gotoButton" class="tooltipable" onclick="$('#wheretogoWindow').dialog('open')"> <i18n:message key="sidebar.button.goto"> Enter address </i18n:message> </button><br/>
				<span class="authOnly">
				<button id="homeButton" class="tooltipable" onclick="goHome()"> <i18n:message key="sidebar.button.gohome"> Go Home </i18n:message> </button><br/>
				<button id="addIssueButton" class="tooltipable"  onclick="enterAddIssue()"> <i18n:message key="sidebar.button.addIssue"> Add issue </i18n:message> </button><br/>
				</span>
			</div>
			<h3><a href="#" id="infoAccordionLink" class="tooltipable"><i18n:message key="sidebar.accordion.info">Info</i18n:message></a></h3>
			<div class="sidebarControls">
				<button id="infoButton" class="tooltipable" onclick="$('#productInfoWindow').dialog('open')"> <i18n:message key="sidebar.button.about">About todomap </i18n:message></button><br/>
				<button id="statisticsButton" class="tooltipable" onclick="$('#statisticsWindow').dialog('open')"> <i18n:message key="sidebar.button.statistics">Statistics</i18n:message> </button><br/>
				<button id="helpButton" class="tooltipable" onclick="$(helpWindow).dialog('open')"> <i18n:message key="sidebar.button.help">Help</i18n:message> </button><br/>
			</div>
			<h3><a href="#" id="bookmarksAccordionLink" class="tooltipable"><i18n:message key="sidebar.accordion.bookmarks">Bookmarks</i18n:message></a></h3>
			<div class="sidebarControls">
				<span class="authOnly">
					<span id="bookmarks">
						&nbsp;
					</span>
				</span>
				<!-- 
				<span  class="noAuthOnly">
					<p>
						<img alt="bookmark" src="img/bookmark.png"> You have to log in to see your bookmarks.
					</p>
				</span>
				 -->
			</div>
		</div>
		<div class="versionInfo">
			<span>
			<img alt="logo" src="img/todomap-logo.jpg" width="80%"><br/>
			<i18n:message key="etc.version">Version</i18n:message>: <%= VersionUtil.getVersionNumber() %></span>
		</div>
		<div class="licenseInfo">
			<a href="http://creativecommons.org/licenses/by/3.0/">
				<img alt="creative commons logo" src="img/cc88x31.png"/>
			</a>
			<i18n:message key="etc.license">
			The content of the site is published under creative commons license.<br/>
			The map is copyrighted by <a href="http://www.google.com/">google<a/>.
			</i18n:message>
		</div>
		<a class="addthis_button" href="http://www.addthis.com/bookmark.php?v=250&amp;username=kozka"><img src="http://s7.addthis.com/static/btn/v2/lg-share-en.gif" width="125" height="16" alt="Bookmark and Share" style="border:0"/></a><script type="text/javascript" src="http://s7.addthis.com/js/250/addthis_widget.js#username=kozka"></script>
	</div>
	<span style="width: 80%; height: 100%; position: absolute; right: 0px;">
		<div id="map_canvas" class="tooltipable" style="width: 100%; height: 100%"></div>
	</span>
	<div id='tooltip'>
		&nbsp;
	</div>
</div>

<div id="searchWindow">
	<span id="searchWindowContent"></span>
</div>


<div id="helpWindow">
	<i18n:message key="etc.underconstruction">TODO.</i18n:message>
</div>

<div id="todoWindow">
	<iframe id="todoWindow_contentFrame" style="width: 100%; height: 100%; border: none;" src="about:blank"></iframe>
</div>

<div id="linksWindow" title="Links to this map">
	<span style="width: 100%" onclick="javascript:linkToThisMap.select()">
		<label for="linkToThisPage" style="width: 40%"><i18n:message key="window.links.link">Link to this map</i18n:message></label>
		<img style="cursor: pointer;" src="img/external-link.png" onclick="window.open( $('#linkToThisMap').val(), '_blank' )"></img>
		<input type="text" id="linkToThisMap" value="" style="width: 40%" /> 
	</span>
	<span style="width: 100%" onclick="javascript:rssLinkToThisMap.select()">
		<label for="rssLinkToThisMap" style="width:40%" ><i18n:message key="window.links.rss">RSS to this map</i18n:message></label>
		<img style="cursor: pointer;" src="img/external-link.png" onclick="window.open( $('#rssLinkToThisMap').val(), '_blank' )"></img>
		<input type="text" id="rssLinkToThisMap" value="" style="width: 40%"/> 
	</span>
</div>

<div id="todoExtraWindow" style="overflow: hidden">
	<div id="todoExtraWindow_thanks">
		<input type="hidden" id="lastTodoId"/>
		<i18n:message key="todo.done">Thank you! Your TODO was registered in the database.</i18n:message>
		<i18n:message key="todo.more">Could you help with more information?</i18n:message>
	</div>
	<div id="todoExtraTabs" style="width:100%; height: 100%">
		<ul>
			<li><a href="#todoextra-tagcloud"><i18n:message key="sidebar.accordion.tagcloud">Tag cloud</i18n:message></a></li>
			<li><a href="#todoextra-attach"><i18n:message key="todo.attachments">Attachments</i18n:message></a></li>
		</ul>
		<div id="todoextra-tagcloud">
			<div style="width: 70%; float: left">
				<div id="newTodoTagCloud" class="tagcloud" style="width: 100%; height: 100%;"></div>
			</div>
			<div style="width: 30%; float: left">
				<div id="tagList" class="tagList" style="width: 100%; height: 100%;"></div>
			</div>
			<div style="position: absolute; bottom: 10px;">
				<input id="addTags"/>
				<button id="addTag" class="tooltipable" onclick="addTag($('#lastTodoId').val(), '<%=locale.getLanguage()%>', $('#addTags').val())"><i18n:message key="tag.add">add tag</i18n:message></button>
			</div>
		</div>
		<div id="todoextra-attach">
			<div style="width: 100%" id="attachments">
				
			</div>
			<div style="position: absolute; bottom: 10px;">
				<input type="file" id="fileToUpload" name="file"/>
				<button id="uploadButton" onclick="return attachFile();">
					<i18n:message key="todo.uploadButton">upload</i18n:message>
				</button>
			</div>
		</div>
	</div>
</div>

<div id="loginWindow" title="Log in">

	<form name="loginForm" action="<c:url value='j_spring_openid_security_check'/>" method="POST">

	<input type="hidden" name="returnToLat" id="returnToLat" value=""/>
	<input type="hidden" name="returnToLng" id="returnToLng" value=""/>
	<input type="hidden" name="returnToZoom" id="returnToZoom" value=""/>

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
			    <label for="j_username"><i18n:message key="window.login.username"> Your <a id="openIdLink" class="tooltipable" href="https://openid.org/home">OpenID</a> Identity:</i18n:message></label> 
			    <input id="openidUrl" type='text' name='openid_identifier' value='<c:if test="${not empty param.login_error}"><c:out value="${SPRING_SECURITY_LAST_USERNAME}"/></c:if>'/><br/>
			</div>
			<div>
			    <button onclick="document.loginForm.submit()"><i18n:message key="window.login.login">Log in</i18n:message></button>
		    </div>
		</div>
		<div id="logintab-google">
			<p>
				<img src="img/google-logo.png" style="float: right;"/> 
				<span><i18n:message key="window.login.googletext">Please click on the button below to log in with your google account!</i18n:message></span>
			</p>
			<button id="googleLoginButton" onclick="updateMapReturnValues(); $('#openidUrl').val('https://www.google.com/accounts/o8/id'); document.loginForm.submit()"><i18n:message key="window.login.login">Log in</i18n:message></button>
		</div>
		<div id="logintab-yahooid">
			<label for="yahooIdInput">
				<i18n:message key="window.login.yahooIdInput">Enter your Yahoo! ID</i18n:message>
			</label>
			<input 
				id="yahooIdInput" 
				onkeyup="$('#openidUrl').val('https://me.yahoo.com/'+$('#yahooIdInput').val())"/><br/>
		    <button onclick="updateMapReturnValues(); document.loginForm.submit()"><i18n:message key="window.login.login">Log in</i18n:message></button>
		</div>
		<div id="logintab-bloggercom">
			<label for="bloggerInput">
				<i18n:message key="window.login.bloggerInput">Enter your Blogger blog URL</i18n:message>
			</label>
			<input 
				id="bloggerInput" 
				onkeyup="$('#openidUrl').val($('#bloggerInput').val())"/><br/>
		    <button onclick="updateMapReturnValues(); document.loginForm.submit()"><i18n:message key="window.login.login">Log in</i18n:message></button>
		</div>
	</div>
	<label for="returnToPosition"><i18n:message key="window.login.returnhere">return here</i18n:message></label>
	<input type="checkbox" id="returnToPosition" name="returnToPosition" checked="checked"/><br/>
	<% 
	if(request.getParameter("error") != null 
			&& session != null 
			&& session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION") != null) { %>
	  <font color="red">
	    <i18n:message key="">Login error</i18n:message><br/>
	    <%= ((Exception)request.getSession(false).getAttribute("SPRING_SECURITY_LAST_EXCEPTION")).getMessage() %>
	  </font>
	<% } %>
	
	</form>
</div>

<div id="productInfoWindow">
	<div id="infoTabs">
		<ul>
			<li><a href="#infotab-intro"><i18n:message key="window.productInfoWindow.intro">Intro</i18n:message></a></li>
			<li><a href="#infotab-about"><i18n:message key="window.productInfoWindow.about">About todomap</i18n:message></a></li>
		</ul>
		<div id="infotab-intro">
			<i18n:message key="window.productInfoWindow.introText">detailed info</i18n:message>
		</div>
		<div id="infotab-about">
			<h2>
			<i18n:message key="window.productInfoWindow.head">Todomap - development version</i18n:message>
			</h2>
			<p>
			<i18n:message key="window.productInfoWindow.version">This site is running todomap version</i18n:message> <%= VersionUtil.getVersionNumber() %>
			</p>
			<p>
			<i18n:message key="window.productInfoWindow.betanote"> Please note that this is an <strong>early beta</strong> version of the application under active development since Q3 2009.</i18n:message>
			</p>
			<p>
			<i18n:message key="window.productInfoWindow.more">
			To find out more about the software, please visit the project page at 
			<a id="googleCodeLink" class="tooltipable" target="_blank" href="http://code.google.com/p/todomap/">google code</a>! Your contribution 
			to todomap's success is highly appreciated!
			</i18n:message>
			</p>
			<p>
				<img alt="todomap logo" src="img/todomap-logo.jpg">
			</p>
		</div>
	</div>
</div>

<div id="newTodo">
	<form>
		<div id="newTodoAccordion">
			<h3><a href="newTodoDescription"> <i18n:message key="todo.details"> Details </i18n:message> </a></h3>
			<div>
				<label for="newTodoShortDescr"><i18n:message key="window.newTodo.newTodoShortDescr">Short description</i18n:message></label> <input type="text" name="newTodoShortDescr" id="newTodoShortDescr"/><br/>
				<label for="newTodoDescription"><i18n:message key="window.newTodo.newTodoDescription">Details</i18n:message></label><br/>
				<textarea style="width: 100%" id="newTodoDescription" name="newTodoDesription"></textarea><br/>
			</div>
			<h3><a href="#newTodoLocation"> <i18n:message key="todo.location"> Location </i18n:message> </a></h3>
			<div>
				<label for="newTodoLat"><i18n:message key="etc.latitude">Latitude</i18n:message></label><input id="newTodoLat" name="newTodoLat" onkeyup="updateAddr('todoReverseGeo','newTodoLat','newTodoLng')"/><br/>
				<label for="newTodoLng"><i18n:message key="etc.longitude">Longitude</i18n:message></label><input id="newTodoLng" name="newTodoLng" onkeyup="updateAddr('todoReverseGeo','newTodoLat','newTodoLng')"/><br/>
				<div class="calculated">
				<label id="todoReverseGeo-label" class="tooltipable" for="todoReverseGeo"><i18n:message key="window.newTodo.reversegeo">Address</i18n:message></label><input id="todoReverseGeo" name="newTodoLng" onkeyup="updateLatLong('todoReverseGeo','newTodoLat','newTodoLng')"/><br/>
				</div>
			</div>
		</div>
	</form>
	<button id="submitNewTodoButton" onclick="submitNewTodo()"> <i18n:message key="etc.save">save</i18n:message></button>
	<button id="cancelNewTodoButton" onclick="$('#newTodo').dialog('close')"><i18n:message key="etc.close">close</i18n:message></button>
</div>

<div id="wheretogoWindow">
	<div>
		<!-- GeoIP is racism. -->
		<label for="wheretogoLocation"><i18n:message key="etc.location">Location</i18n:message></label>
		<input id="wheretogoLocation" onchange="gotoLocation()"/><br/>
		<button id="wheretogoButton" onclick="gotoLocation()"><i18n:message key="etc.go">Go!</i18n:message></button>
		<button id="nogoButton" onclick="$('#wheretogoWindow').dialog('close')"><i18n:message key="etc.nogo">I like it here</i18n:message></button>
		<span id="wheretogoErrors"></span>
	</div>
</div>

<div id="userDetailsWindow">
	<script type="text/javascript">
	function updateLocation() {
		$('#userDetailsHomeLocationLat').val(map.getCenter().lat());
		$('#userDetailsHomeLocationLng').val(map.getCenter().lng());
	}
	</script>
	<div id="userDetailsAccordion">
		<h3><a href="#userDetails"> <i18n:message key="window.userDetailsWindow.userDetails"> Your data </i18n:message> </a></h3>
		<span>
			<label for="userDetailsDisplayName"><i18n:message key="window.userDetailsWindow.userDetailsDisplayName">Display name</i18n:message></label>
			<input id="userDetailsDisplayName"/><br/>
			<label for="userDetailsEmail"><i18n:message key="window.userDetailsWindow.userDetailsEmail">Email address (never displayed)</i18n:message></label>
			<input id="userDetailsEmail"/><br/>
		</span>
		<h3><a href="#homeLocation"> <i18n:message key="window.userDetailsWindow.homeLocation"> Your home location </i18n:message></a></h3>
		<div>
			<label for="userDetailsHomeLocationLat"><i18n:message key="etc.latitude">Latitude</i18n:message></label>
			<input id="userDetailsHomeLocationLat" onkeyup="updateAddr('userDetailsHomeLocationReverseGeo','userDetailsHomeLocationLat','userDetailsHomeLocationLng')"/><br>
			<label for="userDetailsHomeLocationLng"><i18n:message key="etc.longitude">Longitude</i18n:message></label>
			<input id="userDetailsHomeLocationLng" onkeyup="updateAddr('userDetailsHomeLocationReverseGeo','userDetailsHomeLocationLat','userDetailsHomeLocationLng')"/><br/>
			<div class="calculated">
			<label id="userDetailsHomeLocationReverseGeo-label" for="userDetailsHomeLocationReverseGeo"><i18n:message key="window.userDetailsWindow.reversegeo"></i18n:message></label>
			<input class="tooltipable" id="userDetailsHomeLocationReverseGeo" onkeyup="updateLatLong('userDetailsHomeLocationReverseGeo','userDetailsHomeLocationLat','userDetailsHomeLocationLng')"/><br/>
			</div>
			<button id="userDetailsUseCurrentLocation" onclick="updateLocation()">
				<i18n:message key="window.userDetailsWindow.useCurrent">use map center</i18n:message>
			</button>
		</div>
		<h3><a href="#profileLinks"> <i18n:message key="window.userDetailsWindow.profileLinks">Profile Links</i18n:message></a></h3>
		<div>
			<span id="userLinks">
			</span>
			<script type="text/javascript">
			function removeUserLink(id) {
				$.ajax( {
					type : 'POST',
					url : 'services/rest/users/user/link/'+id+'/remove',
					success : getUserDetails,
					error : function(XMLHttpRequest, textStatus, errorThrown) {
						handleErrors(XMLHttpRequest);
					},
					contentType : 'application/json',
					dataType : 'json'
				});
			}
			function addUserLink() {
				var data = {link: {url: $('#userLinkAdd_url').val(), desc: $('#userLinkAdd_desc').val()}};
				$.ajax( {
					type : 'POST',
					url : 'services/rest/users/user/link/add',
					data : JSON.stringify(data),
					success : getUserDetails,
					error : function(XMLHttpRequest, textStatus, errorThrown) {
						handleErrors(XMLHttpRequest);
					},
					contentType : 'application/json',
					dataType : 'json'
				});
			}
			</script>
			<button id="userLinkAdd" onclick="$('#userLinkAddForm').show(); $('#userLinkAdd').hide()">add</button>
			<div id="userLinkAddForm" style="display: none">
				<label for="userLinkAdd_desc">Description</label>
				<input type="text" id="userLinkAdd_desc"><br/>
				<label for="userLinkAdd_url">URL</label>
				<input type="text" id="userLinkAdd_url"><br/>
				<button onclick="addUserLink()">Add</button>
				<button onclick="$('#userLinkAddForm').hide(); $('#userLinkAdd').show()">Cancel</button>
			</div>
		</div>
	</div>
	<button id="saveUserDetailsButton" onclick="saveUserDetails()"><i18n:message key="etc.save">Save</i18n:message></button>
</div>

<jsp:include page="WEB-INF/jsp/includes/statistics.jsp"></jsp:include>

<!-- tooltips here -->
<div id="tooltips" style="overflow: hidden; display: none;">
	<div id="searchButton-tooltip">
		<p><img src="img/search.png"/><i18n:message key="tooltip.searchButton"> Search the site content </i18n:message></p>
	</div>
	<div id="homeButton-tooltip">
		<p><img src="img/gohome.png"/> <i18n:message key="tooltip.homeButton"> Position the map to your home location </i18n:message> </p>
	</div>
	<div id="loginButton-tooltip">
		<p><img src="img/keys.png"/> <i18n:message key="tooltip.loginButton"> Log in with your OpenID account.<br/>You can use your <strong>Yahoo! ID</strong>, your <strong>google/blogger</strong> account, or <strong>any OpenID provider</strong>. </i18n:message></p>
	</div>
	<div id="logoutButton-tooltip">
		<p><img src="img/lock.png"/> <i18n:message key="tooltip.logoutButton"> Close your session to protect your data. </i18n:message></p>
	</div>
	<div id="yourDetailsButton-tooltip">
		<p><img src="img/user.png"/> <i18n:message key="tooltip.yourDetailsButton"> Edit your personal data, like home location, email address, etc </i18n:message></p>
	</div>
	<div id="embedButton-tooltip">
		<p> <img src="img/feed.png"> <i18n:message key="tooltip.embedButton"> Link and RSS feed URL to this map. </i18n:message> </p>
	</div>
	<div id="infoButton-tooltip">
		<p> <img src="img/info.png"/> <i18n:message key="tooltip.infoButton"> Information about the software and todomap.org </i18n:message> </p>
	</div>
	<div id="statisticsButton-tooltip">
		<p> <img src="img/math.png"> <i18n:message key="tooltip.statisticsButton"> Service statistics </i18n:message></p>
	</div>
	<div id="helpButton-tooltip">
		<p> <img src="img/help.png"> <i18n:message key="tooltip.helpButton"> Todomap help </i18n:message></p>
	</div>
	<div id="gotoButton-tooltip">
		<p> <img src="img/earth.png"> <i18n:message key="tooltip.gotoButton">Enter an address to jump to.</i18n:message> </p>
	</div>
	<div id="openIdLink-tooltip">
		<p> <i18n:message key="tooltip.openIdLink"> Find your OpenID provider at openid.org! </i18n:message> </p>
	</div>
	<div id="map_canvas-tooltip">
		<p> <img src="img/earth.png"/> <i18n:message key="tooltip.map_canvas"> You can navigate on the map or click on the flags for details. </i18n:message> </p>
	</div>
	<div id="googleCodeLink-tooltip">
		<p> <img src="img/gear.png"/> <i18n:message key="tooltip.googleCodeLink"> This link will take you to the developer site where you can find the
		<ul>
			<li>bugtracker</li>
			<li>wiki</li>
			<li>and all the source code</li>
		</ul>
		</i18n:message>
		</p>
	</div>
	<div id="todoReverseGeo-label-tooltip">
		<p>
			<img src="img/mail.png"> <i18n:message key="tooltip.reverseGeo"/>
		</p>
	</div>
	<div id="userDetailsHomeLocationReverseGeo-label-tooltip">
		<p>
			<img src="img/mail.png"> <i18n:message key="tooltip.reverseGeo"/>
		</p>
	</div>
	<div id="addTag-tooltip">
		<p>
			<i18n:message key="toolTip.addTag"></i18n:message>
		</p>
	</div>
	<div id="addIssueButton-tooltip">
		<p>
			<img src="img/wizard.png"/><i18n:message key="toolTip.addIssueButton">add issue</i18n:message>
		</p>
	</div>
	<div id="bookmarksAccordionLink-tooltip">
		<p>
			<img src="img/bookmark.png" /><i18n:message key="toolTip.bookmarksAccordion">Bookmarks</i18n:message>
		</p>
	</div>
	<div id="infoAccordionLink-tooltip">
		<p>
			<img src="img/bookmark.png" /><i18n:message key="toolTip.infoAccordion">Informations</i18n:message>
		</p>
	</div>
	<div id="toolsAccordionLink-tooltp">
		<p>
			<img src="img/configure.png"/><i18n:message key="toolTip.toolsAccordion">Tools</i18n:message>
		</p>
	</div>
</div>

</body>
</html>
