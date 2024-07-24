package hexlet.code.service;

import hexlet.code.dto.tasks.TaskCreateDTO;
import hexlet.code.dto.tasks.TaskDTO;
import hexlet.code.dto.tasks.TaskFilterDTO;
import hexlet.code.dto.tasks.TaskUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.specification.TaskSpecification;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.NoSuchElementException;


@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = {"tasks"})
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final TaskSpecification taskSpecification;
    private final UserRepository userRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final LabelRepository labelRepository;

    //@CacheEvict(allEntries = true)
    //@CacheEvict(cacheNames = "tasks", key = "#task.id")
    public TaskDTO create(TaskCreateDTO taskCreateDTO) {

        // TODO: УБРАТЬ try-catch
        // TODO: пусть исключение обрабатывается в ControllerAdvice
        try {
            var task = taskMapper.map(taskCreateDTO);

            //записываю таску автору и автора таске:
            var assignee = task.getAssignee();
            if (assignee != null) {
                assignee.addTask(task);
            }

            //записываю таску статусу и статус таске:
            var status = task.getTaskStatus();
            status.addTask(task);

            //записываю таску в метку и метку в таску:
            var labels = task.getLabels();
            labels.forEach(label -> label.addTask(task));

            taskRepository.save(task);
            return taskMapper.map(task);

        } catch (NoSuchElementException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    public List<TaskDTO> getAll() {
        return taskRepository.findAll().stream()
                .map(taskMapper::map).toList();
    }

    public List<TaskDTO> getAll(TaskFilterDTO taskFilterDTO) {
        var filter = taskSpecification.build(taskFilterDTO);
        var tasks = taskRepository.findAll(filter);

        return tasks.stream()
                .map(taskMapper::map)
                .toList();
    }

    //@Cacheable(cacheNames = "tasks", key = "#id")
    //@Cacheable(cacheNames = "tasks", key = "#id != null ? #id : '-1'")
    public TaskDTO findById(Long id) {
        // вместо findById(id)
        var task = taskRepository.findByIdWithLazyFields(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task With Id: " + id + " Not Found"));
        return taskMapper.map(task);
    }

    //@Cacheable("tasks")
    //@CacheEvict(allEntries = true)
    //@CacheEvict(cacheNames = "tasks", key = "#task.id")
    public TaskDTO update(TaskUpdateDTO taskUpdateDTO, Long id) {

        try {
            // вместо findById(id)
            var task = taskRepository.findByIdWithLazyFields(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Task With Id: " + id + " Not Found"));

            taskMapper.update(taskUpdateDTO, task);

            // TODO: выходит, ЭТО ТОЖЕ НЕ НУЖНО?

//            //записываю таску автору и автора таске:
//            var assignee = task.getAssignee();
//            if (assignee != null) {
//                userRepository.findByEmail(assignee.getEmail())
//                    .ifPresent(user -> user.addTask(task));
//            }
//
//            //записываю таску статусу и статус таске:
//            // вместо findBySlug(id)
//            taskStatusRepository.findBySlugWithTasks(task.getTaskStatus().getSlug())
//                    .ifPresent(status -> status.addTask(task));
//
//            //записываю таску в метки:
//            Set<Long> labelIds = task.getLabels().stream()
//                    .map(Label::getId)
//                    .collect(Collectors.toSet());
//
//            Set<Label> labels = labelRepository.findByIdIn(labelIds);
//            labels.forEach(label -> label.addTask(task));

            taskRepository.save(task);
            return taskMapper.map(task);

        } catch (NoSuchElementException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    public void delete(Long id) {
        taskRepository.deleteById(id);
    }
}
