/**
 * This is a Bean Class containing all Course Information including:
 * Course Name, Course Number, Course Major.
 * @author Raghu Keerthi Petla, Aditi Vishwas Rao, Mehul Parmar, Anirudh Palakurthi 
 */
package com.uic.ids520.bean;

public class CourseBean {
	private String courseName;
	private String courseNumber;
	private String courseMajor;
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getCourseNumber() {
		return courseNumber;
	}
	public void setCourseNumber(String courseNumber) {
		this.courseNumber = courseNumber;
	}
	public String getCourseMajor() {
		return courseMajor;
	}
	public void setCourseMajor(String courseMajor) {
		this.courseMajor = courseMajor;
	}
	
}
