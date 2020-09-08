function commentSend(imageId) {
	let content = $("#content-"+imageId).val();

	if (content == "" || content == null) {
		alert("댓글 입력이 필요합니다.");
		return;
	}

	let data = $("#frm-"+imageId).serialize();
	console.log(1, data);

	fetch("/comment", {
		method: "post",
		body: data,
		headers: {
			"Content-Type": "application/x-www-form-urlencoded; charset=utf-8"
		}
	}).then(function (res) {
		return res.text();
	}).then(function (res) {
		//alert("댓글 작성 성공");
		location.reload();
	});
}

function commentDelete(commentId) {
	fetch("/comment/"+commentId, {
		method: "delete"
	}).then(function (res) {
		return res.text();
	}).then(function (res) {
		//alert("댓글 삭제 성공");
		location.reload();
	});
}