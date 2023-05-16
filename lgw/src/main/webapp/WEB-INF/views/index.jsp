<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
 <%@ include file="/WEB-INF/views/javascript.jsp" %>
<html>
		<title>LGW</title>
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
	<!-- 	<link rel="stylesheet" href="assets/css/main.css" />
		<noscript><link rel="stylesheet" href="assets/css/noscript.css" /></noscript> -->
	</head>
	<body class="is-preload">
	 <%@ include file="/WEB-INF/views/common.jsp" %>
		<!-- Wrapper -->
			<div id="wrapper" class="fade-in">
				<!-- Intro -->
					<div id="intro">
						<h1>Show my history</h1>
						<p>a junior Developer LIM GUN WOO, 1993y 12m 31d</p>
						<ul class="actions">
							<li><a href="#header" class="button icon solid solo fa-arrow-down scrolly">Continue</a></li>
						</ul>
					</div>

				<!-- Header -->
					<header id="header">
						<a href="index.html" class="logo">LOGIN</a>
					</header>

				<!-- Nav -->
					<nav id="nav">
						<ul class="links">
							<li class="active"><a href="${pageContext.request.contextPath}/index.html">LOGIN</a></li>
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
									<h2>LOGIN PAGE</h2>
								</header>
								<form id="loginForm" name="loginForm" method="post" action="#">
								<div class="fields">					
									<div class="field half" >
										<label for="name">ID</label>
										<input type="text" name="id" id="id" />
									</div>
									<div class="field half" >
										<label for="email">Password</label>
										<input type="password" name="password" id="password" />
									</div>
								</div>
								<ul class="actions special">
									<li><a class="button large" onclick="javascript:goSignUp();">SIGN UP</a></li>
									<li><a class="button large" onclick="javascript:goLogin();">LOGIN</a></li>
								</ul>
								</form>		
								<br/>														
						<div id="snsDiv">
							<a class="button small" onclick="javascript:snsJoin('naver');" >NAVER LOGIN</a><br/>
							<a class="button small" onclick="javascript:snsJoin('kakao');">KAKAO LOGIN</a><br/>
							<a class="button small" onclick="javascript:snsJoin('google');">GOOGLE LOGIN</a>
							
						</div>
					</article>
					<!-- Copyright -->
					<div id="copyright">
						<ul><li>&copy; Untitled</li><li>Design: <a href="https://html5up.net">HTML5 UP</a></li></ul>
					</div>

			</div>

			<!-- Scripts -->
		<!-- 	<script src="assets/js/jquery.min.js"></script>
			<script src="assets/js/jquery.scrollex.min.js"></script>
			<script src="assets/js/jquery.scrolly.min.js"></script>
			<script src="assets/js/browser.min.js"></script>
			<script src="assets/js/breakpoints.min.js"></script>
			<script src="assets/js/util.js"></script>
			<script src="assets/js/main.js"></script> 
 -->
	</body>
</html>