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
<%@page import="org.todomap.o29.beans.BaseBean"%>
<%@page import="org.todomap.o29.beans.Locatable"%>
<%
	final BaseBean item = (BaseBean) request.getAttribute("data");
	final Locale locale = (Locale) request.getAttribute("locale");
	final RatingService ratingService = (RatingService) WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext()).getBean("ratings");
	final RatingSummary ratings = ratingService.getRatingSummary(item.getId());
%>
<i18n:setLocale value="<%= locale %>"/>
<i18n:setBundle basename="Messages"/>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="generator" content="Todomap <%= VersionUtil.getVersionNumber() %>"/>
<% if(item instanceof User) {
	for(final Link link : ((User)item).getUserLinks()) {
%>
<link rel="me" href="<%= link.getUrl() %>"/>
<%
	}
} 
%>

<%
	if (item.getTags().size() > 0) {
%>
<meta name="keywords" content="<%=StringUtils.join(item.getTags(), ',')%>" />
<%
	} else {
%>
<meta name="keywords" content="<i18n:message key="etc.keywords">democracy, direct democracy, map, public infrastructure, issues</i18n:message>" />
<%
	}
%>

<link REL="SHORTCUT ICON" HREF="img/earth.ico"/>
<link rel="license" href="http://creativecommons.org/licenses/by/3.0/"/>

<link rel="stylesheet" type="text/css"
	href="style/jquery-ui-1.7.2.custom.css" media="all" />
<link rel="stylesheet" type="text/css"
	href="style/jquery.rating.css" media="all" />
<link rel="stylesheet" type="text/css"
	href="style/default.css" media="all" />
<link rel="stylesheet" type="text/css"
	href="style/base.css" media="all" />
<link type="text/css" rel="stylesheet" href="style/jquery.rte.css" />
<link type="text/css" rel="stylesheet" href="style/jquery.tooltip.css" />

<script type="text/javascript" src="scripts/json.js">
</script>
<script type="text/javascript" src="scripts/jquery-1.3.2.min.js">
</script>
<script type="text/javascript" src="scripts/jquery-ui-1.7.2.js">
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

<% if(item instanceof Todo) {%>
<title><%=((Todo)item).getShortDescr()%> - <i18n:message key="title">todomap</i18n:message></title>
<% } %>
<script type="text/javascript">

var isAuthenticated = null;
var version = <%=item.getVersion()%>;
var id = <%=item.getId()%>;
var marker;
var map;

function initialize() {

	<% if(item instanceof Locatable) { %>
	if(self != top) {
		debug('parent exists');
		$('#locationinfo').hide(0);
		$('#returntomapDiv').hide(0);
	} else {
		debug('parent does not exist');
	}
	<% } %>
	
    checkLoginStatus();
    $(document).ready(function(){
    	$('#descriptionEdit').hide();

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

    });

}

