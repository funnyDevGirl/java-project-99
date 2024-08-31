package hexlet.code.service;

import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.model.Task;
import hexlet.code.model.User;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import hexlet.code.dto.userstasks.UserWithTaskDTO;



@Service
@AllArgsConstructor
public class UserTaskService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;


    public void updateUserAndTask(Long userId, Long taskId, UserWithTaskDTO userTaskDTO) {
        User user = userRepository.findByIdWithEagerUpload(userId).orElseThrow(
                () -> new ResourceNotFoundException("User With Id: " + userId + " Not Found"));

        Task task = taskRepository.findByIdWithEagerUpload(taskId).orElseThrow(
                () -> new ResourceNotFoundException("Task With Id: " + taskId + " Not Found"));

        user.setLastName(userTaskDTO.getLastName());
        task.setName(userTaskDTO.getTaskDTO().getTitle());

        user.addTask(task); // MERGE
        userRepository.save(user);

        ResponseEntity.ok().build();
    }
}
