<%@ page language="java" contentType="text/html; charset-UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인</title>
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
          <!-- Register -->
          <div class="card">
            <div class="card-body">
              <!-- Logo -->
              <div class="app-brand justify-content-center">
                 <span class="app-brand-text demo text-body fw-bolder" style="padding-bottom:10px;">로그인</span>
              </div>
              <!-- /Logo -->
            <form method="post" action="/user/auth" onsubmit="return login()">
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
              </div>
              <div class="mb-3 form-password-toggle">
                <div class="d-flex justify-content-between">
                  <label class="form-label" for="password">비밀번호</label>
                  <a href="FindPw">
                    <small>비밀번호 찾기</small>
                  </a>
                </div>
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
              <div class="mb-3">
                <input type="hidden" id="RSAModulus" value="${RSAModulus}" />
                <input type="hidden" id="RSAExponent" value="${RSAExponent}" />
                <input type="hidden" id="USER_ID" name="USER_ID">
                <input type="hidden" id="USER_PW" name="USER_PW">
                <input class="btn btn-primary d-grid w-100" type="submit" value="로그인">
              </div>
              </form>
                <form id="securedLoginForm" name="securedLoginForm" action="<%=request.getContextPath()%>/login" method="post" style="display: none;">
                    <input type="hidden" name="securedUsername" id="securedUsername" value="" />
                    <input type="hidden" name="securedPassword" id="securedPassword" value="" />
                </form>

              <p class="text-center">
                <a href="SignUp">
                  <span>회원가입</span>
                </a>
              </p>
            </div>
          </div>
          <!-- /Register -->
        </div>
      </div>
    </div>
<script>
    document.addEventListener('DOMContentLoaded', function() {
        <%
            session = request.getSession();
            String login = (String)session.getAttribute("login");
            String ID = (String)session.getAttribute("ID");

            if(login == "999"){
            %>
                alert("로그인에 실패하였습니다. \n 다시 로그인하여 주십시오.");
                debugger;
                $("#userId").attr('value',"<%=ID%>");
                $("#password").val = "";
            <%
            }
        %>
    })

  function login(){
      var id = $("#userId");
      var pw = $("#password");

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

      // rsa 암호화
      var rsa = new RSAKey();
      rsa.setPublic($('#RSAModulus').val(),$('#RSAExponent').val());

      $("#USER_ID").val(rsa.encrypt(id.val()));
      $("#USER_PW").val(rsa.encrypt(pw.val()));

      console.log("encrypt ID :: " + rsa.encrypt(id.val()));
      console.log("encrypt PASSWORD :: " + rsa.encrypt(pw.val()));

      id.val("");
      pw.val("");
      return true;
  }

  window.onpageshow = function(event){
      if(event.persisted){
          location.href = "/login";
      }
  }
</script>
</body>
</html>