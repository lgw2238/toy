<%@ page language="java"  pageEncoding="utf-8"%>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
<meta name="description" content="">
<meta name="author" content="">
<script type="text/javascript">



/* 회원가입 함수 */
function goSignUp(){
	if(confirm("회원 가입을 진행하시겠습니까?")){
		location.href = "/signUp";
	}else{
		return false;
	}
}

/* 회원 가입 완료  */
function completeJoin(){
	var validation = $("#signValidation").val();
	if(validationUserInfo()){
		var userId = $("#id").val();
		var userPw = $("#password").val();
		var userNm = $("#name").val();
		var postAddress = $("#postcode").val();
		var roadAddress = $("#roadAddress").val();
		var jibunAddress = $("#jibunAddress").val();
		var email = $("#email").val();
		
		var param = {};
        param.userId = userId;
        param.userPw = userPw;
        param.userNm = userNm;
        param.postAddress = postAddress;
        param.roadAddress = roadAddress;
        param.jibunAddress = jibunAddress;
        param.email = email;
		
		console.log("param:", param);
		/* 회원가입 ajax */
		$.ajax({
			url : "${pageContext.request.contextPath}/user/signUp",
			type : "post",
			dataType : "json",
			contentType : "application/json",
			data : JSON.stringify(param),
			success : function(json){
				if(json.resultCode == "200"){
					alert(json.resultMsg+"\n로그인 화면으로 이동합니다.");
					location.href = "/";
				}else{
					alert(json.resultMsg);
				
				}
			}
		})
		
		
	}else{
		
	}
	
}

/* id check */
function checkId(){
	if($("#id").val() == ""){
		alert("ID를 입력해주세요.");
		return;
	}

	var status = idInputCheck($("#id").val());
	if(!status){
		return;
	}
	
	$.ajax({
		url : "${pageContext.request.contextPath}/user/userIdChk",
		type : "post",
		dataType : "json",
		data : {
			 	userId : $("#id").val()
			   },
		success : function(result){
			if(result == 1){
				alert("현재 사용하고 있는 ID입니다.\n다른 ID를 입력해주세요.");
				$('#id').val('').focus();
			}else if(result == 0){
				$("#idChk").attr("value", "Y");
				alert("사용가능한 ID입니다.");
			/* 	$("#idDuplication").val("idCheck"); */
			}
		}
	})
}

function idInputCheck(asValue) {
	var regType1 = /^[A-Za-z0-9+]*$/;
    if(!regType1.test(asValue)){
        alert('아이디 조건에 맞지 않습니다.');
        $('#id').val('').focus();
        return false;
     }else{
        return true;
     }
}

/*password check */
function validationMyPagePasswordChange(){
	var password1 = $("#password").val();
	var password2 = $("#password2").val();
	console.log("=============================");
	console.log("password1:", password1);
	console.log("password2:", password2);
	console.log("=============================");
	if( password1 == "" || password2 == "" ){
		alert("필수 입력 정보가 입력되지 않았습니다.\n필수 입력 정보를 입력해주세요.");
		return false;
	}

	if( password1 != password2 ){
		alert("입력하신 비밀번호와\n비밀번호 재입력의 입력정보가 일치 하지 않습니다.\n다시 한 번 입력해주세요.");
		$("#password1").focus();
		return false;
	}

	if( checkPassword(password1) ){
		return checkPassword(password2);	
	}else{
		return false;
	}
}

function validationUserInfo(){
	var status = true;
	
	if( $("#id").val() == "" ){
		alert("아이디를 입력 해주세요.");
		return false;
	}

	if( $("#password").val() == "" ){
		alert("비밀번호를 입력 해주세요.");
		return false;		
	}

	if( $("#password2").val() == "" ){
		alert("비밀번호 재입력란을  입력 해주세요.");
		return false;		
	}

	if(validationMyPagePasswordChange()){
		alert("성공");
	}else{
		return false;
	}
	
	if( $("#name").val() == "" ){
		alert("이름을  입력 해주세요.");
		return false;		
	}
	
	if( $("#postcode").val() == "" ||
		 $("#roadAddress").val() == "" ||
		 $("#jibunAddress").val() == "" ){
		 
		alert("주소를 입력 해주세요.");
		return false;		
	}
	
	if( $("#email").val() == "" ){
		alert("이메일을 입력해주세요.");
		return false;	
			
	}else{
		// 이메일 입력한 경우 정규식 validation
		if(isEmail($("#email").val())){
			
		}else{
			return false;	
		}
	}

	return status;
	
	$("#signValidation").val(status);
	
}


