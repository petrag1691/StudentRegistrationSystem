/**
 * This is a Servlet Class performing the functionality required for the User Registration
 * @author  Raghu Keerthi Petla, Aditi Vishwas Rao, Mehul Parmar , Anirudh Palakurthi
 */
package com.uic.ids520.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.uic.ids520.bean.DatabaseAccessBean;
import com.uic.ids520.bean.ErrorMessageBean;

@WebServlet("/UserRegisterServlet")
public class UserRegistrationServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			HttpSession httpSession = request.getSession();
			DatabaseAccessBean dbbean = (DatabaseAccessBean)httpSession.getAttribute("dbbean");
			ErrorMessageBean errorbean = (ErrorMessageBean)httpSession.getAttribute("errorBean");
			
			if(errorbean==null){
				errorbean = new ErrorMessageBean();
			}else{
				errorbean.setErrorMessage("");
			}
			if(dbbean==null){
				dbbean = new DatabaseAccessBean(errorbean);
			}
			errorbean.setErrorMessage(getErrorMessage(request));
			if(errorbean.getErrorMessage().equals("")){
				
				
				String issuccess = dbbean.connect();		
				String userid = request.getParameter("user_id");
				String password = request.getParameter("password");
				String firstName = request.getParameter("firstName");
				String lastName = request.getParameter("lastName");
				String major = request.getParameter("major");
				if(issuccess.equals("SUCCESS")){
					List<String> constraints = new ArrayList<>();
					List<String> queryValues = new ArrayList<>();
					constraints.add("user_id");
					queryValues.add(userid);
					String query = dbbean.createPrepareStatement("SELECT", "student", null,constraints, null);
					
					if(dbbean.getPrepStatement(query).equals("SUCCESS")){
						String status = dbbean.processQueryPreparedStatement(query,queryValues);
						if(status.equals("SUCCESS")){
							if(dbbean.getRowCount()==0){
								List<String> columns = new ArrayList<>();
								columns.add("user_id");
								columns.add("passwd");
								columns.add("first_name");
								columns.add("last_name");
								columns.add("major");
								queryValues.clear();
								queryValues.add(userid);
								queryValues.add(password);
								queryValues.add(firstName);
								queryValues.add(lastName);
								queryValues.add(major);
								if(constraints!=null && constraints.size()>0){
									constraints.clear();
								}
								query = dbbean.createPrepareStatement("INSERT", "student", columns, constraints, null);
								if(dbbean.getPrepStatement(query).equals("SUCCESS")){
									status = dbbean.processQueryPreparedStatement(query,queryValues);
									if(status.equals("SUCCESS")){
										int changedRows = dbbean.getChangedRows();
										dbbean.closeDBResources();
										if(changedRows==1){
											errorbean.setErrorMessage("Registration successful");
											
											request.setAttribute("error", errorbean);
											getServletContext().getRequestDispatcher("/login.jsp").forward(request, response);
										}
									}else{
										request.setAttribute("error", errorbean);
										dbbean.closeDBResources();
										request.getRequestDispatcher("/register.jsp").forward(request, response);
									}
								}else{
									request.setAttribute("error", errorbean);
									dbbean.closeDBResources();
									request.getRequestDispatcher("/register.jsp").forward(request, response);
								}
							}else{
								errorbean.setErrorMessage("Student already exists");
								request.setAttribute("error", errorbean);
								dbbean.closeDBResources();
								request.getRequestDispatcher("/register.jsp").forward(request, response);
							}
						}
					}else{
						errorbean = dbbean.getError();
						request.setAttribute("error", errorbean);
						dbbean.closeDBResources();
						request.getRequestDispatcher("/register.jsp").forward(request, response);
					}
				}else{
					errorbean = dbbean.getError();
					request.setAttribute("error", errorbean);
					request.getRequestDispatcher("/register.jsp").forward(request, response);
				}
			}else{
				request.setAttribute("error", errorbean);
				request.getRequestDispatcher("/register.jsp").forward(request, response);
			}
		}catch(NullPointerException ex){
			ex.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private String getErrorMessage(HttpServletRequest request) {
		
		String message = "Following fields are required. Please enter values for :userid,password,firstname and lastname";
		String errorMessage = "";
		String pattern = "^[a-zA-Z0-9]+$";
	   if(request.getParameter("user_id")==null || request.getParameter("user_id").isEmpty()) 
	   {
		   errorMessage= message;
	   }else{
		   String user_id=request.getParameter("user_id");
		   if (user_id.length()<6 || user_id.length()>12){
			 errorMessage = "Userid and Password should be between 6 and 12 characters.";  
		   }
		   if(!user_id.matches(pattern) && errorMessage.trim().isEmpty()){
			   errorMessage = "Special characters are not allowed in userid and password fields";
		   }
	   }
	   		
	   if(request.getParameter("password")==null || request.getParameter("password").isEmpty())
	   {
		   errorMessage = message;
	   }else{
		   String password=request.getParameter("password");
		   if (password.length()<6 || password.length()>12){
			 errorMessage = "Userid and Password should be between 6 and 12 characters";  
		   }
		   if(!password.matches(pattern) && errorMessage.trim().isEmpty()){
			   errorMessage = "Special characters are not allowed in userid and password fields";
		   }
	   }
	   if(request.getParameter("firstName")==null || request.getParameter("firstName").trim().isEmpty() || request.getParameter("lastName")==null || request.getParameter("lastName").trim().isEmpty()){
		   errorMessage = message;
	   }
		return errorMessage;
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
