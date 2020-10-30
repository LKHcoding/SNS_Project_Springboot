function passwordCheckk() {
  var newPassword = document.getElementById("newPassword").value;
  var newRePassword = document.getElementById("newRePassword").value;

  if (newRePassword == "") {
    document.getElementById("passwordCheckText").innerHTML = "";
  } else if (newPassword != newRePassword) {
    document.getElementById("passwordCheckText").innerHTML =
      "<b><font color=red size=1pt> 새 비밀번호를 다시 확인해주세요. </font></b>";
  } else {
    document.getElementById("passwordCheckText").innerHTML =
      "<b><font color=blue size=1pt> 사용가능한 비밀번호 입니다. </font></b>";
  }
}

async function change() {
  var pwChange = document.pwChange;

  var newPassword = document.getElementById("newPassword").value;
  var newRePassword = document.getElementById("newRePassword").value;

  if (newPassword == newRePassword) {
    console.log("새 비밀번호 일치");
    pwChange.submit();
  } else {
    console.log("새 비밀번호 불일치");
    alert("새 비밀번호를 확인해주세요.");
    return false;
  }
}
