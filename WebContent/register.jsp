<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Register</title>
<link rel="stylesheet" href="loginPageStyle.css">
</head>
<jsp:useBean id="error" class="com.uic.ids520.bean.ErrorMessageBean"
	scope="request"></jsp:useBean>

<body>
	<h1>Student Registration System</h1>
	<div class="form">
		<form action="UserRegisterServlet" method="post">
			<input type="text" name="user_id" value="" placeholder="User ID"> 
			<input type="password" name="password" value="" placeholder="Password">
			<input type="text" name="firstName" value="" placeholder="First Name">
			<input type="text" name="lastName" value="" placeholder="Last Name">
			<p>Select you Major</p>
			<br>
			<select name="major">
				<option name="accounting" value="Accounting">Accounting</option>
				<option name="marketing" value="Marketing">Marketing</option>
				<option name="finance" value="Finance">Finance</option>
			</select>
			<br>
			<br>
			<button>Register</button>
		</form>
		<p><jsp:getProperty name="error" property="errorMessage" /></p>
		<br>
		<form action="./login.jsp">
			<button>Back to Login Page</button>
		</form>
	</div>
</body>
</html>