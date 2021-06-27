package master.sheet.mastersheet.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import master.sheet.mastersheet.Entity.ProjectEntity;
import master.sheet.mastersheet.Repository.ProjectRepository;

@Service
public class ProjectService {
   @Autowired
   ProjectRepository projectRepository;
   public List<ProjectEntity> getAllProjects(){
    List<ProjectEntity> projectList = projectRepository.findAll();
    return projectList;
}
public ProjectEntity getProjectById(String project_id)throws Exception{
    Optional<ProjectEntity> projectList = projectRepository.findByProjectId(project_id);
    return projectList.get();
}
public ProjectEntity updateProject(ProjectEntity project)throws Exception{
    ProjectEntity pe = getProjectById(project.getProject_id());
    if (pe !=null){
        pe.setProject_name(project.getProject_name());
        pe.setStart_date(project.getStart_date());
        pe.setEnd_date(project.getEnd_date());
        pe.setRemarks(project.getRemarks());
        pe.setProject_manager(project.getProject_manager());
        pe.setProject_max_amount(project.getProject_max_amount());
        pe.setProject_type(project.getProject_type());
        pe.setProject_status(project.getProject_status());
        pe = projectRepository.save(pe);
    return pe;
    }
    else
    throw new Exception("project not Found");
}
public ProjectEntity insertProject(ProjectEntity project){
   ProjectEntity pe= projectRepository.save(project);
    return pe;
}
public boolean isExist(String project_id){
    return projectRepository.existsByProjectId(project_id);
}
}
