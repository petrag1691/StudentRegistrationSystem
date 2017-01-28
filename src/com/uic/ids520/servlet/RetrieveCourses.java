/**
 * This is a Servlet Class performing the functionality required for the course retrieval
 * @author  Raghu Keerthi Petla, Aditi Vishwas Rao, Mehul Parmar , Anirudh Palakurthi
 */
package com.uic.ids520.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.uic.ids520.bean.CourseBean;
import com.uic.ids520.bean.DatabaseAccessBean;
import com.uic.ids520.bean.ErrorMessageBean;
import com.uic.ids520.bean.UserBean;

@WebServlet("/RetrieveCourses/*")
public class RetrieveCourses extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public RetrieveCourses() {
        super();
        
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ArrayList<CourseBean> courseList = null;
		ArrayList<CourseBean> studentCourseList = null;
		String nextUrl = "homePage.jsp";
		HttpSession session = request.getSession();
		ErrorMessageBean errorMessageBean = (ErrorMessageBean) request.getAttribute("errorMessageBean");
		DatabaseAccessBean databaseAccessBean = new DatabaseAccessBean(errorMessageBean);
		UserBean userBean = (UserBean) session.getAttribute("userBean");
	
		if(databaseAccessBean.connect().equals("SUCCESS")){
			String query="SELECT eligible_major, course_number, course_name FROM course ORDER BY eligible_major";
			String studentQuery="SELECT c.eligible_major, c.course_number, c.course_name FROM course c, registration r WHERE c.course_number=r.course_number AND r.user_id='"+userBean.getUserId()+"'";
			if(databaseAccessBean.processQuery(query).equals("SUCCESS"))
			{
				courseList = new ArrayList<CourseBean>();
				try{
				while(databaseAccessBean.getResultSet().next()){
					CourseBean cb = new CourseBean();
					cb.setCourseNumber(databaseAccessBean.getResultSet().getString("course_number"));
					cb.setCourseName(databaseAccessBean.getResultSet().getString("course_name"));
					cb.setCourseMajor(databaseAccessBean.getResultSet().getString("eligible_major"));
					courseList.add(cb);
				}
				databaseAccessBean.getResultSet().close();
				}
				catch(SQLException e){
					errorMessageBean.setErrorMessage(e.getErrorCode(),e.getSQLState(),e.getMessage());
					e.printStackTrace();
				}
				request.setAttribute("courseList", courseList);
				
			}
			else{
				errorMessageBean.setErrorMessage("Trouble retrieving course information. Please try again or contact the website administrator if the problem persist.");
			}
			
			if(databaseAccessBean.processQuery(studentQuery).equals("SUCCESS"))
			{
				studentCourseList = new ArrayList<CourseBean>();
				try{
				while(databaseAccessBean.getResultSet().next()){
					CourseBean cb = new CourseBean();
					cb.setCourseNumber(databaseAccessBean.getResultSet().getString("course_number"));
					cb.setCourseName(databaseAccessBean.getResultSet().getString("course_name"));
					cb.setCourseMajor(databaseAccessBean.getResultSet().getString("eligible_major"));
					studentCourseList.add(cb);
				}
				}
				catch(SQLException e){
					errorMessageBean.setErrorMessage(e.getErrorCode(),e.getSQLState(),e.getMessage());
					e.printStackTrace();
				}
				nextUrl = "courseregistration.jsp";
				request.setAttribute("studentCourseList", studentCourseList);
			}
			else{
				errorMessageBean.setErrorMessage("Trouble retrieving course information. Please try again or contact the website administrator if the problem persist.");
			}
			databaseAccessBean.closeDBResources();;
		}
		else{
			errorMessageBean.setErrorMessage("Trouble connecting to database. Please try again or contact the website administrator if the problem persist.");
		}
		session.setAttribute("userBean", userBean);
		request.setAttribute("errorMessageBean", errorMessageBean);
		RequestDispatcher dispatcher = request.getRequestDispatcher(nextUrl);
		dispatcher.forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
