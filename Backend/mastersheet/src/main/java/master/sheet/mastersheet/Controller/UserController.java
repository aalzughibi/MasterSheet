package master.sheet.mastersheet.Controller;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Random;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import master.sheet.mastersheet.Auth.Auth;
import master.sheet.mastersheet.Entity.UserEntity;
import master.sheet.mastersheet.Service.UserSerivce;
import master.sheet.mastersheet.payload.reponse.LoginReponse;
import master.sheet.mastersheet.payload.reponse.SignUpResponse;
import master.sheet.mastersheet.payload.request.LoginRequest;
import master.sheet.mastersheet.payload.request.SignUpRequest;
import master.sheet.mastersheet.payload.request.UpdateUserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    // this method for genetare passoword with param length
    private static char[] generatePassword(int length) {
        String capitalCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String specialCharacters = "!@#%_-";
        String numbers = "1234567890";
        String combinedChars = capitalCaseLetters + lowerCaseLetters + specialCharacters + numbers;
        Random random = new Random();
        char[] password = new char[length];

        password[0] = lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length()));
        password[1] = capitalCaseLetters.charAt(random.nextInt(capitalCaseLetters.length()));
        password[2] = specialCharacters.charAt(random.nextInt(specialCharacters.length()));
        password[3] = numbers.charAt(random.nextInt(numbers.length()));

        for (int i = 4; i < length; i++) {
            password[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
        }
        return password;
    }

    @Autowired
    UserSerivce service;
    // [GET] all users from database
    // url: /users
    @GetMapping
    public ResponseEntity<?> getAllUsers(@RequestHeader("Authorization") String header_jwt){
        try{
            if(Auth.isAdmin(service,header_jwt))
                return new ResponseEntity<>(service.getAllUsers(),HttpStatus.OK);
            else{
                
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

        }catch(Exception e){
            logger.info(e.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    // [GET] user by userId from database
    // url: /users/{userId}
    @GetMapping(path = "/{userId}")
    public ResponseEntity<?> getUserByUID(@RequestHeader("Authorization") String header_jwt, @PathVariable String userId){
        try {
            if(!Auth.isAdmin(service,header_jwt))
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

            UserEntity ue = service.getUserByUID(userId);
            return new ResponseEntity<>(ue,HttpStatus.OK);
        } catch (Exception e) {
            logger.info(e.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    // [POST] user into database
    // url: /users
    /* --Request Body--
       {
        "username":"mmmmasda",
        "email":"mrab7dullah012@gmail.com",
        "role":0,
        "first_name":"Abdullah",
        "last_name":"Alzughibi",
        "birthDate":"2021-02-21",
        "display_name":"AB"
    }
    Response Body:
    {
        "uid":"8hdskjhsdkhdk"
    }
    Status: 200 OK --> insert to database successfully

    */
    @PostMapping()
    public ResponseEntity<?> postUser(@RequestHeader("Authorization") String header_jwt, @RequestBody SignUpRequest entity) {
        //TODO: process POST request
        try {
            if(!Auth.isAdmin(service,header_jwt))
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                   UserEntity ue = new UserEntity(entity);
                   ue.setPassword(String.valueOf(generatePassword(10)));
                   ue.setUid(RandomStringUtils.randomAlphanumeric(40));
                   ue.setFirst_time(true);
                   ue = service.insertUser(ue);
                   SignUpResponse sr = new SignUpResponse(ue.getUid());
                   return new ResponseEntity<>(sr,HttpStatus.OK);
                    // Send password to user by email
        } catch (Exception e) {
            logger.info(e.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    // [PUT] update user if exist in database
    // url: /users/{userId}
    // -----Request Body-----
    // {
    //     "email":"mrab7dullah012@gmail.com",
    //     "role":0,
    //     "first_name":"Abdullah",
    //     "last_name":"Alzughibi",
    //     "birthDate":"2021-02-21",
    //     "display_name":"AB"
    // }
    // Response 200 OK --> update successfully
    @PutMapping(path = "/{userId}")
    public ResponseEntity<?> updateUser(@RequestHeader("Authorization") String header_jwt,@RequestBody UpdateUserRequest entity,@PathVariable String userId){
    try{
        if(!Auth.isAdmin(service,header_jwt))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            UserEntity userEntity = service.getUserByUID(userId);
            if(userEntity!=null){
                // validate data
                userEntity = new UserEntity(entity);
                userEntity.setUid(userId);
                service.updateUser(userEntity);
                return new ResponseEntity<>(HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

    }catch(Exception e){
        logger.info(e.toString());
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    }
    // [PUT] update display name in database
    // url: /users/displayName/{userId}
    // -----Request Body-----
    // {
    // "display_name":"AZ"
    // }
    // Response 200 OK -->update successfully
    @PutMapping(path="/displayName/{userId}")
    public ResponseEntity<?> updateDisplayName(@RequestHeader("Authorization") String header_jwt,@RequestBody UserEntity entity,@PathVariable String userId){
        try{
            if(!Auth.isAuthenticated(service,header_jwt))
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                UserEntity userEntity = service.getUserByUID(userId);
                if(userEntity!=null){
                    if(userEntity.getDisplay_name().equals(entity.getDisplay_name())){
                        entity.setUid(userId);
                        service.updateDisplayName(entity);
                        return new ResponseEntity<>(HttpStatus.OK);
                    }else{
                        return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
                    }
                }
                else{
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
        }catch(Exception e){
            logger.info(e.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    // [PUT] update password for specific user in database
    // url: /users/password/{userId}
    // -----Request Body-----
    // {
    // "password":"111222333"
    // }
    // Response 200 OK -->update successfully
    @PutMapping(path="/password/{userId}")
    public ResponseEntity<?> updatePassword(@RequestHeader("Authorization") String header_jwt,@RequestBody UserEntity entity,@PathVariable String userId){
        try {
            if(!Auth.isAuthenticated(service,header_jwt))
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

                UserEntity userEntity = service.getUserByUID(userId);
                if (userEntity!=null){
                    if(!userEntity.getPassword().equals(entity.getPassword())){
                        entity.setUid(userId);
                        service.updatePassword(entity);
                        return new ResponseEntity<>(HttpStatus.OK);
                    }
                    else{
                        return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
                    }
                } 
                else{
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }  
        } catch (Exception e) {
            logger.info(e.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    // [POST] login user into system
    // url: /users/Login
    // -----Request Body-----
    // {
    //  "username":"abdullah0102" 
    // "password":"111222333"
    // }
    // Response 200 OK -->
    // {
    //     "jwtToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJMT0dJTl9BVVRIIiwidXNlcklkIjoiQUJEVUxMQUhVSUQxMSIsImV4cCI6MTYyNTE1MjIwNSwidXNlcm5hbWUiOiJhYmR1bGxhaDAxMDIifQ._V2hziIZNTFPH0cxmzXnWEG4lyGXT1o8p-uu3uLNo10",
    //     "first_time_login": false,
    //     "expiredAt": 1625152205,
    //     "role": 0
    // }
    @PostMapping(path="/Login")
    public ResponseEntity<?> LoginUser(@RequestBody LoginRequest loginRequest){
        try{
            UserEntity ue = service.checkLoginAndRetrive(loginRequest.getUsername(), loginRequest.getPassword());
            if (ue!=null){
                LoginReponse loginReponse = new LoginReponse();
                loginReponse.setRole(ue.getRole());
                loginReponse.setFirst_time_login(ue.isFirst_time());
                LocalDateTime ldt = LocalDateTime.now().plusHours(1);
                long expired = ldt.toEpochSecond(ZoneOffset.UTC);
                loginReponse.setJwtToken(Auth.setJWT("LOGIN_AUTH", ue.getUid(),expired, ue.getUsername()));
                loginReponse.setExpiredAt(expired);
                return new ResponseEntity<>(loginReponse,HttpStatus.OK);
            }else
            throw new Exception("NotFound");
            
        }catch(Exception e){
            logger.info(e.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
