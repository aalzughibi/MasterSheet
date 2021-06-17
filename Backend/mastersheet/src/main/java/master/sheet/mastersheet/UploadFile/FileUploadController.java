package master.sheet.mastersheet.UploadFile;
import master.sheet.mastersheet.SheetsModel.task;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
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
import master.sheet.mastersheet.SheetsModel.sheet;

@RestController
@RequestMapping("UploadFile")
public class FileUploadController {
    public static String port = "3306";
    public static  String database = "mastersheetdatabase";
    public static String username = "root";
    public static String password = "1234";
	public static String userTable = "user";
	public static String masterDataTable = "masterdata";
	public static String taskTable = "task";

    @PostMapping()
    public ResponseEntity<Map<String,Object>> UploadFile(@RequestHeader("Authorization") String header_auth,@RequestParam("items") MultipartFile itemsfile,
    @RequestParam("po") MultipartFile pofile,@RequestParam("project") MultipartFile projectfile,@RequestParam("tasks") MultipartFile tasksfile){
        try {
            if(Auth.validJWT(header_auth)){
                JSONObject jo =  Auth.convert_JsonString_To_Json(Auth.getJWTToken(header_auth)[1]);
                if(UserController.isAdmin(String.valueOf(jo.get("userId")))){
                    // Project ---> item
                    // item ------> task

                    Map<String,Object> items = upload_file(itemsfile);
                    Map<String,String> not_found=new HashMap<>();
                    Map<String,Object> poFile = upload_file(pofile);
                    Map<String,Object> proFile = upload_file(projectfile);
                    for(Map.Entry<String,Object> ite:items.entrySet()){
                        if(!isProject(proFile,((item)ite.getValue()).getProject_id())){
                            not_found.put(((item)ite.getValue()).getItem_id(),"");
                            System.out.println("Project not Found");
                        }
                    }
                    for(Map.Entry<String,String> notf:not_found.entrySet()){
                        items.remove(notf.getKey());
                    }
                    not_found.clear();
                    Map<String,Object> tasksFile = upload_file(tasksfile);
                    for(Map.Entry<String,Object> ite:tasksFile.entrySet()){
                        if(!isItem(items,((task)ite.getValue()).getItem_id())){
                            not_found.put(((task)ite.getValue()).getTask_id(),"");
                            System.out.println("item not found");
                        }
                    }
                    for(Map.Entry<String,String> notf:not_found.entrySet()){
                        items.remove(notf.getKey());
                    }
                    for(Map.Entry<String,String> notf:not_found.entrySet()){
                        items.remove(notf.getKey());
                    }
                    Map<String,Object> mp =new HashMap<>();
                    // insert to master sheet
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
    public boolean isProject(Map<String,Object> projects,String project_id){
        if (projects==null||project_id == null) return false;
        return projects.get(project_id)!=null?true:false;
    }
    public boolean isItem(Map<String,Object> items,String item_id){
        if (items==null ||item_id == null) return false;
        return items.get(item_id)!=null?true:false;
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
    // method to add to database
    public static boolean insertIntoMasterData(Map<String,Object> items,Map<String,Object> projects,Map<String,Object> pos,Map<String,Object> tasks){
        for(Map.Entry<String,Object> proj:projects.entrySet()){
        for(Map.Entry<String,Object> item:items.entrySet()){
            if(proj.getKey().equals(((item)item.getValue()).getProject_id())){
                sheet sh = new sheet();
                sh.setItem_id(((item)item.getValue()).getItem_id());
                sh.setItem_name(((item)item.getValue()).getItem_name());
                sh.setItem_type(((item)item.getValue()).getItem_type());
                sh.setItem_start_date(((item)item.getValue()).getStart_date());
                sh.setItem_end_date(((item)item.getValue()).getEnd_date());
                sh.setItem_remarks(((item)item.getValue()).getItem_remarks());
                sh.setPo_no(((item)item.getValue()).getPo_no());
                sh.setPo_value(((item)item.getValue()).getPo_value());
                sh.setPo_start_date(((po)pos.get(((item)item.getValue()).getPo_no())).getStart_date());
                sh.setPo_end_date(((po)pos.get(((item)item.getValue()).getPo_no())).getEnd_date());
                sh.setProject_id(((project)proj.getValue()).getProject_id());
                sh.setProject_name(((project)proj.getValue()).getProject_name());
                sh.setProject_start_date(((project)proj.getValue()).getStart_date());
                sh.setProject_end_date(((project)proj.getValue()).getEnd_date());
                sh.setProject_remarks(((project)proj.getValue()).getRemarks());
                sh.setProject_manager(((project)proj.getValue()).getProject_manager());
                sh.setProject_type(((project)proj.getValue()).getProject_type());
                sh.setProject_stauts(((project)proj.getValue()).getProject_status());
                sh.setPayment_value("");
                sh.setPayment_date("");
                insertMasterData(sh);
            }
        }
        }
        return false;
    }
    public static boolean insertMasterData(sheet sh){
        try{
			Class.forName("org.mariadb.jdbc.Driver");  
			Connection con=DriverManager.getConnection(  
			"jdbc:mariadb://localhost:"+port+"/"+database,username,password);
			Statement stmt = con.createStatement();
			String sql = "INSERT INTO "+masterDataTable+" (item_id,item_name,item_type,item_start_date,item_end_date,item_remarks,po_no,"+
            "po_start_date,po_end_date,po_value,project_id,project_name,project_start_date,project_end_date,project_remarks,project_manager,project_type,"+
            "project_stauts,payment_value,payment_date) "+
			String.format("VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')"
            ,sh.getItem_id(),sh.getItem_name(),sh.getItem_type(),sh.getItem_start_date(),sh.getItem_end_date(),sh.getItem_remarks(),sh.getPo_no()
            ,sh.getPo_start_date(),sh.getPo_end_date(),sh.getPo_value(),sh.getProject_id(),sh.getProject_name(),sh.getProject_start_date(),sh.getProject_end_date()
            ,sh.getProject_remarks(),sh.getProject_manager(),sh.getProject_type(),sh.getProject_stauts(),sh.getPayment_value(),sh.getPayment_date());
			stmt.executeUpdate(sql);
			return true;
		}catch(Exception e){
			System.out.println(e);
			return false;
		}
    }
    public static boolean insertTask(task tas){
        try{
			Class.forName("org.mariadb.jdbc.Driver");  
			Connection con=DriverManager.getConnection(  
			"jdbc:mariadb://localhost:"+port+"/"+database,username,password);
			Statement stmt = con.createStatement();
			String sql = "INSERT INTO "+masterDataTable+" (task_id,item_id,task_description) "+
			String.format("VALUES ('%s','%s','%s')"
            ,tas.getTask_id(),tas.getItem_id(),tas.getTask_description());
			stmt.executeUpdate(sql);
			return true;
		}catch(Exception e){
			System.out.println(e);
			return false;
		}
    }
    // 0112000033
}
