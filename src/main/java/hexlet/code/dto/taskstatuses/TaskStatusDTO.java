package hexlet.code.dto.taskstatuses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskStatusDTO {
    private long id;
    private String name;
    private String slug;
    private String createdAt;
}
