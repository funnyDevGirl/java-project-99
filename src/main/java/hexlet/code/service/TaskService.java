package hexlet.code.service;

import hexlet.code.dto.tasks.TaskCreateDTO;
import hexlet.code.dto.tasks.TaskDTO;
import hexlet.code.dto.tasks.TaskUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskMapper taskMapper;

    public TaskDTO create(TaskCreateDTO taskCreateDTO) {
        var task = taskMapper.map(taskCreateDTO);
        taskRepository.save(task);
        return taskMapper.map(task);
    }

    public List<TaskDTO> getAll() {
        var taskStatuses = taskRepository.findAll();
        return taskStatuses.stream()
                .map(taskMapper::map)
                .toList();
    }

    public TaskDTO findById(Long id) {
        var taskStatus = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task With Id: " + id + " Not Found"));
        return taskMapper.map(taskStatus);
    }

    public TaskDTO update(TaskUpdateDTO taskUpdateDTO, Long id) {
        var taskStatus = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task With Id: " + id + " Not Found"));
        taskMapper.update(taskUpdateDTO, taskStatus);
        taskRepository.save(taskStatus);
        return taskMapper.map(taskStatus);
    }

    public void delete(Long id) {
        taskRepository.deleteById(id);
    }
}
