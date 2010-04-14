
function anonRate(id, value, onSuccess) {
	$.ajax({
		type : 'POST',
		url : 'services/anon/rating/rate/' + id + '/' + value,
		success : onSuccess
	});
}

function showRatings(id) {
	$('#question').hide();
	$('#thx').show();
	$('#voteResults').attr('src','chart?w=120&h=120&id='+id);
}
