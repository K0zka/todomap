<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="org.todomap.o29.beans.Todo"%>
<%@page import="org.todomap.o29.beans.Attachment"%>
<%
Todo todo = (Todo)request.getAttribute("todo");

%>

<%@page import="org.todomap.o29.beans.Comment"%>
<%@page import="org.apache.commons.lang.StringUtils"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<jsp:include page="includes/sitemeta.jsp"/>

<script type="text/javascript"
	src="http://maps.google.com/maps/api/js?sensor=false">
</script>

<link rel="stylesheet" type="text/css"
	href="style/jquery-ui-1.7.2.custom.css" media="all" />
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
<script type="text/javascript" src="scripts/jquery.dimensions.min.js">
</script>
<script type="text/javascript" src="scripts/jquery.tooltip.min.js">
</script>
<script type="text/javascript" src="scripts/jquery-ui-1.7.2.js">
</script>
<script type="text/javascript" src="scripts/o29.js">
</script>
<script type="text/javascript" src="scripts/ajaxupload.js">
</script>
<script type="text/javascript" src="scripts/jquery.rte.js">
</script>
<script type="text/javascript" src="scripts/jquery.rte.tb.js">
</script>


<title><%= todo.getShortDescr() %></title>

<script type="text/javascript">

var version = <%= todo.getVersion() %>;
var id = <%= todo.getId() %>;
var marker;
var map;

function initialize() {
    checkLoginStatus();
	var mapCenter = new google.maps.LatLng(<%= todo.getLocation().getLatitude() %>, <%= todo.getLocation().getLongitude() %>);
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
    	$('#todoDescriptionShow').tooltip({
    		bodyHandler : function() {
    			return $('#'+this.id+"-tooltip").html();
    		},
			delay : 1000
        	});
    	$('button').tooltip({
    		bodyHandler : function() {
    			return $('#'+this.id+"-tooltip").html();
    		},
			delay : 1000
        	});
    	$('span').tooltip({
    		bodyHandler : function() {
    			return $('#'+this.id+"-tooltip").html();
    		},
			delay : 1000
        	});
    	$("#todoDetails").accordion({
        	navigation: true
           });

		$('#imageWindow').dialog({
			autoOpen:false,
			width : 400,
			height: 360
			});

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

        new AjaxUpload('uploadButton', 
                {
            	action: 'upload/<%= todo.getId() %>', 
            	autoSubmit: true, 
            	name: 'file',
            	onComplete: updateAttachments
    			}
        );
    });

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

function updateAttachments(file, response) {
	$.get('services/attachments/<%= todo.getId() %>/get.shrt', function(data) {
			var attachments = eval('('+data+')');
			var html = ''
			$('#attachments').empty();
			$.each(attachments['atchmnt'], function(i, val) {
				html = html + '<div id="attachment-'+val['id']+'">'
					+ '<img alt="'+ val['id'] +'" src="thumbnail/'+val['id']+'"/>'
					+ '<span>'+val['filename']+'</span>'
					+ '</div>'
			});
			$('#attachments').html(html);
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

}

function refreshComments() {
	$.get('services/comments/get/<%= todo.getId() %>','', function(data, status) {
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
			"latitude": <%= todo.getLocation().getLatitude() %>,
			"longitude":<%= todo.getLocation().getLongitude() %>
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
			$('#descriptionEdit').hide(1000);
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

<div style="width: 100%; height: 800px;">
<div id="todoDetails">

	<h3><a href="#">Location</a></h3>
	<div style="height: 600px">
		<div id="map_canvas" style="width: 100%; height: 100%"></div>
		<div style="color: grey; font-size: 0.8em;">
			<% if(todo.getAddr() != null) { %>
			<%= todo.getAddr().getCountry() == null ? "-" : todo.getAddr().getCountry() %>
			&gt; <%= todo.getAddr().getState() == null ? "-" : todo.getAddr().getState() %>
			&gt; <%= todo.getAddr().getTown() == null ? "-" : todo.getAddr().getTown() %>
			&gt; <%= todo.getAddr().getAddress() == null ? "-" : todo.getAddr().getAddress() %>
			<% } %>
		</div>
		<% if(todo.getCreator() != null && !StringUtils.isEmpty(todo.getCreator().getDisplayName())) { %>
		<div style="position: absolute; bottom: 0px; right: 0px; color: grey; font-size: 0.8em;">
			Submitted by <%= todo.getCreator().getDisplayName() %> <span id="authorId" class="authorId">[<%= todo.getCreator().getId() %>]</span>
		</div>
		<% } %>
	</div>
	<h3><a href="#">Details</a></h3>
	<div>
		<span id="descriptionNoEdit">
			<span id="todoDescriptionShow" ondblclick="editDescription()" style="cursor: text; width: 100%; height: 100%"><%= todo.getDescription() %></span>
		</span>
		<span id="descriptionEdit" style="display: none">
			<textarea id="todoDescriptionEditor" class="todoDescription"></textarea>
			<button id="saveButton" onclick="saveData()">save</button>
		</span>
	</div>
	<h3><a href="#">Attachments (<%= todo.getAttachments().size() %>)</a></h3>
	<div>
	
		<span id="attachments">
			<% for(Attachment attachment : todo.getAttachments()) { %>
				<div id="attachment-<%= attachment.getId() %>" onclick="downloadAttachment(<%= attachment.getId() %>)">
					<a href="#attchment-window-<%= attachment.getId() %>">
						<img alt="<%= attachment.getFileName() %>" src="thumbnail/<%=attachment.getId() %>"/>
						<span><%=attachment.getFileName() %></span>
					</a>
				</div>
			<% } %>
		</span>
	
		<span class="authOnly">
			<button id="uploadButton">upload</button>
		</span>
		<span class="noAuthOnly">
			<h4>You are not signed in</h4>
			<p>Please sign in to attach files</p>
		</span>
	</div>
	<h3><a href="#">Comments (<%= todo.getComments().size() %>)</a></h3>
	<div>
		<div id="newComment" style="">
			<textarea id="commentEditor" class="todoDescription"></textarea> <br/>
			<button id="uploadButton" onclick="submitComment();">submit</button>
		</div>
		<span class="authOnly">
			<button id="addCommentButton" onclick="addComment()">add</button>
		</span>
		<span class="noAuthOnly">
			<h4>You are not signed in</h4>
			<p>Please sign in to comment</p>
		</span>
		<div id="comments" class="comments">
			<% for(Comment comment : todo.getComments()) { %>
				<span id="comment-<%= comment.getId() %>" class="comment"><%= comment.getText() %></span>
			<% } %>
		</div>
	</div>
	<h3><a href="#">Ratings details</a></h3>
	<div>
	</div>

<div id="imageWindow" title="Picture">
	<img id="bigPicture" style="width: 100%; height: 100%" src=""/>
</div>

</div>
</div>

<span style="visibility: hidden">
	<div id="todoDescriptionShow-tooltip">
		<p><img src="img/edit.png"/>Double click the text to start editing.</p>
	</div>
	<div id="saveButton-tooltip">
		<p><img src="img/floppy.png"/> Click save to submit your modifications. </p>
	</div>
</span>

</body>
</html>