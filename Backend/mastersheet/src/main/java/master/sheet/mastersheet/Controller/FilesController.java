package master.sheet.mastersheet.Controller;

import java.io.*;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import master.sheet.mastersheet.Auth.Auth;
import master.sheet.mastersheet.Entity.ItemEntity;
import master.sheet.mastersheet.Entity.PoEntity;
import master.sheet.mastersheet.Entity.ProjectEntity;
import master.sheet.mastersheet.Entity.TaskEntity;
import master.sheet.mastersheet.Service.ItemService;
import master.sheet.mastersheet.Service.PoService;
import master.sheet.mastersheet.Service.ProjectService;
import master.sheet.mastersheet.Service.TaskService;
import master.sheet.mastersheet.Service.UserSerivce;
import master.sheet.mastersheet.excelHelper.excelHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RestController
@RequestMapping("file")
public class FilesController {
    private static final Logger logger = LoggerFactory.getLogger(FilesController.class);
    @Autowired
    ProjectService projectService;
    @Autowired
    ItemService itemService;
    @Autowired
    PoService poService;
    @Autowired
    TaskService taskService;
    @Autowired
    UserSerivce userService;
    // [POST] upload files into database with proccessing
    // url: /file/upload
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFiles(@RequestHeader("Authorization") String header_jwt,@RequestParam("Files") MultipartFile[] files) {
        try {
            logger.info("Start upload file");
            if(!Auth.isAdmin(userService,header_jwt))
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            Map<String, Object> items = new HashMap<>();
            Map<String, String> not_found = new HashMap<>();
            Map<String, Object> poFile = new HashMap<>();
            Map<String, Object> proFile = new HashMap<>();
            Map<String, Object> tasksFile = new HashMap<>();
            for (MultipartFile multipartFile : files) {
                switch (multipartFile.getOriginalFilename()) {
                    case "items.xlsx":
                        items = upload_file(multipartFile);
                        break;
                    case "po.xlsx":
                        poFile = upload_file(multipartFile);
                        break;
                    case "project.xlsx":
                        proFile = upload_file(multipartFile);
                        break;
                    case "tasks.xlsx":
                        tasksFile = upload_file(multipartFile);
                        break;
                }
            }
            for (Map.Entry<String, Object> ite : items.entrySet()) {
                if (!isProject(proFile, ((ItemEntity) ite.getValue()).getProject_id())) {
                    not_found.put(((ItemEntity) ite.getValue()).getItem_id(), "");
                    System.out.println("Project not Found");
                }
            }
            for (Map.Entry<String, String> notf : not_found.entrySet()) {
                items.remove(notf.getKey());
            }
            not_found.clear();
            for (Map.Entry<String, Object> ite : tasksFile.entrySet()) {
                if (!isItem(items, ((TaskEntity) ite.getValue()).getItem_id())) {
                    not_found.put(((TaskEntity) ite.getValue()).getTask_id(), "");
                    System.out.println("item not found");
                }
            }
            for (Map.Entry<String, String> notf : not_found.entrySet()) {
                items.remove(notf.getKey());
            }
            for (Map.Entry<String, String> notf : not_found.entrySet()) {
                items.remove(notf.getKey());
            }
            if (insertIntoMasterData(items, proFile, poFile, tasksFile))
                System.out.println("Add successfully");
            else
                System.out.println("Fail to Add");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            logger.info(e.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public boolean insertIntoMasterData(Map<String, Object> items, Map<String, Object> projects,
            Map<String, Object> pos, Map<String, Object> tasks) throws Exception {
        System.out.println("start at:" + new Date());
        // excelHelper.wrtieExcelFile(projects, items, pos, tasks);
        for (Map.Entry<String, Object> proj : projects.entrySet()) {
            if (!projectService.isExist(proj.getKey())) {
                ProjectEntity pe = ((ProjectEntity) proj.getValue());
                projectService.insertProject(pe);
            } else {

                projectService.updateProject(((ProjectEntity) proj.getValue()));
            }
        }
        for (Map.Entry<String, Object> proj : items.entrySet()) {
            if (!itemService.isExist(proj.getKey())) {
                ItemEntity ie = ((ItemEntity) proj.getValue());
                itemService.insertItem(ie);
            } else {

                itemService.updateItem(((ItemEntity) proj.getValue()));
            }
        }
        for (Map.Entry<String, Object> proj : pos.entrySet()) {
            if (!poService.isExist(proj.getKey())) {
                PoEntity pe = ((PoEntity) proj.getValue());
                poService.insertPo(pe);
            } else {
                // if updated or not and updated
                poService.updatePo(((PoEntity) proj.getValue()));
            }
        }
        for (Map.Entry<String, Object> proj : tasks.entrySet()) {
            if (!taskService.isExist(proj.getKey())) {
                TaskEntity te = ((TaskEntity) proj.getValue());
                taskService.insertTask(te);
            } else {
                taskService.updateTask(((TaskEntity) proj.getValue()));
                System.out.println("isExist - Task");
            }
        }
        System.out.println("end at:" + new Date());
        return true;
    }

    public boolean isItem(Map<String, Object> items, String item_id) {
        if (items == null || item_id == null)
            return false;
        return items.get(item_id) != null ? true : false;
    }

    public boolean isProject(Map<String, Object> projects, String project_id) {
        if (projects == null || project_id == null)
            return false;
        return projects.get(project_id) != null ? true : false;
    }

    public Map<String, Object> upload_file(MultipartFile file) throws IOException {
        System.out.println("------------------Start------------------");
        System.out.println("OriginalFilename: "+file.getOriginalFilename());
        System.out.println("Size: "+file.getSize());
        System.out.println("Name: "+file.getName());
        System.out.println("Content: "+file.getContentType());
        System.out.println("Resource: "+file.getResource());
        System.out.println("Bytes: "+file.getBytes());
        System.out.println("------------------End------------------");
        // FILE_DIRECTORY+
        // File myFile = new File(file.getOriginalFilename());
        // myFile.createNewFile();
        // FileOutputStream fos = new FileOutputStream(myFile);
        // fos.write(file.getBytes());
        // fos.close();
        return excelHelper.readFromExcel(file.getOriginalFilename(),file.getInputStream());
    }
// [GET] Download excel sheet into user device
// url: /file/download
    @GetMapping("/download")
    public ResponseEntity<?> downloadFile(@RequestHeader("Authorization") String header_jwt) {
        try {
            if(Auth.isAdmin(userService,header_jwt)){
            excelHelper.wrtieExcelFile(projectService.getAllProjects(), itemService.getAllItems(),
                    poService.getAllPos(), taskService.getAllTasks());
            return new ResponseEntity<>(HttpStatus.OK);
        }else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        } catch (Exception e) {
            logger.info(e.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
