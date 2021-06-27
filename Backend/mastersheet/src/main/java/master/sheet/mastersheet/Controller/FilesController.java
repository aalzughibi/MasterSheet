package master.sheet.mastersheet.Controller;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import master.sheet.mastersheet.Entity.ItemEntity;
import master.sheet.mastersheet.Entity.PoEntity;
import master.sheet.mastersheet.Entity.ProjectEntity;
import master.sheet.mastersheet.Entity.TaskEntity;
import master.sheet.mastersheet.Service.ItemService;
import master.sheet.mastersheet.Service.PoService;
import master.sheet.mastersheet.Service.ProjectService;
import master.sheet.mastersheet.Service.TaskService;
import master.sheet.mastersheet.SheetsModel.project;

@RestController
@RequestMapping("file")
public class FilesController {
    @Autowired
    ProjectService projectService;
    @Autowired
    ItemService itemService;
    @Autowired
    PoService poService;
    @Autowired
    TaskService taskService;

    // uploadfile and insert to database
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFiles(@RequestParam("Files") MultipartFile[] files) {
        try {
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
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            // TODO: handle exception
        }
    }
    public  boolean insertIntoMasterData(Map<String, Object> items, Map<String, Object> projects,
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
        // FILE_DIRECTORY+
        File myFile = new File(file.getOriginalFilename());
        myFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(myFile);
        fos.write(file.getBytes());
        fos.close();
        return readFromExcel(file.getOriginalFilename());
    }

    public static Map<String, Object> readFromExcel(String filename) {
        Map<String, Object> data = new HashMap<>();
        try {
            System.out.println("start");
            Workbook wb;
            wb = WorkbookFactory.create(new File(filename));
            Sheet sh;
            sh = wb.getSheet("Sheet1");
            int noOfRows = sh.getLastRowNum() + 1;
            System.out.println("number of rows " + filename + ":" + noOfRows);
            // items.xlsx
            // po.xlsx
            // project.xlsx
            // tasks.xlsx
            DataFormatter formatter = new DataFormatter();
            switch (filename) {
                case "items.xlsx":
                    for (int i = 1; i < noOfRows; i++) {

                        ItemEntity items = new ItemEntity();
                        // Timestamp ts = new Timestamp();
                        items.setItem_id(formatter.formatCellValue(sh.getRow(i).getCell(0)));
                        items.setProject_id(formatter.formatCellValue(sh.getRow(i).getCell(1)));
                        items.setItem_name(formatter.formatCellValue(sh.getRow(i).getCell(2)));
                        items.setItem_remarks(formatter.formatCellValue(sh.getRow(i).getCell(3)));
                        items.setItem_type(formatter.formatCellValue(sh.getRow(i).getCell(4)));
                        // items.setStart_date(formatter.formatCellValue(sh.getRow(i).getCell(5)));
                        // new
                        // SimpleDateFormat("MM/dd/yyyy").parse(formatter.formatCellValue(sh.getRow(i).getCell(5)))
                        items.setStart_date(new SimpleDateFormat("MM/dd/yyyy")
                                .parse(formatter.formatCellValue(sh.getRow(i).getCell(5))));
                        items.setEnd_date(new SimpleDateFormat("MM/dd/yyyy")
                                .parse(formatter.formatCellValue(sh.getRow(i).getCell(6))));
                        items.setPo_remarks(formatter.formatCellValue(sh.getRow(i).getCell(7)));
                        items.setPo_no(formatter.formatCellValue(sh.getRow(i).getCell(8)));
                        items.setPo_value(formatter.formatCellValue(sh.getRow(i).getCell(9)));
                        data.put(formatter.formatCellValue(sh.getRow(i).getCell(0)), items);
                    }
                    System.out.println("data: " + data.toString());
                    return data;
                // break;
                case "po.xlsx":
                    for (int i = 1; i < noOfRows; i++) {
                        PoEntity p = new PoEntity();
                        p.setPo_id(formatter.formatCellValue(sh.getRow(i).getCell(0)));
                        p.setStart_date(new SimpleDateFormat("MM/dd/yyyy")
                                .parse(formatter.formatCellValue(sh.getRow(i).getCell(1))));
                        p.setEnd_date(new SimpleDateFormat("MM/dd/yyyy")
                                .parse(formatter.formatCellValue(sh.getRow(i).getCell(2))));
                        data.put(formatter.formatCellValue(sh.getRow(i).getCell(0)), p);
                    }
                    return data;
                // break;
                case "project.xlsx":
                    for (int i = 1; i < noOfRows; i++) {
                        if (formatter.formatCellValue(sh.getRow(i).getCell(0)) != "") {
                            // System.out.println(formatter.formatCellValue(sh.getRow(i).getCell(0)));
                            ProjectEntity project = new ProjectEntity();
                            project.setProject_id(formatter.formatCellValue(sh.getRow(i).getCell(0)));
                            project.setProject_name(formatter.formatCellValue(sh.getRow(i).getCell(1)));
                            project.setStart_date(new SimpleDateFormat("MM/dd/yyyy")
                                    .parse(formatter.formatCellValue(sh.getRow(i).getCell(2))));
                            project.setEnd_date(new SimpleDateFormat("MM/dd/yyyy")
                                    .parse(formatter.formatCellValue(sh.getRow(i).getCell(3))));
                            project.setRemarks(formatter.formatCellValue(sh.getRow(i).getCell(4)));
                            project.setProject_manager(formatter.formatCellValue(sh.getRow(i).getCell(5)));
                            project.setProject_max_amount(formatter.formatCellValue(sh.getRow(i).getCell(6)));
                            project.setProject_type(formatter.formatCellValue(sh.getRow(i).getCell(7)));
                            project.setProject_status(formatter.formatCellValue(sh.getRow(i).getCell(8)));
                            data.put(formatter.formatCellValue(sh.getRow(i).getCell(0)), project);
                        }

                    }
                    return data;
                // break;
                case "tasks.xlsx":
                    for (int i = 1; i < noOfRows; i++) {
                        TaskEntity ta = new TaskEntity();
                        ta.setTask_id(formatter.formatCellValue(sh.getRow(i).getCell(0)));
                        ta.setItem_id(formatter.formatCellValue(sh.getRow(i).getCell(1)));
                        ta.setTask_description(formatter.formatCellValue(sh.getRow(i).getCell(2)));
                        data.put(formatter.formatCellValue(sh.getRow(i).getCell(0)), ta);
                    }
                    return data;
                // break;
            }
            wb.close();
            return data;
        } catch (Exception e) {
            System.out.println("Error:" + e);
            return data;
        }

    }
}
