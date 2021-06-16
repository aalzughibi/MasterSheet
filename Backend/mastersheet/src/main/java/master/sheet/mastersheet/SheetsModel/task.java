package master.sheet.mastersheet.SheetsModel;

public class task {
    private String task_id;
    private String item_id;
    private String task_description;

    /**
     * @return String return the task_id
     */
    public String getTask_id() {
        return task_id;
    }

    /**
     * @param task_id the task_id to set
     */
    public void setTask_id(String task_id) {
        this.task_id = task_id;
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

}
