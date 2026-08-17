package com.vishwa.saas_rbac_backend.controller;

import com.vishwa.saas_rbac_backend.dto.*;
import com.vishwa.saas_rbac_backend.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<ProjectResponse> create(@RequestBody ProjectRequest request) {
        return ResponseEntity.ok(projectService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> listAll() {
        return ResponseEntity.ok(projectService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getOne(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        projectService.delete(id);
        return ResponseEntity.noContent().build();
    }
}