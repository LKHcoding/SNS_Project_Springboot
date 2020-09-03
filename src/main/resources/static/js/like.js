async function like(imageId){
	let response = await fetch("/likes/" + imageId, {
		method: "post"
	});
	let result = await response.text();
	if(result === "ok"){
		location.reload();
	}
}
async function unLike(imageId){
	let response = await fetch("/likes/" + imageId, {
		method: "delete"
	});
	let result = await response.text();
	if(result === "ok"){
		location.reload();
	}
}
	
// fa fa-heart-o heart (빈하트)
// fa heart heart-clicked fa-heart (빨간하트)