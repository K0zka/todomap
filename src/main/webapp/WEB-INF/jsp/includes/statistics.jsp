<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<script type="text/javascript">
$(function() {
	$('#statisticsWindow').dialog({
        autoOpen : false,
        width: 600,
        height: 400
    });
    $('#statisticsTab').tabs({
        show:function(event, ui) {
        	
        },
        width: 600,
        height: 400
        });
    updateTotals();
});

function updateTotals() {
	$.getJSON('services/rest/statistics/totals', function(data) {
		$('#nrOfUsers').html(data.totals.nrOfUsers);
		$('#nrOfTodos').html(data.totals.nrOfTodos);
		$('#nrOfUnresolvedTodos').html(data.totals.nrOfUnresolvedTodos);
		$('#nrOfNewsposts').html(data.totals.nrOfNewsposts);
		$('#nrOfProjects').html(data.totals.nrOfProjects);
		$('#nrOfActiveProjects').html(data.totals.nrOfActiveProjects);
	});
}

</script>
<div id="statisticsWindow">
	<div id="statisticsTab" style="width: 90%; height: 90%; overflow: hidden;">
		<ul>
		<li><a href="#totals">Totals</a></li>
		<li><a href="#heatmap">Heatmap</a></li>
		<li><a href="#timebreakdown">Time breakdown</a></li>
		</ul>
		<div id="totals">
			<div class="totalsElem">
				<p>
					<img src="img/user.png"/> Number of users: <span id="nrOfUsers"></span>
				</p>
			</div>
			<div class="totalsElem">
				<p>
					<img src="img/core.png"> Number of Issues: <span id="nrOfTodos"></span>, <span id="nrOfUnresolvedTodos"></span> open
				</p>
			</div>
			<div class="totalsElem">
				<p>
					<img src="img/feed.png"> Number of newsposts: <span id="nrOfNewsposts"></span>
				</p>
			</div>
			<div class="totalsElem">
				<p>
					<img src="img/configure.png"> Number of projects: <span id="nrOfProjects"></span>, <span id="nrOfActiveProjects"></span> active
				</p>
			</div>
		</div>
		<div id="heatmap">
		</div>
		<div id="timebreakdown">
		</div>
	</div>
</div>
