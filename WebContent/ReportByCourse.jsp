<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*"%>
<%@ page import="java.io.*"%>
<%@ page import="com.uic.ids520.bean.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Report by Course</title>
<link rel="stylesheet" href="reportStyle.css">
<jsp:useBean id="userBean" scope="session"
	class="com.uic.ids520.bean.UserBean">
</jsp:useBean>
<jsp:useBean id="errorMessageBean" scope="session"
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
		<p>
			<c:out value="${answer}" />
		</p>
		<table>
			<tr>
				<th><p>SELECT A COURSE</p></th>
				<th><p>STUDENTS</p></th>
			</tr>
			<tr>
				<td><BR />
					<form method="post" action="ReportServlet">
						<SELECT id="courses" NAME="courses" SIZE="4">
							<c:forEach items="${list}" var="i">
								<OPTION value="${i.courseNumber}">
									<c:out value="${i.courseName}"></c:out>
							</c:forEach>
						</SELECT> <br /> <br /> <input type="hidden" name="action"
							value="studentByCourse">
						<button>Display Students for this course</button>
					</form></td>

				<td>
					<ul style="list-style: none;">
						<c:forEach items="${studentlist}" var="j">
							<li><p><c:out value="${j}" /></p></li>
						</c:forEach>
					</ul>
				</td>
			</tr>
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