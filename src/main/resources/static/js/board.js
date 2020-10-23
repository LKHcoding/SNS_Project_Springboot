async function boardDelete(imageId, imageUserId) {
	let response = await fetch("/image/"+imageId +"/" + imageUserId, {
		method: "delete"
  });
  let result = await response.text();
  if(result === "ok"){
    alert("게시물이 삭제 되었습니다.");
    history.back();
  }
  else{
    alert(result);
  }
}

// async function like(imageId){
// 	let response = await fetch("/likes/" + imageId, {
// 		method: "post"
// 	});
// 	let result = await response.text();
// 	if(result === "ok"){
// 		location.reload();
// 	}
// }