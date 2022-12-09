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
	    if(result.dinnerYn == "Y" || result.taxiYn == "Y"){
            $("#btn_delete").show();
	    }else{
	        $("#btn_delete").hide();
	    }

		if(result.dinnerYn == "Y"){
			$("input:checkbox[id='dinnerYn']").prop("checked", true);
		}
		if(result.taxiYn == "Y"){
			$("input:checkbox[id='taxiYn']").prop("checked", true);
			$("#taxiPay").removeAttr("disabled");
		}

		$("#taxiPay").val(result.taxiPay);
		$("#startHour").val(result.startTime.substr(0,2));
		$("#startMin").val(result.startTime.substr(3,2));
		$("#endHour").val(result.endTime.substr(0,2));
		$("#endMin").val(result.endTime.substr(3,2));
		$("#remarks").val(result.remarks);

	}
});

var fSave = function(url){
	if(url.indexOf("Delete") == -1 && !checkVaild()){
		return;
	}
    const imageInput = $("#taxiFile")[0];
    let image = "";
    if(imageInput.files[0] !== undefined){
        image = imageInput.files[0].name
    }
    var formData = new FormData();
    formData.append("fileObj",imageInput.files[0]);

    let startHour = $('#startHour').val();
    let startMin  = $('#startMin').val();
    let endHour   = $('#endHour').val();
    let endMin    = $('#endMin').val();
    debugger;
    if(startHour.length == 1){
        startHour = "0" + startHour;
    }
    if(startMin.length == 1){
            startMin = "0" + startMin;
    }
    if(endHour.length == 1){
            endHour = "0" + endHour;
    }
    if(endMin.length == 1){
            endMin = "0" + endMin;
    }

	$.ajax({
		url:url,
		type:"POST",
		data:JSON.stringify({userID : "jhyuk97", workDt : params, dinnerYn:$('#dinnerYn').is(':checked')
			 , taxiYn:$('#taxiYn').is(':checked'), Img:image, taxiPay:$('#taxiPay').val()
			 , startTime:startHour+":"+startMin, endTime:endHour+":"+endMin, remarks:$('#remarks').val()}),
		contentType:"application/json",
		success: function(result){
		debugger;
			alert("처리성공");
			parent.document.querySelector(".modal").classList.add("hidden");
            parent.location.reload();

		}
	});

};

var checkVaild = function(){
    if(!$('#taxiYn').is(':checked') && !$('#dinnerYn').is(':checked')){
        alert("체크박스를 선택해주세요");
        return false;
    }

	if($('#taxiYn').is(':checked') && $('#taxiPay').val() == ""){
		alert("택시비를 입력해주세요.");
		return false;
	}

	if($('#dinnerYn').is(':checked') && $('#endHour').val() < 21 && $('#endHour').val() > 9){
	    alert('21시 이전에 퇴근한 경우\n야근식대 신청이 불가합니다.');
	    return false;
	}
debugger;
	if($('#taxiYn').is(':checked') && $('#endHour').val() < 23 && $('#endHour').val() > 9){
	    alert('23시 이전에 퇴근한 경우\n택시비 신청이 불가합니다.');
	    return false;
	}

	return true;

};

var check = function(){
	if($('#taxiYn').is(':checked')){
		$("#taxiPay").removeAttr("disabled");
	}else{
		$("#taxiPay").attr("disabled",true);
	}
};

function minChk(obj){
    if(obj.value > 59)
        obj.value = 59;
    if(obj.value < 0)
        obj.value = 00;
    if(obj.value.length > 2)
        obj.value = 00;
}

function hourChk(obj){
debugger;
    if(obj.value > 23)
        obj.value = 23;
    if(obj.value < 0)
        obj.value = 00;
    if(obj.value.length > 2)
        obj.value = 00;
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
			<div class="mb-3 text-center">
                <input class="form-check-input" type="checkbox" id="dinnerYn">식대 요청</input>
                <input class="form-check-input" type="checkbox" id="taxiYn" onclick="check();">택시비 요청</input>
			</div>
            <div class="mb-3">
                <label>출근시간</label>
                <input class="form-control" style="width:100px; display:inline;" type="number" id="startHour" min="00" max="23" oninput="hourChk(this)" placeholder="시"></input> :
                <input class="form-control" style="width:100px; display:inline;" type="number" id="startMin" min="00" max="59" oninput="minChk(this)" placeholder="분"></input>
            </div>
            <div class="mb-3">
                <label>퇴근시간</label>
                <input class="form-control" style="width:100px; display:inline;" type="number" id="endHour" min="00" max="23" oninput="hourChk(this)" placeholder="시"></input> :
                <input class="form-control" style="width:100px; display:inline;" type="number" id="endMin" min="00" max="59" oninput="minChk(this)" placeholder="분"></input>
            </div>
			<div class="mb-3">
			<label class="form-label">택시비</label>
			<input class="form-control" type="text" id="taxiPay" disabled/>
			<label class="form-label">택시 영수증</label>
			<input class="form-control" type="file" id="taxiFile" accept="image/*"/>
			</div>

			<div class="mb-3">
			<textarea class="form-control" id="remarks" placeholder="비고"></textarea>
			</div>

			<div class="mb-3 text-center">
              <button class="btn btn-primary d-inline w-25" href="Calendar" onclick="fSave('/work/SaveWork')">확인</button>
              <button class="btn btn-primary w-25" href="Calendar" style="display:none;" id="btn_delete" onclick="fSave('/work/DeleteWork')">삭제</button>
            </div>
		</div>
	</div>
</div>
</div>
</div>

</body>
</html>