package com.example.adminstatisticsscreen;


import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;


public class Kid {
	private String id;
	private String fullName;
	private Date dateOfBirth;
	private Gender gender;
	private ArrayList<String> activeCourses = new ArrayList<String>();;
	private ArrayList<String> completedCourses = new ArrayList<String>();;
	private String parentId;
	private Status status;
	private Date activeDate;
	private String image;
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public Kid() {
		
	}
	public Kid(String id,String fullName, Date dateOfBirth, Gender gender,String parentId) {
		super();
		this.id= id;
		this.fullName = fullName;
		this.dateOfBirth = dateOfBirth;
		this.gender = gender;
		this.parentId = parentId;
		this.status=Status.Active;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public Gender getGender() {
		return gender;
	}
	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public ArrayList<String> addCourse(String courseID){
		if(activeCourses.contains(courseID)) {
			System.out.println("Couldn't add, the course already enrolled");
			return null;
		}
		activeCourses.add(courseID);
		return activeCourses;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public ArrayList<String> getActiveCourses() {
		return activeCourses;
	}
	public void setActiveCourses(ArrayList<String> activeCourses) {
		this.activeCourses = activeCourses;
	}
	public ArrayList<String> getCompletedCourses() {
		return completedCourses;
	}
	public void setCompletedCourses(ArrayList<String> completedCourses) {
		this.completedCourses = completedCourses;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public Date getActiveDate() {
		return activeDate;
	}
	public void setActiveDate(Date activeDate) {
		this.activeDate = activeDate;
	}
	
	public boolean deleteCourse(String courseId) {
		if(activeCourses.remove(id)) {
			completedCourses.add(courseId);
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(dateOfBirth, fullName, gender, id, parentId);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Kid other = (Kid) obj;
		return Objects.equals(dateOfBirth, other.dateOfBirth) && Objects.equals(fullName, other.fullName)
				&& gender == other.gender && Objects.equals(id, other.id) && Objects.equals(parentId, other.parentId);
	}
	@Override
	public String toString() {
		return "Kid [fullName=" + fullName + ", dateOfBirth=" + dateOfBirth + ", parentId=" + parentId + "]";
	}
	


}
