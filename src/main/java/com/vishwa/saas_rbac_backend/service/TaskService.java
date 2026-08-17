package com.vishwa.saas_rbac_backend.service;

import com.vishwa.saas_rbac_backend.dto.*;
import com.vishwa.saas_rbac_backend.entity.*;
import com.vishwa.saas_rbac_backend.exception.*;
import com.vishwa.saas_rbac_backend.repository.*;
import com.vishwa.saas_rbac_backend.util.TenantContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public TaskResponse create(TaskRequest request) {
        Long orgId = TenantContext.getOrgId();
        String email = TenantContext.getEmail();

        User creator = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Project project = projectRepository.findByIdAndOrganizationId(request.getProjectId(), orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        Task task = new Task();
        task.setOrganization(project.getOrganization());
        task.setProject(project);
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(TaskStatus.TODO);
        task.setCreatedBy(creator);

        if (request.getAssignedToUserId() != null) {
            User assignee = userRepository.findById(request.getAssignedToUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Assigned user not found"));
            task.setAssignedTo(assignee);
        }

        taskRepository.save(task);
        return toResponse(task);
    }

    public List<TaskResponse> listAll() {
        Long orgId = TenantContext.getOrgId();
        return taskRepository.findByOrganizationId(orgId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public TaskResponse getOne(Long id) {
        Long orgId = TenantContext.getOrgId();
        Task task = taskRepository.findByIdAndOrganizationId(id, orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        return toResponse(task);
    }

    public TaskResponse updateStatus(Long id, TaskStatusUpdateRequest request) {
        Task task = getOwnedTask(id);
        task.setStatus(TaskStatus.valueOf(request.getStatus()));
        taskRepository.save(task);
        return toResponse(task);
    }

    public void delete(Long id) {
        Task task = getOwnedTask(id);
        taskRepository.delete(task);
    }

    private Task getOwnedTask(Long id) {
        Long orgId = TenantContext.getOrgId();
        String role = TenantContext.getRole();
        String email = TenantContext.getEmail();

        Task task = taskRepository.findByIdAndOrganizationId(id, orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (role.equals("ADMIN")) {
            return task;
        }

        boolean isCreator = task.getCreatedBy().getEmail().equals(email);
        boolean isAssignee = task.getAssignedTo() != null && task.getAssignedTo().getEmail().equals(email);

        if (!isCreator && !isAssignee) {
            throw new AccessDeniedException("You can only modify tasks you created or are assigned to");
        }

        return task;
    }

    private TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus().name(),
                task.getProject().getName(),
                task.getCreatedBy().getFullName(),
                task.getAssignedTo() != null ? task.getAssignedTo().getFullName() : null,
                task.getCreatedAt()
        );
    }
}