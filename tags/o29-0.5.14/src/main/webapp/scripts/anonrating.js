
function anonRate(id, value, onSuccess) {
	$.ajax({
		type : 'POST',
		url : 'services/anon/rating/rate/' + id + '/' + value,
		success : onSuccess
	});
}

function showRatings() {
	$('#question').hide();
	$('#ratingSummary').hide();
}
