<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*"%>
<%@ page import="java.io.*"%>
<%@ page import="com.uic.ids520.bean.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>All Students and Courses List</title>
<link rel="stylesheet" href="reportStyle.css">
<jsp:useBean id="userBean" scope="session"
	class="com.uic.ids520.bean.UserBean">
</jsp:useBean>
<jsp:useBean id="errorMessageBean" scope="request"
	class="com.uic.ids520.bean.ErrorMessageBean">
</jsp:useBean>
</head>
<body>
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
		if(!userBean.getUserId().equalsIgnoreCase("admin123")){
			errorMessageBean.setErrorMessage("The requested page cannot be displayed due to security reasons.");
	        request.setAttribute("error", errorMessageBean);
	        request.getRequestDispatcher("./homePage.jsp").forward(request, response);
		}
	%>
	<h1>Report by Course</h1>
	<p>
		<b>Logged in as: <%=userBean.getFirstName()%> <%=userBean.getLastName()%></b>
	</p>
	<div class="form">
		<table border="1" align="center">
			<tr>
				<th><p>Student ID</p></th>
				<th><p>First Name</p></th>
				<th><p>Last Name</p></th>
				<th><p>Course ID</p></th>
				<th><p>Course Name</p></th>
			</tr>

			<c:forEach items="${list}" var="i">
				<tr>
					<c:forEach begin="0" end="${fn:length(i) - 1}" var="index">
						<td><p>
								<c:out value="${i[index]}"></c:out>
							</p></td>
					</c:forEach>
				</tr>
			</c:forEach>
		</table>
		<br /> <br />
		<form action="ReportServlet" method="post">
			<input type="hidden" name="action" value="goBack">
			<button>Back to Report Options</button>
		</form>
		<form action="LogoutServlet" method="post">
			<button>Logout</button>
		</form>
		<form action="ReportServlet" method="post">
			<input type="hidden" name="action" value="goBackToHome">
			<button>Back to Home Page</button>
		</form>
	</div>
</body>
</html>