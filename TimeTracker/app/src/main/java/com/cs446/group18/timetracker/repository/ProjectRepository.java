package com.cs446.group18.timetracker.repository;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.cs446.group18.timetracker.dao.ProjectDao;
import com.cs446.group18.timetracker.entity.Project;

import java.util.List;

public class ProjectRepository {
    private ProjectDao projectDao;
    private static volatile ProjectRepository instance = null;

    public ProjectRepository(ProjectDao projectDao) {
        this.projectDao = projectDao;
    }

    public static ProjectRepository getInstance(ProjectDao projectDao) {
        if (instance == null) {
            synchronized (ProjectRepository.class) {
                if (instance == null)
                    instance = new ProjectRepository(projectDao);
            }
        }
        return instance;
    }

    public LiveData<List<Project>> getProjects() {
        return projectDao.getProjects();
    }

    public void createProject(Project project) {
        AsyncTask.execute(() -> projectDao.insert(project));
    }
}
