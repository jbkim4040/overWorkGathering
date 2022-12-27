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
        <div class="dropdown float-end" style="width:12%">
            <a href="#" class="btn btn-primary dropdown-toggle float-end" id="btn_dropdown" style="margin-right:5px;" role="button" aria-expanded="false" onclick="dropdown()">
            menu</a>
            <ul class="dropdown-menu float-end" style="top:100%; right:0; margin-top:0.125rem;" id="menuList">
            </ul>
        </div>
            <a id="userInfoDtl" style = 'margin-right:15px;' href="" value=""></a>

            <input class="btn btn-primary" type="button" id="btn_download" value="다운로드" onclick="fileDownlaod()">
    </header>
<script>
    document.addEventListener('DOMContentLoaded', function() {
        <%
         session = request.getSession();
         String userName = (String)session.getAttribute("userName");
         String userId = (String)session.getAttribute("userId");
         String userAuth = "1";
        %>
        debugger;
        setDropdownMenu();
    });


    logout = function(){
        debugger;
        $.ajax({
            url:"/user/logOut",
            type:"POST",
            dataType : "json",
            success: function(result) {
                location.href="/login";
            },
            error: function() {
                location.href="/login";
            }
        })
    }

    fileDownlaod = function(){
        debugger;
        $.ajax({
            url:"/work/taxiReceiptImgFile",
            type:"GET",
            dataType : "json",
            success: function(result) {

            },
            error: function() {
            }
        })
    }

    setDropdownMenu = function(){
        const adminMenu = [{id:"master_dtlMenu", name:"관리화면"}, {id:"calendarMenu", name:"캘린더로"}]
        const userMenu = ["calendarMenu","user_dtlMenu"];

        if(<%=userAuth%> == "1"){
            for(var i=0; i<adminMenu.length; i++){
                const li = document.createElement("li");
                li.setAttribute("id", adminMenu[i].id);
                li.innerHTML="<a class='dropdown-item' href='/calendar'>"+ adminMenu[i].name +"</a>";

                document.getElementById("menuList").appendChild(li);
            }
        }else{
            for(var i=0; i<userMenu.length; i++){
                const li = document.createElement("li");
                li.setAttribute("id", userMenu[i].id);
                li.innerHTML="<a class='dropdown-item' href='/calendar'>"+ userMenu[i].name +"</a>";

                document.getElementById("menuList").appendChild(li);
            }
        }

        const logOut = document.createElement("li");
        logOut.setAttribute("id", "logout");
        logOut.innerHTML="<a class='dropdown-item' onclick='logout();'>로그아웃</a>";

        document.getElementById("menuList").appendChild(logOut);
    }

    dropdown = function(){
        const btn_dropdown = document.getElementById("btn_dropdown");
        const ul = document.getElementById("menuList");
        if(!btn_dropdown.classList.contains("show")){
            btn_dropdown.classList.add("show");
            btn_dropdown.ariaExpanded = "true";
            ul.classList.add("show");

        }else{
            btn_dropdown.classList.remove("show");
            btn_dropdown.ariaExpanded = "false";
            ul.classList.remove("show");
        }
    }

</script>
</body>
</html>