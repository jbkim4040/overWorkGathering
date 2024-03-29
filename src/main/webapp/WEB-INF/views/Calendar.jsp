<%@ page language="java" contentType="text/html; charset-UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<!-- 화면 해상도에 따라 글자 크기 대응(모바일 대응) -->
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<!-- jquery CDN -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<!-- fullcalendar CDN -->
<link href='https://cdn.jsdelivr.net/npm/fullcalendar@5.11.0/main.min.css' rel='stylesheet' />
<script src='https://cdn.jsdelivr.net/npm/fullcalendar@5.11.0/main.min.js'></script>
<!-- fullcalendar 언어 CDN -->
<script src='https://cdn.jsdelivr.net/npm/fullcalendar@5.11.0/locales-all.min.js'></script>
<!-- 모달 CDN -->
<link rel="stylesheet" href="./static/css/modal.css" />
<title>Insert title here</title>
</head>
<body>
<%@ include file="/WEB-INF/fix/header.jsp"%>
	<div id='calendar-container' style="margin:0 auto; width:60%;">
		<div id ='calendar'></div>
	</div>

<div class="modal">
  <div class="bg"></div>
  <div class="modalBox" style="width:495px; height:695px;">
    <iframe id="CalendarPopup" width="465" height="670"></iframe>
  </div>
</div>
<%@ include file="/WEB-INF/fix/footer.jsp"%>
<script>
var id = "";
var name = "";
var calendarDt = "";

  document.addEventListener('DOMContentLoaded', function() {
    var calendarEl = document.getElementById('calendar');
    var calendar = new FullCalendar.Calendar(calendarEl, {
      initialView: 'dayGridMonth',

	  views: {
	  	dayGridMonth: {
           type: 'dayGrid',
           duration: { months: 1 },
           monthMode: true,
           fixedWeekCount: false
      	}
	  },
	  dateClick: function(e) {
	    document.getElementById("CalendarPopup").src = "/CalendarPopup?workDt="+ e.dateStr;
	    debugger;
	    document.querySelector(".modal").classList.remove("hidden");
	    document.querySelector(".modal").style.display="flex";
	  }
    });
    calendar.render();


    $(".fc-prev-button").click(function(){
        setpCalendarDt();
    });

    $(".fc-next-button").click(function(){
        setpCalendarDt();
    });

    setpCalendarDt = function(){
        calendarDt = calendar.getDate().getFullYear() + "-" + String(Number(calendar.getDate().getMonth())+1) ;
    };

    $.ajax({
        url:"/work/retrievework",
        type:"get",
        dataType : "json",
        success: function(result) {
        	debugger;
            for(i = 0; i < result.length; i++){
                calendar.addEvent({
                	title : "  근무 시간  " + result[i].startTime + " ~ " + result[i].endTime + " ",
                	start : result[i].workDt
                });
                calendar.addEvent({
                	title : " 택시비 신청 여부  :  " + result[i].taxiYn,
                	start : result[i].workDt
                });
            };
            setpCalendarDt();
        },
        error: function() {
            alert("에러 발생");
        }
    })
  });



  const close = () => {
	  debugger;
    document.querySelector(".modal").classList.add("hidden");
    document.querySelector(".modal").style.display="none";
  }

  document.querySelector(".bg").addEventListener("click", close);

</script>
</body>
</html>