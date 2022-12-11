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
<--<%@ include file="/fix/header.jsp"%>-->
<div class="card">
  <div class="card-body" id="card_body">
  <input class="btn btn-primary" type="button" id="btn_excel_create" value="엑셀" onClick="createWorkCollectionExcel()">
    <div class="table-responsive text-nowrap">
      <table class="table table-bordered">
        <tbody id="tbody">
          <tr>
            <th style="width:200px;"><i class="fab fa-angular fa-lg text-danger me-3"></i></th>
          </tr>
          <tr>
            <td><i class="fab fa-angular fa-lg text-danger me-3"></i> <strong>발의일자</strong></td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</div>
<--<%@ include file="/fix/footer.jsp"%>-->
<script>
let workCollectionDtl;

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
            workCollectionDtl = result;

            let reqPersonnel = calculationReqPersonnel(result);

            setpDateOfInitiation(dt);

            for (i=0; i<reqPersonnel.length; i++) {
                setpUserTable(Object.values(result)[i], reqPersonnel[i], dt);
            }
        },
        error: function() {
            alert("에러 발생");
        }
    })

};

createWorkCollectionExcel = function(){
    debugger;
    $.ajax({
            url:"/excel/workCollection",
            type:"post",
            data: JSON.stringify(workCollectionDtl),
            dataType : "json",
            contentType: "application/json; charset=UTF-8",
            success: function(result) {
            	debugger;

            },
            error: function() {
                alert("에러 발생");
            }
        })
}

calculationReqPersonnel = function(result){
    return Object.keys(result);
};

setpDateOfInitiation = function(dt) {
	var yyyy = dt.substr(0, 4);
	var MM = dt.substr(5);

	var date = new Date(yyyy, MM, 0).getDate();
    var tbody = document.getElementById("tbody");
	for(var i = 1; i <= date; i++){
		var row = tbody.insertRow( tbody.rows.length ); // 하단에 추가
		var cell1 = row.insertCell(0);
		cell1.innerHTML = yyyy + "년 " + MM + "월 " + i + "일";
	}
    var row = tbody.insertRow( tbody.rows.length ); // 하단에 추가
    var cell1 = row.insertCell(0);
    cell1.innerHTML = "<strong>합 계<strong>";
};

setpUserTable = function(result, reqPersonnel, dt) {
debugger;
    let tbody = document.getElementById("tbody");
    let row = tbody.rows.length;
    for (var j = 0; j < 3; j++) {
        let newCell;
        for(var i = 0; i < row; i++){
            newCell = tbody.rows[i].insertCell(-1);
            newCell.width = 200;

            let day = calculationCellDt(i, tbody);

            if ( result.filter(item => reqPersonnel == item.userId).filter(item => item.workDt.substring(8) == day).length > 0 ){

                setpMealsAndTaxPay( j, newCell, result, reqPersonnel, day );

            } else {
                newCell.innerHTML = "0 원";

                if ( j == 2 ) {
                    newCell.innerHTML = '';
                };
            }
            setpHeader(i, j, tbody, row, newCell, reqPersonnel, result);
        }
        if ( j == 0 ) {
            newCell.innerHTML =  plusMeals(j, result, reqPersonnel);
        } else if ( j == 1 ) {
            newCell.innerHTML = plusTaxiPay(j, result, reqPersonnel);
        } else if (j == 2 ) {
            newCell.innerHTML = 'c'//plusAll();
        }
    }
debugger;
};

setpHeader = function (i, j, tbody, row, newCell, reqPersonnel, result) {

    let userNm = result.filter(item => reqPersonnel == item.userId)[0].name;

    if ( j == 0 && i == 0){
        newCell.innerHTML = "<strong>직급</strong>";
    }
    if ( j == 1 && i == 0){
        newCell.innerHTML = "<strong>" + userNm + "</strong>";
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


calculationCellDt = function(i, tbody) {
    let day = tbody.rows[i].cells[0].innerText.substring(10).replaceAll('일', '');
    return day.length == 1 ? '0' + day : day;
};

setpMealsAndTaxPay = function ( j, newCell, result, reqPersonnel, day) {
    if ( j == 0 ) {
        newCell.innerHTML = "<strong>9000 원</strong>";
    } else if ( j == 1 ) {
        if (result.filter(item => reqPersonnel == item.userId).filter(item => item.workDt.substring(8) == day)[0].taxiPay == ''){
            newCell.innerHTML = "0 원";
        } else {
            newCell.innerHTML = "<strong>" + result.filter(item => reqPersonnel == item.userId).filter(item => item.workDt.substring(8) == day)[0].taxiPay + "원</strong>";
        }
    }
};

plusMeals = function(j, result, reqPersonnel){
    let cnt = result.filter(item => reqPersonnel == item.userId).length;
    return "<strong>" + cnt * 9000  + "원</strong>";
};

plusTaxiPay = function(j, result, reqPersonnel){
    let data = result.filter(item => reqPersonnel == item.userId).filter(item => '' != item.taxiPay).map(item => item.taxiPay);
    if (data.length == 0) {
        taxiPay = 0
    } else {
        taxiPay = data.reduce(function add(sum, currValue){
            return Number(sum) + Number(currValue);
        }, 0);
    }
    return "<strong>" + taxiPay  + "원</strong>";
};

</script>
</body>
</html>