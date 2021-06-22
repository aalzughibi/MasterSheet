package master.sheet.mastersheet.SheetsModel;

import java.util.Date;

public class item {
    private String item_id;
    private String item_type;
    private String item_name;
    private String project_id;
    private Date start_date;
    private Date end_date;
    private String item_remarks;
    private String po_remarks;
    private String po_no;
    private String po_value;

    /**
     * @return String return the item_id
     */
    public String getItem_id() {
        return item_id;
    }
    
public String toString(){
    return item_id+item_type+item_name+project_id+start_date+end_date+item_remarks+po_remarks+po_no+po_value;
}
    /**
     * @param item_id the item_id to set
     */
    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    /**
     * @return String return the item_type
     */
    public String getItem_type() {
        return item_type;
    }

    /**
     * @param item_type the item_type to set
     */
    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }

    /**
     * @return String return the item_name
     */
    public String getItem_name() {
        return item_name;
    }

    /**
     * @param item_name the item_name to set
     */
    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    /**
     * @return String return the project_id
     */
    public String getProject_id() {
        return project_id;
    }

    /**
     * @param project_id the project_id to set
     */
    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    /**
     * @return String return the start_date
     */
    public Date getStart_date() {
        return start_date;
    }

    /**
     * @param start_date the start_date to set
     */
    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    /**
     * @return String return the end_date
     */
    public Date getEnd_date() {
        return end_date;
    }

    /**
     * @param end_date the end_date to set
     */
    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    /**
     * @return String return the item_remarks
     */
    public String getItem_remarks() {
        return item_remarks;
    }

    /**
     * @param item_remarks the item_remarks to set
     */
    public void setItem_remarks(String item_remarks) {
        this.item_remarks = item_remarks;
    }

    /**
     * @return String return the po_remarks
     */
    public String getPo_remarks() {
        return po_remarks;
    }

    /**
     * @param po_remarks the po_remarks to set
     */
    public void setPo_remarks(String po_remarks) {
        this.po_remarks = po_remarks;
    }

    /**
     * @return String return the po_no
     */
    public String getPo_no() {
        return po_no;
    }

    /**
     * @param po_no the po_no to set
     */
    public void setPo_no(String po_no) {
        this.po_no = po_no;
    }

    /**
     * @return String return the po_value
     */
    public String getPo_value() {
        return po_value;
    }

    /**
     * @param po_value the po_value to set
     */
    public void setPo_value(String po_value) {
        this.po_value = po_value;
    }

}
