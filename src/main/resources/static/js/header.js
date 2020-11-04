
function notifyClick(fromuser, touser, message, UrlId, notiType, imageId){
  //alert("aa");
    //notify('bell','Title of the message!','This is a sample message! Lorem ipsum!');
//send(LoginUser.username, imageUsername, "님이 회원님의 사진을 좋아합니다", true, "like");
   $.notify({
	// options
	icon: 'glyphicon glyphicon-warning-sign',
	/*title: notiType,*/
	message: fromuser + message,
	url: notiType === "follow" ? '/user/'+UrlId : '/image/' + imageId,
	target: '_self'
},{
	// settings
	element: 'body',
	position: 'fixed',
	type: "info",
	allow_dismiss: true,
	newest_on_top: true,
	showProgressbar: false,
	placement: {
		from: "top",
		align: "center"
	},
	offset: 20,
	spacing: 10,
	z_index: 1031,
	delay: 2500,
	timer: 200,
	url_target: '_blank',
	mouse_over: 'pause',
	animate: {
		enter: 'animated fadeInDown',
		exit: 'animated fadeOutUp'
	},
	onShow: null,
	onShown: null,
	onClose: null,
	onClosed: null,
	icon_type: 'class',
	template: '<div data-notify="container" class="col-xs-11 col-sm-3 alert alert-{0}" role="alert">' +
		'<button type="button" aria-hidden="true" class="close" data-notify="dismiss">×</button>' +
		'<span data-notify="icon"></span> ' +
		'<span data-notify="title">{1}</span> ' +
		'<span data-notify="message">{2}</span>' +
		'<div class="progress" data-notify="progressbar">' +
			'<div class="progress-bar progress-bar-{0}" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0%;"></div>' +
		'</div>' +
		'<a href="{3}" target="{4}" data-notify="url"></a>' +
	'</div>' 
});
};