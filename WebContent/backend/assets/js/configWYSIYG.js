CKEDITOR.config.extraAllowedContent = 'video [*]{*}(*);source [*]{*}(*);';
CKEDITOR.on('dialogDefinition', function(ev) {
	var dialogName = ev.data.name;
	var dialogDefinition = ev.data.definition;

	ev.data.definition.dialog.on('show', function (ev) {
        zAu.send(new zk.Event(zk.Widget.$('$wdn'), 'onCkePopupShow', {}, {toServer:true}));
    });
	ev.data.definition.dialog.on('hide', function (ev) {
        zAu.send(new zk.Event(zk.Widget.$('$wdn'), 'onCkePopupHide', {}, {toServer:true}));
    });

	if (dialogName == 'table') {
		var info = dialogDefinition.getContents('info');

		info.get('txtWidth')['default'] = '100%';
		info.get('txtBorder')['default'] = '0';
		info.get('txtRows')['default'] = '2';
		info.get('txtCols')['default'] = '1';
		info.get('txtCellPad')['default'] = '4';
		info.get('cmbAlign')['default'] = 'center';
	}
});

CKEDITOR.editorConfig = function(config) {
	config.resize_enabled = true;
	config.extraPlugins = 'video,youtube';
	config.toolbarGroups = [
	                        { name: 'clipboard',   groups: [ 'clipboard', 'undo' ] },
	                        { name: 'links' },
	                        { name: 'insert' },
	                        { name: 'video' },
	                        { name: 'tools' },
	                        { name: 'document',    groups: [ 'mode', 'document', 'doctools' ] },
	                        { name: 'others' },
	                        { name: 'basicstyles', groups: [ 'basicstyles', 'cleanup' ] },
	                        { name: 'paragraph',   groups: [ 'list', 'indent', 'blocks', 'align' ] },
	                        { name: 'styles' },
	                        { name: 'colors' },
	                    ];
	config.fontSize_sizes = '8/8px;9/9px;10/10px;11/11px;12/12px;13/13px;14/14px;15/15px;16/16px;17/17px;18/18px;19/19px;20/20px;22/22px;24/24px;26/26px;28/28px;32/32px;48/48px;';
	//config.entities  = false;
	//config.basicEntities = false;
	config.entities_greek = false;
	config.entities_latin = false;
//	config.toolbar = 'Simple';
//	config.toolbar_Simple = [ [ 'Bold', 'Italic', '-', 'NumberedList',
//			'BulletedList', '-', 'Link', 'Unlink', '-', 'About' ] ];
//	config.toolbar_Complex = [ ['Styles', 'Format', 'Font',
//	                 			'FontSize', 'Bold', 'Italic', 'Underline', 'Strike',
//			'TextColor', 'Link', 'Unlink', 'Undo', 'Redo',
//			'PageBreak', 'JustifyLeft', 'JustifyCenter', 'JustifyRight',
//			'JustifyBlock','SpecialChar'] ];
	//config.skin = 'kama';
	//console.log("a");
};
