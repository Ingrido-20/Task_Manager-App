package com.ingrid.Task.manager.controller;

import com.ingrid.Task.manager.model.Task;
import com.ingrid.Task.manager.repository.TaskRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "http://localhost:4200")
public class TaskController {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @PostMapping
    public Task createTask(@Valid @RequestBody Task task) {
        // Set default status if not provided
        if (task.getStatus() == null || task.getStatus().isEmpty()) {
            task.setStatus("todo");
        }
        if (task.getPriority() == null || task.getPriority().isEmpty()) {
            task.setPriority("medium");
        }
        return taskRepository.save(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @Valid @RequestBody Task taskDetails) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Task existingTask = optionalTask.get();
        existingTask.setTitle(taskDetails.getTitle());
        existingTask.setDescription(taskDetails.getDescription());
        existingTask.setPriority(taskDetails.getPriority());
        existingTask.setStatus(taskDetails.getStatus());
        existingTask.setDueDate(taskDetails.getDueDate());

        Task updatedTask = taskRepository.save(existingTask);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        taskRepository.delete(optionalTask.get());
        return ResponseEntity.noContent().build();
    }
}
