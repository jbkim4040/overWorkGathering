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
  <div class="card-body" id="card_body">
  <a />
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
<%@ include file="/WEB-INF/fix/footer.jsp"%>
<script>
let workCollectionDtl;

window.onload = function(){
    var userId = "<%=userId%>";
	var dt = getParams();


    $.ajax({
        url:"/work/retrieveWorkCollectionDtl",
        type:"get",
        data: {
            userId : userId,
        	dt : dt.calendarDt
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
    var url = '/excel/workCollection';
    var data = JSON.stringify(workCollectionDtl);

    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function(res) {
      if (this.readyState == 4 && this.status == 200) {
        var _data = this.response;
        var _blob = new Blob([_data], {type : 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'});
        debugger;
        var link = document.createElement('a');
        link.href = window.URL.createObjectURL(_blob);
        link.download = '야근식대.xlsx';
        link.click();
      };
    };

    xhr.open('POST', url);
    xhr.responseType = 'blob';
    xhr.setRequestHeader('Content-type', 'application/json');
    xhr.send(data);
}

calculationReqPersonnel = function(result){
    return Object.keys(result);
};

setpDateOfInitiation = function(dt) {
	var yyyy = dt.calendarDt.substring(0, 4);
	var MM = dt.calendarDt.substring(5);

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
            newCell.innerHTML =  "<strong>" + plusMeals(j, result, reqPersonnel) + "원</strong>";
        } else if ( j == 1 ) {
            newCell.innerHTML = "<strong>" + plusTaxiPay(j, result, reqPersonnel) + "원</strong>";
        } else if (j == 2 ) {
            newCell.innerHTML = "<strong>" + plusAll(j, result, reqPersonnel) + "원</strong>";
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
    } else if ( j == 2 ) {
        if ( result.filter(item => reqPersonnel == item.userId).filter(item => item.workDt.substring(8) == day)[0].remarks != null) {
            newCell.innerHTML = "<strong>" + result.filter(item => reqPersonnel == item.userId).filter(item => item.workDt.substring(8) == day)[0].remarks + "</strong>";
        }
    }
};

plusMeals = function(j, result, reqPersonnel){
    let cnt = result.filter(item => reqPersonnel == item.userId).length;
    return cnt * 9000;
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
    return taxiPay;
};

plusAll = function(j, result, reqPersonnel){
    let meals =  plusMeals(j, result, reqPersonnel);
    let taxiPays = plusTaxiPay(j, result, reqPersonnel);
    return meals + taxiPays;
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