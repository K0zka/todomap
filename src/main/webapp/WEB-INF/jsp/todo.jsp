<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="i18n" %>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.util.Locale"%>
<%@page import="org.todomap.o29.beans.Tag"%>
<%@page import="org.todomap.o29.beans.Todo"%>
<%@page import="org.todomap.o29.beans.Attachment"%>
<%@page import="org.todomap.o29.beans.Comment"%>
<%
	final Todo todo = (Todo) request.getAttribute("todo");
	final Locale locale = (Locale) request.getAttribute("locale");
%>
<i18n:setLocale value="<%= locale %>"/>
<i18n:setBundle basename="Messages"/>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<%
	if (todo.getTags().size() > 0) {
%>
<meta name="keywords" content="<%=StringUtils.join(todo.getTags(), ',')%>" />
<%
	} else {
%>
<meta name="keywords" content="<i18n:message key="etc.keywords">democracy, direct democracy, map, public infrastructure, issues</i18n:message>" />
<%
	}
%>

<link REL="SHORTCUT ICON" HREF="img/earth.ico"/>

<script type="text/javascript"
	src="http://maps.google.com/maps/api/js?sensor=false&language=<%=locale.getLanguage()%>">
</script>

<link rel="stylesheet" type="text/css"
	href="style/jquery-ui-1.7.2.custom.css" media="all" />
<link rel="stylesheet" type="text/css"
	href="style/jquery.rating.css" media="all" />
<link rel="stylesheet" type="text/css"
	href="style/default.css" media="all" />
<link type="text/css" rel="stylesheet" href="style/jquery.rte.css" />
<link type="text/css" rel="stylesheet" href="style/jquery.tooltip.css" />

<script type="text/javascript" src="scripts/json.js">
</script>
<script type="text/javascript" src="scripts/jquery-1.3.2.min.js">
</script>
<script type="text/javascript" src="scripts/jquery-ui-1.7.2.js">
</script>
<script type="text/javascript" src="scripts/jquery.qtip-1.0.0-rc3.min.js">
</script>
<script type="text/javascript" src="scripts/jquery-ui-1.7.2.js">
</script>
<script type="text/javascript" src="scripts/o29.js">
</script>
<script type="text/javascript" src="scripts/jquery.rating.js">
</script>
<script type="text/javascript" src="scripts/jquery.rte.js">
</script>
<script type="text/javascript" src="scripts/jquery.rte.tb.js">
</script>
<script type="text/javascript" src="scripts/ajaxfileupload.js">
</script>
<script type="text/javascript" src="scripts/mbscrollable-1.5.7.js">
</script>
<script type="text/javascript" src="scripts/jquery.heavylogic.autocomplete-1.0.js">
</script>

<title><%=todo.getShortDescr()%></title>

<script type="text/javascript">

var isAuthenticated = null;
var version = <%=todo.getVersion()%>;
var id = <%=todo.getId()%>;
var marker;
var map;

