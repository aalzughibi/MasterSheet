package master.sheet.mastersheet.UploadFile;
import master.sheet.mastersheet.SheetsModel.task;

import java.io.*;

import java.util.*;

import org.apache.poi.hssf.usermodel.*;
// import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.*;
// import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;

import jdk.incubator.vector.VectorOperators.Test;
import master.sheet.mastersheet.UserController;
import master.sheet.mastersheet.Auth.Auth;
import master.sheet.mastersheet.SheetsModel.item;
import master.sheet.mastersheet.SheetsModel.po;
import master.sheet.mastersheet.SheetsModel.project;

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
                    Map<String,Object> items = upload_file(itemsfile);
                    Map<String,Object> poFile = upload_file(pofile);
                    Map<String,Object> proFile = upload_file(projectfile);
                    Map<String,Object> tasksFile = upload_file(tasksfile);
                    Map<String,Object> mp =new HashMap<>();
                    mp.put("items", items);
                    mp.put("PO", poFile);
                    mp.put("Project", proFile);
                    mp.put("tasks", tasksFile);
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
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    public  Map<String,Object> upload_file(MultipartFile file) throws IOException{
        // FILE_DIRECTORY+
        File myFile = new File(file.getOriginalFilename());
        myFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(myFile);
        fos.write(file.getBytes());
        fos.close();
        return readFromExcel(file.getOriginalFilename());
    }
	public static Map<String,Object> readFromExcel(String filename){
        Map<String,Object> data = new HashMap<>();
		try{
		System.out.println("start");
		Workbook wb;
		wb = WorkbookFactory.create(new File(filename));
		Sheet sh;
		sh = wb.getSheet("Sheet1");
	int noOfRows = sh.getLastRowNum()+1;
	System.out.println("number of rows "+filename+":"+noOfRows);
    // items.xlsx
    // po.xlsx
    // project.xlsx
    // tasks.xlsx
    DataFormatter formatter = new DataFormatter();
    switch(filename){
        case "items.xlsx":
        for(int i=1;i<noOfRows;i++){
            item items = new item();
            items.setItem_id(formatter.formatCellValue(sh.getRow(i).getCell(0)));
            items.setProject_id(formatter.formatCellValue(sh.getRow(i).getCell(1)));
            items.setItem_name(formatter.formatCellValue(sh.getRow(i).getCell(2)));
            items.setItem_remarks(formatter.formatCellValue(sh.getRow(i).getCell(3)));
            items.setItem_type(formatter.formatCellValue(sh.getRow(i).getCell(4)));
            items.setStart_date(formatter.formatCellValue(sh.getRow(i).getCell(5)));
            items.setEnd_date(formatter.formatCellValue(sh.getRow(i).getCell(6)));
            items.setPo_remarks(formatter.formatCellValue(sh.getRow(i).getCell(7)));
            items.setPo_no(formatter.formatCellValue(sh.getRow(i).getCell(8)));
            items.setPo_value(formatter.formatCellValue(sh.getRow(i).getCell(9)));
            data.put(formatter.formatCellValue(sh.getRow(i).getCell(0)), items);
        }
        System.out.println("data: "+data.toString());
        return data;
        // break;
        case "po.xlsx":
        for(int i=1;i<noOfRows;i++){
            po p = new po();
            p.setPo_id(formatter.formatCellValue(sh.getRow(i).getCell(0)));
            p.setStart_date(formatter.formatCellValue(sh.getRow(i).getCell(1)));
            p.setEnd_date(formatter.formatCellValue(sh.getRow(i).getCell(2)));
            data.put(formatter.formatCellValue(sh.getRow(i).getCell(0)), p);
            }
            return data;
        // break;
        case "project.xlsx":
        for(int i=1;i<noOfRows;i++){
            project project=new project();
            project.setProject_id(formatter.formatCellValue(sh.getRow(i).getCell(0)));
            project.setProject_name(formatter.formatCellValue(sh.getRow(i).getCell(1)));
            project.setStart_date(formatter.formatCellValue(sh.getRow(i).getCell(2)));
            project.setEnd_date(formatter.formatCellValue(sh.getRow(i).getCell(3)));
            project.setRemarks(formatter.formatCellValue(sh.getRow(i).getCell(5)));
            project.setProject_manager(formatter.formatCellValue(sh.getRow(i).getCell(6)));
            project.setProject_max_amount(formatter.formatCellValue(sh.getRow(i).getCell(7)));
            project.setProject_type(formatter.formatCellValue(sh.getRow(i).getCell(8)));
            project.setProject_status(formatter.formatCellValue(sh.getRow(i).getCell(9)));
            data.put(formatter.formatCellValue(sh.getRow(i).getCell(0)), project);
        }
        return data;
        // break;
        case "tasks.xlsx":
        for(int i=1;i<noOfRows;i++){
            task ta= new task();
            ta.setTask_id(formatter.formatCellValue(sh.getRow(i).getCell(0)));
            ta.setItem_id(formatter.formatCellValue(sh.getRow(i).getCell(1)));
            ta.setTask_description(formatter.formatCellValue(sh.getRow(i).getCell(2)));
            data.put(formatter.formatCellValue(sh.getRow(i).getCell(0)),ta);
            }
            return data;
        // break;
    }
	wb.close();
    return data;
		}
		catch(Exception e){
			System.out.println("Error:"+e);
            return data;
		}
        
	}
    // 0112000033
}
