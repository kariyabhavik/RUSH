package com.example.registration.models;

import android.text.Editable;

public class DriverRegistration {

    String UserName, Email, password, PhoneNumber, C1License, DrivingLincese, PanNumber , driverid;
    boolean EMT, AmbulanceDriver, EVOC;

    public DriverRegistration(String userName, String email, String password, String phoneNumber, String c1License, String drivingLincese, String panNumber , boolean EMT, boolean ambulanceDriver, boolean EVOC) {
        UserName = userName;
        Email = email;
        this.password = password;
        PhoneNumber = phoneNumber;
        C1License = c1License;
        DrivingLincese = drivingLincese;
        PanNumber = panNumber;
        driverid = driverid;
        this.EMT = EMT;
        AmbulanceDriver = ambulanceDriver;
        this.EVOC = EVOC;
    }

    public String getUserName() {
        return UserName.trim();
    }

    public void setUserName(String userName) {
        UserName = userName.trim();
    }

    public String getEmail() {
        return Email.trim();
    }

    public void setEmail(String email) {
        Email = email.trim();
    }

    public String getPassword() {
        return password.trim();
    }

    public void setPassword(String password) {
        this.password = password.trim();
    }

    public String getPhoneNumber() {
        return PhoneNumber.trim();
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber.trim();
    }

    public String getC1License() {
        return C1License.trim();
    }

    public void setC1License(String c1License) {
        C1License = c1License.trim();
    }

    public String getDrivingLincese() {
        return DrivingLincese.trim();
    }

    public void setDrivingLincese(String drivingLincese) {
        DrivingLincese = drivingLincese.trim();
    }

    public String getPanNumber() {
        return PanNumber.trim();
    }

    public void setPanNumber(String panNumber) {
        PanNumber = panNumber.trim();
    }

    public boolean isEMT() {
        return EMT;
    }

    public void setEMT(boolean EMT) {
        this.EMT = EMT;
    }

    public boolean isAmbulanceDriver() {
        return AmbulanceDriver;
    }

    public void setAmbulanceDriver(boolean ambulanceDriver) {
        AmbulanceDriver = ambulanceDriver;
    }

    public boolean isEVOC() {
        return EVOC;
    }

    public void setEVOC(boolean EVOC) {
        this.EVOC = EVOC;
    }
}