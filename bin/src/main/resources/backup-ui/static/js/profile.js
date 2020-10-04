function userUpdate(userId) {
	let data = $("#frm").serialize();
	console.log(1, data);

	fetch("/user", {
		method: "put",
		body: data,
		headers: {
			"Content-Type": "application/x-www-form-urlencoded; charset=utf-8"
		}
	}).then(function (res) {
		return res.text();
	}).then(function (res) {
		alert("회원수정 성공");
		location.href = "/user/" + userId;
	});
}