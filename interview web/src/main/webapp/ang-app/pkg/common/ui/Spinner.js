

Package("common.ui")

.Spinner = Class.extend({
    init: function() {
        this.spinner = null;
        this.overlay = null;
    },
    addOverlay: function () {

        return $('<div style="position: fixed;top:0; left:0;height: 100%;width: 100%;z-index: 1000000;background-color:rgba(0,0,0,0.5);"></div>').prependTo('body').attr('id', 'overlay');
    },
    createSpinner: function (element) {
        var opts = {
            lines: 13, // The number of lines to draw
            length: 20, // The length of each line
            width: 10, // The line thickness
            radius: 30, // The radius of the inner circle
            corners: 1, // Corner roundness (0..1)
            rotate: 0, // The rotation offset
            direction: 1, // 1: clockwise, -1: counterclockwise
            color: '#000', // #rgb or #rrggbb or array of colors
            speed: 1, // Rounds per second
            trail: 60, // Afterglow percentage
            shadow: true, // Whether to render a shadow
            hwaccel: false, // Whether to use hardware acceleration
            className: 'spinner', // The CSS class to assign to the spinner
            zIndex: 2e9, // The z-index (defaults to 2000000000)
            top: '50%', // Top position relative to parent
            left: '50%' // Left position relative to parent
        };
        var target = $(element);
    	
        var spinner = new Spinner(opts).spin();
        target.after(spinner.el);
        return spinner;
    },
    startSpin: function (overlay) {
    	overlay = overlay == undefined? true: overlay;
        if (this.spinner == null) {
        	if (overlay)
        		this.overlay = this.addOverlay();
            this.spinner = this.createSpinner(this.overlay);
        }
    },
    stopSpin: function () {
        this.spinner.stop();
        if (this.overlay)
        	this.overlay.remove();
        this.spinner = null;
        this.overlay = null;
    },
});