package master.sheet.mastersheet.Service.interfaces;

import java.util.List;

import master.sheet.mastersheet.Entity.TaskEntity;

public interface TaskInterface {
    public List<TaskEntity> getAllTasks();
    public TaskEntity getTaskById(String Task_id)throws Exception;
    public TaskEntity updateTask(TaskEntity Task)throws Exception;
    public TaskEntity insertTask(TaskEntity Task);
    public boolean isExist(String task_id);
}
