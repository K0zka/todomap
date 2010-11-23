/**
 * Websocket, if available.
 */
var socket = null;
/**
 * Messages queued to send over websocket while disconnected.
 */
var queue = [];

/**
 * Debug to firebug console if available.
 * @param obj	something to debug.
 */
function debug(obj) {
	try {
		console.log(obj);
	} catch (fubar) {
		//ok, no console, so no debug
	}
}

/**
 * Parse a XML and return DOM.
 * @param data		stringified xml
 */
function ec_parsexml(data) {
	if (window.DOMParser)
	  {
	  parser=new DOMParser();
	  return parser.parseFromString(data,"text/xml");
	  }
	else // Internet Explorer
	  {
	  xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
	  xmlDoc.async="false";
	  return xmlDoc.loadXML(data);
	  }
}

/**
 * Send a message through websocket, or queue it if not connected at the moment.
 * 
 * @param msg	the message to send
 */
function ec_socket_send(msg) {
	try {
		if(socket.status == 'OPEN') {
			socket.send(msg);
		} else {
			queue[queue.length] = msg;
		}
	} catch (fubar) {
		debug('problem with websocket');
		debug(fubar);
	}
}

/**
 * Send the message through on the communication channel.
 * 
 * @param msg	the message to send
 */
function ec_send(msg) {
	if(socket != null) {
		ec_socket_send(msg);
	} else {
		ec_longpoll_send(msg);
	}
}

/**
 * Subscribe to events.
 * @param channelName the name of the channel
 */
function ec_subscribe(channelName) {
	debug('subscribing to '+channelName)
	ec_send('<subscribe channel="'+channelName+'"/>');
}

/**
 * Unsubscribe from events.
 * @param channelName the name of the channel
 */
function ec_unsubscribe(channelName) {
	debug('unsubscribing from '+channelName);
	ec_send('<unsubscribe channel="'+channelName+'"/>');
}

/**
 * List subscriptions.
 */
function ec_subscriptions() {
	
}

/**
 * Handle a received message.
 * 
 * @param msg	a message from the server
 */
function ec_receive_message(msg) {
	var dom = ec_parsexml(msg);
	var channelname = dom.getElementsByTagName('update')[0].getAttribute('channel');
	var value = dom.getElementsByTagName('update')[0].innerHTML;
	debug('event on channel '+channelname);
	$('*[ecid]').each(function(i, item) {
		if($(item).attr('ecid') == channelname) {
			debug('updating '+$(item).attr('id'));
			debug($(dom.getElementsByTagName('update')[0]).text());
			$(item).html($(dom.getElementsByTagName('update')[0]).text());
		}
	});
}

$(document).ready(function() {
	//try to initialize websocket
	try {
		socketUrl = document.URL.replace("http://", "ws://")+'eventchannel';
		debug('connecting to '+socketUrl);
		socket = new WebSocket(socketUrl);
		socket.onopen = function () {
			for(i = 0; i < queue.length; i++) {
				socket.send(queue[i]);
			}
			//zero the queue
			queue = [];
		};
		socket.onmessage = function(msg) {
			ec_receive_message(msg.data);
		};
	} catch (e) {
		//no websocket
		debug('No websocket available. Trying to initialize polling. (TODO)');
	}
	
	debug('initializing event channel');
	/* subsribe to all requested channel */
	$('*[ecid]').each(function(i, item) {
		debug('element \''+$(item).attr('id')+'\' refers to channel: '+$(item).attr('ecid'));
			ec_subscribe($(item).attr('ecid'));
		});
	
});
