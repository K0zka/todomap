<%@page import="java.util.Locale"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="i18n" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
final Locale locale = (Locale)request.getAttribute("locale");
%>
<i18n:setLocale value="<%= locale %>"/>
<i18n:setBundle basename="Messages"/>
<script type="text/javascript">
</script>

<div id="tooltips" style="overflow: hidden; display: none;">
	<div id="searchButton-tooltip">
		<p><img src="img/search.png"/><i18n:message key="tooltip.searchButton"> Search the site content </i18n:message></p>
	</div>
	<div id="homeButton-tooltip">
		<p><img src="img/gohome.png"/> <i18n:message key="tooltip.homeButton"> Position the map to your home location </i18n:message> </p>
	</div>
	<div id="loginButton-tooltip">
		<p><img src="img/keys.png"/> <i18n:message key="tooltip.loginButton"> Log in with your OpenID account.<br/>You can use your <strong>Yahoo! ID</strong>, your <strong>google/blogger</strong> account, or <strong>any OpenID provider</strong>. </i18n:message></p>
	</div>
	<div id="logoutButton-tooltip">
		<p><img src="img/lock.png"/> <i18n:message key="tooltip.logoutButton"> Close your session to protect your data. </i18n:message></p>
	</div>
	<div id="yourDetailsButton-tooltip">
		<p><img src="img/user.png"/> <i18n:message key="tooltip.yourDetailsButton"> Edit your personal data, like home location, email address, etc </i18n:message></p>
	</div>
	<div id="embedButton-tooltip">
		<p> <img src="img/feed.png"> <i18n:message key="tooltip.embedButton"> Link and RSS feed URL to this map. </i18n:message> </p>
	</div>
	<div id="infoButton-tooltip">
		<p> <img src="img/info.png"/> <i18n:message key="tooltip.infoButton"> Information about the software and todomap.org </i18n:message> </p>
	</div>
	<div id="statisticsButton-tooltip">
		<p> <img src="img/math.png"> <i18n:message key="tooltip.statisticsButton"> Service statistics </i18n:message></p>
	</div>
	<div id="helpButton-tooltip">
		<p> <img src="img/help.png"> <i18n:message key="tooltip.helpButton"> Todomap help </i18n:message></p>
	</div>
	<div id="gotoButton-tooltip">
		<p> <img src="img/earth.png"> <i18n:message key="tooltip.gotoButton">Enter an address to jump to.</i18n:message> </p>
	</div>
	<div id="openIdLink-tooltip">
		<p> <i18n:message key="tooltip.openIdLink"> Find your OpenID provider at openid.org! </i18n:message> </p>
	</div>
	<div id="map_canvas-tooltip">
		<p> <img src="img/earth.png"/> <i18n:message key="tooltip.map_canvas"> You can navigate on the map or click on the flags for details. </i18n:message> </p>
	</div>
	<div id="googleCodeLink-tooltip">
		<p> <img src="img/gear.png"/> <i18n:message key="tooltip.googleCodeLink"> This link will take you to the developer site where you can find the
		<ul>
			<li>bugtracker</li>
			<li>wiki</li>
			<li>and all the source code</li>
		</ul>
		</i18n:message>
		</p>
	</div>
	<div id="todoReverseGeo-label-tooltip">
		<p>
			<img src="img/mail.png"> <i18n:message key="tooltip.reverseGeo"/>
		</p>
	</div>
	<div id="userDetailsHomeLocationReverseGeo-label-tooltip">
		<p>
			<img src="img/mail.png"> <i18n:message key="tooltip.reverseGeo"/>
		</p>
	</div>
	<div id="addTag-tooltip">
		<p>
			<i18n:message key="toolTip.addTag"></i18n:message>
		</p>
	</div>
	<div id="addIssueButton-tooltip">
		<p>
			<img src="img/wizard.png"/><i18n:message key="toolTip.addIssueButton">add issue</i18n:message>
		</p>
	</div>
	<div id="bookmarksAccordionLink-tooltip">
		<p>
			<img src="img/bookmark.png" /><i18n:message key="toolTip.bookmarksAccordion">Bookmarks</i18n:message>
		</p>
	</div>
	<div id="infoAccordionLink-tooltip">
		<p>
			<img src="img/bookmark.png" /><i18n:message key="toolTip.infoAccordion">Informations</i18n:message>
		</p>
	</div>
	<div id="toolsAccordionLink-tooltp">
		<p>
			<img src="img/configure.png"/><i18n:message key="toolTip.toolsAccordion">Tools</i18n:message>
		</p>
	</div>
</div>
