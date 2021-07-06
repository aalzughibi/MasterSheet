package master.sheet.mastersheet.Entity;

import java.io.Serializable;

import javax.persistence.*;
@Entity
@Table(name="task")
public class TaskEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "task_id",unique = true,nullable=false)
    private String taskId;
    @Column(name = "item_id")
    private String item_id;
    @Column(name = "task_description")
    private String task_description;
    @Column(name = "version")
	@Version
	private Long version;
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
     * @return String return the task_id
     */
    public String getTask_id() {
        return taskId;
    }

    /**
     * @param task_id the task_id to set
     */
    public void setTask_id(String task_id) {
        this.taskId = task_id;
    }

    /**
     * @return String return the item_id
     */
    public String getItem_id() {
        return item_id;
    }

    /**
     * @param item_id the item_id to set
     */
    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    /**
     * @return String return the task_description
     */
    public String getTask_description() {
        return task_description;
    }

    /**
     * @param task_description the task_description to set
     */
    public void setTask_description(String task_description) {
        this.task_description = task_description;
    }


    /**
     * @return String return the taskId
     */
    public String getTaskId() {
        return taskId;
    }

    /**
     * @param taskId the taskId to set
     */
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    /**
     * @return Long return the version
     */
    public Long getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(Long version) {
        this.version = version;
    }

}
