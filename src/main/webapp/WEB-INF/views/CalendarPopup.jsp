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
var params = window.location.search.substr(1);
var a = "N";
var b = "N";
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
		if(result.dinnerYn == "Y"){
			$("input:checkbox[id='dinnerYn']").prop("checked", true);
			a = "Y";
		}
		if(result.taxiYn == "Y"){
			$("input:checkbox[id='taxiYn']").prop("checked", true);
			b = "Y";
			$("#taxiPay").removeAttr("disabled");
		}

		$("#taxiPay").val(result.taxiPay);
	}
});

var saveWork = function(){
debugger;
	if(a != "Y" && b != "Y"){
		fSave("/work/SaveWork");
	}else{
		if(!$('#dinnerYn').is(':checked') && !$('#taxiYn').is(':checked')){
			fSave("/work/DeleteWork");
		}else{
			fSave("/work/SaveWork");
		}
	}

	parent.document.querySelector(".modal").classList.add("hidden");
}

var fSave = function(url){
	if(!checkVaild()){
		return;
	}
    const imageInput = $("#taxiFile")[0];
    const image = imageInput.files[0];
	$.ajax({
		url:url,
		type:"POST",
		data:JSON.stringify({userID : "jhyuk97", workDt : params, dinnerYn:$('#dinnerYn').is(':checked')
			 , taxiYn:$('#taxiYn').is(':checked'), Img:image.name, taxiPay:$('#taxiPay').val(), startTime:$('#startTime').val(), endTime:$('#endTime').val()}),
		contentType:"application/json",
		success: function(result){
			alert("처리성공");
		}
	});

}

var checkVaild = function(){

	if($('#taxiYn').is(':checked') && $('#taxiPay').val() == ""){
		alert("택시비를 입력해주세요.");
		return false;
	}

	return true;

}

var check = function(){
	if($('#taxiYn').is(':checked')){
		$("#taxiPay").removeAttr("disabled");
	}else{
		$("#taxiPay").attr("disabled",true);
	}
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
			<input class="form-check-input" type="checkbox" id="taxiYn" onclick="check();">택시비 요청</input>
            <div class="mb-3"></br>
                <input class="form-control" type="text" id="startTime" placeholder="출근시간"/></br>
                <input class="form-control" type="text" id="endTime" placeholder="퇴근시간"/>
            </div>
			<div class="mb-3">
			<label class="form-label">택시비</label>
			<input class="form-control" type="text" id="taxiPay" disabled/>
			<label class="form-label">택시 영수증</label>
			<input class="form-control" type="file" id="taxiFile" accept="image/*"/>
			</div>

			<div class="mb-3">
              <button class="btn btn-primary d-grid w-100" href="Calendar" onclick="saveWork()">확인</button>
            </div>
		</div>
	</div>
</div>
</div>
</div>

</body>
</html>