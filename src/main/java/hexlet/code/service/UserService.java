package hexlet.code.service;

import hexlet.code.dto.users.UserCreateDTO;
import hexlet.code.dto.users.UserDTO;
import hexlet.code.dto.users.UserUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.UserMapper;
import hexlet.code.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    public UserDTO create(UserCreateDTO userCreateDTO) {
        var user = userMapper.map(userCreateDTO);
        log.debug("User before save: {}", user);
        userRepository.save(user);
        log.debug("User saved.\nUser: {}", user);
        log.info("Return created User");
        return userMapper.map(user);
    }

    public List<UserDTO> getAll() {
        var users = userRepository.findAll();

        return users.stream()
                .map(userMapper::map)
                .toList();
    }

    public UserDTO findById(Long id) {
        var user = userRepository.findByIdWithEagerUpload(id)
                .orElseThrow(() -> new ResourceNotFoundException("User With Id: " + id + " Not Found"));

        return userMapper.map(user);
    }

    public UserDTO update(UserUpdateDTO userUpdateDTO, Long id) {
        var user = userRepository.findByIdWithEagerUpload(id)
                .orElseThrow(() -> new ResourceNotFoundException("User With Id: " + id + " Not Found"));

        userMapper.update(userUpdateDTO, user);
        userRepository.save(user);

        return userMapper.map(user);
    }

    public void delete(Long id) throws Exception {
        userRepository.deleteById(id);
    }

    public String generateUsersTable() {
        List<UserDTO> users = getAll();
        StringBuilder htmlBuilder = new StringBuilder();

        htmlBuilder.append("<table border='1'>");
        htmlBuilder.append("<tr><th>First Name</th><th>Last Name</th><th>Email</th></tr>");

        for (UserDTO user : users) {
            htmlBuilder.append("<tr>")
                    .append("<td>").append(user.getFirstName()).append("</td>")
                    .append("<td>").append(user.getLastName()).append("</td>")
                    .append("<td>").append(user.getEmail()).append("</td>")
                    .append("</tr>");
        }
        htmlBuilder.append("</table>");

        return htmlBuilder.toString(); // Возвращаю HTML строку
    }
}
