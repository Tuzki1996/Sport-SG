package com.example.chen_hsi.androidtutnonfregment;

/**
 * Created by Chen-Hsi on 2016/10/12.
 */

public class AccountInfo {
    private boolean loginStatus;
    private String userId;
    private String userName;


    public boolean getLoginStatus() {
        return loginStatus;
    }
    public String getUserId(){
        return userId;
    }
    public  String getUserName(){
        return userName;
    }


    public void setLoginStatus(boolean status) {
        this.loginStatus = status;
    }
    public void setUserId(String id){
        this.userId= id;
    }
    public void setUserName(String name){
        this.userName=name;
    }






    private static final AccountInfo holder = new AccountInfo();
    public static AccountInfo getInstance() {return holder;}



}
