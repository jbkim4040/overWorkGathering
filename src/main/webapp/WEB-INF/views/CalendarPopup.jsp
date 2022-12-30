<%@ page language="java" contentType="text/html; charset-UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<!-- jquery CDN -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
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
<script>
var params = window.location.search.substr(1);
params = params.split("=")[1];
document.addEventListener("DOMContentLoaded", function(){
	document.getElementById("workDt").innerHTML = params;

    <%
     session = request.getSession();
     String userName = (String)session.getAttribute("userName");
     String userId = (String)session.getAttribute("userId");
    %>
});

$.ajax({
	url:"/work/retrieveWorkOne",
	type:"get",
	data:{userID : "<%=userId%>", workDt : params},
	dataType:"json",
	contentType:"application/json",
	success: function(result){
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
	},
	complete: function(){
	    debugger;
	    const today = new Date();
	    const writeDay = new Date(params);
	    if(!(today.getYear() == writeDay.getYear() &&
	    (12-today.getMonth()+1 == 12-writeDay.getMonth() || 12-today.getMonth() == 12-writeDay.getMonth())
	    )){
	        $("#btn_delete").attr("disabled",true);
	        $("#btn_save").attr("disabled",true);
	    }
	}
});

var fSave = function(url){
	if(url.indexOf("Delete") == -1 && !checkVaild()){
		return;
	}
	debugger;

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

    var formData = new FormData();
    formData.append("userID","<%=userId%>");
    formData.append("workDt",params);
    formData.append("dinnerYn",$('#dinnerYn').is(':checked'));
    formData.append("taxiYn",$('#taxiYn').is(':checked'));
    formData.append("Img",image);
    formData.append("taxiPay",$('#taxiPay').val());
    formData.append("startTime",startHour+":"+startMin);
    formData.append("endTime",endHour+":"+endMin);
    formData.append("remarks",$('#remarks').val());
    formData.append("file",imageInput.files[0]);

    $("#btn_delete").attr("disabled",true);
    $("#btn_save").attr("disabled",true);
	$.ajax({
		url: url,
		enctype:'multipart/form-data',
		processData: false,
        contentType: false,
		type: "POST",
		data: formData,
		success: function(result){
		    debugger;
			alert("처리성공");
			parent.document.querySelector(".modal").classList.add("hidden");
            parent.location.reload();

		},
		error:function(result){
		    alert("등록 실패하였습니다.");
		    $("#btn_delete").attr("disabled",false);
            $("#btn_save").attr("disabled",false);
		}
	});

};

var setTime = function(hour, min){
    if(hour.length == 1) hour = "0" + hour;
    if(min.length == 1) min = "0" + min;

    return (hour + ":" + min);
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
};

function hourChk(obj){
debugger;
    if(obj.value > 23)
        obj.value = 23;
    if(obj.value < 0)
        obj.value = 00;
    if(obj.value.length > 2)
        obj.value = 00;
};

function regex(obj){
    const regex = /^[0-9]+$/;
    if(!regex.test(obj.value)){
    debugger;
        obj.value = obj.value.substr(0, obj.value.length-1);
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
			<div class="mb-3 text-center">
                <input class="form-check-input" type="checkbox" id="dinnerYn">식대 요청</input>
                <input class="form-check-input" type="checkbox" id="taxiYn" onclick="check();">택시비 요청</input>
			</div>
            <div class="mb-3">
                <label>출근시간</label>
                <input class="form-control" style="width:100px; display:inline;" type="number" id="startHour" min="00" max="23" oninput="hourChk(this)" placeholder="시"></input> :
                <input class="form-control" style="width:100px; display:inline;" type="number" id="startMin" min="00" max="59" value="00" oninput="minChk(this)" placeholder="분"></input>
            </div>
            <div class="mb-3">
                <label>퇴근시간</label>
                <input class="form-control" style="width:100px; display:inline;" type="number" id="endHour" min="00" max="23" oninput="hourChk(this)" placeholder="시"></input> :
                <input class="form-control" style="width:100px; display:inline;" type="number" id="endMin" min="00" max="59" value="00" oninput="minChk(this)" placeholder="분"></input>
            </div>
			<div class="mb-3">
			<label class="form-label">택시비</label>
			<input class="form-control" type="text" id="taxiPay" oninput="regex(this)" disabled/>
			<label class="form-label">택시 영수증</label>
			<input class="form-control" type="file" id="taxiFile" accept="image/*"/>
			</div>

			<div class="mb-3">
			<textarea class="form-control" id="remarks" placeholder="비고"></textarea>
			</div>

			<div class="mb-3 text-center">
              <button class="btn btn-primary d-inline w-25" id="btn_save" href="Calendar" onclick="fSave('/work/SaveWork')">확인</button>
              <button class="btn btn-primary w-25" href="Calendar" style="display:none;" id="btn_delete" onclick="fSave('/work/DeleteWork')">삭제</button>
            </div>
		</div>
	</div>
</div>
</div>
</div>

</body>
</html>