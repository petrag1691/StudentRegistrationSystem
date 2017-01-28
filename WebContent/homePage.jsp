<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Home Page</title>
<link rel="stylesheet" href="loginPageStyle.css">
</head>
<body>
	<jsp:useBean id="userBean" scope="session"
		class="com.uic.ids520.bean.UserBean">
	</jsp:useBean>
	<jsp:useBean id="errorMessageBean" scope="request"
		class="com.uic.ids520.bean.ErrorMessageBean">
	</jsp:useBean>
	<jsp:useBean id="error" class="com.uic.ids520.bean.ErrorMessageBean" scope="request"></jsp:useBean>
	<%
   response.addHeader("Cache-Control", "no-cache,no-store,private,must-revalidate,max-stale=0,post-check=0,pre-check=0"); 
   response.addHeader("Pragma", "no-cache"); 
   response.addDateHeader ("Expires", 0);
   %>
	<%
	if(userBean.getUserId()==null){
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
	<h1>Home Page</h1>
	<p>
		<b>Logged in as: <%=userBean.getFirstName()%> <%=userBean.getLastName()%></b>
	</p>
	<div class="form">
		<form action="LogoutServlet" method="post">
			<button>Logout</button>
		</form>
		<form id="retrieveCourses" method="get" action="RetrieveCourses">
			<button>Course Registration</button>
		</form>
		<p class="errormessage"><jsp:getProperty name="error" property="errorMessage" /></p>
	</div>
	<%
		errorMessageBean.setErrorMessage("");
	%>
</body>
</html>