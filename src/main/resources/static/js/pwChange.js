      // $.ajax({
      //   type: "POST",
      //   url: "../auth/pwChange",
      //   headers: {
      //     "Content-Type": "application/json",
      //     "X-HTTP-Method-Override": "POST",
      //   },
      //   data: password,
      //   datatype: "json",
      //   success: function (result) {
      //     console.log(result);

      //     if (result === "pwConfirmOK") {
      //       $("#pwMsg").html("");
      //       chk1 = true;
      //     } else {
      //       $("#pwMsg").html("");
      //       chk1 = false;
      //     }
      //   },
      //   error: function (error) {
      //     console.log("error : " + error);
      //   },
      // });

      // //새로운 비번
      // $("#newPasswrod").on("keyup", function () {
      //   //비밀번호 공백 확인
      //   if ($("#newPasswrod").val() === "") {
      //     $("#newPwMsg").html("<b>비밀번호는 필수 정보입니다.</b>");
      //     chk2 = false;
      //   }
      //   //비밀번호 유효성검사
      //   else if (
      //     !getPwCheck.test($("#newPasswrod").val()) ||
      //     $("#newPasswrod").val().length < 8
      //   ) {
      //     $("#newPwMsg").html("<b>특수문자 포함 8자 이상 입력하세요</b>");
      //     chk2 = false;
      //   } else {
      //     $("#newPwMsg").html("");
      //     chk2 = true;
      //   }
      // }); //end of new password

      // //비밀번호 확인
      // $("#newPwCheck").on("keyup", function () {
      //   if ($("#newPwCheck").val() === "") {
      //     $("#newPwMsg").html('<b">비밀번호 확인은 필수 정보입니다.</b>');
      //     chk3 = false;
      //   } else if ($("#newPw").val() != $("#newPwCheck").val()) {
      //     $("#newPwMsg").html("<b>비밀번호가 일치하지 않습니다.</b>");
      //     chk3 = false;
      //   } else {
      //     $("#newPwMsg").html("");
      //     chk3 = true;
      //   }
      // }); //end of passwordCheck

      // //비밀번호 변경 요청처리하기
      // $(".emailChkBtn").click(function (e) {
      //   if (chk1 == false) {
      //     alert("현재 비밀번호가 틀렸습니다.");
      //   } else if (chk2 == false) {
      //     alert("2번 틀림");
      //   } else if (chk3 == false) {
      //     alert("3번 틀림");
      //   } else if (chk1 && chk2 && chk3) {
      //     const username = $("#username").val();
      //     const password = $("#newPasswrod").val();
      //     const email = $("#email").val();
      //     const id = $("#id").val();
      //     const name = $("#name").val();
      //     const user = {
      //       username: username,
      //       id: id,
      //       password: password,
      //       email: email,
      //       name: name,
      //     };
      //     console.log(user);

      //     $.ajax({
      //       type: "POST",
      //       url: "../auth/pwChange",
      //       headers: {
      //         "Content-Type": "application/json",
      //         "X-HTTP-Method-Override": "POST",
      //       },
      //       dataType: "text",
      //       data: JSON.stringify(user),
      //       success: function (result) {
      //         console.log("result: " + result);
      //         if (result === "changeSuccess") {
      //           alert("비밀번호가 변경되었습니다.");
      //           location.href = "../auth/pwChange";
      //         } else {
      //           alert("현재 비밀번호가 틀렸습니다.");
      //         }
      //       },
      //     });
      //   } else {
      //     alert("입력정보를 다시 확인하세요.");
      //   }
      // });
