package hexlet.code.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.util.ModelGenerator;
import org.instancio.Instancio;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.tasks.TaskCreateDTO;
import hexlet.code.dto.tasks.TaskDTO;
import hexlet.code.dto.users.UserCreateDTO;
import hexlet.code.dto.userstasks.UserWithTaskDTO;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.mapper.UserMapper;
import hexlet.code.model.Task;
import hexlet.code.model.User;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

//import hexlet.code.dto.tasks.TaskUpdateDTO;
//import hexlet.code.dto.userstasks.UserTaskUpdateDTO;
//import org.openapitools.jackson.nullable.JsonNullable;


@SpringBootTest
@AutoConfigureMockMvc
public class UserTaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private ModelGenerator modelGenerator;

    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;
    private UserCreateDTO userCreateDTO;
    private TaskCreateDTO taskCreateDTO;
    private UserWithTaskDTO userTaskDTO;
    private User userModel;
    private TaskStatus testStatus;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity()).build();

        userModel = Instancio.of(modelGenerator.getUserModel()).create();
        userRepository.save(userModel);
        token = jwt().jwt(builder -> builder.subject(userModel.getEmail()));

        testStatus = Instancio.of(modelGenerator.getTaskStatusModel()).create();
        taskStatusRepository.save(testStatus);

        userCreateDTO = new UserCreateDTO();
        userCreateDTO.setFirstName("FirstName");
        userCreateDTO.setLastName("LastName");
        userCreateDTO.setEmail("test.user@example.com");
        userCreateDTO.setPassword("password");

        taskCreateDTO = new TaskCreateDTO();
        taskCreateDTO.setTitle("Task Title");
        taskCreateDTO.setContent("Task Content");
        taskCreateDTO.setStatus(testStatus.getSlug());
//        taskCreateDTO.setStatus("draft"); // так падает с 401 ("TaskStatus with slug draft not found")
        taskCreateDTO.setAssigneeId(userModel.getId());
    }

    @AfterEach
    public void clean() {
        taskRepository.deleteAll();
        taskStatusRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testCreateUser() throws Exception {

        mockMvc.perform(post("/api/create/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(userCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("FirstName"))
                .andExpect(jsonPath("$.email").value("test.user@example.com"));
    }

    @Test
    public void testCreateTask() throws Exception {

        mockMvc.perform(post("/api/create/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(taskCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Task Title"))
                .andExpect(jsonPath("$.content").value("Task Content"));
    }


    @Test
    public void testUpdateUserAndTask() throws Exception {
        UserCreateDTO testUser = new UserCreateDTO();
        testUser.setFirstName("FirstName");
        testUser.setLastName("LastName");
        testUser.setEmail("test.user@example.com");
        testUser.setPassword("password");

        User user = userMapper.map(testUser);
        userRepository.save(user); // user.id = 2;
        token = jwt().jwt(builder -> builder.subject(user.getEmail()));

        TaskCreateDTO testTask = new TaskCreateDTO();
        testTask.setTitle("Task Title");
        testTask.setContent("Task Content");
        testTask.setStatus(testStatus.getSlug());
        //testTask.setStatus("draft"); // так падает с 401 ("TaskStatus with slug draft not found")
        testTask.setAssigneeId(user.getId());

        Task task = taskMapper.map(testTask);
        taskRepository.save(task); // task.id = 1;

        // merge
        user.addTask(task);
        userRepository.save(user);

        // Если используется UserWithTaskDTO:
        userTaskDTO = new UserWithTaskDTO();
        userTaskDTO.setId(user.getId());
        userTaskDTO.setFirstName(user.getFirstName());
        userTaskDTO.setLastName("***");
        userTaskDTO.setEmail(user.getEmail());

        TaskDTO taskDTO = taskMapper.map(task);
        taskDTO.setTitle("***");
        userTaskDTO.setTaskDTO(taskDTO);

//        // Если используется UserTaskUpdateDTO:
//        UserTaskUpdateDTO userTaskUpdateDTO = new UserTaskUpdateDTO();
//        TaskUpdateDTO taskUpdateDTO = new TaskUpdateDTO();
//        taskUpdateDTO.setTitle(JsonNullable.of("***"));
//        userTaskUpdateDTO.setTaskUpdateDTO(taskUpdateDTO);
//        userTaskUpdateDTO.setLastName(JsonNullable.of("***"));



        mockMvc.perform(put("/api/user/2/task/1")
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(userTaskDTO)))
                        //.content(om.writeValueAsString(userTaskUpdateDTO)))
                .andExpect(status().isOk());

        // Проверка обновления пользователя
        User updatedUser = userRepository.findByIdWithEagerUpload(2L).orElseThrow();
        assertThat(updatedUser.getLastName()).isEqualTo("***");

        // Проверка обновления задачи
        Task updatedTask = taskRepository.findByIdWithEagerUpload(task.getId()).orElseThrow();
        assertThat(updatedTask.getName()).isEqualTo("***");

        // Проверка связи между пользователем и задачей
        assertThat(updatedUser.getTasks().size()).isEqualTo(1);
        assertThat(updatedTask.getAssignee()).isEqualTo(updatedUser);
    }
}
