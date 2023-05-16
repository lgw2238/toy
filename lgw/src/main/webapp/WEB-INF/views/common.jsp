<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/main.css" /> 
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/jquery.min.js"></script> 
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/jquery.scrolly.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/jquery.scrollex.min.js"></script> 
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/browser.min.js"></script> 
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/breakpoints.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/util.js"></script> 
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/main.js"></script>	

<script src="https://static.nid.naver.com/js/naveridlogin_js_sdk_2.0.2.js" charset="utf-8"></script>
<script src="https://developers.kakao.com/sdk/js/kakao.js"></script>
<script>


function snsJoin(domain){
	var domain = domain;
	if(domain == "naver"){
		naverLogin();
	}else if(domain =="kakao"){
		kakaoLogin();
	}else{
		alert("test 실패");
	}
	console.log("domain:", domain);
	
}

function naverLogin(){
	location.href="user/login/naver";
	/* $.ajax({
		async       : false,
		type        : "POST",
		url         : "${pageContext.request.contextPath}/user/login/naver",
		contentType : "application/x-www-form-urlencoded;charset=UTF-8", 
		data        : { 
			
					},
		dataType    : "json",
		success     : function(json) {
				console.log(json);
			
		},
		error       : function(data, status, error) {
			location.href = "${pageContext.request.contextPath}/error"
		} 
	});		 */

	
}

//카카오 로그인 버튼 클릭
function kakaoLogin(){
    $.ajax({
        url: 'user/login/kakao',
        type: 'get',
    }).done(function (res) {
        location.href = res;
    });


}

</script>

</html>