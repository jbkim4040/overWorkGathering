<%@ page language="java" contentType="text/html; charset-UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인</title>


    <script src="http://code.jquery.com/jquery-latest.js"></script>
    <script type="text/javascript" src="/js/rsa.js"></script>
    <script type="text/javascript" src="/js/jsbn.js"></script>
    <script type="text/javascript" src="/js/prng4.js"></script>
    <script type="text/javascript" src="/js/rng.js"></script>
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
          <!-- Register -->
          <div class="card">
            <div class="card-body">
              <!-- Logo -->
              <div class="app-brand justify-content-center">
                 <span class="app-brand-text demo text-body fw-bolder" style="padding-bottom:10px;">로그인</span>
              </div>
              <!-- /Logo -->


              <div class="mb-3">
                <label for="userId" class="form-label">아이디</label>
                <input
                  type="text"
                  class="form-control"
                  id="userId"
                  name="email-username"
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
                <div class="form-check">
                  <input class="form-check-input" type="checkbox" id="remember-me" />
                  <label class="form-check-label" for="remember-me"> Remember Me </label>
                </div>
              </div>
              <div class="mb-3">
                <input type="hidden" id="RSAModulus" value="${RSAModulus}" />
                <input type="hidden" id="RSAExponent" value="${RSAExponent}" />
                <button class="btn btn-primary d-grid w-100" href="btn_login" onclick="btn_login_onclick()">로그인</button>
              </div>
                <input type="hidden" id="rsaPublicKeyModulus" value="<%=publicKeyModulus%>" />
                <input type="hidden" id="rsaPublicKeyExponent" value="<%=publicKeyExponent%>" />
                <a href="<%=request.getContextPath()%>/loginFailure.jsp" onclick="validateEncryptedForm(); return false;">로그인</a>
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
  btn_login_onclick = function(){
    var id = $("#userId");
    var pw = $("#password");

    if(id.val() == ""){
        alert("아이디를 입력해주세요.");
        id.focus();
        return false;
    }

    if(pw.val() == ""){
        alert("비밀번호를 입력해주세요.");
        pw.focus();
        return false;
    }
    console.log("로그인 버튼 클릭");
    console.log("아이디 >>>>> " + id.val());
    console.log("비밀번호 >>>>> " + pw.val());

    var rsa = new RSAKey();
    rsa.setPublic($('#RSAModulus').val(),$('#RSAExponent').val());
    console.log("RSAModulus >>>>> " + $('#RSAModulus').val());
    console.log("RSAExponent >>>>> " + $('#RSAExponent').val());

    var encryptId = "";
    var encryptPw = "";

    if(id.val() == "test"){
        encryptId = id.val();
        encryptPw = pw.val();
    }else{
        encryptId = rsa.encrypt(id.val());
        encryptPw = rsa.encrypt(pw.val());
    }

    console.log("암호화 아이디 >>>>> " + encryptId);
    console.log("암호화 비밀번호 >>>>> " + encryptPw);

    $.ajax({
        url:"/user/auth",
        type:"POST",
        data: {
          userId : encryptId,
          pw : encryptPw
        },
        dataType : "json",
        success: function(result) {
        },
        error: function() {
        }
    })
  }


</script>
</body>
</html>