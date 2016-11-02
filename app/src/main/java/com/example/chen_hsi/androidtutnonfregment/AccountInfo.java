package com.example.chen_hsi.androidtutnonfregment;

/**
 * Created by Chen-Hsi on 2016/10/12.
 */

public class AccountInfo {
    private boolean loginStatus;
    private String userId;
    private String userName;
    private String lastName;
    private String email;
    private String phoneNo;

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public boolean getLoginStatus() {
        return loginStatus;
    }
    public String getUserId(){
        return userId;
    }
    public  String getUserName(){
        return userName;
    }
    public String getLastName(){return lastName;}
    public  String getEmail(){return email;}


    public void setLoginStatus(boolean status) {
        this.loginStatus = status;
    }
    public void setUserId(String id){
        this.userId= id;
    }
    public void setUserName(String name){
        this.userName=name;
    }
    public void setLastName(String lastName1){this.lastName=lastName1;}
    public void setEmail(String email1){this.email=email1;}






    private static final AccountInfo holder = new AccountInfo();
    public static AccountInfo getInstance() {return holder;}



}
