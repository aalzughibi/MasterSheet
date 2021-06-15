package master.sheet.mastersheet.UploadFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;

import master.sheet.mastersheet.UserController;
import master.sheet.mastersheet.Auth.Auth;

@RestController
@RequestMapping("UploadFile")
public class FileUploadController {
    // @Value("${file.upload-dir}")
    String FILE_DIRECTORY = "C:\\storage\\";
    @PostMapping()
    public ResponseEntity<Map<String,Object>> UploadFile(@RequestHeader("Authorization") String header_auth,@RequestParam("items") MultipartFile itemsfile,
    @RequestParam("po") MultipartFile pofile,@RequestParam("project") MultipartFile projectfile,@RequestParam("tasks") MultipartFile tasksfile){
        try {
            if(Auth.validJWT(header_auth)){
                JSONObject jo =  Auth.convert_JsonString_To_Json(Auth.getJWTToken(header_auth)[1]);
                if(UserController.isAdmin(String.valueOf(jo.get("userId")))){
                    upload_file(itemsfile);
                    upload_file(pofile);
                    upload_file(projectfile);
                    upload_file(tasksfile);
                    Map<String,Object> mp =new HashMap<>();
                    mp.put("items", itemsfile.getOriginalFilename());
                    mp.put("PO", pofile.getOriginalFilename());
                    mp.put("Project", projectfile.getOriginalFilename());
                    mp.put("tasks", tasksfile.getOriginalFilename());
                    return new ResponseEntity<>(mp,HttpStatus.OK);
                }
                else{
                    
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
            }
            else{

                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            
        }
    }
    public boolean upload_file(MultipartFile file) throws IOException{
        // FILE_DIRECTORY+
        File myFile = new File(file.getOriginalFilename());
        myFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(myFile);
        fos.write(file.getBytes());
        fos.close();
        return true;
    }
}
