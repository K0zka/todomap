/*
 * Sitewide javascript utils.
 * 
 * Required libraries: jquery, jquery ui, json
 */

var bookmarks = [];

/**
 * Tries to find out if a variable is defined or not.
 * 
 * @param variable
 *            name of the variable
 * @return true or false
 */
function isDefined(variable) {
	return (typeof (window[variable]) != 'undefined');
}

/**
 * Debug the message on the firebug console, if available.
 * 
 * @param message
 *            the message to log
 * @return nothing
 */
function debug(message) {
	try {
		// the good old firebug console
		console.log(message)
	} catch (fubar) {
		// do nothing. No console, no log.
	}
}

function ipBasedLocation() {
	try {
		return new google.maps.LatLng(google.loader.ClientLocation['latitude'],
				google.loader.ClientLocation['longitude']);
	} catch (fubar) {
		return null;
	}
}

function zoomLevel() {
	try {
		if (isDefined(google.loader.ClientLocation)) {
			return 4;
		}
	} catch (fubar) {
		debug(fubar);
	}
	return 8;

}

/**
 * Check the login status and hide/show UI items based on this info.
 * 
 * @return nothing
 */
function checkLoginStatus() {
	$.ajax( {
		type : 'GET',
		url : 'services/home/auth',
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			handleErrors(XMLHttpRequest);
		},
		success : function(msg) {
			var isLoggedIn = eval("(" + msg + ")");
			try {
				if (isLoggedIn != isAuthenticated) {
					if (isLoggedIn) {
						$("#loginWindow").dialog('close');
						$(".authOnly").show(1000);
						$(".noAuthOnly").hide(1000);
						isAuthenticated = true;
					} else {
						$(".authOnly").hide(1000);
						$(".noAuthOnly").show(1000);
						isAuthenticated = false;
					}
				}
			} catch (error) {
				debug(error);
			}
		},
		processData : false,
		contentType : 'application/json',
		dataType : 'json'
	});
}

/**
 * Call the server side to close session.
 * 
 * @return
 */
function logOut() {
	$.ajax( {
		type : 'GET',
		url : 'logout.jsp',
		success : checkLoginStatus,
		error : checkLoginStatus
	});
}

function checkLoginStatus_periodically() {
	checkLoginStatus();
	setTimeout("checkLoginStatus_periodically()", 20000);
}

checkLoginStatus_periodically();

function refreshBookmarks() {
	$
			.get(
					'services/bookmarks',
					function(data) {
						bookmarks = eval('(' + data + ')');
						$('#bookmarks').empty();
						$
								.each(
										bookmarks['bookmark'],
										function(i, val) {
											$('#bookmarks')
													.append(
															"<a class='bookmark' href=\""
																	+ val['id']
																	+ '-'
																	+ val['text']
																	+ ".html\" target=\"_blank\">"
																	+ val['text']
																	+ "</a> <img src='img/external-link.png' onclick='' style='cursor:pointer;'/> <br/>");
										});
					});
}

function bookmarkItem(itemId) {
	debug('Bookmarking ' + itemId);
	$.ajax( {
		type : 'POST',
		url : 'services/bookmarks/bookmark/' + itemId,
		data : itemId,
		success : refreshBookmarks,
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			handleErrors(XMLHttpRequest);
		}
	});
}

function unbookmarkItem(itemId) {
	$.ajax( {
		type : 'POST',
		url : 'services/bookmarks/unbookmark/' + itemId,
		data : itemId,
		success : refreshBookmarks,
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			handleErrors(XMLHttpRequest);
		},
		contentType : 'application/json',
		dataType : 'json'
	});
}

function isBookmarked(itemId) {
	// for(var i =0; i < bookmarks['bookmark'].) {
	//		
	// }
}

function isBookmarked(itemId) {
	// TODO
	return false;
}

function unBookmarkItem(itemId) {
	$.ajax( {
		type : 'DELETE',
		url : 'services/bookmarks/' + itemId
	});
}

function increaseCounter(name) {
	$('#' + name).text(eval($('#' + name).text() + '+ 1'));
}

function setCounter(name, value) {
	$('#' + name).text(value);
}

function deleteResource(id, callback) {
	$.ajax( {
		type : 'POST',
		url : 'services/base/remove/' + id,
		success : callback
	});
}

