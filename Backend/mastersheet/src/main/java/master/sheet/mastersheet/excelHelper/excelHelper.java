package master.sheet.mastersheet.excelHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

// import com.mysql.cj.result.Row;

// import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.*;

import master.sheet.mastersheet.SheetsModel.item;
import master.sheet.mastersheet.SheetsModel.po;
import master.sheet.mastersheet.SheetsModel.project;
import master.sheet.mastersheet.SheetsModel.task;

public class excelHelper {
    // Map<String,project> project,Map<String,item> item,Map<String,po> po,Map<String,task> task
    public static void wrtieExcelFile(Map<String,Object> project,Map<String,Object> item,Map<String,Object> po,Map<String,Object> task){
        XSSFWorkbook workbook = new XSSFWorkbook(); 
         XSSFSheet sheet = workbook.createSheet("MasterSheet");
            Map<String,Object[]> data = new TreeMap<String,Object[]>();
            // set data in tree
            // for project
                // for item
                    // for task
                    data.put("1",new Object[]{"item id ","item name","item type","item start date",
                    "item end date","item remarks","po no","po start date","po end date","po value","project id",
                    "project name","project start date","project end date","project remarks","project manager",
                    "project type","project stauts","tasks list"});
                    for(Map.Entry<String,Object> proj:project.entrySet()){
                        for(Map.Entry<String,Object> ite:item.entrySet()){
                            if(ite.getKey().equals(proj.getKey())){
                                String tasklist="";
                                for(Map.Entry<String,Object> taskk:task.entrySet()){
                                    if(((task)taskk.getValue()).getItem_id()==ite.getKey()){
                                        tasklist+=((task)taskk.getValue()).getTask_id()+"-"+((task)taskk.getValue()).getTask_description()+"\n\r";
                                    }
                                }
                                item it = ((item)ite.getValue());
                                po p = ((po)po.get(it.getPo_no()));
                                project projj = ((project)proj.getValue());
                                data.put(it.getItem_id(),new Object[]{it.getItem_id(),it.getItem_name(),it.getItem_type(),it.getStart_date()
                                    ,it.getEnd_date(),it.getItem_remarks(),it.getPo_no(),p.getStart_date(),p.getEnd_date(),
                                    it.getPo_value(),it.getProject_id(),projj.getProject_name(),projj.getStart_date(),
                                    projj.getEnd_date(),projj.getRemarks(),projj.getProject_manager(),projj.getProject_type(),tasklist});
                            }
                            
                        }
                    }
            // data.put("1", new Object[]{"id","name","lastname"});
            // data.put("2", new Object[]{1,"Khaled","Mohammed"});
            // data.put("2ss", new Object[]{1,"Khaled","Mohammed"});
            Set<String> keyset = data.keySet();
            int rownum=0;
            for (String key : keyset)
            {
                Row row = sheet.createRow(rownum++);
                Object [] objArr = data.get(key);
                int cellnum = 0;
                for (Object obj : objArr)
                {
                   Cell cell = row.createCell(cellnum++);
                //    cell.setCellValue((String)obj);
                   if(obj instanceof String)
                        cell.setCellValue((String)obj);
                    else if(obj instanceof Integer)
                        cell.setCellValue((Integer)obj);
                }
            }
            try {
                FileOutputStream out = new FileOutputStream(new File("testDemo.xlsx"));
                workbook.write(out);
                out.close();
                System.out.println("Done");
            } catch (Exception e) {
                System.out.println(e);
                //TODO: handle exception
            }
            }
        }

