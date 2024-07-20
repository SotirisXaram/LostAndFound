package com.charamidis.lostAndFound.models;

public class Return {
    private Integer id;
    private Integer returnOfficer;
    private String returnLastName;
    private String returnFirstName;
    private String returnDate;
    private String returnTime;
    private String returnTelephone;
    private String returnIdNumber;
    private String returnFatherName;
    private String returnDateOfBirth;
    private String returnStreetAddress;
    private String returnStreetNumber;
    private String returnTimestamp;
    private String comment;

    public Return(){

    }



    // Constructor
    public Return(Integer id, Integer returnOfficer, String returnLastName, String returnFirstName,
                  String returnDate, String returnTime, String returnTelephone, String returnIdNumber,
                  String returnFatherName, String returnDateOfBirth, String returnStreetAddress,
                  String returnStreetNumber,String returnTimestamp,String comment) {
        this.id = id;
        this.returnOfficer = returnOfficer;
        this.returnLastName = returnLastName;
        this.returnFirstName = returnFirstName;
        this.returnDate = returnDate;
        this.returnTime = returnTime;
        this.returnTelephone = returnTelephone;
        this.returnIdNumber = returnIdNumber;
        this.returnFatherName = returnFatherName;
        this.returnDateOfBirth = returnDateOfBirth;
        this.returnStreetAddress = returnStreetAddress;
        this.returnStreetNumber = returnStreetNumber;
        this.returnTimestamp = returnTimestamp;
        this.comment = comment;
    }


    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReturnTimestamp() {
        return returnTimestamp;
    }

    public void setReturnTimestamp(String returnTimestamp) {
        this.returnTimestamp = returnTimestamp;
    }


    public Integer getReturnOfficer() {
        return returnOfficer;
    }

    public void setReturnOfficer(Integer returnOfficer) {
        this.returnOfficer = returnOfficer;
    }

    public String getReturnLastName() {
        return returnLastName;
    }

    public void setReturnLastName(String returnLastName) {
        this.returnLastName = returnLastName;
    }

    public String getReturnFirstName() {
        return returnFirstName;
    }

    public void setReturnFirstName(String returnFirstName) {
        this.returnFirstName = returnFirstName;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(String returnTime) {
        this.returnTime = returnTime;
    }

    public String getReturnTelephone() {
        return returnTelephone;
    }

    public void setReturnTelephone(String returnTelephone) {
        this.returnTelephone = returnTelephone;
    }

    public String getReturnIdNumber() {
        return returnIdNumber;
    }

    public void setReturnIdNumber(String returnIdNumber) {
        this.returnIdNumber = returnIdNumber;
    }

    public String getReturnFatherName() {
        return returnFatherName;
    }

    public void setReturnFatherName(String returnFatherName) {
        this.returnFatherName = returnFatherName;
    }

    public String getReturnDateOfBirth() {
        return returnDateOfBirth;
    }

    public void setReturnDateOfBirth(String returnDateOfBirth) {
        this.returnDateOfBirth = returnDateOfBirth;
    }

    public String getReturnStreetAddress() {
        return returnStreetAddress;
    }

    public void setReturnStreetAddress(String returnStreetAddress) {
        this.returnStreetAddress = returnStreetAddress;
    }

    public String getReturnStreetNumber() {
        return returnStreetNumber;
    }

    public void setReturnStreetNumber(String returnStreetNumber) {
        this.returnStreetNumber = returnStreetNumber;
    }
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
