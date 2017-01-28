
package com.uic.ids520.servlet;

import java.io.IOException;
import java.sql.ResultSet;
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
import com.uic.ids520.bean.UserBean;
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet{

		private static final long serialVersionUID = 1L;
		
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			// TODO Auto-generated method stub
			try{
				HttpSession httpSession = request.getSession();
				DatabaseAccessBean dbbean = (DatabaseAccessBean)httpSession.getAttribute("dbbean");
				ErrorMessageBean errorbean = (ErrorMessageBean)httpSession.getAttribute("errorBean");
				UserBean userbean = (UserBean)httpSession.getAttribute("userBean");
				if(errorbean==null){
					errorbean = new ErrorMessageBean();
				}else{
					errorbean.setErrorMessage("");
				}
				if(dbbean==null){
					dbbean = new DatabaseAccessBean(errorbean);
				}
				if(userbean==null){
					userbean = new UserBean();
				}
				errorbean.setErrorMessage(getErrorMessage(request));
				if(errorbean.getErrorMessage().equals("")){
					//System.out.println("DBAccessServlet.doGet() schema is "+dbbean.getDb_schema());
					
					String issuccess = dbbean.connect();		
					String userid = request.getParameter("user_id");
					String password = request.getParameter("password");
					if(issuccess.equals("SUCCESS")){
						List<String> constraints = new ArrayList<>();
						List<String> queryValues = new ArrayList<>();
						constraints.add("user_id");
						constraints.add("passwd");
						queryValues.add(userid);
						queryValues.add(password);
						String query = dbbean.createPrepareStatement("SELECT", "student", null,constraints, null);
						System.out.println("query="+query);
						if(dbbean.getPrepStatement(query).equals("SUCCESS")){
							String status = dbbean.processQueryPreparedStatement(query,queryValues);
							System.out.println("status="+status);
							if(status.equals("SUCCESS")){
								ResultSet resultSet= dbbean.getResultSet();
								if(dbbean.getRowCount()==1){
									if(resultSet!=null){
										while(resultSet.next()){
											if(resultSet.getString("user_id")!=null){
												userbean.setUserId(resultSet.getString("user_id"));
												System.out.println("LoginServlet.doGet() userid is "+userbean.getUserId());
											}
											if(resultSet.getString("first_name")!=null){
												userbean.setFirstName(resultSet.getString("first_name"));
											}
											if(resultSet.getString("last_name")!=null){
												userbean.setLastName(resultSet.getString("last_name"));
											}
											if(resultSet.getString("passwd")!=null){
												userbean.setPassword(resultSet.getString("passwd"));
											}
											if(resultSet.getString("major")!=null){
												userbean.setMajor(resultSet.getString("major"));
											}
										}
										String ip = request.getLocalAddr();
										System.out.println("LoginServlet.doGet() ip is "+ip);
										userbean.setIpaddress(ip);
										httpSession.setAttribute("userBean",userbean);
										System.out.println("LoginServlet.doGet() already logged in "+userbean.isAlreadyLoggedIn());
										if(userbean.isAlreadyLoggedIn()){
											errorbean.setErrorMessage("User already logged in from: "+UserBean.oldip);
											httpSession.removeAttribute("userBean");
											request.setAttribute("error", errorbean);
											dbbean.closeDBResources();
											//userbean = null;
											//response.sendRedirect("login.jsp");
											//httpSession = null;
											//userbean.setAlreadyLoggedIn(false);
											request.getRequestDispatcher("/login.jsp").forward(request, response);
											httpSession.invalidate();
										}else{
											httpSession.setAttribute("dbbean", dbbean);
											dbbean.closeDBResources();
											//getServletContext().getRequestDispatcher("/homePage.jsp").forward(request, response);
											if(userbean.getUserId().equalsIgnoreCase("admin123")){
												getServletContext().getRequestDispatcher("/adminHomePage.jsp").forward(request, response);
											} else{
												getServletContext().getRequestDispatcher("/homePage.jsp").forward(request, response);
											}
										}
									}
								}else{
									errorbean.setErrorMessage("Invalid credentials");
									request.setAttribute("error", errorbean);
									dbbean.closeDBResources();
									request.getRequestDispatcher("/login.jsp").forward(request, response);
								}
							}
						}else{
							//errorbean.setErrorMessage("Connection unsuccessful");
							errorbean = dbbean.getError();
							request.setAttribute("error", errorbean);
							dbbean.closeDBResources();
							request.getRequestDispatcher("/login.jsp").forward(request, response);
							
						}
					}else{
						//errorbean.setErrorMessage("Connection unsuccessful");
						errorbean = dbbean.getError();
						request.setAttribute("error", errorbean);
						//dbbean.closeDBResources();
						request.getRequestDispatcher("/login.jsp").forward(request, response);
					}
				}else{
					request.setAttribute("error", errorbean);
					request.getRequestDispatcher("/login.jsp").forward(request, response);
				}
			}catch(NullPointerException ex){
				ex.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			}
			//response.getWriter().append("Served at: ").append(request.getContextPath());
		}

		private String getErrorMessage(HttpServletRequest request) {
			// TODO Auto-generated method stub
			String message = "Following fields are required. Please enter values :";
			String errorMessage = "";
		   if(request.getParameter("user_id")==null || request.getParameter("user_id").isEmpty()) 
		   {
			   errorMessage= message+"user_id ";
		   }else{
			   String user_id=request.getParameter("user_id");
			   if (user_id.length()<6 || user_id.length()>12){
				 errorMessage = "Userid should be between 6 and 12 characters";  
			   }
		   }
		   		
		   if(request.getParameter("password")==null || request.getParameter("password").isEmpty())
		   {
			   errorMessage=message+ "password ";   
		   }
			return errorMessage;
		}

		/**
		 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
		 */
		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			// TODO Auto-generated method stub
			//System.out.println("DBAccessServlet.doPost()");
			doGet(request, response);
		}
} 
	

