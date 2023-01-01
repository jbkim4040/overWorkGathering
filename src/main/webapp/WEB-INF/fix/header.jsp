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
        <div class="dropdown float-start" style="">
            <a href="#" class="btn btn-primary dropdown-toggle float-end" id="btn_dropdown" style="margin-right:5px;" role="button" aria-expanded="false" onclick="dropdown()">
            menu</a>
            <ul class="dropdown-menu float-end" style="top:100%; left:0; margin-top:0.125rem;" id="menuList">
            </ul>
        </div>
            <a id="userInfoDtl" style = 'margin-right:15px;' href="" value=""></a>

    </header>
<script>
var calendarDt;
    document.addEventListener('DOMContentLoaded', function() {
        <%
         session = request.getSession();
         String userName = (String)session.getAttribute("userName");
         String userId = (String)session.getAttribute("userId");
         String userAuth = (String)session.getAttribute("auth");
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

    setDropdownMenu = function(){
        const adminMenu = [{id:"master_Menu", name:"관리화면", url:"Master"}, {id:"master_dtlMenu", name:"관리자상세", url:"Master_dtl"}, {id:"user_dtlMenu", name:"사용자상세", url:"User_dtl"}, {id:"calendarMenu", name:"캘린더", url:"calendar"}]
        const userMenu = [{id:"calendarMenu", name:"캘린더", url:"calendar"}, {id:"user_dtlMenu", name:"사용자상세", url:"User_dtl"}];

        if("<%=userAuth%>" == "M"){
            for(var i=0; i<adminMenu.length; i++){
                const li = document.createElement("li");
                li.setAttribute("id", adminMenu[i].id);
                li.innerHTML="<a class='dropdown-item' onclick=moveMenu('"+ adminMenu[i].url.toString() +"')>"+ adminMenu[i].name +"</a>";

                document.getElementById("menuList").appendChild(li);
            }
        }else{
            for(var i=0; i<userMenu.length; i++){
                const li = document.createElement("li");
                li.setAttribute("id", userMenu[i].id);
                li.innerHTML="<a class='dropdown-item' onclick=moveMenu('" + userMenu[i].url.toString() + "')>"+ userMenu[i].name +"</a>";

                document.getElementById("menuList").appendChild(li);
            }
        }

        const logOut = document.createElement("li");
        logOut.setAttribute("id", "logout");
        logOut.innerHTML="<a class='dropdown-item' onclick='logout();'>로그아웃</a>";

        document.getElementById("menuList").appendChild(logOut);
    }

    moveMenu = function(url){
    debugger;
        if(calendarDt.length == 6){
            calendarDt = calendarDt.substring(0, 5) + "0" + calendarDt.substring(5);
        }
        window.location.href="/"+url+"?calendarDt="+calendarDt;
    }

    dropdown = function(){
    debugger;
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