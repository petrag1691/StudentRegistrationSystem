<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.uic.ids520.bean.CourseBean"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Course Registration</title>
<link rel="stylesheet" href="loginPageStyle.css">
</head>
<body>
	<jsp:useBean id="userBean" scope="session"
		class="com.uic.ids520.bean.UserBean">
	</jsp:useBean>
	<jsp:useBean id="errorMessageBean" scope="session"
		class="com.uic.ids520.bean.ErrorMessageBean">
	</jsp:useBean>
	<%
		response.addHeader("Cache-Control",
				"no-cache,no-store,private,must-revalidate,max-stale=0,post-check=0,pre-check=0");
		response.addHeader("Pragma", "no-cache");
		response.addDateHeader("Expires", 0);
	%>
	<%
		if (userBean.getUserId() == null) {
			errorMessageBean.setErrorMessage("Please login.");
			request.setAttribute("error", errorMessageBean);
			request.getRequestDispatcher("./login.jsp").forward(request, response);
		}
		if(userBean.getUserId().equalsIgnoreCase("admin123")){
			errorMessageBean.setErrorMessage("The requested page cannot be displayed due to security reasons.");
	        request.setAttribute("error", errorMessageBean);
	        request.getRequestDispatcher("./adminHomePage.jsp").forward(request, response);
		}
	%>
	<h1>Course Registration</h1>
	<p><b>Logged in as: <%=userBean.getFirstName()%> <%=userBean.getLastName()%></b></p>
	<div class="form">
		<p>Courses Already Registered</p>
		<table border="1" align="center" rules=all>
			<tr>
				<th><p>Course ID</p></th>
				<th><p>Course Name</p></th>
				<th><p>Eligible Major</p></th>
			</tr>
			<c:forEach items="${studentCourseList}" var="studentCourseValue">
			<tr>	
					<td><p><c:out value="${studentCourseValue.courseNumber}" /></p></td>
					<td><p><c:out value="${studentCourseValue.courseName}" /></p></td>
					<td><p><c:out value="${studentCourseValue.courseMajor}" /></p></td>	
			</tr>
			</c:forEach>
		</table>
		<br>
		<form id="registerCourse" method="post" action="courseRegisterServlet">
			<p>Select a course to add or drop</p>
			<select name="courseValue">
				<c:forEach items="${courseList}" var="courseValue">
					<option value="${courseValue.courseName}">
						${courseValue.courseName}</option>
				</c:forEach>
			</select>
			<br>
			<br>
			<button name="addButton">Add Course</button>
		</form>
		<br>
		<p><jsp:getProperty name="errorMessageBean"
				property="errorMessage" /></p>
		<br>
		<form action="LogoutServlet" method="post">
			<button>Logout</button>
		</form>
		<form action="./homePage.jsp">
			<button>Back to Home Page</button>
		</form>
	</div>
</body>
</html>