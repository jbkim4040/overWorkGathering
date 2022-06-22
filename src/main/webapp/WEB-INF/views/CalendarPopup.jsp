<%@ page language="java" contentType="text/html; charset-UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<!-- jquery CDN -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
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
<script>
debugger;
var params = window.location.search.substr(1);
params = params.split("=")[1];
document.addEventListener("DOMContentLoaded", function(){
	document.getElementById("workDt").innerHTML = params;

});

$.ajax({
	url:"/work/retrieveWorkOne",
	type:"get",
	data:{userID : "jhyuk97", workDt : params},
	dataType:"json",
	contentType:"application/json",
	success: function(result){
		debugger;
		if(result.dinnerYn == "Y"){
			$("input:checkbox[id='dinnerYn']").prop("checked", true);
		}
		if(result.taxiYn == "Y"){
			$("input:checkbox[id='taxiYn']").prop("checked", true);
		}
	}
});

var save = function(){
debugger;
	parent.document.querySelector(".modal").classList.add("hidden");
}
</script>
<div class="container-xxl">
<div class="authentication-wrapper authentication-basic container-p-y">
<div class="authentication-inner py-4">
	<div class="card">
		<div class="card-body">
			<div class="app-brand justify-content-center">
				<span id="workDt" class="app-brand-text demo text-body fw-bolder" style="padding-bottom:10px;">날짜적기</span>
			</div>
			<input class="form-check-input" type="checkbox" id="dinnerYn">식대 요청</input>
			<input class="form-check-input" type="checkbox" id="taxiYn">택시비 요청</input>

			<div class="mb-3">
			<label class="form-label">택시 영수증</label>
			<input class="form-control" type="file"/>
			</div>

			<div class="mb-3">
              <button class="btn btn-primary d-grid w-100" href="Calendar" onclick="save()">확인</button>
            </div>
		</div>
	</div>
</div>
</div>
</div>

</body>
</html>