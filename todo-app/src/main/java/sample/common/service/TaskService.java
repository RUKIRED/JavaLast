package sample.common.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.common.dao.entity.Task;
import sample.common.dao.mapper.TaskMapper;

@Service
public class TaskService {
  private final TaskMapper taskMapper;

  public TaskService(TaskMapper taskMapper) {
    this.taskMapper = taskMapper;
  }

  public List<Task> findByUsername(String username, int limit, int offset) {
    return taskMapper.findByUsername(username, limit, offset);
  }

  public int countByUsername(String username) {
    return taskMapper.countByUsername(username);
  }

  public Task findById(Long id) {
    return taskMapper.findById(id);
  }

  @Transactional
  public void create(String username, String title, String content, String name,
      LocalDate startDate, LocalDate endDate) {
    Task task = new Task();
    task.setUsername(username);
    task.setTitle(title);
    task.setContent(content);
    task.setName(name);
    task.setStartDate(startDate);
    task.setEndDate(endDate);
    taskMapper.insert(task);
  }

  @Transactional
  public boolean update(Long id, String title, String content, String name,
      LocalDate startDate, LocalDate endDate) {
    Task task = taskMapper.findById(id);
    if (task == null) {
      return false;
    }
    task.setTitle(title);
    task.setContent(content);
    task.setName(name);
    task.setStartDate(startDate);
    task.setEndDate(endDate);
    return taskMapper.update(task) > 0;
  }

  @Transactional
  public void delete(Long id) {
    taskMapper.delete(id);
  }
}
