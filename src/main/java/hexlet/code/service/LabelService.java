package hexlet.code.service;

import hexlet.code.dto.labels.LabelCreateDTO;
import hexlet.code.dto.labels.LabelDTO;
import hexlet.code.dto.labels.LabelUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.model.Task;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = {"labels"})
public class LabelService {

    private final LabelRepository labelRepository;
    private final LabelMapper labelMapper;
    private final TaskRepository taskRepository;

    //@CacheEvict(allEntries = true)
    //@CacheEvict(cacheNames = "labels", key = "#label.id")
    public LabelDTO create(LabelCreateDTO labelCreateDTO) {

        var label = labelMapper.map(labelCreateDTO);

        //записываю метку в таски:
//        var tasks = label.getTasks();
//        tasks.forEach(task -> task.addLabel(label));

        labelRepository.save(label);

        return labelMapper.map(label);
    }


    public List<LabelDTO> getAll() {

        var tasks = labelRepository.findAll();

        return tasks.stream()
                .map(labelMapper::map)
                .toList();
    }


//    @Cacheable(cacheNames = "labels", key = "#id")
    public LabelDTO findById(Long id) {

    // захожу в сервисный метод findById(), пишу об этом в лог:
        log.info("Finding label by id: {}", id);
        var label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label With Id: " + id + " Not Found"));

        return labelMapper.map(label);
    }


//    @Cacheable(cacheNames = "labels", key = "#id")
//    public LabelDTO findById(Long id) {
//
//        // захожу в сервисный метод findById(), пишу об этом в лог:
//        log.info("Finding label by id: {}", id);
//
////        var label = labelRepository.findById(id)
////                .orElseThrow(() -> new ResourceNotFoundException("Label With Id: " + id + " Not Found"));
//
//        var label = labelRepository.findByIdWithTasks(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Label With Id: " + id + " Not Found"));
//
//        log.info("Label found in DB {}", id);
//
//        return labelMapper.map(label);
//    }

    //@Cacheable(cacheNames = "labels", key = "#id") //IllegalArgumentException
    //@CacheEvict(allEntries = true)
    //@CacheEvict(cacheNames = "labels", key = "#label.id")
    public LabelDTO update(LabelUpdateDTO labelUpdateDTO, Long id) {

        var label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label With Id: " + id + " Not Found"));

//        var label = labelRepository.findByIdWithTasks(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Label With Id: " + id + " Not Found"));

        labelMapper.update(labelUpdateDTO, label);

        // записываю метку в таску:
        Set<Long> tasksIds = label.getTasks().stream()
                .map(Task::getId)
                .collect(Collectors.toSet());

        Set<Task> tasks = taskRepository.findByIdIn(tasksIds);
        tasks.forEach(task -> task.addLabel(label));

        labelRepository.save(label);
        return labelMapper.map(label);
    }

    public void delete(Long id) {

        var label = labelRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Label with id " + id + " not found"));


        // Падает с ResourseFound..404:
//        var label = labelRepository.findByIdWithTasks(id).orElseThrow(() ->
//                new ResourceNotFoundException("Label with id " + id + " not found"));

        //удаляю метку из такси:
        label.getTasks().forEach(task -> task.removeLabel(label));

        //удаляю метку из репо:
        labelRepository.deleteById(id);
    }
}