function attachFile() {
	$.ajaxFileUpload
	(
		{
			url: 'upload/<%=item.getId()%>',
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
				updateAttachments(<%=item.getId()%>);
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
	window.open('download/'+id);
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
		url : 'services/comments/add/<%=item.getId()%>',
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
	$.get('services/comments/get/<%=item.getId()%>','', function(data, status) {
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

function showDetailText() {
	$('#todoDescriptionShow').html(editors['todoDescriptionEditor'].get_content());
	//chrome could not tolerate slow hiding, so this remained no-effect....
	$('#descriptionEdit').hide();
	$('#todoDescriptionShow').show(1000);
}

function saveData() {
	var todo = {"todo":
		{
		"id":id,
		"version":version,
<% if (item instanceof Locatable) { %>
		"location" : {
			"latitude": <%=((Locatable)item).getLocation().getLatitude()%>,
			"longitude":<%=((Locatable)item).getLocation().getLongitude()%>
			},
<% } %>
			"description": editors['todoDescriptionEditor'].get_content()
		}
	};
	var strData = JSON.stringify(todo);
	$.ajax({
		type : 'POST',
		url : 'services/todos/update',
		data: strData,
		success: showDetailText,
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

<a name="top"/>

<div style="width: 100%;">

<div class="baseheader">
	<div id="sharethis">
		<a class="addthis_button" href="http://www.addthis.com/bookmark.php?v=250&amp;username=kozka"><img src="http://s7.addthis.com/static/btn/v2/lg-share-en.gif" width="125" height="16" alt="Bookmark and Share" style="border:0"/></a><script type="text/javascript" src="http://s7.addthis.com/js/250/addthis_widget.js#username=kozka"></script>
	</div>
	<div id="embedButton">
		<button id="voteEmbedButton" onclick="$('#embed').show(100);">&lt;embed&gt;</button>
	</div>
	<% if(item instanceof Todo) { %>
	<div id="closeIssueButton">
		<button id="closeIssueButton" onclick="$('#closeIssue').show(100);"><i18n:message key="todo.close.close">close issue</i18n:message></button>
	</div>
	<div id="returntomapDiv">
		<button id="returntomap" type="button" onclick="window.location='index.jsp?lat=<%=((Locatable)item).getLocation().getLatitude()%>&lng=<%=((Locatable)item).getLocation().getLongitude()%>&zoom=13'"> <i18n:message key="etc.returnToMap">return to map</i18n:message> </button>
	</div>
	<% } %>
	<%
		if (item.getCreator() != null
				&& !StringUtils.isEmpty(item.getCreator().getDisplayName())) {
	%>
	<div style="color: grey; font-size: 0.8em;">
		<i18n:message key="todo.submittedby">Submitted by</i18n:message> <a href="<%= URLUtil.getApplicationRoot(request) + item.getCreator().getId() + ".html" %>"> <%=item.getCreator().getDisplayName()%> <span id="authorId" class="authorId">[<%=item.getCreator().getId()%>]</span></a>
	</div>
	<%
		}
	%>
</div>


<% if(item instanceof Todo) {
	Todo todo = (Todo)item;
%>

<% if(todo.getStatus() == TodoStatus.Open || todo.getStatus() == null) { %>
	<div id="closeIssue" style="display: none;">
		<input type="radio" checked="checked" name="resolution" value="Fixed" id="close-Fixed"> <label for="close-Fixed"> <i18n:message key="todo.close.resolution.Fixed"/> </label><br/>
		<input type="radio" value="Canceled" name="resolution" id="close-Canceled"> <label for="close-Canceled"> <i18n:message key="todo.close.resolution.Canceled"/> </label><br/>
		<input type="radio" value="VotedDown" name="resolution" id="close-VotedDown"> <label for="close-VotedDown"> <i18n:message key="todo.close.resolution.VotedDown"/> </label><br/>
		<input type="radio" value="Outdated" name="resolution" id="close-Outdated"> <label for="close-Outdated"> <i18n:message key="todo.close.resolution.Outdated"/> </label><br/>
		<input type="radio" value="Duplicate" name="resolution" id="close-Duplicate"> <label for="close-Duplicate"> <i18n:message key="todo.close.resolution.Duplicate"/> </label><br/>
		<button onclick="closeTodo(<%= todo.getId() %>, 'Fixed', function(){ $('#closeIssue').hide(); ('#closeIssueButton').hide(); });"> <i18n:message key="todo.close.close">close issue</i18n:message> </button>
		<button onclick="$('#closeIssue').hide(100);"><i18n:message key="etc.cancel">Cancel</i18n:message></button>
	</div>
<% } %>

<% } %> 

	<div id="embed" style="display: none;">
		<i18n:message key="ratings.embed.copypaste">Copy-paste this code to the html code of your site:</i18n:message>
		<br/>
		<textarea id="iframebox"><![CDATA[ <iframe width="125" height="125" style="border: none;" src="<%= URLUtil.getApplicationRoot(request) %>/embed/125x125/<%= URLEncoder.encode(item.getId() + "-" +"embed","UTF-8") %>.html">
</iframe>]]></textarea>
		<br/>
		<button onclick="$('#embed').hide(100);">done</button>
	</div>


<div id="todoDetails">

	<% if(item instanceof Locatable) { 
		final Coordinate location = ((Locatable)item).getLocation();
		final Address address = ((Locatable)item).getAddr();
	%>
	<span id="locationinfo">
	<h3><i18n:message key="todo.location">Location</i18n:message></h3>
	<img
		src="http://maps.google.com/maps/api/staticmap?center=<%=location.getLatitude() %>,<%= location.getLongitude() %>&zoom=14&size=240x180&markers=color:red|label:X|<%=location.getLatitude() %>,<%= location.getLongitude() %>&sensor=false">
	<div style="color: grey; font-size: 0.8em;">
		<%
			if (address != null) {
		%>
		<%= address.getCountry() == null ? "-" : address.getCountry()%>
		&gt; <%=address.getState() == null ? "-" : address.getState()%>
		&gt; <%=address.getTown() == null ? "-" : address.getTown()%>
		&gt; <%=address.getAddress() == null ? "-" : address.getAddress()%>
		<%
			}
		%>
	</div>
	</span>
	<% } %>
	<h3><i18n:message key="todo.details">Details</i18n:message></h3>
	<% if(item instanceof Todo) { %>
	<span id="descriptionNoEdit">
		<span id="todoDescriptionShow" ondblclick="editDescription()" style="cursor: text; width: 100%; height: 100%"><%=((Todo)item).getDescription()%></span>
	</span>
	<% } %>
	<span id="descriptionEdit" style="display: none">
		<textarea id="todoDescriptionEditor" class="todoDescription"></textarea>
		<button id="saveButton" onclick="saveData()"><i18n:message key="etc.save">save</i18n:message></button>
		<button id="cancelButton" onclick="showDetailText()"><i18n:message key="etc.cancel">cancel</i18n:message></button>
	</span>

	<h3><i18n:message key="todo.tags">tags</i18n:message></h3>

	<span class="tags">
		<span id="tagList">
		<ul>
			<%
				for (Tag tag : item.getTags()) {
			%>
				<li><%=tag.getName()%></li>
			<%
				}
			%>
		</ul>
		</span>
		
		<span id="addTagSpan" style="display: none;">
				<input id="addTags"/>
				<button id="addTag" onclick="addTag( <%=item.getId()%> , '<%=locale.getLanguage()%>', $('#addTags').val()); $('#addTags').val(''); $('#addTagSpan').hide(1000)"><i18n:message key="tag.add">add tag</i18n:message></button>
		</span>
		
		<span class="authOnly">
			<button onclick="$('#addTagSpan').show(1000)">+/-</button>
		</span>
	</span>


	<h3><i18n:message key="todo.attachments"> Attachments </i18n:message> <span id="nrOfAttachments" class="counter"><%=item.getAttachments().size()%></span></h3>
	<span class="authOnly">
		<input type="file" id="fileToUpload" name="file"/>
		<button id="uploadButton" onclick="return attachFile();"><i18n:message key="todo.uploadButton">upload</i18n:message></button>
	</span>
	<span class="noAuthOnly">
		<i18n:message key="todo.uploadNoAuth">
		<p>Please sign in to attach files</p>
		</i18n:message>
	</span>

	<span id="attachments" class="attachments">
		<%
			for (Attachment attachment : item.getAttachments()) {
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

	<h3><i18n:message key="todo.ratingdetails">Ratings details</i18n:message></h3>
	<ul>
		<li><i18n:message key="ratings.bookmarkedby">Bookmarked by:</i18n:message> <%= ratings.getBookmarked() %></li>
		<li><i18n:message key="ratings.authenticated">Authenticated users:</i18n:message> <%= ratings.getNrOfRatings() %></li>
		<li><i18n:message key="ratings.authenticated_avg">Authenticated users average:</i18n:message> <%= ratings.getAverage() == null ? "-" :  ratings.getAverage() %></li>
		<li><i18n:message key="ratings.anon">Anon users:</i18n:message> <%= ratings.getNrOfAnonRatings() %></li>
		<li><i18n:message key="ratings.anon_avg">Anon users average:</i18n:message> <%= ratings.getAnonAverage() == null ? "-" : ratings.getAnonAverage() %></li>
	</ul>
	
	
	
	<div id="ratingDetails">
		<input name="simpleRating" type="radio" class="star"/> 
		<input name="simpleRating" type="radio" class="star"/> 
		<input name="simpleRating" type="radio" class="star"/> 
		<input name="simpleRating" type="radio" class="star"/> 
		<input name="simpleRating" type="radio" class="star"/>
	</div>

	<h3><i18n:message key="todo.comments">Comments</i18n:message> <span id="nrOfComments" class="counter"><%=item.getComments().size()%></span></h3>
	<div id="newComment" style="">
		<textarea id="commentEditor" class="todoDescription"></textarea> <br/>
		<button id="commentButton" onclick="submitComment();">submit</button>
	</div>
	<span class="authOnly">
		<button id="addCommentButton" onclick="addComment()">add</button>
	</span>
	<span class="noAuthOnly">
		<i18n:message key="todo.commentNoAuth">
		<p>Please sign in to comment</p>
		</i18n:message>
	</span>
	<div id="comments" class="comments">
		<%
			for (Comment comment : item.getComments()) {
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

</div>

<span style="display: none;">
	<div id="todoDescriptionShow-tooltip">
		<p><img src="img/edit.png"/> <i18n:message key="tooltip.todoDescriptionShow">Double click the text to start editing.</i18n:message></p>
	</div>
	<div id="saveButton-tooltip">
		<p><img src="img/floppy.png"/> <i18n:message key="tooltip.saveButton"> Click save to submit your modifications. </i18n:message></p>
	</div>
	<div id="authorId-tooltip">
		<p><img src="img/user.png"/> <i18n:message key="tooltip.authorId">This is the unique ID of the user. Names are not neccesarily unique.</i18n:message></p>
	</div>
	<div id="voteEmbedButton-tooltip">
		<p><img src="img/gear.png"/> <i18n:message key="tooltip.voteEmbedButton">Embed this!</i18n:message> </p>
	</div>
</span>


<input type="hidden" id="lastTodoId" value="<%=item.getId() %>" style="display: none;"/>


</body>


<%@page import="org.todomap.o29.beans.Coordinate"%>
<%@page import="org.todomap.geocoder.Address"%>
<%@page import="org.todomap.o29.logic.RatingService"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.todomap.o29.beans.RatingSummary"%>
<%@page import="org.todomap.o29.utils.URLUtil"%>
<%@page import="org.apache.cxf.wsdl.http.UrlEncoded"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="org.todomap.o29.beans.User"%>
<%@page import="org.todomap.o29.beans.Link"%>
<%@page import="org.todomap.o29.utils.VersionUtil"%>
<%@page import="org.todomap.o29.beans.TodoStatus"%></html>