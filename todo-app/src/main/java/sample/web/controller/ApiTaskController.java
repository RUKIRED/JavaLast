package sample.web.controller;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sample.common.dao.entity.Task;
import sample.common.service.TaskService;

@RestController
@RequestMapping("/api")
public class ApiTaskController {
  private final TaskService taskService;

  public ApiTaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  private String currentUser(HttpSession session) {
    return (String) session.getAttribute("username");
  }

  @GetMapping("/tasks")
  public ResponseEntity<List<Task>> list(HttpSession session) {
    String username = currentUser(session);
    if (username == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    List<Task> tasks = taskService.findByUsername(username, 1000, 0);
    return ResponseEntity.ok(tasks);
  }

  @GetMapping("/tasks/{id}")
  public ResponseEntity<Task> find(@PathVariable("id") Long id, HttpSession session) {
    String username = currentUser(session);
    if (username == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    Task task = taskService.findById(id);
    if (task == null || !username.equals(task.getUsername())) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(task);
  }

  @PostMapping("/tasks")
  public ResponseEntity<Void> create(@RequestBody Task task, HttpSession session) {
    String username = currentUser(session);
    if (username == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    taskService.create(
        username,
        task.getTitle(),
        task.getContent(),
        task.getName(),
        task.getStartDate(),
        task.getEndDate()
    );
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PutMapping("/tasks/{id}")
  public ResponseEntity<Void> update(@PathVariable("id") Long id,
      @RequestBody Task task, HttpSession session) {
    String username = currentUser(session);
    if (username == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    Task existing = taskService.findById(id);
    if (existing == null || !username.equals(existing.getUsername())) {
      return ResponseEntity.notFound().build();
    }
    taskService.update(
        id,
        task.getTitle(),
        task.getContent(),
        task.getName(),
        task.getStartDate(),
        task.getEndDate()
    );
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/tasks/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") Long id, HttpSession session) {
    String username = currentUser(session);
    if (username == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    Task existing = taskService.findById(id);
    if (existing == null || !username.equals(existing.getUsername())) {
      return ResponseEntity.notFound().build();
    }
    taskService.delete(id);
    return ResponseEntity.ok().build();
  }
}
