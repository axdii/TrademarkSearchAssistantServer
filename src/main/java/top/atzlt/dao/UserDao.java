package top.atzlt.dao;

import top.atzlt.domain.User;

import java.util.List;

public interface UserDao {

    public List<User> getAllUser();
    public User getUserById(int userId);
    public User getUserByPhone(String userPhone);
    public boolean addUser(User user);
//    public boolean deleteUserById(int id);//不一定使用
    public boolean updateUser(User user);
    public boolean isExist(User user);
//    public boolean setUserLockState();
    public int getLastUserId();


}
