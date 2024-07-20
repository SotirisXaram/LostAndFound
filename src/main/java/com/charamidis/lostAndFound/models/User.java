package com.charamidis.lostAndFound.models;

public class User {
    private Integer am;
    private String firstName;
    private String lastName;
    private String dateLoggedIn ;
    private String timeLoggedIn;
    private String role;
    private String birthday;

    public User(){}


    public User(Integer am, String firstName, String lastName, String birthday, String dateLoggedIn, String timeLoggedIn, String role) {
        this.am = am;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.dateLoggedIn = dateLoggedIn;
        this.timeLoggedIn = timeLoggedIn;
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getAm() {
        return am;
    }

    public void setAm(int am) {
        this.am = am;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }


    public String getDateLoggedIn() {
        return dateLoggedIn;
    }

    public void setDateLoggedIn(String dateLoggedIn) {
        this.dateLoggedIn = dateLoggedIn;
    }

    public String getTimeLoggedIn() {
        return timeLoggedIn;
    }

    public void setTimeLoggedIn(String timeLoggedIn) {
        this.timeLoggedIn = timeLoggedIn;
    }

    public boolean clear(){

        firstName = null ;
        lastName = null ;
        am = -1 ;
        dateLoggedIn = null;
        timeLoggedIn = null;
        birthday = null;
        role = null;

        return true;
    }

    @Override
    public String toString() {
        return "User{" +
                "am=" + am +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateLoggedIn='" + dateLoggedIn + '\'' +
                ", timeLoggedIn='" + timeLoggedIn + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
