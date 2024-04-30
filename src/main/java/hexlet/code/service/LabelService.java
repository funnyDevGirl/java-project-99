package hexlet.code.service;

import hexlet.code.dto.labels.LabelCreateDTO;
import hexlet.code.dto.labels.LabelDTO;
import hexlet.code.dto.labels.LabelUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.repository.LabelRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@AllArgsConstructor
public class LabelService {

    private final LabelRepository labelRepository;
    private final LabelMapper labelMapper;


    public LabelDTO create(LabelCreateDTO labelCreateDTO) {

        var label = labelMapper.map(labelCreateDTO);

//        //записываю метку в таски:
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

    public LabelDTO findById(Long id) {

        var label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label With Id: " + id + " Not Found"));

        return labelMapper.map(label);
    }

    public LabelDTO update(LabelUpdateDTO labelUpdateDTO, Long id) {

        var label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label With Id: " + id + " Not Found"));

        labelMapper.update(labelUpdateDTO, label);
        labelRepository.save(label);

        return labelMapper.map(label);
    }

    public void delete(Long id) throws Exception {

        labelRepository.deleteById(id);
    }
}
