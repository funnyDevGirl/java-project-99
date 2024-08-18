package hexlet.code.dto.userstasks;

import hexlet.code.dto.tasks.TaskDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserWithTaskDTO {
    private long id;
    private String email;
    private String firstName;
    private String lastName;
    private TaskDTO taskDTO;
}
