/*
    The UserModel class extracts some information from the User object, which the client needs for login process, and
    is used to be returned to the client.
 */

package com.frank.crm.model;

public class UserModel {

    // Encoded user id (Safe when displayed in cookie)
    private String userIdEncoded;
    private String userName;
    private String trueName;

    public String getUserIdEncoded() {
        return userIdEncoded;
    }

    public void setUserIdEncoded(String userIdEncoded) {
        this.userIdEncoded = userIdEncoded;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }
}
