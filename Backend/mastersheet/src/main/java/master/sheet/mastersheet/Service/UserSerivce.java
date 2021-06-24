package master.sheet.mastersheet.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import master.sheet.mastersheet.Entity.UserEntity;
import master.sheet.mastersheet.Repository.UserRepository;

@Service
public class UserSerivce {
    @Autowired
    UserRepository userRepository;
    public List<UserEntity> getAllUsers(){
        List<UserEntity> userList = userRepository.findAll();
        return userList;
    }
    public UserEntity getUserByUID(String uid)throws Exception{
        List<UserEntity> userList = getAllUsers();
        for(UserEntity ul:userList){
            if(ul.getUid().equals(uid))
                return ul;
        }
        throw new Exception("User not Found");
    }
    
    public UserEntity updateUser(UserEntity user)throws Exception{
        UserEntity us = getUserByUID(user.getUid());
        if (us !=null){
        us.setEmail(us.getEmail()==user.getEmail()?us.getEmail():user.getEmail());
        us.setRole(us.getRole()==user.getRole()?us.getRole():user.getRole());
        us.setFirst_name(us.getFirst_name()==user.getFirst_name()?us.getFirst_name():user.getFirst_name());
        us.setLast_name(us.getLast_name()==user.getLast_name()?us.getLast_name():user.getLast_name());
        us.setBirthDate(us.getBirthDate()==user.getBirthDate()?us.getBirthDate():user.getBirthDate());
        us.setDisplay_name(us.getDisplay_name()==user.getDisplay_name()?us.getDisplay_name():user.getDisplay_name());
        us = userRepository.save(us);
        return us;
        }
        else
        throw new Exception("User not Found");
    }
    public UserEntity insertUser(UserEntity user){
        user = userRepository.save(user);
            return user;
    }
    public UserEntity updateDisplayName(UserEntity user)throws Exception{
        UserEntity us = getUserByUID(user.getUid());
        if (us !=null){
        us.setDisplay_name(us.getDisplay_name()==user.getDisplay_name()?us.getDisplay_name():user.getDisplay_name());
        us = userRepository.save(us);
        return us;
        }
        else
        throw new Exception("User not Found");
    }
    public UserEntity updatePassword(UserEntity user)throws Exception{
        UserEntity us = getUserByUID(user.getUid());
        if (us !=null){
        us.setPassword(us.getPassword()==user.getPassword()?us.getPassword():user.getPassword());
        us.setFirst_time(true);
        us = userRepository.save(us);
        return us;
        }
        else
        throw new Exception("User not Found");
    }
    public boolean isAdmin(String uid)throws Exception{
        UserEntity us = getUserByUID(uid);
        if (us !=null){
        return us.getRole()==0?true:false;
        }
        else
        throw new Exception("User not Found");
    }
    public boolean check_username_and_password(String username,String password){
        List<UserEntity> userList = getAllUsers();
        for(UserEntity ue:userList){
            if(ue.getUsername().equals(username) && ue.getPassword().equals(password))
            return true;
        }
        return false;
    }
    public UserEntity check_and_retrive(String username,String password){
        List<UserEntity> userList = getAllUsers();
        for(UserEntity ue:userList){
            if(ue.getUsername().equals(username) && ue.getPassword().equals(password))
            return ue;
        }
        return null;
    }
}
