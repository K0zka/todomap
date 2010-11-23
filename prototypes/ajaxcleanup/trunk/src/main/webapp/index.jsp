<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:ec="http://www.todomap.org/eventchannel" xsi:schemaLocation="http://www.todomap.org/eventchannel eventchannel.xsd">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>event thingies</title>
        <script type="text/javascript" src="js/jquery-1.4.4.min.js"></script>
        <script type="text/javascript" src="js/eventchannel.js"></script>
    </head>
    <body>
    	MOTD:
    	<span id="motd" ecid="motd">STATIC MOTD</span> <br/>
    	Greeting:
    	<span id="greeting" ecid="greeting"></span> <br/>
    	Server clock:
    	<span id="serverclock" ecid="time"></span>
    	
    	<br/>
    	<textarea id="chat" ecid="chat"></textarea><br/>
    	<input id="txt"><br/>
    	<button onclick="">go</button>
    	
    </body>
</html>
