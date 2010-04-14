/**
 * The script was somewhat modified.
 */

/*
version 1.0

$('input').autocomplete(obj,cls,url,f);

obj = id of new unordered list object to create
cls = class to apply to obj
url = url to load results from
f   = callback function

*/

$.replace = function(s,w) {
	
	var r = new RegExp( '(' + w + ')', 'gi' );
	return s.replace(r, "<b>$1</b>" );
}

$.fn.extend({
		
	autocomplete: function(obj,cls,url,f, id, lang) {
		
		this.keypress( function(event) {
		
			debug('keypress');
			var el      = $(this);
			var keycode = event.which;
			var query   = $.trim(el.val() + String.fromCharCode(keycode));
			
			if(query.length > 2 && keycode != 8) {
			
				$('body').append('<ul id="' + obj + '"></ul>');
				
				var autocomplete = $('#' + obj);
				
				autocomplete.css({
					top: (el.offset().top + el.height() + 13) + 'px',
					left: (el.offset().left) + 'px',
					width: (el.outerWidth() - 12) + 'px'
				});
				
				autocomplete.addClass(cls);
				
				$.get(url + query, function (data) {
			
					if(data.length > 0) {
						autocomplete.show(250);
						
						var tags = eval('('+data+')');
						var frg = '<ul>'
						for(var i = 0; i < tags.tag.length; i++) {
							frg += '<li onclick="addTag('+$('#'+id).val()+',\''+lang+'\',\''+tags.tag[i].name+'\'); $(\'#addTags\').val(\'\')">' + tags.tag[i].name + '</li>'
						}
						frg += '</ul>';
						
						debug(frg);
						autocomplete.html($.replace(frg,query));
						
					} else {
						autocomplete.hide();
					}
				});
			} else {
				debug('fade out');
				$('#' + autocomplete).fadeOut();
			}
		
		});
		
		this.blur( function() {
			$('#' + obj).fadeOut();
		});
	}
	
});