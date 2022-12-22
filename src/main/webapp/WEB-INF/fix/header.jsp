<%@ page language="java" contentType="text/html; charset-UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>헤더입니다</title>
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
    <header>
        <div style= 'text-align:right;'>
            <a id="userInfoDtl" style = 'margin-right:15px;' href="" value=""></a>
            <input class="btn btn-primary" type="button" id="btn_logout" value="로그아웃" onclick="">
        </div>
    </header>
<script>
    document.addEventListener('DOMContentLoaded', function() {
        <%
          session = request.getSession();
          String userId = (String)session.getAttribute("userId");
          String userName = (String)session.getAttribute("userName");
        %>
    });
</script>
</body>
</html>