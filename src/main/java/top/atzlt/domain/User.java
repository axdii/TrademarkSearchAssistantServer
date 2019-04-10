package top.atzlt.domain;

import top.atzlt.cons.CommonConstant;

public class User extends BaseDomain<User>{

    public static final int USER_LOCK = 1;

    public static final int USER_UNLOCK = 0;

    public static final int ADMIN_USER = 1;

    public static final int NORMAL_USER = 0;

    private int userId = CommonConstant.DEFAULT_NUMBER;

    private String userName;

    private String userPassword;

    private String userRealName;

    //private String userPhone;//已在父类添加该属性，这里就不再重复

    private String userEmail;

    private String userAddress;

    private int userType;

    private int userLocked;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserRealName() {
        return userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getUserLocked() {
        return userLocked;
    }

    public void setUserLocked(int userLocked) {
        this.userLocked = userLocked;
    }


}
