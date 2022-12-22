<%@ page language="java" contentType="text/html; charset-UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
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
<%@ include file="/WEB-INF/fix/footer.jsp"%>
<div class="card">
  <div class="card-body" style="width:800px; margin:0 auto;">
    <div class="table-responsive text-nowrap">
      <table class="table table-bordered">
        <thead>
          <tr>
            <th><i class="fab fa-angular fa-lg text-danger me-3"></i> <strong>지출 내역서</strong></th>
            <th><i class="fab fa-angular fa-lg text-danger me-3"></i> <strong>팀</strong></th>
            <th><i class="fab fa-angular fa-lg text-danger me-3"></i> <strong>팀장</strong></th>
          </tr>
        </thead>
        <table class="table table-bordered">
        <tbody id="tbody">
          <tr>
            <td><i class="fab fa-angular fa-lg text-danger me-3"></i> <strong>발의일자</strong></td>
            <td><i class="fab fa-angular fa-lg text-danger me-3"></i> <strong>야근식대</strong></td>
            <td><i class="fab fa-angular fa-lg text-danger me-3"></i> <strong>교통비</strong></td>
            <td><i class="fab fa-angular fa-lg text-danger me-3"></i> <strong>비고</strong></td>
          </tr>
        </tbody>
        </table>
      </table>
    </div>
  </div>
</div>
<%@ include file="/WEB-INF/fix/footer.jsp"%>
<script>
window.onload = function(){
    <%
        session = request.getSession();
        String userId = (String)session.getAttribute("userId");
        String userName = (String)session.getAttribute("userName");
    %>

    var userId = "<%=userId%>";

	var part = "본구축프로젝트";
	var dt = "2022-12";


    $.ajax({
        url:"/work/retrieveWorkCollection",
        type:"get",
        data: {
        	part : part,
        	dt : dt
        		},
        dataType : "json",
        success: function(result) {
        	debugger;
        	setpTable(dt);

        	setpData(result);

        },
        error: function() {
            alert("에러 발생");
        }
    })

};

setpTable = function(dt){

	var yyyy = dt.substr(0, 4);;
	var MM = dt.substr(5);;

	var date = new Date(yyyy, MM, 0).getDate();

	for(var i = 1; i <= date; i++){
		var tbody = document.getElementById("tbody");
		var row = tbody.insertRow( tbody.rows.length ); // 하단에 추가
		var cell1 = row.insertCell(0);
		var cell2 = row.insertCell(1);
		var cell3 = row.insertCell(2);
		var cell4 = row.insertCell(3);
		cell2.setAttribute("id", "dinner" + i);
		cell3.setAttribute("id", "taxi" + i);
		cell4.setAttribute("id", "other" + i);
		cell1.innerHTML = yyyy + "년 " + MM + "월 " + i + "일";
	    cell2.innerHTML = "0 원";
	    cell3.innerHTML = "0 원";
	    cell4.innerHTML = "";
	}

	var tbody = document.getElementById("tbody");
	var row = tbody.insertRow( tbody.rows.length ); // 하단에 추가
	var cell1 = row.insertCell(0);
	var cell2 = row.insertCell(1);
	var cell3 = row.insertCell(2);
	var cell4 = row.insertCell(3);
	cell2.setAttribute("id", "plusDinner");
	cell3.setAttribute("id", "plusTaxi");
	cell1.innerHTML = "합계";
    cell2.innerHTML = "0 원";
    cell3.innerHTML = "0 원";
    cell4.innerHTML = "";

	var tbody = document.getElementById("tbody");
	var row = tbody.insertRow( tbody.rows.length ); // 하단에 추가
	var cell1 = row.insertCell(0);
	var cell2 = row.insertCell(1);
	var cell3 = row.insertCell(2);
	var cell4 = row.insertCell(3);
	cell3.setAttribute("id", "plusAll");
	cell1.innerHTML = "총 합계";
    cell2.innerHTML = "";
    cell3.innerHTML = "0 원";
    cell4.innerHTML = "";

};

setpData = function(result){

	debugger;

	var plusDinner = 0;
	var plusTaxi = 0;
	var plusAll = 0;

	for(var i = 0;  i < result.length ; i++){

		var dd = Number(result[i].workDt.substr(8));

		document.getElementById("dinner" + dd).innerText = result[i].dinnerPay + " 원";
		document.getElementById("taxi" + dd).innerText = result[i].taxiPay + " 원";

		plusDinner += Number(result[i].dinnerPay);
		plusTaxi += Number(result[i].taxiPay);
	}

	plusAll = plusDinner + plusTaxi;

	document.getElementById("plusDinner").innerText = plusDinner + " 원";
	document.getElementById("plusTaxi").innerText = plusTaxi + " 원";
	document.getElementById("plusAll").innerText = plusAll + " 원";

};
</script>
</body>
</html>