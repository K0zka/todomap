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
		debug(fubar);
		$('#wheretogoWindow').dialog('open');
		return new google.maps.LatLng(0, 0);
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
	debug('checkLoginStatus()');
	$.ajax( {
		type : 'GET',
		url : 'services/home/auth',
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			handleErrors(XMLHttpRequest);
		},
		success : function(msg) {
			debug('login status:' + msg);
			var isLoggedIn = eval("(" + msg + ")");
			if (isLoggedIn) {
				$("#loginWindow").dialog('close');
				$(".authOnly").show(1000);
				$(".noAuthOnly").hide(1000);
			} else {
				$(".authOnly").hide(1000);
				$(".noAuthOnly").show(1000);
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
