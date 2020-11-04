async function chatSave(data, selectedUserId) {
	let response = await fetch("/chatSave/" + selectedUserId, {
		method: "post",
		body: data,
		headers: {
			"Content-Type": "text/html"
		}
	}).then(function(res) {
		return res.text();
	}).then(function(res) {
		//alert("채팅성공");
	});



	// let result = await response.text();
	// if(result === "ok"){
	// 	//location.reload();
	// }
}
/*$(document).ready(function(){
	$('.hangoverClass').hover(
		function(){ $(this).li.addClass('active') },
		function(){ $(this).li.removeClass('active') }
	)
});*/


