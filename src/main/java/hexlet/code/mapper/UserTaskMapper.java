package hexlet.code.mapper;

import hexlet.code.dto.tasks.TaskDTO;
import hexlet.code.dto.userstasks.UserWithTaskDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;


@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class UserTaskMapper {
    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private UserRepository userRepository;

    public User map(UserWithTaskDTO userTaskDTO) {
        User user = new User();
        user.setId(userTaskDTO.getId());
        user.setFirstName(userTaskDTO.getFirstName());
        user.setLastName(userTaskDTO.getLastName());
        user.setEmail(userTaskDTO.getEmail());
        user.getTasks().add(map(userTaskDTO.getTaskDTO()));
        return user;
    }

    public Task map(TaskDTO taskDTO) {
        Task task = new Task();
        task.setId(taskDTO.getId());
        task.setName(taskDTO.getTitle());
        task.setDescription(taskDTO.getContent());
        task.setTaskStatus(slugToTaskStatus(taskDTO.getStatus()));
        task.setAssignee(idToAssignee(taskDTO.getAssigneeId()));
        return task;
    }

    private TaskStatus slugToTaskStatus(String slug) {
        return taskStatusRepository.findBySlugWithEagerUpload(slug).orElseThrow(
                () -> new ResourceNotFoundException("TaskStatus with slug " + slug + " not found"));
    }

    private User idToAssignee(Long assigneeId) {
        return userRepository.findById(assigneeId).orElseThrow(
                () -> new ResourceNotFoundException("User with id " + assigneeId + " not found"));
    }
}
