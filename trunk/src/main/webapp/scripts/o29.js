/*
 * Sitewide javascript utils.
 * 
 * Required libraries: jquery, jquery ui, json
 */

/**
 * Tries to find out if a variable is defined or not.
 * 
 * @param variable name of the variable
 * @return true or false
 */
function isDefined(variable) {
	return (typeof (window[variable]) != 'undefined');
}

/**
 * Debug the message on the firebug console, if available.
 * @param message the message to log
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
		
		//try to resolve country with internal geoip
		
		$.get('/geoip', function(data) {
			if(data != 'unknown') {
				geocoder = new google.maps.Geocoder();
				geocoder.geocode( { 'address': data }, function(results, status) {
			        if (status == google.maps.GeocoderStatus.OK) {
			          map.setCenter(results[0].geometry.location);
			        }
			      });
			}
		});
		
		$('#wheretogoWindow').dialog('open');
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
				if(isLoggedIn != isAuthenticated) {
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
			} catch (errror) {
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
 * @return
 */
function logOut() {
	$.ajax({
			type	:	'GET',
			url		: 'logout.jsp',
			success : checkLoginStatus,
			error	: checkLoginStatus
		});
}

function checkLoginStatus_periodically() {
	checkLoginStatus();
	setTimeout("checkLoginStatus_periodically()", 20000);
}

checkLoginStatus_periodically();

function bookmarkItem(itemId) {
	debug('Bookmarking '+itemId);
	$.ajax({
		type:	'POST',
		url:	'services/bookmarks/bookmark/'+itemId,
		data:	itemId
	});
}

function isBookmarked(itemId) {
	//TODO
	return false;
}

function unBookmarkItem(itemId) {
	$.ajax({
		type:	'DELETE',
		url:	'services/bookmarks/'+itemId
	});
}

function increaseCounter(name) {
	$('#'+name).text(eval($('#'+name).text()+ '+ 1'));
}

function setCounter(name, value) {
	$('#'+name).text(value);
}

function deleteResource(id, callback) {
	$.ajax({
		type: 	'POST',
		url: 	'services/base/remove/'+id,
		success:callback
	});
}

function deleteAttachment(id, callback) {
	$.ajax({
		type: 	'POST',
		url: 	'services/attachments/delete/'+id,
		success:callback
	});
}

function updateLatLong(value, latResult, lngResult) {
	var addr = $('#'+value).val();
	debug(addr);
	geocoder = new google.maps.Geocoder();
	geocoder.geocode( { 'address': addr}, function(results, status) {
		debug('status:' + status);
		try {
			$('#'+latResult).val(results[0]['geometry']['location'].lat());
			$('#'+lngResult).val(results[0]['geometry']['location'].lng());
		} catch (err) {
			debug('Error: '+err);
		}
      });
}

function updateAddr(addr, latInput, lngInput) {
	geocoder = new google.maps.Geocoder();
	var lat = $('#'+latInput).val();
	var lng = $('#'+lngInput).val();
	geocoder.geocode( {'latLng' : new google.maps.LatLng(lat, lng)}, function(results, status) {
		debug('status: ' + status);
		debug(results);
		try {
			if(results[0]) {
					$('#'+addr).val(results[0].formatted_address);
			}
		} catch (err) {
			debug('Error: '+err);
		}
      });
}
