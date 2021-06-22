package master.sheet.mastersheet.UploadFile;
import master.sheet.mastersheet.SheetsModel.task;
import master.sheet.mastersheet.excelHelper.excelHelper;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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
	public static String poTable = "po";
	public static String projectTable = "project";
	public static String itemTable = "item";
    public static String logTable = "log";
    @PostMapping()
    public ResponseEntity<Map<String,Object>> UploadFile(@RequestHeader("Authorization") String header_auth,@RequestParam("items") MultipartFile itemsfile,
    @RequestParam("po") MultipartFile pofile,@RequestParam("project") MultipartFile projectfile,@RequestParam("tasks") MultipartFile tasksfile){
        try {
            if(Auth.validJWT(header_auth)){
                JSONObject jo =  Auth.convert_JsonString_To_Json(Auth.getJWTToken(header_auth)[1]);
                String usernameU=jo.getString("username");
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
                    if(insertIntoMasterData(items,proFile,poFile,tasksFile,usernameU))
                    System.out.println("Add successfully");
                    else 
                    System.out.println("Fail to Add");
                    
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
    public static boolean insertLog(String usernameU,String colName,String rowName,Date dateOfUpdate,String beforeChange,String afterChange){
        // String sql = "CREATE TABLE "+logTable+"(id int NOT NULL PRIMARY KEY AUTO_INCREMENT,username VARCHAR(255) NOT NULL,"+
		// 		"colName VARCHAR(255) NOT NULL,rowName VARCHAR(255) NOT NULL,"+
		// 		"dateOfUpdate VARCHAR(255) NOT NULL,beforeChange VARCHAR(255) NOT NULL,afterChange VARCHAR(255) NOT NULL)";
        try {
            Class.forName("org.mariadb.jdbc.Driver");  
			Connection con=DriverManager.getConnection(  
			"jdbc:mariadb://localhost:"+port+"/"+database,username,password);
			Statement stmt = con.createStatement();
            String sql = "INSERT INTO "+logTable+" (username,colName,rowName,dateOfUpdate,beforeChange,afterChange) "+
            String.format("VALUES ('%s','%s','%s','%s','%s','%s')"
            ,usernameU,colName,rowName,new Timestamp(dateOfUpdate.getTime()),beforeChange,afterChange);
            stmt.executeUpdate(sql);
            stmt.close();
            con.close();
            return true;
        } catch (Exception e) {
            //TODO: handle exception
            System.out.println(e);
            return false;
        }
    }
    public static boolean check_and_update_project(project projects,String usernameU){
        try {
            String usernameUser=usernameU;
            Class.forName("org.mariadb.jdbc.Driver");  
			Connection con=DriverManager.getConnection(  
			"jdbc:mariadb://localhost:"+port+"/"+database,username,password);
			Statement stmt = con.createStatement();
            String sql = "select * from "+projectTable+" where project_id='"+projects.getProject_id()+"'";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                if(!rs.getString("project_name").equals(projects.getProject_name())){
                        stmt.execute("UPDATE "+projectTable+" SET project_name='"+projects.getProject_name()+"' WHERE project_id='"+projects.getProject_id()+"'");
                        insertLog(usernameUser,"project_name",projects.getProject_id(),new Date(),rs.getString("project_name"),projects.getProject_name());
                    }
                if(!rs.getDate("project_start_date").equals(projects.getStart_date())){
                    stmt.execute("UPDATE "+projectTable+" SET project_start_date='"+projects.getStart_date()+"' WHERE project_id='"+projects.getProject_id()+"'");
                    insertLog(usernameUser,"project_start_date",projects.getProject_id(),new Date(),rs.getString("project_start_date").toString(),projects.getStart_date().toString());
                }
                if(!rs.getDate("project_end_date").equals(projects.getEnd_date())){
                    stmt.execute("UPDATE "+projectTable+" SET project_end_date='"+projects.getEnd_date()+"' WHERE project_id='"+projects.getProject_id()+"'");
                    insertLog(usernameUser,"project_end_date",projects.getProject_id(),new Date(),rs.getString("project_end_date").toString(),projects.getEnd_date().toString());
                }
                if(!rs.getString("project_remarks").equals(projects.getRemarks())){
                    stmt.execute("UPDATE "+projectTable+" SET project_remarks='"+projects.getRemarks()+"' WHERE project_id='"+projects.getProject_id()+"'");
                    insertLog(usernameUser,"project_remarks",projects.getProject_id(),new Date(),rs.getString("project_remarks"),projects.getRemarks());
                }
                if(!rs.getString("project_manager").equals(projects.getProject_manager())){
                    stmt.execute("UPDATE "+projectTable+" SET project_manager='"+projects.getProject_manager()+"' WHERE project_id='"+projects.getProject_id()+"'");
                    insertLog(usernameUser,"project_manager",projects.getProject_id(),new Date(),rs.getString("project_manager"),projects.getProject_manager());
                }
                if(!rs.getString("project_type").equals(projects.getProject_type())){
                    stmt.execute("UPDATE "+projectTable+" SET project_type='"+projects.getProject_type()+"' WHERE project_id='"+projects.getProject_id()+"'");
                    insertLog(usernameUser,"project_type",projects.getProject_id(),new Date(),rs.getString("project_type"),projects.getProject_type());
                }
                if(!rs.getString("project_stauts").equals(projects.getProject_status())){
                    stmt.execute("UPDATE "+projectTable+" SET project_stauts='"+projects.getProject_status()+"' WHERE project_id='"+projects.getProject_id()+"'");
                    insertLog(usernameUser,"project_stauts",projects.getProject_id(),new Date(),rs.getString("project_stauts"),projects.getProject_status());
                }
                if(!rs.getString("project_max_amount").equals(projects.getProject_max_amount())){
                    stmt.execute("UPDATE "+projectTable+" SET project_max_amount='"+projects.getProject_max_amount()+"' WHERE project_id='"+projects.getProject_id()+"'");
                    insertLog(usernameUser,"project_max_amount",projects.getProject_id(),new Date(),rs.getString("project_max_amount"),projects.getProject_max_amount());
                }
            }
            stmt.close();
            con.close();
        } catch (Exception e) {
            //TODO: handle exception
            System.err.println(e);
        }
        return true;
    }
    public static boolean check_and_update_item(item item,String usernameU){
        try {
            String usernameUser=usernameU;
            Class.forName("org.mariadb.jdbc.Driver");  
			Connection con=DriverManager.getConnection(  
			"jdbc:mariadb://localhost:"+port+"/"+database,username,password);
			Statement stmt = con.createStatement();
            String sql = "select * from "+itemTable+" where item_id='"+item.getItem_id()+"'";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                if(!rs.getString("item_name").equals(item.getItem_name())){
                        stmt.execute("UPDATE "+itemTable+" SET item_name='"+item.getItem_name()+"' WHERE item_id='"+item.getItem_id()+"'");
                        insertLog(usernameUser,"item_name",item.getItem_id(),new Date(),rs.getString("item_name"),item.getItem_name());
                }
                if(!rs.getString("item_type").equals(item.getItem_type())){
                    stmt.execute("UPDATE "+itemTable+" SET item_type='"+item.getItem_type()+"' WHERE item_id='"+item.getItem_id()+"'");
                    insertLog(usernameUser,"item_type",item.getItem_id(),new Date(),rs.getString("item_type"),item.getItem_type());
                }
                if(!rs.getDate("item_start_date").equals(item.getStart_date())){
                    stmt.execute("UPDATE "+itemTable+" SET item_start_date='"+item.getStart_date()+"' WHERE item_id='"+item.getItem_id()+"'");
                    insertLog(usernameUser,"item_start_date",item.getItem_id(),new Date(),rs.getString("item_start_date").toString(),item.getStart_date().toString());
                }
                if(!rs.getDate("item_end_date").equals(item.getEnd_date())){
                    stmt.execute("UPDATE "+itemTable+" SET item_end_date='"+item.getEnd_date()+"' WHERE item_id='"+item.getItem_id()+"'");
                    insertLog(usernameUser,"item_end_date",item.getItem_id(),new Date(),rs.getString("item_end_date").toString(),item.getEnd_date().toString());
                }
                if(!rs.getString("item_remarks").equals(item.getItem_remarks())){
                    stmt.execute("UPDATE "+itemTable+" SET item_remarks='"+item.getItem_remarks()+"' WHERE item_id='"+item.getItem_id()+"'");
                    insertLog(usernameUser,"item_remarks",item.getItem_id(),new Date(),rs.getString("item_remarks"),item.getItem_remarks());
                }
                if(!rs.getString("po_no").equals(item.getPo_no())){
                    stmt.execute("UPDATE "+itemTable+" SET po_no='"+item.getPo_no()+"' WHERE item_id='"+item.getItem_id()+"'");
                    insertLog(usernameUser,"po_no",item.getItem_id(),new Date(),rs.getString("po_no"),item.getPo_no());
                }
                if(!rs.getString("project_id").equals(item.getProject_id())){
                    stmt.execute("UPDATE "+itemTable+" SET project_id='"+item.getProject_id()+"' WHERE item_id='"+item.getItem_id()+"'");
                    insertLog(usernameUser,"project_id",item.getItem_id(),new Date(),rs.getString("project_id"),item.getProject_id());
                }
                if(!rs.getString("po_value").equals(item.getPo_value())){
                    stmt.execute("UPDATE "+itemTable+" SET po_value='"+item.getPo_value()+"' WHERE item_id='"+item.getItem_id()+"'");
                    insertLog(usernameUser,"po_value",item.getItem_id(),new Date(),rs.getString("po_value"),item.getPo_value());
                }
               
            }
            stmt.close();
            con.close();
        } catch (Exception e) {
            //TODO: handle exception
            System.err.println(e);
        }
        return true;
    }
    public static boolean check_and_update_task(task task,String usernameU){
        String usernameUser=usernameU;
        try {
            // task_id,item_id,task_description
            Class.forName("org.mariadb.jdbc.Driver");  
			Connection con=DriverManager.getConnection(  
			"jdbc:mariadb://localhost:"+port+"/"+database,username,password);
			Statement stmt = con.createStatement();
            String sql = "select * from "+taskTable+" where task_id='"+task.getTask_id()+"'";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                if(!rs.getString("item_id").equals(task.getItem_id())){
                    stmt.execute("UPDATE "+taskTable+" SET item_id='"+task.getItem_id()+"' WHERE task_id='"+task.getTask_id()+"'");
                    insertLog(usernameUser,"item_id",task.getTask_id(),new Date(),rs.getString("item_id"),task.getItem_id());
                }
                if(!rs.getString("task_description").equals(task.getTask_description())){
                    stmt.execute("UPDATE "+taskTable+" SET task_description='"+task.getTask_description()+"' WHERE task_id='"+task.getTask_id()+"'");
                    insertLog(usernameUser,"task_description",task.getTask_id(),new Date(),rs.getString("task_description"),task.getTask_description());
                }
            }
            stmt.close();
            con.close();
        } catch (Exception e) {
            //TODO: handle exception
            System.err.println(e);
        }
        return true;
    }
    public static boolean check_and_update_po(po po,String usernameU){
        String usernameUser = usernameU;
        try {
                    // id
        // po_no
        // po_start_date
        // po_end_date
            // task_id,item_id,task_description
            Class.forName("org.mariadb.jdbc.Driver");  
			Connection con=DriverManager.getConnection(  
			"jdbc:mariadb://localhost:"+port+"/"+database,username,password);
			Statement stmt = con.createStatement();
            String sql = "select * from "+poTable+" where po_no='"+po.getPo_id()+"'";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                if(!rs.getString("po_start_date").equals(po.getStart_date())){
                    stmt.execute("UPDATE "+poTable+" SET po_start_date='"+po.getStart_date()+"' WHERE po_no='"+po.getPo_id()+"'");
                    insertLog(usernameUser,"po_start_date",po.getPo_id(),new Date(),rs.getString("po_start_date").toString(),po.getStart_date().toString());
                }
                if(!rs.getString("po_end_date").equals(po.getEnd_date())){
                    stmt.execute("UPDATE "+poTable+" SET po_end_date='"+po.getEnd_date()+"' WHERE po_no='"+po.getPo_id()+"'");
                    insertLog(usernameUser,"po_end_date",po.getPo_id(),new Date(),rs.getString("po_end_date").toString(),po.getEnd_date().toString());
                }
            }
            stmt.close();
            con.close();
        } catch (Exception e) {
            System.err.println(e);
        }
        return true;
    }
    public static boolean insertIntoMasterData(Map<String,Object> items,Map<String,Object> projects,Map<String,Object> pos,Map<String,Object> tasks,String usernameU){
        System.out.println("start at:"+new Date());
        excelHelper.wrtieExcelFile(projects,items,pos,tasks);
        for(Map.Entry<String,Object> proj:projects.entrySet()){
            if(!checkProjectExist(proj.getKey())){
                insertIntoProject(((project)proj.getValue()));
            }
            else{
                check_and_update_project(((project)proj.getValue()),usernameU);
            }
        }
        for(Map.Entry<String,Object> proj:items.entrySet()){
            if(!checkItemExist(proj.getKey())){
                insertIntoItem(((item)proj.getValue()));
            }
            else{
                check_and_update_item(((item)proj.getValue()),usernameU);
            }
        }
        for(Map.Entry<String,Object> proj:pos.entrySet()){
            if(!checkPoExist(proj.getKey())){
                insertIntoPo(((po)proj.getValue()));
            }
            else{
                // if updated or not and updated
                check_and_update_po(((po)proj.getValue()),usernameU);
            }
            
        }
        for(Map.Entry<String,Object> proj:tasks.entrySet()){
            if(!checktaskExist(proj.getKey()))
            {
                insertTask(((task)proj.getValue()));
                System.out.println("success in task");
            }
            else{
                check_and_update_task(((task)proj.getValue()),usernameU);
                System.out.println("isExist - Task");
            }
            
        }
        System.out.println("end at:"+new Date());
        return true;
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
            // Timestamp ts = new Timestamp();
            items.setItem_id(formatter.formatCellValue(sh.getRow(i).getCell(0)));
            items.setProject_id(formatter.formatCellValue(sh.getRow(i).getCell(1)));
            items.setItem_name(formatter.formatCellValue(sh.getRow(i).getCell(2)));
            items.setItem_remarks(formatter.formatCellValue(sh.getRow(i).getCell(3)));
            items.setItem_type(formatter.formatCellValue(sh.getRow(i).getCell(4)));
            // items.setStart_date(formatter.formatCellValue(sh.getRow(i).getCell(5)));
            // new SimpleDateFormat("MM/dd/yyyy").parse(formatter.formatCellValue(sh.getRow(i).getCell(5)))
            items.setStart_date(new SimpleDateFormat("MM/dd/yyyy").parse(formatter.formatCellValue(sh.getRow(i).getCell(5))));
            items.setEnd_date(new SimpleDateFormat("MM/dd/yyyy").parse(formatter.formatCellValue(sh.getRow(i).getCell(6))));
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
            p.setStart_date(new SimpleDateFormat("MM/dd/yyyy").parse(formatter.formatCellValue(sh.getRow(i).getCell(1))));
            p.setEnd_date(new SimpleDateFormat("MM/dd/yyyy").parse(formatter.formatCellValue(sh.getRow(i).getCell(2))));
            data.put(formatter.formatCellValue(sh.getRow(i).getCell(0)), p);
            }
            return data;
        // break;
        case "project.xlsx":
        for(int i=1;i<noOfRows;i++){
            if(formatter.formatCellValue(sh.getRow(i).getCell(0))!=""){
                // System.out.println(formatter.formatCellValue(sh.getRow(i).getCell(0)));
                project project=new project();
                project.setProject_id(formatter.formatCellValue(sh.getRow(i).getCell(0)));
                project.setProject_name(formatter.formatCellValue(sh.getRow(i).getCell(1)));
                project.setStart_date(new SimpleDateFormat("MM/dd/yyyy").parse(formatter.formatCellValue(sh.getRow(i).getCell(2))));
                project.setEnd_date(new SimpleDateFormat("MM/dd/yyyy").parse(formatter.formatCellValue(sh.getRow(i).getCell(3))));
                project.setRemarks(formatter.formatCellValue(sh.getRow(i).getCell(5)));
                project.setProject_manager(formatter.formatCellValue(sh.getRow(i).getCell(6)));
                project.setProject_max_amount(formatter.formatCellValue(sh.getRow(i).getCell(7)));
                project.setProject_type(formatter.formatCellValue(sh.getRow(i).getCell(8)));
                project.setProject_status(formatter.formatCellValue(sh.getRow(i).getCell(9)));
                data.put(formatter.formatCellValue(sh.getRow(i).getCell(0)), project);
            }

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

    public static boolean updateMasterSheetRow(String column,String condition,String newValue,Statement stmt){
        try {
            stmt.executeUpdate("UPDATE "+masterDataTable+" SET "+column+"='"+newValue+"' WHERE "+condition);
            System.out.println("updated : "+column);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
            //TODO: handle exception
        }
    }
    public static ResultSet retriveMasterData(String item_id){
        try {
            Class.forName("org.mariadb.jdbc.Driver");  
			Connection con=DriverManager.getConnection(  
			"jdbc:mariadb://localhost:"+port+"/"+database,username,password);
			Statement stmt = con.createStatement();
            String sql = "select * from "+masterDataTable+" where item_id='"+item_id+"'";
            ResultSet rs = stmt.executeQuery(sql);
            stmt.close();
            con.close();
            return rs;
        } catch (Exception e) {
            //TODO: handle exception
            return null;
        }
    }
    public static boolean checkProjectExist(String project_id){
        try {
            Class.forName("org.mariadb.jdbc.Driver");  
			Connection con=DriverManager.getConnection(  
			"jdbc:mariadb://localhost:"+port+"/"+database,username,password);
			Statement stmt = con.createStatement();
            String sql = "select * from "+projectTable+" where project_id='"+project_id+"'";
            ResultSet rs = stmt.executeQuery(sql);
            stmt.close();
            con.close();
            boolean flag  = rs.next();
            // System.out.println(flag);
            return flag;
        } catch (Exception e) {
            //TODO: handle exception
            return false;
        }
    }
    public static boolean checkItemExist(String item_id){
        try {
            Class.forName("org.mariadb.jdbc.Driver");  
			Connection con=DriverManager.getConnection(  
			"jdbc:mariadb://localhost:"+port+"/"+database,username,password);
			Statement stmt = con.createStatement();
            String sql = "select * from "+itemTable+" where item_id='"+item_id+"'";
            ResultSet rs = stmt.executeQuery(sql);
            stmt.close();
            con.close();
            boolean flag  = rs.next();
            // System.out.println(flag);
            return flag;
        } catch (Exception e) {
            //TODO: handle exception
            return false;
        }
    }
    public static boolean checkPoExist(String po_no){
        try {
            Class.forName("org.mariadb.jdbc.Driver");  
			Connection con=DriverManager.getConnection(  
			"jdbc:mariadb://localhost:"+port+"/"+database,username,password);
			Statement stmt = con.createStatement();
            String sql = "select * from "+poTable+" where po_no='"+po_no+"'";
            ResultSet rs = stmt.executeQuery(sql);
            stmt.close();
            con.close();
            boolean flag  = rs.next();
            // System.out.println(flag);
            return flag;
        } catch (Exception e) {
            //TODO: handle exception
            return false;
        }
    }
    public static boolean checktaskExist(String task_id){
        try {
            Class.forName("org.mariadb.jdbc.Driver");  
			Connection con=DriverManager.getConnection(  
			"jdbc:mariadb://localhost:"+port+"/"+database,username,password);
			Statement stmt = con.createStatement();
            String sql = "select * from "+taskTable+" where task_id='"+task_id+"'";
            ResultSet rs = stmt.executeQuery(sql);
            stmt.close();
            con.close();
            boolean flag  = rs.next();
            // System.out.println(flag);
            return flag;
        } catch (Exception e) {
            //TODO: handle exception
            return false;
        }
    }
   public static boolean insertIntoProject(project pro){
    try{
        Class.forName("org.mariadb.jdbc.Driver");  
        Connection con=DriverManager.getConnection(  
        "jdbc:mariadb://localhost:"+port+"/"+database,username,password);
        Statement stmt = con.createStatement();
//         id
// project_id
// project_name
// project_start_date
// project_end_date
// project_remarks
// project_manager
// project_type
// project_stauts
// project_max_amount
// payment_value
// payment_date
System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp(pro.getStart_date().getTime())));
        String sql = "INSERT INTO "+projectTable+" (project_id,project_name,project_start_date,project_end_date,project_remarks,project_manager,project_type,"+
        "project_stauts,project_max_amount,payment_value,payment_date) "+
        String.format("VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')"
        ,pro.getProject_id(),pro.getProject_name(),new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp(pro.getStart_date().getTime())),new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp(pro.getEnd_date().getTime())),pro.getRemarks(),pro.getProject_manager(),pro.getProject_type(),pro.getProject_status(),pro.getProject_max_amount(),"","");
        stmt.executeUpdate(sql);
        stmt.close();
        con.close();
        return true;
    }catch(Exception e){
        System.out.println(e);
        return false;
    }
   }
   public static boolean insertIntoItem(item ite){
    try{
        Class.forName("org.mariadb.jdbc.Driver");  
        Connection con=DriverManager.getConnection(  
        "jdbc:mariadb://localhost:"+port+"/"+database,username,password);
        Statement stmt = con.createStatement();
        String sql = "INSERT INTO "+itemTable+" (item_id,item_name,item_type,item_start_date,item_end_date,item_remarks,po_no,"+
        "po_value,project_id,payment_value,payment_date) "+
        String.format("VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')"
        ,ite.getItem_id(),ite.getItem_name(),ite.getItem_type(),new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp(ite.getStart_date().getTime())),new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp(ite.getEnd_date().getTime())),ite.getItem_remarks(),ite.getPo_no(),ite.getPo_value(),ite.getProject_id(),"","");
        stmt.executeUpdate(sql);
        stmt.close();
        con.close();
        return true;
    }catch(Exception e){
        System.out.println(e);
        return false;
    }
   }
   public static boolean insertIntoPo(po po){
    try{
        Class.forName("org.mariadb.jdbc.Driver");  
        Connection con=DriverManager.getConnection(  
        "jdbc:mariadb://localhost:"+port+"/"+database,username,password);
        Statement stmt = con.createStatement();
        String sql = "INSERT INTO "+poTable+" (po_no,po_start_date,po_end_date) "+
        String.format("VALUES ('%s','%s','%s')"
        ,po.getPo_id(),new Timestamp(po.getStart_date().getTime()),new Timestamp(po.getEnd_date().getTime()));
        stmt.executeUpdate(sql);
        stmt.close();
        con.close();
        return true;
    }catch(Exception e){
        System.out.println(e);
        return false;
    }
   }
    // public static boolean insertMasterData(sheet sh){
    //     try{
	// 		Class.forName("org.mariadb.jdbc.Driver");  
	// 		Connection con=DriverManager.getConnection(  
	// 		"jdbc:mariadb://localhost:"+port+"/"+database,username,password);
	// 		Statement stmt = con.createStatement();
	// 		String sql = "INSERT INTO "+masterDataTable+" (item_id,item_name,item_type,item_start_date,item_end_date,item_remarks,po_no,"+
    //         "po_start_date,po_end_date,po_value,project_id,project_name,project_start_date,project_end_date,project_remarks,project_manager,project_type,"+
    //         "project_stauts,payment_value,payment_date) "+
	// 		String.format("VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')"
    //         ,sh.getItem_id(),sh.getItem_name(),sh.getItem_type(),sh.getItem_start_date(),sh.getItem_end_date(),sh.getItem_remarks(),sh.getPo_no()
    //         ,sh.getPo_start_date(),sh.getPo_end_date(),sh.getPo_value(),sh.getProject_id(),sh.getProject_name(),sh.getProject_start_date(),sh.getProject_end_date()
    //         ,sh.getProject_remarks(),sh.getProject_manager(),sh.getProject_type(),sh.getProject_stauts(),sh.getPayment_value(),sh.getPayment_date());
	// 		stmt.executeUpdate(sql);
    //         stmt.close();
    //         con.close();
	// 		return true;
	// 	}catch(Exception e){
	// 		System.out.println(e);
	// 		return false;
	// 	}
    // }
    public static boolean insertTask(task tas){
        try{
			Class.forName("org.mariadb.jdbc.Driver");  
			Connection con=DriverManager.getConnection(  
			"jdbc:mariadb://localhost:"+port+"/"+database,username,password);
			Statement stmt = con.createStatement();
			String sql = "INSERT INTO "+taskTable+" (task_id,item_id,task_description) "+
			String.format("VALUES ('%s','%s','%s')"
            ,tas.getTask_id(),tas.getItem_id(),tas.getTask_description());
			stmt.executeUpdate(sql);
            stmt.close();
            con.close();
			return true;
		}catch(Exception e){
			System.out.println(e);
			return false;
		}
    }
    // 0112000033
}
