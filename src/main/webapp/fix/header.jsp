<%@ page language="java" contentType="text/html; charset-UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>헤더입니다</title>
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
    <header>

        <div style= 'text-align:right;'>
            <a style = 'margin-right:15px;' href="">나의 정보</a>
            <input class="btn btn-primary" type="button" id="btn_logout" value="로그아웃" onclick="logout()">
        </div>
    </header>
<script>

    logout = function(){
        $.ajax({
            url:"/user/logOut",
            type:"POST",
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