package master.sheet.mastersheet.Service.interfaces;

import java.util.List;

import master.sheet.mastersheet.Entity.ProjectEntity;

public interface ProjectInterface {
    public List<ProjectEntity> getAllProjects();
    public ProjectEntity getProjectById(String project_id)throws Exception;
    public ProjectEntity updateProject(ProjectEntity project)throws Exception;
    public ProjectEntity insertProject(ProjectEntity project);
    public boolean isExist(String project_id);
}
