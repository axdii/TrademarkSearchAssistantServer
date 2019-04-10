package top.atzlt.service;

import top.atzlt.domain.InteractionDomain;
import top.atzlt.domain.User;

public interface UserService {

    public User register(User user);
    public User login(User user);
    public void logout(User user);
    public InteractionDomain interaction(InteractionDomain interactionDomain);
    public User updateUserInfo(User user);



}
