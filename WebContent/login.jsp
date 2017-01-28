<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login Page</title>
<link rel="stylesheet" href="loginPageStyle.css">
</head>
<jsp:useBean id="error" class="com.uic.ids520.bean.ErrorMessageBean" scope="request"></jsp:useBean>
<body>
	<h1>Student Course Registration</h1>
	<div class="login-page">
		<div class="form">
			<form action="LoginServlet" method="post">
				<input type="text" name="user_id" value="avishw4" placeholder="User ID" />
				<input type="password" name="password" value="aditi34" placeholder="Password">
				<button>login</button>
				<p class="message">Not registered? <a href="register.jsp">Create an account</a>
				</p>
				<p class="errormessage"><jsp:getProperty name="error" property="errorMessage" /></p>
			</form>
		</div>
	</div>
</body>
</html>