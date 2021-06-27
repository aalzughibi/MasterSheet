package master.sheet.mastersheet.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import master.sheet.mastersheet.Entity.TaskEntity;
import master.sheet.mastersheet.Repository.TaskRepository;

@Service
public class TaskService {
    @Autowired
    TaskRepository taskRepository;
    
    public List<TaskEntity> getAllTasks(){
        List<TaskEntity> TaskList = taskRepository.findAll();
        return TaskList;
    }
    public TaskEntity getTaskById(String Task_id)throws Exception{
        Optional<TaskEntity> TaskList = taskRepository.findByTaskId(Task_id);
        return TaskList.get();
    }
    public TaskEntity updateTask(TaskEntity Task)throws Exception{
        TaskEntity pe = getTaskById(Task.getTask_id());
        if (pe !=null){
            pe.setItem_id(Task.getItem_id());
            pe.setTask_description(Task.getTask_description());
            pe = taskRepository.save(pe);
        return pe;
        }
        else
        throw new Exception("Task not Found");
    }
    public TaskEntity insertTask(TaskEntity Task){
       TaskEntity pe= taskRepository.save(Task);
        return pe;
    }
    public boolean isExist(String task_id){
        return taskRepository.existsByTaskId(task_id);
    }
}
