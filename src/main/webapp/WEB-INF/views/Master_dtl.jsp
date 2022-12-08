<%@ page language="java" contentType="text/html; charset-UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
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
<div class="card">
  <div class="card-body" id="card_body">
    <div class="table-responsive text-nowrap">
      <table class="table table-bordered">
        <tbody id="tbody">
          <tr>
            <th><i class="fab fa-angular fa-lg text-danger me-3"></i></th>
          </tr>
          <tr>
            <td><i class="fab fa-angular fa-lg text-danger me-3"></i> <strong>발의일자</strong></td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</div>
<script>
window.onload = function(){

	var part = "본구축프로젝트";
	var dt = "2022-12";


    $.ajax({
        url:"/work/retrieveWorkCollectionDtl",
        type:"get",
        data: {
        	part : part,
        	dt : dt
        		},
        dataType : "json",
        success: function(result) {
        	debugger;
        	setpTable(dt);

        	setpUserTable(result);
        },
        error: function() {
            alert("에러 발생");
        }
    })

};

setpUserTable= function(result) {
debugger;
    let tbody = document.getElementById("tbody");
    let row = tbody.rows.length;
    for (var j = 0; j < 3; j++) {
        for(var i = 0; i < row; i++){
            const newCell = tbody.rows[i].insertCell(-1);
            newCell.width = 200;
            if ( j == 0 && i == 0){
                newCell.innerHTML = "<strong>직급</strong>";
            }
            if ( j == 1 && i == 0){
                newCell.innerHTML = "<strong>"이름"</strong>";
            }

            if ( j == 0 && i == 1) {
                newCell.innerHTML = "<strong>야근식대</strong>";
            }
            if ( j == 1 && i == 1) {
                newCell.innerHTML = "<strong>교통비</strong>";
            }
            if ( j == 2 && i == 1) {
                newCell.innerHTML = "<strong>비고</strong>";
            }

        }
    }

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
</script>
</body>
</html>