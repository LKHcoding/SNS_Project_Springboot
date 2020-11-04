async function follow(pageUserId, pageUsername) {
	let response = await fetch("/follow/" + pageUserId, {
		method: "post"
	});
	let result = await response.text();
	if(result === "ok"){
		send(LoginUser.username, pageUsername, "님이 회원님을 팔로우 하기 시작했습니다", true, "follow",LoginUser.id);
		location.reload(); //페이지 새로고침으로 변경(함수 재사용 위해)
	}
}

async function unFollow(pageUserId) {
	let response = await fetch("/follow/" + pageUserId, {
		method: "delete"
	});
	let result = await response.text();
	if(result === "ok"){
		location.reload();
	}
}