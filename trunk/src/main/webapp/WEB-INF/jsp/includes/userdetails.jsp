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
function updateLocation() {
	$('#userDetailsHomeLocationLat').val(map.getCenter().lat());
	$('#userDetailsHomeLocationLng').val(map.getCenter().lng());
}

$(document).ready(function(){
    $("#userDetailsAccordion").accordion({});
    $('#userDetailsWindow').dialog({
    	autoOpen : false,
        width: 500,
        height: 450,
        title:	'<i18n:message key="window.userDetailsWindow.title"/>'
        });
});

</script>

<div id="userDetailsWindow">
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