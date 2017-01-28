/**
 * This is a Servlet Class performing the functionality required for the course registrations.
 * @author Raghu Keerthi Petla, Aditi Vishwas Rao, Mehul Parmar, Anirudh Palakurthi 
 */
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

@WebServlet("/courseRegisterServlet")
public class CourseRegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String mutex = "";

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			HttpSession session = request.getSession();
			UserBean user = (UserBean) session.getAttribute("userBean");

			ErrorMessageBean error = new ErrorMessageBean();
			if (user == null) {
				error.setErrorMessage("Session timed out.Please login again");
				session.invalidate();
				request.getRequestDispatcher("/login.jsp").forward(request, response);
			} else {
				DatabaseAccessBean dbbean = new DatabaseAccessBean(error);
				String issuccess = dbbean.connect();
				if (issuccess.equals("SUCCESS")) {
					String user_id = user.getUserId();
					String user_major = user.getMajor();
					String selected_course = request.getParameter("courseValue");

					List<String> constraints = new ArrayList<>();
					constraints.add("user_id");
					String query = dbbean.createPrepareStatement("select", "registration", null, constraints, null);
					List<String> queryValues = new ArrayList<>();
					queryValues.add(user_id);
					List<String> columns = new ArrayList<>();
					if (dbbean.getPrepStatement(query).equals("SUCCESS")) {
						String status = dbbean.processQueryPreparedStatement(query, queryValues);
						if (status.equals("SUCCESS")) {
							if (dbbean.getRowCount() < 5) {
								ResultSet resultSet = dbbean.getResultSet();
								List<String> regCourseIds = new ArrayList<>();
								if (resultSet != null) {
									while (resultSet.next()) {
										if (resultSet.getString("course_number") != null) {
											regCourseIds.add(resultSet.getString("course_number"));
										}
									}
									constraints.clear();
									constraints.add("course_name");
									query = dbbean.createPrepareStatement("select", "course", null, constraints, null);
									String course_major = "";
									int maxsize = 0;
									int registered = 0;
									synchronized (mutex) {
										if (dbbean.getPrepStatement(query).equals("SUCCESS")) {
											queryValues.clear();
											queryValues.add(selected_course);
											status = dbbean.processQueryPreparedStatement(query, queryValues);
											if (status.equals("SUCCESS")) {
												resultSet = dbbean.getResultSet();
												String course_num = "";
												if (resultSet != null) {
													while (resultSet.next()) {
														if (resultSet.getString("course_number") != null) {
															course_num = resultSet.getString("course_number");
														}
														if (resultSet.getString("eligible_major") != null) {
															course_major = resultSet.getString("eligible_major");
														}
														if (resultSet.getString("maxsize") != null) {
															maxsize = resultSet.getInt("maxsize");
														}
														if (resultSet.getString("registered") != null) {
															registered = resultSet.getInt("registered");
														}
													}
												}
												if (!course_num.isEmpty()) {
													if (regCourseIds.contains(course_num)) {
														error.setErrorMessage(
																"You have already registered for this course");
														dbbean.closeDBResources();
														request.setAttribute("errorMessageBean", error);
														request.getRequestDispatcher("/RetrieveCourses")
																.forward(request, response);
													} else {
														if (course_major.equalsIgnoreCase(user_major)
																|| course_major.equalsIgnoreCase("Any")) {
															if ((maxsize > registered)) {
																columns.add("user_id");
																columns.add("course_number");
																query = dbbean.createPrepareStatement("insert",
																		"registration", columns, null, null);
																if (dbbean.getPrepStatement(query).equals("SUCCESS")) {
																	queryValues.clear();
																	queryValues.add(user_id);
																	queryValues.add(course_num);
																	status = dbbean.processQueryPreparedStatement(query,
																			queryValues);
																	if (status.equals("SUCCESS")) {
																		int changedRows = dbbean.getChangedRows();
																		if (changedRows == 1) {
																			columns.clear();
																			registered += 1;
																			columns.add("registered");
																			constraints.clear();
																			constraints.add("course_number");
																			query = dbbean.createPrepareStatement(
																					"update", "course", columns,
																					constraints, null);
																			System.out.println(query);
																			if (dbbean.getPrepStatement(query)
																					.equals("SUCCESS")) {
																				queryValues.clear();
																				queryValues.add(registered + "");
																				queryValues.add(course_num);
																				status = dbbean
																						.processQueryPreparedStatement(
																								query, queryValues);
																				if (!status.equals("SUCCESS")) {
																					error.setErrorMessage(
																							"Connection Failed");
																					query = "DELETE FROM registration WHERE user_id='"
																							+ user_id
																							+ "' AND course_number = '"
																							+ selected_course + "'";
																					dbbean.processQuery(query);
																				} else {
																					error.setErrorMessage(
																							"Course successfully registered");
																				}
																			}

																			request.setAttribute("errorMessageBean",
																					error);
																			request.getRequestDispatcher(
																					"/RetrieveCourses")
																					.forward(request, response);

																		} else if (changedRows != 1) {
																			error.setErrorMessage("Duplicate courses");
																			dbbean.closeDBResources();
																			request.setAttribute("errorMessageBean",
																					error);
																			request.getRequestDispatcher(
																					"/RetrieveCourses")
																					.forward(request, response);
																		} else {
																			error = dbbean.getError();
																			request.setAttribute("errorMessageBean",
																					error);
																			dbbean.closeDBResources();
																			request.getRequestDispatcher(
																					"/RetrieveCourses")
																					.forward(request, response);
																		}
																	} else {
																		error = dbbean.getError();
																		request.setAttribute("errorMessageBean", error);
																		dbbean.closeDBResources();
																		request.getRequestDispatcher("/RetrieveCourses")
																				.forward(request, response);
																	}
																} else {
																	error = dbbean.getError();
																	request.setAttribute("errorMessageBean", error);
																	dbbean.closeDBResources();
																	request.getRequestDispatcher("/RetrieveCourses")
																			.forward(request, response);
																}
															} else {
																error.setErrorMessage(
																		"There are no seats available for this course!");
																request.setAttribute("errorMessageBean", error);
																dbbean.closeDBResources();
																request.getRequestDispatcher("/RetrieveCourses")
																		.forward(request, response);
															}
														} else {
															error.setErrorMessage(
																	"The selected course is not available for your major");
															request.setAttribute("errorMessageBean", error);
															dbbean.closeDBResources();
															request.getRequestDispatcher("/RetrieveCourses")
																	.forward(request, response);
														}
													}
												}
											} else {
												error = dbbean.getError();
												request.setAttribute("errorMessageBean", error);
												dbbean.closeDBResources();
												request.getRequestDispatcher("/RetrieveCourses").forward(request,
														response);
											}
										} else {
											error = dbbean.getError();
											request.setAttribute("errorMessageBean", error);
											dbbean.closeDBResources();
											request.getRequestDispatcher("/RetrieveCourses").forward(request, response);
										}
									}
								}
							} else {
								error.setErrorMessage("You have reached the limit for maximum courses registrations");
								request.setAttribute("errorMessageBean", error);
								dbbean.closeDBResources();
								request.getRequestDispatcher("/RetrieveCourses").forward(request, response);
							}
						} else {
							error = dbbean.getError();
							request.setAttribute("errorMessageBean", error);
							dbbean.closeDBResources();
							request.getRequestDispatcher("/RetrieveCourses").forward(request, response);
						}
					} else {
						error = dbbean.getError();
						request.setAttribute("errorMessageBean", error);
						dbbean.closeDBResources();
						request.getRequestDispatcher("/RetrieveCourses").forward(request, response);
					}
				} else {
					error = dbbean.getError();
					request.setAttribute("errorMessageBean", error);

					request.getRequestDispatcher("/login.jsp").forward(request, response);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
