/**
 * Trolltip is todomap's own tooltip solution. It is very simple.
 * $Id$
 * @author kocka
 */

var pointingOnTooltipable = false;
var pointingOnTooltip = false;
var helpShowingFor = '';

function trolltip_init() {
}

function trolltip_showHelp(id) {
	var helpEnabled = $('#helpEnabled').attr('checked');
	if(pointingOnTooltipable) {
		$('#tooltip').empty();
		$('#tooltip').html( $('#'+id+'-tooltip').html() );
		$('#tooltip').fadeIn(300);
		helpShowingFor = id;
	}
}

function trolltip_hideHelp() {
	if(!pointingOnTooltipable && !pointingOnTooltip) {
		$('#tooltip').fadeOut(300);
	}
}

$(function() {
	$('.tooltipable').mouseover(function() {var id = this.id; pointingOnTooltipable = true; window.setTimeout(function() {trolltip_showHelp(id);}, 500);});
	$('.tooltipable').mouseout(function() {	pointingOnTooltipable = false; window.setTimeout(trolltip_hideHelp, 1000);});
	$('#tooltip').mouseover(function() {pointingOnTooltip = true;});
	$('#tooltip').mouseout(function() {pointingOnTooltip = false; window.setTimeout(trolltip_hideHelp, 1000);});
});
