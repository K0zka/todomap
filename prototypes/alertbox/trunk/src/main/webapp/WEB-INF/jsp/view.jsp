<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.util.Map"%>
<%@page import="org.todomap.alertbox.Monitorable"%>
<%@page import="org.todomap.alertbox.Monitorable.StatusDescription"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%
@SuppressWarnings("unchecked")
Map<Monitorable, StatusDescription> statuses = (Map<Monitorable, StatusDescription>)request.getAttribute("statuses");
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>AlertBox</title>
        <link href="css/alertbox.css" rel="stylesheet" type="text/css" media="all"/>
        <script type="text/javascript" src="js/jquery-1.4.4.min.js">
        </script>
        <script type="text/javascript">
    		var nrOfAlerts = 0;
	        function decorate() {
		        $.each($('a[rel="documentation"]'), function(i,doc) {
			        $(doc).html('<img src="img/txt.svg"/>');
			        $(doc).attr('target','__blank');
			    });
	    		$.each($('div .status'), function(i, val) {
		    		value = $(val).text();
	    			$(val).html('<img src="img/'+value.toLowerCase()+'.svg"/>');
	    			if(value == 'Fail') {
	    				$($(val).parent()).addClass('failed-resource');
	    				nrOfAlerts++;
		    		} else if(value == 'Ok') {
		    			$($(val).parent()).addClass('good-resource');
			    	} else if(value == 'Warning') {
		    			$($(val).parent()).addClass('warn-resource');
		    			nrOfAlerts++;
			    	}
	    		});
	    		if(nrOfAlerts != 0) {
		    		$('title').html('AlertBox ['+nrOfAlerts+"]");
		    	} else {
		    		$('title').html('AlertBox');
			    }
	    		$.each($('div .type'), function(i, val) {
		    		value = $(val).text();
	    			$(val).html('<img src="img/'+value.toLowerCase()+'.svg"/>');
	    		});
	    		$('div .resource').click(function(target) {
		    		$(target.currentTarget).toggleClass('details');
		    	});
		    	const labels = new Array();
		    	labels['*'] = $('div .resource').size();
		    	$.each($('div .labels span.label'),function(i, label) {
			    	var labelText = $(label).text();
			    	var val = labels[labelText];
			    	if(typeof val != 'undefined') {
			    		labels[labelText] = val + 1;
				    } else {
				    	labels[labelText] = 1;
					}
			    });
			    $('#labels').empty();
		    	for(val in labels) {
		    		$('#labels').append('<span><span class="labelname">'+val+'</span><span class="labelcntr">'+labels[val]+'</span>'+'<span>');
		    	}
		    	$('#labels span.labelname').click(function(event) {
			    	$('div .resource').show();
			    	var label = $(event.target).text()
		    		$('div .resource').each(function(i, val) {
			    		var labelFound = (label == '*');
						if($(val).find('span.label').each(function(i, span){
							if($(span).text() == label) {
								labelFound = true;
							}
						}));
						if(!labelFound) {
							$(val).fadeOut(500);
						}
			    	});
			    });
			    $('#nofilter').click(function() {
			    	$('div .resource').fadeIn(500);
				});
            }
        	$(document).ready(function() {
            	decorate();
            });
        </script>
    </head>
    <body>
        <h1>Resources</h1>
        <div id="labels"></div>
        <div id="resources">
        <% for(Map.Entry<Monitorable, StatusDescription> entry : statuses.entrySet()) { %>
        	<div class="resource">
	        	<% final String doc = entry.getKey().getDocUrl(); 
	        	if(doc != null) { %>
	        	<a href="<%= doc %>" rel="documentation"></a>
	        	<% } %>
	        	<div class="type"><%= entry.getKey().getTypeId() %></div>
	        	<div class="status"><%=entry.getValue().getStatus() %></div>
	        	<div class="name"><%=entry.getKey().getName() %></div>
	        	<div class="description"><%=entry.getValue().getDescription()%></div>
	        	<div class="labels">
	        	<% for(final String tag : entry.getKey().getTags()) { %>
	        		<span class="label"><%= tag %></span>
	        	<% } %>
	        	</div>
        	</div>
        <% } %>
        </div>
        <div id="version">
        	<%= StringUtils.substringBetween("$HeadURL$", "/alertbox/", "/src/") %>
        	$Id$
        </div>
    </body>
</html>
