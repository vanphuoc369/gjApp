$(function() {

    $('#side-menu').metisMenu();
    var oldAlert = jq.alert;
	jq.alert = function (msg, opts) {
		if (!msg.indexOf('the request was rejected because its size')) {
			msg = msg.replace('the request was rejected because its size (', '');
			msg = msg.replace(') exceeds the configured maximum (', '_');
			msg = msg.replace(')', '');
			var v2 = msg.substring(msg.indexOf('_')+1, msg.length);
			var n2 = (parseInt(v2)/1024/ 1024).toFixed(2) + 'MB';
			newMsg = "File upload có dung lượng vượt quá " + n2;
			oldAlert(newMsg, opts);
		}
		else
			oldAlert(msg, opts);
	}
});

//Loads the correct sidebar on window load,
//collapses the sidebar on window resize.
// Sets the min-height of #page-wrapper to window size
$(function() {
    $(window).bind("load resize", function() {
        topOffset = 50;
        width = (this.window.innerWidth > 0) ? this.window.innerWidth : this.screen.width;
        if (width < 768) {
            $('div.navbar-collapse').addClass('collapse')
            topOffset = 100; // 2-row-menu
        } else {
            $('div.navbar-collapse').removeClass('collapse')
        }

        height = (this.window.innerHeight > 0) ? this.window.innerHeight : this.screen.height;
        height = height - topOffset;
        if (height < 1) height = 1;
        if (height > topOffset) {
            $("#page-wrapper").css("min-height", (height) + "px");
        }
    })
});