function initialize() {
    checkLoginStatus();
	var mapCenter = new google.maps.LatLng(<%=todo.getLocation().getLatitude()%>, <%=todo.getLocation().getLongitude()%>);
	var mapOptions = {
          zoom: 15,
          center: mapCenter,
          mapTypeId: google.maps.MapTypeId.ROADMAP
        };

    map = new google.maps.Map(document.getElementById("map_canvas"), mapOptions);
    marker = new google.maps.Marker({
        position: mapCenter, 
        map: map,
        title:'-'
    });

    $(document).ready(function(){
    	$('#descriptionEdit').hide();
    	applyTooltip('#todoDescriptionShow');
    	applyTooltip('button');
    	applyTooltip('span');
    	$("#todoDetails").accordion({
        	navigation: true,
        	collapsible : true
           });

    	//This code will switch to the detailed description tab
    	//when the map finished loading.
    	google.maps.event.addListener(map, "tilesloaded", function() {
    		$("#todoDetails").accordion("activate", 1);
    		google.maps.event.clearListeners(map, "tilesloaded");
        });

		$('#imageWindow').dialog({
			autoOpen:false,
			width : 400,
			height: 360,
			title: '<i18n:message key="todo.window.imageWindow"/>'
			});

		$('#tagWindow').dialog({
			autoOpen:false,
			width : 400,
			height: 150,
			modal: true,
			title: '<i18n:message key="todo.window.tags"/>'
			});
        $('#addTags').autocomplete(
                'addtags-autocompletelist',
                'autocomplete',
                'services/tags/list/<%=locale.getLanguage()%>/', 
                function(){ debug('click me babe, I am not afraid'); },
                'lastTodoId',
                '<%=locale.getLanguage()%>'
         );
		
		$('#newComment').hide(0);

		var editor = $('#todoDescriptionEditor').rte({
			css: ['style/default.css'],
			controls_rte: rte_toolbar,
			controls_html: html_toolbar,
			frame_class: 'frameBody'
		});
		editors = $('#commentEditor').rte({
			css: ['style/default.css'],
			controls_rte: rte_toolbar,
			controls_html: html_toolbar,
			frame_class: 'frameBody'
		});
		editors['todoDescriptionEditor'] = editor['todoDescriptionEditor'];

		$("#comments").mbScrollable({
	        dir:"vertical",
	        elementsInPage:3,
	        elementMargin:6,
	        shadow:"#999 2px 2px 2px",
	        slideTimer:600,
	        autoscroll:false,
	        scrollTimer:6000
	      });
		
    });

}

function attachFile() {
	$.ajaxFileUpload
	(
		{
			url: 'upload/<%=todo.getId()%>',
			secureuri:false,
			fileElementId:'fileToUpload',
			beforeSend:function()
			{
				$("#loading").show();
			},
			complete:function()
			{
				debug('loading');
			},				
			success: function (data, status)
			{
				debug('success');
				updateAttachments(<%=todo.getId()%>);
			},
			error: function (data, status, e)
			{
				alert(e);
			}
		}
	);
}

function handleErrors(request) {
	debug('problem:'+request);
}

function downloadAttachment(id) {
	$.get('services/attachments/'+id, function(data) {
			var attachment = eval('('+data+')');
			var mimeType = attachment['attachment']['mime'];
			if(mimeType.indexOf('image/') == 0) {
				$('#imageWindow').dialog('open');
				$('#bigPicture').attr('src','download/'+id);
			} else {
				//TODO
			}
		});
}


var editors;

function editDescription() {
	$('#todoDescriptionShow').hide();
	$('#descriptionEdit').show();
	debug($('#todoDescriptionShow').html());
	editors['todoDescriptionEditor'].set_content($('#todoDescriptionShow').html());
}

function submitComment() {
	comment = {
			comment: {
				text: editors['commentEditor'].get_content()
			}
	};
	$.ajax({
		type : 'POST',
		url : 'services/comments/add/<%=todo.getId()%>',
		data: JSON.stringify(comment),
		success: function(data, textStatus){
			refreshComments();
			},
		processData : false,
		contentType : 'application/json',
		dataType : 'json'
	});
	increaseCounter('nrOfComments');
}

function refreshComments() {
	$.get('services/comments/get/<%=todo.getId()%>','', function(data, status) {
			$('#comments').empty();
			editors['commentEditor'].set_content('');
			$('#newComment').hide(1000);
			$('#addCommentButton').show(1000);
			var comments = eval('('+data+')');
			$.each(comments['comment'], function(i, val) {
					$('#comments').append('<span class="comment" id="'+val['id']+'">'+val['text']+'</span>');
				});
		});
}

function addComment() {
	$('#newComment').show(1000);
	$('#addCommentButton').hide(1000);
}

