package master.sheet.mastersheet.Service.interfaces;

import java.util.List;

import master.sheet.mastersheet.Entity.UserEntity;

public interface UserInterface {
    public List<UserEntity> getAllUsers();
    public UserEntity getUserByUID(String uid);
    public UserEntity getUserByUsername(String username);
    public UserEntity updateUser(UserEntity user)throws Exception;
    public UserEntity insertUser(UserEntity user);
    public UserEntity updateDisplayName(UserEntity user)throws Exception;
    public UserEntity updatePassword(UserEntity user)throws Exception;
    public boolean isAdmin(String uid)throws Exception;
    public UserEntity checkLoginAndRetrive(String username,String password);
    public boolean isExist(String uid);
}