function deleteAttachment(id, callback) {
	$.ajax( {
		type : 'POST',
		url : 'services/attachments/delete/' + id,
		success : callback
	});
}

function updateLatLong(value, latResult, lngResult) {
	var addr = $('#' + value).val();
	debug(addr);
	geocoder = new google.maps.Geocoder();
	geocoder.geocode( {
		'address' : addr
	}, function(results, status) {
		debug('status:' + status);
		try {
			$('#' + latResult).val(results[0]['geometry']['location'].lat());
			$('#' + lngResult).val(results[0]['geometry']['location'].lng());
		} catch (err) {
			debug('Error: ' + err);
		}
	});
}

function updateAddr(addr, latInput, lngInput) {
	geocoder = new google.maps.Geocoder();
	var lat = $('#' + latInput).val();
	var lng = $('#' + lngInput).val();
	geocoder.geocode( {
		'latLng' : new google.maps.LatLng(lat, lng)
	}, function(results, status) {
		debug('status: ' + status);
		debug(results);
		try {
			if (results[0]) {
				$('#' + addr).val(results[0].formatted_address);
			}
		} catch (err) {
			debug('Error: ' + err);
		}
	});
}

function togle(who, callback) {
	var tog = $('#' + who).attr('class');
	var oldVal = tog.indexOf('inactive') == -1;
	if (oldVal) {
		$('#' + who).attr('class', 'starTogle_inactive');
	} else {
		$('#' + who).attr('class', 'starTogle_active');
	}
	if (callback) {
		callback(who, !oldVal);
	}
}

function rate(id, val, callback) {
	var rating = { rating : {
			rate : val,
			comment : 'auto-magic'
			}
	};
	$.ajax({
		type : 'POST',
		url : 'services/rating/add/'+id,
		data : JSON.stringify(rating),
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			handleErrors(XMLHttpRequest);
		},
		success : callback,
		contentType : 'application/json',
		dataType : 'json'
	});
}

function voteDown(id, callback) {
	rate(id, -10, callback);
}

function voteUp(id, callback) {
	rate(id, 10, callback);
}

function refreshTagsOf(id) {
	$.get('services/tags/tagsof/'+id , function(response) {
		var lst = '<ul>'
		var tags = eval('('+response+')');
		for(i = 0; i < tags.tag.length; i++) {
			lst = lst + '<li>'+tags.tag[i].name+'</li>';
		}
		lst = lst + '</ul>'
		$('#tagList').html(lst);
	});
}

function addTag(id, lang, tag) {
	$.ajax( {
		type : 'POST',
		url : 'services/tags/add/' + lang + '/' + id,
		data : JSON.stringify( {
			tag : {
				name : tag
			}
		}),
		contentType : 'application/json',
		dataType : 'json',
		success : function() {
			refreshTagClouds(lang);
			refreshTagsOf(id);
		}
	});

}
	
function refreshTagClouds(lang) {
	debug('refreshTagClouds' +lang);
	// add tags
	$.get('services/tags/cloud/'+lang, function(response) {
		var tagCloud = eval('('+response+')');
		$('div.tagcloud').empty();
		var tagList = '';
		for(var i = 0; i < tagCloud.tagc.length; i++) {
			tagList = tagList + '<a rel='+tagCloud.tagc[i].weight+' href="#" onclick="addTag( $(\'#lastTodoId\').val(), \''+lang+'\', \''+tagCloud.tagc[i].tag.name + '\')">'+tagCloud.tagc[i].tag.name +'</a> ';
		}
		$('div.tagcloud').html(tagList);
		$('div.tagcloud').tagcloud( {
			size : {
				start : 6,
				end : 14,
				unit : 0.5
			},
			color : {
				start : '#0063DC',
				end : '#1073EC'
			}
		});
	});

}

function updateAttachments(todoId) {
	$.get('services/attachments/'+todoId+'/get.shrt', function(data) {
			var attachments = eval('('+data+')');
			var html = ''
			$('#attachments').empty();
			$.each(attachments['atchmnt'], function(i, val) {
				html = html + '<div id="attachment-'+val['id']+'" class="attachment" onclick="downloadAttachment('+val['id']+')">'
					+ '<img alt="'+ val['id'] +'" src="thumbnail/'+val['id']+'"/>' 
					+ '<span>'+val['filename']+'</span>'
					+ '</div>'
			});
			$('#attachments').html(html);
			increaseCounter('nrOfAttachments');
		});
}
