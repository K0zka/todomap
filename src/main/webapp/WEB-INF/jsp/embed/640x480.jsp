<html>

<head>
<style type="text/css">
img.control {
	cursor: pointer;
	position: absolute;
	bottom: 60px;
}

#voteUp {
	left: 120px;
}

#voteDown {
	right: 120px;
}

body {
	font-size: small;
	font-family: sans-serif;
	font-style: normal;
	overflow: hidden;
	margin: 20px;
}

h1 {
	font-size: medium;
}

#desc {
	text-align: justify;
	font-size: small;
	width: 100%;
	height: 100%;
	overflow: hidden;
}

#map {
	color: gray;
	float: left;
	margin-right: 10px;
	margin-bottom: 10px;
	font-size: x-small;
	width: 240;
}

#logo {
	position: absolute;
	bottom: 0px;
	left: 0px;
	height: 40px;
}

p.intro {
	padding-top: 0px;
	margin-top: 0px;
}

</style>

<script type="text/javascript" src="script/jquery-1.3.2.min.js">
</script>

<script type="text/javascript">
	function anonVote(id, value) {
		
	}
</script>

</head>
<body lang="hungarian">
<h1>Lorem ipsum dolor sit amet</h1>

<div id="desc">
<span id="map">
<img
	src="http://maps.google.com/maps/api/staticmap?center=51.477222,0&zoom=14&size=240x180&markers=color:red|label:B|51.477222,0&sensor=false"><br/>
Budapest, Bartok Bela utca 42
</span>
<p class="intro">Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do
eiusmod tempor incididunt ut labore et dolore magna aliqua.</p>
<p>Ut enim ad minim veniam, quis nostrud exercitation ullamco
laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor
in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla
pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa
qui officia deserunt mollit anim id est laborum. Lorem ipsum dolor sit
amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut
labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud
exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.
Duis aute irure dolor in reprehenderit in voluptate velit esse cillum
dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non
proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</p>
</div>

<img id="voteUp" src="img/Add.png" alt="vote up" class="control"
	onclick="anonVote(10,10)" />
<img id="voteDown" src="img/Remove.png" alt="vote down" class="control"
	onclick="anonVote(10,-10)" />
<img id="logo" alt="todomap logo" src="img/todomap-logo.jpg">
</body>
</html>