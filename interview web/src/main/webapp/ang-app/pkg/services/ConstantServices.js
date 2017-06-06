Package("services").
ConstantServices = {
	key: 'constantServices',
	func: function($http) {
		var mapTypeByClass = {
    			'com.cre8techlabs.entity.esigndoc.document.field.basic.TextField': {label: 'Text', icon: 'fa fa-text-width fa-lg', basic: true},
    			'com.cre8techlabs.entity.esigndoc.document.field.basic.RadioField': {label: 'Radio', icon: 'fa fa-circle-o fa-lg', basic: true},
    			'com.cre8techlabs.entity.esigndoc.document.field.basic.CheckboxField': {label: 'Checkbox', icon: 'fa fa-check-square-o fa-lg', basic: true},
    			'com.cre8techlabs.entity.esigndoc.document.field.basic.CommentField': {label: 'Comment', icon: 'fa fa-comment fa-lg', basic: true},
    			'com.cre8techlabs.entity.esigndoc.document.field.basic.DateField': {label: 'Date', icon: 'fa fa-calendar fa-lg', basic: true},
    			'com.cre8techlabs.entity.esigndoc.document.field.basic.NoteField': {label: 'Note', icon: 'fa fa-sticky-note-o fa-lg', basic: true},
    			'com.cre8techlabs.entity.esigndoc.document.field.basic.SelectField': {label: 'Select', icon: 'fa fa-caret-square-o-down fa-lg', basic: true},
    			
    			'com.cre8techlabs.entity.esigndoc.document.field.common.RecipientInitialField': {label: 'Select', icon: 'fa fa-font fa-lg'},
    			'com.cre8techlabs.entity.esigndoc.document.field.common.RecipientNameField': {label: 'Select', icon: 'fa fa-user fa-lg'},
    			'com.cre8techlabs.entity.esigndoc.document.field.common.RecipientSignatureField': {label: 'Select', icon: 'fa fa fa-pencil fa-lg'},
    			'com.cre8techlabs.entity.esigndoc.document.field.common.RecipientSignDateField': {label: 'Select', icon: 'fa fa fa-pencil-square-o fa-lg'},
    			'com.cre8techlabs.entity.esigndoc.document.field.common.RecipientEmailField': {label: 'Select', icon: 'fa fa-caret-square-o-down fa-lg'},
    			
    			'com.cre8techlabs.entity.esigndoc.document.field.common.owner.OwnerInitialField': {label: 'Select', icon: 'fa fa-font fa-lg'},
    			'com.cre8techlabs.entity.esigndoc.document.field.common.owner.OwnerNameField': {label: 'Select', icon: 'fa fa-user fa-lg'},
    			'com.cre8techlabs.entity.esigndoc.document.field.common.owner.OwnerSignatureField': {label: 'Select', icon: 'fa fa fa-pencil fa-lg'},
    			'com.cre8techlabs.entity.esigndoc.document.field.common.owner.OwnerSignDateField': {label: 'Select', icon: 'fa fa fa-pencil-square-o fa-lg'},
    			'com.cre8techlabs.entity.esigndoc.document.field.common.owner.OwnerEmailField': {label: 'Select', icon: 'fa fa-caret-square-o-down fa-lg'},
		};
		return {
    				fieldMap: mapTypeByClass
    			}

	}
};