//비밀번호 정규화 영문,숫자,특문 + 10~15자리 validation
function checkPassword(password){
	var pw = password;
	var number = pw.search(/[0-9]/g);
	var charEng = pw.search(/[a-z]/ig);
	var charSpecial = pw.search(/[`~!@@#$%^&*|\\\'\";:\/?]/gi);
	 
	if(pw.length < 10 || pw.length > 15){
		alert("비밀번호는 10자리 ~ 15자리 이내로 입력해주세요.");
		return false;
	}

	if(number < 0 || charEng < 0 || charSpecial < 0 ){
		alert("비밀번호는 영문,숫자, 특수문자를 조합하여 입력해주세요.");
		return false;
	}
	
	if(pw.search(/\s/) != -1){
		alert("비밀번호는 비밀번호는 공백없이 입력해주세요.");
		return false;
	}
	 
	return true;
}


//이메일 체크 정규식
function isEmail(asValue) {
   	var regExp = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
    if(!regExp.test(asValue)){
        alert('잘못된 이메일 형식입니다.');
        $('#email').val('').focus();
        return false;
     }else{
        //alert('올바른 이메일 형식입니다.');
        return true;
     }
}


/* 다음 주소 api function1 */
function daumPostcode(){
	  new daum.Postcode({
          oncomplete: function(data) {
              // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

              // 도로명 주소의 노출 규칙에 따라 주소를 표시한다.
              // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
              var roadAddr = data.roadAddress; // 도로명 주소 변수
              var extraRoadAddr = ''; // 참고 항목 변수

              // 법정동명이 있을 경우 추가한다. (법정리는 제외)
              // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
              if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                  extraRoadAddr += data.bname;
              }
              // 건물명이 있고, 공동주택일 경우 추가한다.
              if(data.buildingName !== '' && data.apartment === 'Y'){
                 extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
              }
              // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
              if(extraRoadAddr !== ''){
                  extraRoadAddr = ' (' + extraRoadAddr + ')';
              }

              // 우편번호와 주소 정보를 해당 필드에 넣는다.
              document.getElementById('postcode').value = data.zonecode;
              document.getElementById("roadAddress").value = roadAddr;
              document.getElementById("jibunAddress").value = data.jibunAddress;
              
              // 참고항목 문자열이 있을 경우 해당 필드에 넣는다.
              if(roadAddr !== ''){
                  document.getElementById("extraAddress").value = extraRoadAddr;
              } else {
                  document.getElementById("extraAddress").value = '';
              }

              var guideTextBox = document.getElementById("guide");
              // 사용자가 '선택 안함'을 클릭한 경우, 예상 주소라는 표시를 해준다.
              if(data.autoRoadAddress) {
                  var expRoadAddr = data.autoRoadAddress + extraRoadAddr;
                  guideTextBox.innerHTML = '(예상 도로명 주소 : ' + expRoadAddr + ')';
                  guideTextBox.style.display = 'block';

              } else if(data.autoJibunAddress) {
                  var expJibunAddr = data.autoJibunAddress;
                  guideTextBox.innerHTML = '(예상 지번 주소 : ' + expJibunAddr + ')';
                  guideTextBox.style.display = 'block';
              } else {
                  guideTextBox.innerHTML = '';
                  guideTextBox.style.display = 'none';
              }
          }
      }).open();
	
}

/* 한글 정규식 */
function chk_han(id) {
    var regexp = /[a-z0-9]|[ \[\]{}()<>?|`~!@#$%^&*-_+=,.;:\"'\\]/g;
    var value = $("#"+id).val();
    if (regexp.test(value)) {
        $("#"+id).val(value.replace(regexp, ''));
    }
}


</script>