<%@ page language="java" contentType="text/html; charset-UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입</title>
    <script src="http://code.jquery.com/jquery-latest.js"></script>
    <script type="text/javascript" src="./static/js/rsa.js"></script>
    <script type="text/javascript" src="./static/js/jsbn.js"></script>
    <script type="text/javascript" src="./static/js/prng4.js"></script>
    <script type="text/javascript" src="./static/js/rng.js"></script>

<!-- Core CSS -->
    <link rel="stylesheet" href="./static/css/core.css" class="template-customizer-core-css" />
    <link rel="stylesheet" href="./static/css/theme-default.css" class="template-customizer-theme-css" />
    <link rel="stylesheet" href="./static/css/demo.css" />
<!-- Vendors CSS -->
    <link rel="stylesheet" href="./static/css/perfect-scrollbar.css" />
<!-- Page CSS -->
<!-- Page -->
    <link rel="stylesheet" href="./static/css/pages/page-auth.css" />
</head>
<body>

<div class="container-xxl">
      <div class="authentication-wrapper authentication-basic container-p-y">
        <div class="authentication-inner">
          <!-- Register Card -->
          <div class="card">
            <div class="card-body">
              <!-- Logo -->
              <div class="app-brand justify-content-center">
                <a href="index.html" class="app-brand-link gap-2">
                  <span class="app-brand-text demo text-body fw-bolder">회원가입</span>
                </a>
              </div>
              <!-- /Logo -->

              <form id="formAuthentication" class="mb-3" action="/user/signUp" method="POST" onsubmit="return login()">
                <div class="mb-3">
                  <label for="userId" class="form-label">아이디</label>
                  <div class="row">
                    <div class="col-sm-8">
                        <input
                            type="text"
                            class="form-control"
                            id="userId"
                            name="userId"
                            placeholder="ID"
                            style="ime-mode:disabled;"
                            onkeyup="checkcorrectform(this);"
                            autofocus
                        />
                    </div>
                    <div class="col-sm-4">
                        <input type="button" class="btn btn-primary d-grid w-100" id="btn_dupIdChk" value="중복체크" onclick="dupIdChk()" disabled>
                    </div>
                  </div>
                  <div id="dupIdChk_div"></div>
                  <div id="userIdChk_div"></div>
                </div>

                <div class="mb-3 form-password-toggle">
                  <label class="form-label" for="password">비밀번호</label>
                  <div class="input-group input-group-merge">
                    <input
                      type="password"
                      id="password"
                      class="form-control"
                      name="password"
                      placeholder="&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;"
                      aria-describedby="password"
                      onkeyup="checkcorrectform(this);"
                    />
                    <span class="input-group-text cursor-pointer"><i class="bx bx-hide"></i></span>
                  </div>
                </div>

                <div class="mb-3 form-password-toggle">
                  <label class="form-label" for="passwordCk">비밀번호 확인</label>
                  <div class="input-group input-group-merge">
                    <input
                      type="password"
                      id="passwordChk"
                      class="form-control"
                      name="passwordChk"
                      placeholder="&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;"
                      aria-describedby="password"
                      onkeyup="checkcorrectform(this); pssChk();"
                    />
                    <span class="input-group-text cursor-pointer"><i class="bx bx-hide"></i></span>
                  </div>
                  <div id="passwordChk_div"></div>
                </div>

                <div class="mb-3">
                  <label for="email" class="form-label">이메일(비즈메카 이메일)</label>
                  <div class="row">
                      <div class="col-sm-9">
                          <div class="row">
                              <div class="col-sm-12">
                                  <input type="text" class="form-control" id="email" name="email" placeholder="email@iabacus.co.kr" style="ime-mode:disabled;"
                            onkeydown="checkEmail();" onkeyup="checkcorrectform(this);"/>
                              </div>
                          </div>
                      </div>
                      <div class="col-sm-3">
                          <input type="button" id="btn_codeSend" class="btn btn-primary d-grid w-100" value="확인" onclick="codeSend()" disabled>
                      </div>
                  </div>
                </div>
                <div class="mb-3">
                  <div id="codeInput_div"></div>
                  <div id="emailChk_div"></div>
                </div>

                <div class="mb-3">
                  <label for="name" class="form-label">이름</label>
                  <input type="text" class="form-control" id="name" name="name" placeholder="name" onkeyup="checkcorrectform(this);"/>
                  <div id="nameChk_div"></div>
                </div>
                <div class="mb-3">
                  <label for="phone" class="form-label">전화번호</label>
                  <input type="text" class="form-control" id="phone" name="phone" placeholder="phone" onkeyup="phonePattern(this); checkcorrectform(this);"/>
                  <div id="phoneChk_div"></div>
                </div>
                <div class="mb-3">
                  <label for="account" class="form-label">계좌번호</label>
                  <div class="row">
                    <div class="col-sm-5">
                        <select class="form-select" name="bank_name" id="bank_name">
                            <option value="KB국민">KB국민</option>
                            <option value="신한">신한</option>
                            <option value="IBK기업">IBK기업</option>
                        </select>
                    </div>
                    <div class="col-sm-7">
                        <input type="text" class="form-control" id="account" name="account" placeholder="account" onkeyup="checkcorrectform(this);"/>
                    </div>
                  </div>
                  <div id="accountChk_div"></div>
                </div>
                <div class="mb-3">
                	<label for="phone" class="form-label">파트</label>
                	<select class="form-select" name="part" id="part">
                	</select>
                </div>

                <input type="hidden" id="RSAModulus" value="${RSAModulus}" />
                <input type="hidden" id="RSAExponent" value="${RSAExponent}" />
                <input type="hidden" id="USER_ID" name="USER_ID">
                <input type="hidden" id="USER_PW" name="USER_PW">
                <input type="hidden" id="USER_NAME" name="USER_NAME">
                <input type="hidden" id="USER_EMAIL" name="USER_EMAIL">
                <input type="hidden" id="USER_PHONE" name="USER_PHONE">
                <input type="hidden" id="USER_ACCOUNT" name="USER_ACCOUNT">
                <input class="btn btn-primary d-grid w-100" id="btn_save" type="submit" value="회원가입">
              </form>
            </div>
          </div>
          <!-- Register Card -->
        </div>
      </div>
    </div>
