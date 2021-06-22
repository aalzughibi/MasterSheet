package master.sheet.mastersheet.SheetsModel;

import java.util.Date;

public class project {
    private String project_id;
    private String project_name;
    private Date start_date;
    private Date end_date;
    private String remarks;
    private String project_manager;
    private String project_max_amount;
    private String project_type;
    private String project_status;

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
     * @return String return the project_name
     */
    public String getProject_name() {
        return project_name;
    }

    /**
     * @param project_name the project_name to set
     */
    public void setProject_name(String project_name) {
        this.project_name = project_name;
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
     * @return String return the remarks
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @param remarks the remarks to set
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * @return String return the project_manager
     */
    public String getProject_manager() {
        return project_manager;
    }

    /**
     * @param project_manager the project_manager to set
     */
    public void setProject_manager(String project_manager) {
        this.project_manager = project_manager;
    }

    /**
     * @return double return the project_max_amount
     */
    public String getProject_max_amount() {
        return project_max_amount;
    }

    /**
     * @param project_max_amount the project_max_amount to set
     */
    public void setProject_max_amount(String project_max_amount) {
        this.project_max_amount = project_max_amount;
    }

    /**
     * @return String return the project_type
     */
    public String getProject_type() {
        return project_type;
    }

    /**
     * @param project_type the project_type to set
     */
    public void setProject_type(String project_type) {
        this.project_type = project_type;
    }

    /**
     * @return String return the project_status
     */
    public String getProject_status() {
        return project_status;
    }

    /**
     * @param project_status the project_status to set
     */
    public void setProject_status(String project_status) {
        this.project_status = project_status;
    }

}
