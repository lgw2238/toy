<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
 <%@ include file="/WEB-INF/views/javascript.jsp" %>
<html>
	<head>
		<title>LGW</title>
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
		<link rel="stylesheet" href="assets/css/main.css" />
		<noscript><link rel="stylesheet" href="assets/css/noscript.css" /></noscript>
	</head>
	<body class="is-preload">
	<form id="signForm" name="signForm" method="get" enctype="multipart/form-data">
		<input type="hidden" id="signValidation" name="signValidation" value=""/> 
	</form>
 <%@ include file="/WEB-INF/views/common.jsp" %>
		<!-- Wrapper -->
			<div id="wrapper" class="fade-in">
				<!-- Intro -->
					<div id="intro">
						<h1>Show my history</h1>
						<p>a junior Developer LIM GUN WOO, 1993y 12m 31d</p>
						<ul class="actions">
							<li><a href="/" class="button icon solid solo fa-arrow-left">Back Page</a></li>
						</ul>
					</div>

				<!-- Header -->
					<header id="header">
						<a href="index.html" class="logo">SIGN UP</a>
					</header>

				<!-- Nav -->
					<nav id="nav">
						<ul class="links">
							<li class="active"><a href="#">SIGN UP</a></li>
							<li><a href="#">HISTORY</a></li>
							<li><a href="#">BLOG</a></li>
						</ul>
						<ul class="icons">
							<li><a href="#" class="icon brands fa-twitter"><span class="label">Twitter</span></a></li>
							<li><a href="#" class="icon brands fa-facebook-f"><span class="label">Facebook</span></a></li>
							<li><a href="#" class="icon brands fa-instagram"><span class="label">Instagram</span></a></li>
							<li><a href="#" class="icon brands fa-github"><span class="label">GitHub</span></a></li>
						</ul>
					</nav>

				<!-- Main -->
					<div id="main">
						<!-- Featuredfeatured Post -->
							<article class="post featured">
								<header class="major">
								 <!-- parameter 화 -->
									<span class="date">${date} </span>									
									<h2>SIGN UP</h2>
								</header>
								<form id="loginForm" name="loginForm" method="post" action="#">
									<input type="hidden" id="idChk" name="idChk" value=""/> 
								<div class="fields">					
									<div class="field half">
										<label for="id" >ID <em>*</em></label>																												
										<input type="text" name="id" id="id" maxlength="10"/>
										<a class="button small" onclick="javascript:checkId();">ID 중복체크</a>
									<div id="idCheck"></div>
									</div>
									<div class="field half" >
										<label for="password">Password <em>*</em></label>
										<input type="password" name="password" id="password" />
									</div>
									<div class="field half" >
										<label for="password2">Password Check</label>
										<input type="password" name="password2" id="password2" />
									</div>
									<div class="field half" >
										<label for="name">name <em>*</em></label>
										<input type="text" name="name" id="name" />
									</div>	
									<div class="field half">	
										<label for="address">address <em>*</em></label>	
										<input type="text" id="postcode" placeholder="우편번호 주소">
										<a class="button small" onclick="javascript:daumPostcode();">우편번호 찾기</a>										
										<input type="text" id="roadAddress" placeholder="도로명주소">
										<input type="text" id="jibunAddress" placeholder="지번주소">
										<span id="guide" style="color:#999;display:none"></span>
										<input type="text" id="detailAddress" placeholder="상세주소">
										<input type="text" id="extraAddress" placeholder="참고항목">
									</div>		
									<!-- <div class="field half" >
										<label for="address">address <em>*</em></label>
										<input type="text" name="address" id="address" />
										<a class="button small" onclick="javascript:daumAddress();">주소 찾기</a>
									</div> -->
									<div class="field half" >
										<label for="email">email <em>*</em></label>
										<input type="text" name="email" id="email" />
									</div>
								</div>
								<ul class="actions special">
									<li><a class="button large" onclick="javascript:completeJoin();">회원가입 완료</a></li>
								</ul>
								</form>
							</article>	
					</div>
				<!-- Copyright -->
					<div id="copyright">
						<ul><li>&copy; Untitled</li><li>Design: <a href="https://html5up.net">HTML5 UP</a></li></ul>
					</div>
			</div>
			<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
	</body>
</html>