<script	src="https://cdnjs.cloudflare.com/ajax/libs/js-sha256/0.9.0/sha256.min.js"></script>
<script>
  var idCheckYn = false;
  var emailCheckYn = false;

  var SetTime;
  var timer;
  var code;


  $.ajax({
        url:"/user/allPartInfo",
        type:"GET",
        dataType : "json",
        success: function(data) {
            debugger;
            var selectEl = document.getElementById("part");
            selectEl.options.length = 0;

            data.forEach((currentElement, index, array) => {
                debugger;
                var objOption = document.createElement("option");
                objOption.text = currentElement.partNm;
                objOption.value = currentElement.partCd;
                selectEl.add(objOption);
            });
        },
        error: function(request,status,error) {
            alert("에러 발생 \n" +
            "에러코드 : "+request.status+"\n"+
            "에러메시지 : "+request.responseText+"\n"+
            "에러 : "+error
            );
        }
    })

    $.ajax({
        url:"/user/temp/info",
        type:"POST",
        success: function(data) {
        },
        error: function(request,status,error) {
            alert("에러 발생 \n" +
            "에러코드 : "+request.status+"\n"+
            "에러메시지 : "+request.responseText+"\n"+
            "에러 : "+error
            );
        }
    })

    window.onbeforeunload = function() {
      $.ajax({
              url:"/user/temp/info",
              type:"DELETE",
              success: function(data) {
              },
              error: function(request,status,error) {
                  alert("에러 발생 \n" +
                  "에러코드 : "+request.status+"\n"+
                  "에러메시지 : "+request.responseText+"\n"+
                  "에러 : "+error
                  );
              }
          })
    };


  function dupIdChk(){
    const element = document.getElementById('dupIdChk_div');
    const userId = document.getElementById('userId');
    userId.value = userId.value.replaceAll(' ', '')

    var id = userId.value;
    var rsa = new RSAKey();
    debugger;

    rsa.setPublic($('#RSAModulus').val(),$('#RSAExponent').val());
    var encryptedId = rsa.encrypt(id);

    $.ajax({
        url:"/user/dupIdChk",
        type:"POST",
        data: {USER_ID : encryptedId},
        dataType : "text",
        success: function(e) {
            element.style.display='block';

            if(e == "Y"){
                element.innerHTML = '<label class="form-label" id="dupChkMsg" style="color:red">사용할수 없는 ID 입니다.</label>';
                userId.focus();
            }else if(e == "N"){
                idCheckYn = true;
                element.innerHTML = '<label class="form-label" id="dupChkMsg" style="color:blue">사용할수 있는 ID 입니다.</label>';
            }else{
                alert("에러 \n" + e);
                element.innerHTML = '<label class="form-label" id="dupChkMsg"></label>';
            }
        },
        error: function(request,status,error) {
            alert("에러 발생 \n" +
            "에러코드 : "+request.status+"\n"+
            "에러메시지 : "+request.responseText+"\n"+
            "에러 : "+error
            );
        }
    })
  }

  function checkcorrectform(event) {
    debugger;
    event.value = event.value.replaceAll(' ', '');
    let id = event.id;
    var value = event.value;

    if(id == "userId"){
        idCheckYn = false;
        document.getElementById('dupIdChk_div').style.display='none';

        const element = document.getElementById('userIdChk_div');

        const regex = /^[a-z|A-Z|0-9]+$/;

        if(value.length == 0){
            element.style.display='none';
            document.getElementById('btn_dupIdChk').disabled = true;
        }else if(!regex.test(value)){
            element.style.display='block';
            element.innerHTML = '<label class="form-label" id="userIdChkMsg" style="color:red">ID는 알파벳과 숫자의 조합만 가능합니다.</label>';
            document.getElementById('btn_dupIdChk').disabled = true;
        }else {
            element.style.display='none';
            document.getElementById('btn_dupIdChk').disabled = false;
        }
    }else if(id == "phone"){
        const element = document.getElementById('phoneChk_div');

        if(value.length == 0){
            element.style.display='none';
        }else if(!(value.substr(0, 3) == "010" && value.replaceAll('-', '').length == 11)){
            element.style.display='block';
            element.innerHTML = '<label class="form-label" id="phoneChkMsg" style="color:red">올바르지 않은 전화번호 형식입니다.</label>';
        }else {
            element.style.display='none';
        }
    }else if(id == "name") {
        const element = document.getElementById('nameChk_div');
        const regex = /^[ㄱ-ㅎ|가-힣]+$/;

        if(value.length == 0){
            element.style.display='none';
        }else if(!regex.test(value)){
            element.style.display='block';
            element.innerHTML = '<label class="form-label" id="nameChkMsg" style="color:red">이름은 한글만 가능합니다.</label>';
        }else {
            element.style.display='none';
        }
    }else if(id == "email") {
        emailCheckYn = false;
        document.getElementById('codeInput_div').style.display='none';
        document.getElementById('btn_codeSend').value = "전송";
        clearInterval(timer);

        const element = document.getElementById('emailChk_div');

        if(value.length == 0){
            element.style.display='none';
            document.getElementById('btn_codeSend').disabled = true;
        }else if (value.indexOf('@iabacus.co.kr') == -1){
            element.style.display='block';
            document.getElementById('btn_codeSend').disabled = true;
            element.innerHTML = '<label class="form-label" id="emailChkMsg" style="color:red">이메일은 비즈메카 메일만 가능합니다.</label>';
        }else {
            element.style.display='none';
            document.getElementById('btn_codeSend').disabled = false;
        }
    }else if(id == "password"){
        const element = document.getElementById('passwordChk');
    }else if(id == "account"){
         const element = document.getElementById('accountChk');
         let account = document.getElementById('account');
         account.value = account.value.replace(/[^-0-9]/g,'');

         var accountNum = account.value;

         if(accountNum.length > 14) {
             account.value = accountNum.substr(0, 14);
         }
     }else if(id == "code"){
        if(value.length > 4){
            event.value = value.substr(0, 4);
        }
     }
  }


  function phonePattern(event) {
      let phone = document.getElementById('phone');
      var number = event.value.replaceAll("-", "").replace(/[^-0-9]/g,'');
      if(number.length > 3 && number.length <= 7){
          phone.value = number.substr(0, 3) + "-" + number.substr(3);
      }else if(number.length > 7) {
          phone.value = number.substr(0, 3) + "-" + number.substr(3, 4) + "-" + number.substr(7, 4);
      }else{
          phone.value = number;
      }
  }

  function pssChk(){
    var password = $("#password").val();
    var passwordChk = $("#passwordChk").val();
    const element = document.getElementById('passwordChk_div');

    if(password == passwordChk){
        element.innerHTML = '<label class="form-label" id="passwordChkMsg" style="color:blue">일치하는 비밀번호입니다.</label>';
    }else{
        element.innerHTML = '<label class="form-label" id="passwordChkMsg" style="color:red">일치하지 않는 비밀번호입니다.</label>';
    }
  }

  function emailCheck(email) {
      var regex=/([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;
      return (email != '' && email != 'undefined' && regex.test(email));
  }

  function codeSend(){
    document.getElementById('codeInput_div').style.display='block';
    document.getElementById('btn_codeSend').disabled = true;
    clearInterval(timer);

    // 최초 설정 시간(기본 : 초)
    let time_minutes = 3;
    let time_seconds = 0;

    SetTime = 180;
    var email = $("#email");
    var rsa = new RSAKey();

    rsa.setPublic($('#RSAModulus').val(),$('#RSAExponent').val());
    var encryptedEmail = rsa.encrypt(email.val());

    const element = document.getElementById('codeInput_div');
    const codeSendBtn = document.getElementById('btn_codeSend');

    codeSendBtn.setAttribute("value", "재전송")

    element.innerHTML =
        '<div class="row" id="codeInput_div">' +
        '<div class="col-sm-4">' +
        '<input type="text" class="form-control" id="code" placeholder="code"  onkeyup="checkcorrectform(this);"/>' +
        '</div>' +
        '<div class="col-sm-2">' +
        '</div>' +
        '<div class="col-sm-3">' +
        '<div class="center" id="ViewTimer"></div>' +
        '</div>' +
        '<div class="col-sm-3">' +
        '<input type="button" class="btn btn-primary d-grid w-100" value="확인" onclick="codeChk()">' +
        '</div>' +
        '</div>'
    ;

    min = Math.floor(SetTime / 60);
    sec = SetTime % 60;

    var msg = "<font color='blue'>" + min + "분 " + sec + "초</font>";

    document.all.ViewTimer.innerHTML = msg;

    $.ajax({
        url:"/user/sendMail",
        type:"POST",
        data: {USER_EMAIL : encryptedEmail},
        dataType : "json",
        success: function(e) {
            if(e.prssYn == "Y"){
                code = e.content;
                timer = setInterval(function() { msg_time(); }, 1000);
                document.getElementById('btn_codeSend').disabled = false;
            }else{
                document.getElementById('btn_codeSend').disabled = false;
                alert("코드전송에 실패하였습니다.\n 다시 시도해 주세요.");
            }
        },
        error: function(request,status,error) {
            alert("에러 발생 \n" +
            "에러코드 : "+request.status+"\n"+
            "에러메시지 : "+request.responseText+"\n"+
            "에러 : "+error
            );
        }
    })
  }

  function msg_time() {   // 1초씩 카운트
      min = Math.floor(SetTime / 60);
      sec = SetTime % 60;

      var msg = "";
      var m = "";

      if(min > 0){
        m = min + "분 " + sec + "초";
      }else {
        m = sec + "초";
      }

      if(min == 0 && sec <= 30){
         msg = "<font color='red'>" + m + "</font>";
      }else{
         msg = "<font color='blue'>" + m + "</font>";
      }

      document.all.ViewTimer.innerHTML = msg;
      --SetTime;

      if (SetTime < 0) {
          $("#codeInput_div").empty();
          document.getElementById('emailChk_div').innerHTML =
          '<label class="form-label" id="dupChkMsg" style="color:red">요청한 시간이 초과되었습니다.<br>코드를 재전송 해주시기 바랍니다.</label>';
          clearInterval(timer);
      }
  }

  function codeChk(){
    var inputCode = sha256($("#code").val());
    const codeSendBtn = document.getElementById('btn_codeSend');
    const email = document.getElementById('email');
    const emailChk_div = document.getElementById('emailChk_div');

    debugger;
    if(code == inputCode){
        clearInterval(timer);
        $("#codeInput_div").remove();
        codeSendBtn.setAttribute("value", "확인");
        codeSendBtn.setAttribute("disabled", "true");
        email.setAttribute("disabled", "true");
        alert("이메일 인증되었습니다.");
        emailCheckYn = true;
        emailChk_div.style.display='block';
        emailChk_div.innerHTML = '<label class="form-label" id="dupChkMsg" style="color:blue">사용 가능한 이메일입니다.</label>';

        checkedEmail();
    }else{
        emailChk_div.style.display='block';
        emailChk_div.innerHTML = '<label class="form-label" id="dupChkMsg" style="color:red">코드가 일치하지 않습니다.</label>';

        nonCheckedEmail();
    }
  }


  function login(){
      var id = $("#userId");
      var pw = $("#password");
      var pwChk = $("#passwordChk");
      var name = $("#name");
      var email = $("#email");
      var phone = $("#phone");
      var account = $("#account");
      var bank_name = $("#bank_name");


      if(id.val() == ""){
      alert("아이디를 입력 해주세요.");
      id.focus();
      return false;
      }

      if(pw.val() == ""){
       alert("비밀번호를 입력 해주세요.");
       pw.focus();
       return false;
      }


      if(email.val() == ""){
       alert("이메일을 입력 해주세요.");
       email.focus();
       return false;
      }

      if(name.val() == ""){
       alert("이름을 입력 해주세요.");
       name.focus();
       return false;
      }

      if(phone.val() == ""){
       alert("전화번호를 입력 해주세요.");
       phone.focus();
       return false;
      }

      if(account.val() == ""){
       alert("계좌번호를 입력 해주세요.");
       account.focus();
       return false;
      }

      if(account.val().length < 11){
       alert("적절하지않은 계좌번호 형식입니다.");
       account.focus();
       return false;
      }

      // rsa 암호화
      var rsa = new RSAKey();
      rsa.setPublic($('#RSAModulus').val(),$('#RSAExponent').val());

      $("#USER_ID").val(rsa.encrypt(id.val()));
      $("#USER_PW").val(rsa.encrypt(pw.val()));
      $("#USER_NAME").val(rsa.encrypt(name.val()));
      $("#USER_EMAIL").val(rsa.encrypt(email.val()));
      $("#USER_PHONE").val(rsa.encrypt(phone.val().replaceAll('-', '')));
      $("#USER_ACCOUNT").val(rsa.encrypt(bank_name.val() + "_" + account.val()));

      id.val("");
      pw.val("");
      name.val("");
      email.val("");
      phone.val("");
      account.val("");
      return true;
  }

  window.onpageshow = function(event){
    if(event.persisted){
        location.href = "/SignUp";
    }
  }
</script>
</body>
</html>