function saveData() {
	var todo = {"todo":
		{
		"id":id,
		"version":version,
		"location" : {
			"latitude": <%=todo.getLocation().getLatitude()%>,
			"longitude":<%=todo.getLocation().getLongitude()%>
			},
		"description": editors['todoDescriptionEditor'].get_content()
		}
	};
	var strData = JSON.stringify(todo);
	$.ajax({
		type : 'POST',
		url : 'services/todos/update',
		data: strData,
		success: function (data, textStatus){
			$('#todoDescriptionShow').html(editors['todoDescriptionEditor'].get_content());
			//chrome could not tolerate slow hiding, so this remained no-effect....
			$('#descriptionEdit').hide();
			$('#todoDescriptionShow').show(1000);
		},
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
<body onload="initialize()">

<input type="hidden" id="lastTodoId" value="<%=todo.getId() %>"/>

<div style="width: 100%; height: 800px;">
<div id="todoDetails">

	<h3><a href="#"> <i18n:message key="todo.location">Location</i18n:message></a></h3>
	<div style="height: 600px">
		<div id="map_canvas" style="width: 100%; height: 100%"></div>
		<div style="position: absolute; top: 0px; right: 0px; color: grey; font-size: 0.8em;">
			<a href="index.jsp?lat=<%=todo.getLocation().getLatitude()%>&lng=<%=todo.getLocation().getLongitude()%>&zoom=13"> <i18n:message key="etc.returnToMap">return to map</i18n:message> </a>
		</div>
		<div style="color: grey; font-size: 0.8em;">
			<%
				if (todo.getAddr() != null) {
			%>
			<%=todo.getAddr().getCountry() == null ? "-" : todo
						.getAddr().getCountry()%>
			&gt; <%=todo.getAddr().getState() == null ? "-" : todo
						.getAddr().getState()%>
			&gt; <%=todo.getAddr().getTown() == null ? "-" : todo
						.getAddr().getTown()%>
			&gt; <%=todo.getAddr().getAddress() == null ? "-" : todo
						.getAddr().getAddress()%>
			<%
				}
			%>
		</div>
		<%
			if (todo.getCreator() != null
					&& !StringUtils.isEmpty(todo.getCreator().getDisplayName())) {
		%>
		<div style="position: absolute; bottom: 0px; right: 0px; color: grey; font-size: 0.8em;">
			<i18n:message key="todo.submittedby">Submitted by</i18n:message> <%=todo.getCreator().getDisplayName()%> <span id="authorId" class="authorId">[<%=todo.getCreator().getId()%>]</span>
		</div>
		<%
			}
		%>
	</div>
	<h3><a href="#"><i18n:message key="todo.details">Details</i18n:message></a></h3>
	<div>
		<span class="tags">
			<span id="tagList">
			<ul>
				<%
					for (Tag tag : todo.getTags()) {
				%>
					<li><%=tag.getName()%></li>
				<%
					}
				%>
			</ul>
			</span>
			<span class="authOnly">
				<button onclick="$('#tagWindow').dialog('open')">+/-</button>
			</span>
		</span>
		<span id="descriptionNoEdit">
			<span id="todoDescriptionShow" ondblclick="editDescription()" style="cursor: text; width: 100%; height: 100%"><%=todo.getDescription()%></span>
		</span>
		<span id="descriptionEdit" style="display: none">
			<textarea id="todoDescriptionEditor" class="todoDescription"></textarea>
			<button id="saveButton" onclick="saveData()"><i18n:message key="etc.save">save</i18n:message></button>
		</span>
	</div>
	<h3><a href="#"> <i18n:message key="todo.attachments"> Attachments </i18n:message> <span id="nrOfAttachments" class="counter"><%=todo.getAttachments().size()%></span></a></h3>
	<div>
		<input type="file" id="fileToUpload" name="file"/>
		<span class="authOnly">
			<button id="uploadButton" onclick="return attachFile();"><i18n:message key="todo.uploadButton">upload</i18n:message></button>
		</span>
		<span class="noAuthOnly">
			<i18n:message key="todo.uploadNoAuth">
			<h4>You are not signed in</h4>
			<p>Please sign in to attach files</p>
			</i18n:message>
		</span>
	
		<span id="attachments" class="attachments">
			<%
				for (Attachment attachment : todo.getAttachments()) {
			%>
				<span id="attachment-<%=attachment.getId()%>" class="attachment">
					<img alt="<%=attachment.getFileName()%>" src="thumbnail/<%=attachment.getId()%>"  onclick="downloadAttachment(<%=attachment.getId()%>)"/>
					<span><%=attachment.getFileName()%></span>
					<!-- 
					<span class="authOnly">
						<span class="datacontrols">
							<img src="img/delete32.png" alt="remove" onclick="deleteAttachment(<%=attachment.getId()%>, function() {alert('ok');})"/>
						</span>
					</span>
					 -->
				</span>
			<%
				}
			%>
		</span>
	</div>
	<h3><a href="#"><i18n:message key="todo.comments">Comments</i18n:message> <span id="nrOfComments" class="counter"><%=todo.getComments().size()%></span></a></h3>
	<div>
		<div id="newComment" style="">
			<textarea id="commentEditor" class="todoDescription"></textarea> <br/>
			<button id="commentButton" onclick="submitComment();">submit</button>
		</div>
		<span class="authOnly">
			<button id="addCommentButton" onclick="addComment()">add</button>
		</span>
		<span class="noAuthOnly">
			<i18n:message key="todo.commentNoAuth">
			<h4>You are not signed in</h4>
			<p>Please sign in to comment</p>
			</i18n:message>
		</span>
		<div id="comments" class="comments">
			<%
				for (Comment comment : todo.getComments()) {
			%>
				<span id="comment-<%=comment.getId()%>" class="comment"><%=comment.getText()%>
				<span style="color: grey; font-size: 0.8em; text-align: right; font-family: monospace;">
					<%=StringUtils.isBlank(comment.getCreator()
								.getDisplayName()) ? "..." : comment
						.getCreator().getDisplayName()%> <span id="authorId" class="authorId">[<%=comment.getCreator().getId()%>]</span>
				</span>
				<span style="color: grey; font-size: 0.8em; text-align: left; font-family: monospace;">
					<i18n:formatDate value="<%= comment.getCreated() %>"/>
				</span>
				</span>
			<%
				}
			%>
		</div>
	</div>
	<h3><a href="#"><i18n:message key="todo.ratingdetails">Ratings details</i18n:message></a></h3>
	<div>
		<div id="ratingDetails">
			<input name="simpleRating" type="radio" class="star"/> 
			<input name="simpleRating" type="radio" class="star"/> 
			<input name="simpleRating" type="radio" class="star"/> 
			<input name="simpleRating" type="radio" class="star"/> 
			<input name="simpleRating" type="radio" class="star"/>
		</div>
	</div>

<div id="imageWindow">
	<img id="bigPicture" style="width: 100%; height: 100%" src=""/>
</div>

<div id="tagWindow">
	<input id="addTags"/>
	<button id="addTag" onclick="addTag( <%=todo.getId()%> , '<%=locale.getLanguage()%>', $('#addTags').val()); $('#addTags').val('')"><i18n:message key="tag.add">add tag</i18n:message></button>
</div>

</div>
</div>

<span style="visibility: hidden">
	<div id="todoDescriptionShow-tooltip">
		<p><img src="img/edit.png"/> <i18n:message key="tooltip.todoDescriptionShow">Double click the text to start editing.</i18n:message></p>
	</div>
	<div id="saveButton-tooltip">
		<p><img src="img/floppy.png"/> <i18n:message key="tooltip.saveButton"> Click save to submit your modifications. </i18n:message></p>
	</div>
	<div id="authorId-tooltip">
		<p><img src="img/user.png"/> <i18n:message key="tooltip.authorId">This is the unique ID of the user. Names are not neccesarily unique.</i18n:message></p>
	</div>
</span>

</body>
</html>