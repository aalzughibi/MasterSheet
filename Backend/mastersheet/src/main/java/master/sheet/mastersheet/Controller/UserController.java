package master.sheet.mastersheet.Controller;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.validator.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import master.sheet.mastersheet.Auth.Auth;
import master.sheet.mastersheet.Entity.UserEntity;
import master.sheet.mastersheet.Service.UserSerivce;
import master.sheet.mastersheet.Validate.Validate;
import master.sheet.mastersheet.payload.reponse.LoginReponse;
import master.sheet.mastersheet.payload.request.LoginRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("users")
public class UserController {
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
            else
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        }catch(Exception e){
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
            //TODO: handle exception
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    // [POST] user into database
    // url: /users
    /* --Body--
    {
        "username":"abdullah12",
        "email":"mrabdullah012@gmail.com",
        "role":0,
        "first_name":"Abdullah",
        "last_name":"Alzughibi",
        "BirthDate":"2021-07-21",
        "display_name":"AB"
    }
    response only status 
    Status: 200 OK --> insert to database successfully

    */
    @PostMapping()
    public ResponseEntity<?> postUser(@RequestHeader("Authorization") String header_jwt, @RequestBody UserEntity entity) {
        //TODO: process POST request
        try {
            if(!Auth.isAdmin(service,header_jwt))
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

            if (Validate.isValidUsername(entity.getUsername())
                            && (EmailValidator.getInstance().isValid(entity.getEmail()))
                            && Validate.isValidFirstName(entity.getFirst_name())
                            && Validate.isValidLastName(entity.getLast_name())
                            && ((entity.getDisplay_name().length() >= 2) && (entity.getDisplay_name().length() <= 20))) {
                                entity.setPassword(String.valueOf(generatePassword(10)));
                                entity.setUid(RandomStringUtils.randomAlphanumeric(40));
                                entity.setFirst_time(true);
                                UserEntity ue = service.insertUser(entity);
                                return new ResponseEntity<>(HttpStatus.OK);
                            }
                            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

            // Send password to user by email
        } catch (Exception e) {
            //TODO: handle exception
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    // [PUT] update user if exist in database
    // url: /users/{userId}
    @PutMapping(path = "/{userId}")
    public ResponseEntity<?> updateUser(@RequestHeader("Authorization") String header_jwt,@RequestBody UserEntity entity,@PathVariable String userId){
    try{
        if(!Auth.isAdmin(service,header_jwt))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        entity.setUid(userId);
        UserEntity ue = service.updateUser(entity);
        return new ResponseEntity<>(HttpStatus.OK);
    }catch(Exception e){
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    }
    // [PUT] update display name in database
    // url: /users/display_name/{userId}
    @PutMapping(path="/display_name/{userId}")
    public ResponseEntity<?> updateDisplayName(@RequestHeader("Authorization") String header_jwt,@RequestBody UserEntity entity,@PathVariable String userId){
        try{
            if(!Auth.isAuthenticated(service,header_jwt))
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            entity.setUid(userId);
            UserEntity ue = service.updateDisplayName(entity);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping(path="/password/{userId}")
    public ResponseEntity<?> updatePassword(@RequestHeader("Authorization") String header_jwt,@RequestBody UserEntity entity,@PathVariable String userId){
        try {
            if(!Auth.isAuthenticated(service,header_jwt))
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            entity.setUid(userId);
            UserEntity ue = service.updatePassword(entity);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping(path="/Login")
    public ResponseEntity<?> LoginUser(@RequestBody LoginRequest loginRequest){
        try{
            UserEntity ue = service.check_and_retrive(loginRequest.getUsername(), loginRequest.getPassword());
            if (ue!=null){
                LoginReponse loginReponse = new LoginReponse();
                loginReponse.setRole(ue.getRole());
                loginReponse.setFirst_time_login(ue.isFirst_time());
                LocalDateTime ldt = LocalDateTime.now().plusHours(1);
                Object expired = ldt.toEpochSecond(ZoneOffset.UTC);
                loginReponse.setJwtToken(Auth.setJWT("LOGIN_AUTH", ue.getUid(),expired, ue.getUsername()));
                loginReponse.setExpiredAt(2);
                return new ResponseEntity<>(loginReponse,HttpStatus.OK);
            }else
            throw new Exception("NotFound");
            
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
