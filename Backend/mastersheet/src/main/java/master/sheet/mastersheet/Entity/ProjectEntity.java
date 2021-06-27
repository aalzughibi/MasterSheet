package master.sheet.mastersheet.Entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
@Entity
@Table(name="Project")
public class ProjectEntity implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="project_id",unique = true,nullable=false)
    private String projectId;
    @Column(name="project_name",unique = false,nullable=false)
    private String project_name;
    @Column(name="start_date")
    @Temporal(TemporalType.DATE)
    private Date start_date;
    @Column(name="end_date")
    @Temporal(TemporalType.DATE)
    private Date end_date;
    @Column(name="remarks")
    private String remarks;
    @Column(name="project_manager")
    private String project_manager;
    @Column(name="project_max_amount")
    private String project_max_amount;
    @Column(name="project_type")
    private String project_type;
    @Column(name="project_status")
    private String project_status;

    /**
     * @return Long return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return String return the project_id
     */
    public String getProject_id() {
        return projectId;
    }

    /**
     * @param project_id the project_id to set
     */
    public void setProject_id(String project_id) {
        this.projectId = project_id;
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
     * @return Date return the start_date
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
     * @return Date return the end_date
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
     * @return String return the project_max_amount
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
