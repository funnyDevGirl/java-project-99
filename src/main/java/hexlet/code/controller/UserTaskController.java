package hexlet.code.controller;

import hexlet.code.dto.tasks.TaskCreateDTO;
import hexlet.code.dto.tasks.TaskDTO;
import hexlet.code.dto.users.UserCreateDTO;
import hexlet.code.dto.users.UserDTO;
import hexlet.code.service.TaskService;
import hexlet.code.service.UserService;
import hexlet.code.service.UserTaskService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;

import hexlet.code.dto.userstasks.UserWithTaskDTO;


@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserTaskController {
    private final UserTaskService userTaskService;
    private final UserService userService;
    private final TaskService taskService;



    @PostMapping(path = "/create/user")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO create(@Valid @RequestBody UserCreateDTO userCreateDTO) {
        return userService.create(userCreateDTO);
    }

    @PostMapping(path = "/create/task")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDTO create(@Valid @RequestBody TaskCreateDTO taskCreateDTO) {
        return taskService.create(taskCreateDTO);
    }

    @PutMapping("/user/{userId}/task/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateUserAndTask(@PathVariable Long userId,
                                  @PathVariable Long taskId,
                                  @RequestBody UserWithTaskDTO userTaskDTO) {
        userTaskService.updateUserAndTask(userId, taskId, userTaskDTO);
    }
}
