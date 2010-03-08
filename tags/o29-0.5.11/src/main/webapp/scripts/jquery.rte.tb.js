var rte_tag = "-rte-tmp-tag-";
var rte_toolbar = {
	s1 : {
		separator : true
	},
	bold : {
		command : "bold",
		tags : [ "b", "strong" ]
	},
	italic : {
		command : "italic",
		tags : [ "i", "em" ]
	},
	strikeThrough : {
		command : "strikethrough",
		tags : [ "s", "strike" ]
	},
	underline : {
		command : "underline",
		tags : [ "u" ]
	},
	s2 : {
		separator : true
	},
	justifyLeft : {
		command : "justifyleft"
	},
	justifyCenter : {
		command : "justifycenter"
	},
	justifyRight : {
		command : "justifyright"
	},
	justifyFull : {
		command : "justifyfull"
	},
	s3 : {
		separator : true
	},
	indent : {
		command : "indent"
	},
	outdent : {
		command : "outdent"
	},
	s4 : {
		separator : true
	},
	subscript : {
		command : "subscript",
		tags : [ "sub" ]
	},
	superscript : {
		command : "superscript",
		tags : [ "sup" ]
	},
	s5 : {
		separator : true
	},
	orderedList : {
		command : "insertorderedlist",
		tags : [ "ol" ]
	},
	unorderedList : {
		command : "insertunorderedlist",
		tags : [ "ul" ]
	},
	s6 : {
		separator : true
	},
	block : {
		command : "formatblock",
		select : '<select>	<option value="<p>" selected="true">Paragraph</option>	<option value="<h1>">Header 1</option>	<option value="<h2>">Header 2</options>	<option value="<h3>">Header 3</option>	<option value="<h4>">Header 4</options>	<option value="<h5>">Header 5</option>	<option value="<h6>">Header 6</options></select>	',
		tag_cmp : lwrte_block_compare,
		tags : [ "p", "h1", "h2", "h3", "h4", "h5", "h6" ]
	},
	link : {
		exec : lwrte_link,
		tags : [ "a" ]
	},
	unlink : {
		command : "unlink"
	},
	s8 : {
		separator : true
	},
	removeFormat : {
		exec : lwrte_unformat
	},
	word : {
		exec : lwrte_cleanup_word
	},
	clear : {
		exec : lwrte_clear
	}
};
var html_toolbar = {
	s1 : {
		separator : true
	},
	word : {
		exec : lwrte_cleanup_word
	},
	clear : {
		exec : lwrte_clear
	}
};
function lwrte_block_compare(b, a) {
	a = a.replace(/<([^>]*)>/, "$1");
	return (a.toLowerCase() == b.nodeName.toLowerCase())
}
function lwrte_image() {
	//TODO
}
function lwrte_unformat() {
	this.editor_cmd("removeFormat");
	this.editor_cmd("unlink")
}
function lwrte_clear() {
	if (confirm("Clear Document?")) {
		this.set_content("")
	}
}
function lwrte_cleanup_word() {
	this.set_content(a(this.get_content(), true, true, true));
	function a(d, b, e, f) {
		d = d.replace(/<o:p>\s*<\/o:p>/g, "");
		d = d.replace(/<o:p>[\s\S]*?<\/o:p>/g, "&nbsp;");
		d = d.replace(/\s*mso-[^:]+:[^;"]+;?/gi, "");
		d = d.replace(/\s*MARGIN: 0cm 0cm 0pt\s*;/gi, "");
		d = d.replace(/\s*MARGIN: 0cm 0cm 0pt\s*"/gi, '"');
		d = d.replace(/\s*TEXT-INDENT: 0cm\s*;/gi, "");
		d = d.replace(/\s*TEXT-INDENT: 0cm\s*"/gi, '"');
		d = d.replace(/\s*TEXT-ALIGN: [^\s;]+;?"/gi, '"');
		d = d.replace(/\s*PAGE-BREAK-BEFORE: [^\s;]+;?"/gi, '"');
		d = d.replace(/\s*FONT-VARIANT: [^\s;]+;?"/gi, '"');
		d = d.replace(/\s*tab-stops:[^;"]*;?/gi, "");
		d = d.replace(/\s*tab-stops:[^"]*/gi, "");
		if (b) {
			d = d.replace(/\s*face="[^"]*"/gi, "");
			d = d.replace(/\s*face=[^ >]*/gi, "");
			d = d.replace(/\s*FONT-FAMILY:[^;"]*;?/gi, "")
		}
		d = d.replace(/<(\w[^>]*) class=([^ |>]*)([^>]*)/gi, "<$1$3");
		if (e) {
			d = d.replace(/<(\w[^>]*) style="([^\"]*)"([^>]*)/gi, "<$1$3")
		}
		d = d.replace(/<STYLE[^>]*>[\s\S]*?<\/STYLE[^>]*>/gi, "");
		d = d.replace(/<(?:META|LINK)[^>]*>\s*/gi, "");
		d = d.replace(/\s*style="\s*"/gi, "");
		d = d.replace(/<SPAN\s*[^>]*>\s*&nbsp;\s*<\/SPAN>/gi, "&nbsp;");
		d = d.replace(/<SPAN\s*[^>]*><\/SPAN>/gi, "");
		d = d.replace(/<(\w[^>]*) lang=([^ |>]*)([^>]*)/gi, "<$1$3");
		d = d.replace(/<SPAN\s*>([\s\S]*?)<\/SPAN>/gi, "$1");
		d = d.replace(/<FONT\s*>([\s\S]*?)<\/FONT>/gi, "$1");
		d = d.replace(/<\\?\?xml[^>]*>/gi, "");
		d = d.replace(/<w:[^>]*>[\s\S]*?<\/w:[^>]*>/gi, "");
		d = d.replace(/<\/?\w+:[^>]*>/gi, "");
		d = d.replace(/<\!--[\s\S]*?-->/g, "");
		d = d.replace(/<(U|I|STRIKE)>&nbsp;<\/\1>/g, "&nbsp;");
		d = d.replace(/<H\d>\s*<\/H\d>/gi, "");
		d = d.replace(
				/<(\w+)[^>]*\sstyle="[^"]*DISPLAY\s?:\s?none[\s\S]*?<\/\1>/ig,
				"");
		d = d.replace(/<(\w[^>]*) language=([^ |>]*)([^>]*)/gi, "<$1$3");
		d = d.replace(/<(\w[^>]*) onmouseover="([^\"]*)"([^>]*)/gi, "<$1$3");
		d = d.replace(/<(\w[^>]*) onmouseout="([^\"]*)"([^>]*)/gi, "<$1$3");
		if (f) {
			d = d.replace(/<H(\d)([^>]*)>/gi, "<h$1>");
			d = d.replace(/<(H\d)><FONT[^>]*>([\s\S]*?)<\/FONT><\/\1>/gi,
					"<$1>$2</$1>");
			d = d.replace(/<(H\d)><EM>([\s\S]*?)<\/EM><\/\1>/gi, "<$1>$2</$1>")
		} else {
			d = d.replace(/<H1([^>]*)>/gi, '<div$1><b><font size="6">');
			d = d.replace(/<H2([^>]*)>/gi, '<div$1><b><font size="5">');
			d = d.replace(/<H3([^>]*)>/gi, '<div$1><b><font size="4">');
			d = d.replace(/<H4([^>]*)>/gi, '<div$1><b><font size="3">');
			d = d.replace(/<H5([^>]*)>/gi, '<div$1><b><font size="2">');
			d = d.replace(/<H6([^>]*)>/gi, '<div$1><b><font size="1">');
			d = d.replace(/<\/H\d>/gi, "</font></b></div>");
			var c = new RegExp("(<P)([^>]*>[\\s\\S]*?)(</P>)", "gi");
			d = d.replace(c, "<div$2</div>");
			d = d.replace(/<([^\s>]+)(\s[^>]*)?>\s*<\/\1>/g, "");
			d = d.replace(/<([^\s>]+)(\s[^>]*)?>\s*<\/\1>/g, "");
			d = d.replace(/<([^\s>]+)(\s[^>]*)?>\s*<\/\1>/g, "")
		}
		return d
	}
}
function lwrte_link() {
	var self = this;
	var panel = self.create_panel("Create link / Attach file", 385);
	panel
			.append(
					'<p><label>URL</label><input type="text" id="url" size="30" value=""><button id="file">Attach File</button><button id="view">View</button></p><div class="clear"></div><p><label>Title</label><input type="text" id="title" size="30" value=""><label>Target</label><select id="target"><option value="">default</option><option value="_blank">new</option></select></p><div class="clear"></div><p class="submit"><button id="ok">Ok</button><button id="cancel">Cancel</button></p>')
			.show();
	$("#cancel", panel).click(function() {
		panel.remove();
		return false
	});
	var url = $("#url", panel);
	var upload = $("#file", panel)
			.upload(
					{
						autoSubmit : true,
						action : "uploader.php",
						onComplete : function(response) {
							if (response.length <= 0) {
								return
							}
							response = eval("(" + response + ")");
							if (response.error && response.error.length > 0) {
								alert(response.error)
							} else {
								url
										.val((response.file && response.file.length > 0) ? response.file
												: "")
							}
						}
					});
	$("#view", panel).click(
			function() {
				(url.val().length > 0) ? window.open(url.val())
						: alert("Enter URL to view");
				return false
			});
	$("#ok", panel).click(function() {
		var url = $("#url", panel).val();
		var target = $("#target", panel).val();
		var title = $("#title", panel).val();
		if (self.get_selected_text().length <= 0) {
			alert("Select the text you wish to link!");
			return false
		}
		panel.remove();
		if (url.length <= 0) {
			return false
		}
		self.editor_cmd("unlink");
		self.editor_cmd("createLink", rte_tag);
		var tmp = $("<span></span>").append(self.get_selected_html());
		if (target.length > 0) {
			$('a[href*="' + rte_tag + '"]', tmp).attr("target", target)
		}
		if (title.length > 0) {
			$('a[href*="' + rte_tag + '"]', tmp).attr("title", title)
		}
		$('a[href*="' + rte_tag + '"]', tmp).attr("href", url);
		self.selection_replace_with(tmp.html());
		return false
	})
};