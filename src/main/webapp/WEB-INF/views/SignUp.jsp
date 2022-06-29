<%@ page language="java" contentType="text/html; charset-UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입</title>
    <script src="http://code.jquery.com/jquery-latest.js"></script>
    <script type="text/javascript" src="../js/rsa.js"></script>
    <script type="text/javascript" src="../js/jsbn.js"></script>
    <script type="text/javascript" src="../js/prng4.js"></script>
    <script type="text/javascript" src="../js/rng.js"></script>

<!-- Core CSS -->
    <link rel="stylesheet" href="../css/core.css" class="template-customizer-core-css" />
    <link rel="stylesheet" href="../css/theme-default.css" class="template-customizer-theme-css" />
    <link rel="stylesheet" href="../css/demo.css" />
<!-- Vendors CSS -->
    <link rel="stylesheet" href="../css/perfect-scrollbar.css" />
<!-- Page CSS -->
<!-- Page -->
    <link rel="stylesheet" href="../css/pages/page-auth.css" />
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
                  <input
                    type="text"
                    class="form-control"
                    id="userId"
                    name="userId"
                    placeholder="ID"
                    autofocus
                  />
                  <button>중복체크</button>
                </div>
                <div class="mb-3">
                  <label for="email" class="form-label">이메일</label>
                  <input type="text" class="form-control" id="email" name="email" placeholder="email" />
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
                    />
                    <span class="input-group-text cursor-pointer"><i class="bx bx-hide"></i></span>
                  </div>
                </div>

                <div class="mb-3 form-password-toggle">
                  <label class="form-label" for="passwordCk">비밀번호 확인</label>
                  <div class="input-group input-group-merge">
                    <input
                      type="password"
                      id="passwordCk"
                      class="form-control"
                      name="passwordCk"
                      placeholder="&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;"
                      aria-describedby="password"
                    />
                    <span class="input-group-text cursor-pointer"><i class="bx bx-hide"></i></span>
                  </div>
                </div>
                <div class="mb-3">
                  <label for="name" class="form-label">이름</label>
                  <input type="text" class="form-control" id="name" name="name" placeholder="name" maxlength="6"/>
                </div>
                <div class="mb-3">
                  <label for="phone" class="form-label">전화번호</label>
                  <input type="text" class="form-control" id="phone" name="phone" placeholder="phone" maxlength="12"/>
                </div>
                <div class="mb-3">
                	<label for="phone" class="form-label">파트</label>
                	<select class="form-select" name="part" id="part">
                		<option value="a">a</option>
                		<option value="b">b</option>
                		<option value="c">c</option>
                	</select>
                </div>
                <div class="mb-3">
                	<label for="phone" class="form-label">파트 리더</label>
                	<select class="form-select" name="partleader" id="partleader">
                		<option value="아무개">아무개</option>
                	</select>
                </div>
                <input type="hidden" id="RSAModulus" value="${RSAModulus}" />
                <input type="hidden" id="RSAExponent" value="${RSAExponent}" />
                <input type="hidden" id="USER_ID" name="USER_ID">
                <input type="hidden" id="USER_PW" name="USER_PW">
                <input type="hidden" id="USER_NAME" name="USER_NAME">
                <input type="hidden" id="USER_EMAIL" name="USER_EMAIL">
                <input type="hidden" id="USER_PHONE" name="USER_PHONE">
                <input class="btn btn-primary d-grid w-100" type="submit" value="회원가입">
              </form>
            </div>
          </div>
          <!-- Register Card -->
        </div>
      </div>
    </div>

<script>
  document.addEventListener('DOMContentLoaded', function() {



    $.ajax({
            url:"/work/retrievework",
            type:"get",
            data: {userId : id},
            dataType : "json",
            success: function(result) {
            	debugger;
                for(i = 0; i < result.length; i++){
                    calendar.addEvent({
                    	title : "  근무 시간  " + result[i].startTime + " ~ " + result[i].endTime + " ",
                    	start : result[i].workDt
                    });
                    calendar.addEvent({
                    	title : " 택시비 신청 여부  :  " + result[i].taxiYn,
                    	start : result[i].workDt
                    });
                };


            },
            error: function() {
                alert("에러 발생");
            }
        })

  })

  function login(){
      var id = $("#userId");
      var pw = $("#password");
      var name = $("#name");
      var email = $("#email");
      var phone = $("#phone");

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

      if(pw.val() == ""){
       alert("이름을 입력 해주세요.");
       pw.focus();
       return false;
      }

      if(email.val() == ""){
       alert("이메일을 입력 해주세요.");
       email.focus();
       return false;
      }

      if(phone.val() == ""){
       alert("전화번호를 입력 해주세요.");
       phone.focus();
       return false;
      }

      // rsa 암호화
      var rsa = new RSAKey();
      rsa.setPublic($('#RSAModulus').val(),$('#RSAExponent').val());

      $("#USER_ID").val(rsa.encrypt(id.val()));
      $("#USER_PW").val(rsa.encrypt(pw.val()));
      $("#USER_NAME").val(rsa.encrypt(name.val()));
      $("#USER_EMAIL").val(rsa.encrypt(email.val()));
      $("#USER_PHONE").val(rsa.encrypt(phone.val()));

      console.log("encrypt ID :: " + rsa.encrypt(id.val()));
      console.log("encrypt PASSWORD :: " + rsa.encrypt(pw.val()));
      console.log("encrypt NAME :: " + rsa.encrypt(name.val()));
      console.log("encrypt EMAIL :: " + rsa.encrypt(email.val()));
      console.log("encrypt PHONE :: " + rsa.encrypt(phone.val()));

      id.val("");
      pw.val("");
      name.val("");
      email.val("");
      phone.val("");
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