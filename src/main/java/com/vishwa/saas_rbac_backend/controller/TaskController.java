package com.vishwa.saas_rbac_backend.controller;

import com.vishwa.saas_rbac_backend.dto.*;
import com.vishwa.saas_rbac_backend.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> create(@RequestBody TaskRequest request) {
        return ResponseEntity.ok(taskService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> listAll() {
        return ResponseEntity.ok(taskService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getOne(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskResponse> updateStatus(@PathVariable Long id, @RequestBody TaskStatusUpdateRequest request) {
        return ResponseEntity.ok(taskService.updateStatus(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}