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
<%@ include file="/WEB-INF/fix/header.jsp"%>
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
    var userId = "<%=userId%>";
	var part = "본구축프로젝트";
	var dt = getParams();


    $.ajax({
        url:"/work/retrieveWorkCollection",
        type:"get",
        data: {
        	part : part,
        	dt : dt.calendarDt
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
debugger;
	var yyyy = dt.calendarDt.substr(0, 4);
	var MM = dt.calendarDt.substr(5);

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

getParams = function() {
    // 파라미터가 담길 배열
    var param = new Array();

    // 현재 페이지의 url
    var url = decodeURIComponent(location.href);
    // url이 encodeURIComponent 로 인코딩 되었을때는 다시 디코딩 해준다.
    url = decodeURIComponent(url);

    var params;
    // url에서 '?' 문자 이후의 파라미터 문자열까지 자르기
    params = url.substring( url.indexOf('?')+1, url.length );
    // 파라미터 구분자("&") 로 분리
    params = params.split("&");

    // params 배열을 다시 "=" 구분자로 분리하여 param 배열에 key = value 로 담는다.
    var size = params.length;
    var key, value;
    for(var i=0 ; i < size ; i++) {
        key = params[i].split("=")[0];
        value = params[i].split("=")[1];

        param[key] = value;
    }

    return param;
}
</script>
</body>
</html>