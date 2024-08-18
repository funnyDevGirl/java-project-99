package hexlet.code.controller;

//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
//import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import hexlet.code.dto.tasks.TaskUpdateDTO;
//import hexlet.code.dto.userstasks.UserTaskUpdateDTO;
//import hexlet.code.repository.TaskStatusRepository;
//import hexlet.code.util.ModelGenerator;
//import org.assertj.core.api.Assertions;
//import org.instancio.Instancio;
//import org.json.JSONException;
//import org.openapitools.jackson.nullable.JsonNullable;
//import org.skyscreamer.jsonassert.JSONAssert;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import hexlet.code.dto.tasks.TaskCreateDTO;
//import hexlet.code.dto.tasks.TaskDTO;
//import hexlet.code.dto.users.UserCreateDTO;
//import hexlet.code.dto.users.UserDTO;
//import hexlet.code.dto.userstasks.UserWithTaskDTO;
//import hexlet.code.mapper.TaskMapper;
//import hexlet.code.mapper.UserMapper;
//import hexlet.code.model.Task;
//import hexlet.code.model.User;
//import hexlet.code.repository.TaskRepository;
//import hexlet.code.repository.UserRepository;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.BeforeEach;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.context.WebApplicationContext;
//
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@RequestMapping("/api")
//public class UserTaskControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper om;
//
//    @Autowired
//    private WebApplicationContext wac;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private TaskRepository taskRepository;
//
//    @Autowired
//    private TaskStatusRepository taskStatusRepository;
//
//    @Autowired
//    private UserMapper userMapper;
//
//    @Autowired
//    private TaskMapper taskMapper;
//
//    @Autowired
//    private ModelGenerator modelGenerator;
//
//    private UserCreateDTO userCreateDTO;
//    private TaskCreateDTO taskCreateDTO;
//    private UserWithTaskDTO userTaskDTO;
//    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;
//    private Task taskModel;
//    private User userModel;
//
//    @BeforeEach
//    public void setup() {
//        mockMvc = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity()).build();
//
//        userCreateDTO = new UserCreateDTO();
//        userCreateDTO.setFirstName("FirstName");
//        userCreateDTO.setLastName("LastName");
//        userCreateDTO.setEmail("test.user@example.com");
//        userCreateDTO.setPassword("password");
//
//        taskCreateDTO = new TaskCreateDTO();
//        taskCreateDTO.setTitle("Task Title");
//        taskCreateDTO.setContent("Task Content");
//        taskCreateDTO.setStatus("draft");
//
//        userModel = Instancio.of(modelGenerator.getUserModel()).create();
//        userRepository.save(userModel);
//        token = jwt().jwt(builder -> builder.subject(userModel.getEmail()));
////
////        TaskStatus testStatus = Instancio.of(modelGenerator.getTaskStatusModel()).create();
////        taskStatusRepository.save(testStatus);
////
////        taskModel = Instancio.of(modelGenerator.getTaskModel()).create();
////        taskModel.setAssignee(userModel);
////        taskModel.setTaskStatus(testStatus);
////        taskRepository.save(taskModel);
//    }
//
//    @AfterEach
//    public void clean() {
//        taskRepository.deleteAll();
//        userRepository.deleteAll();
//    }
//
//    // ПАДАЕТ С 404
//    @Test
//    public void testCreateUser() throws Exception {
//        //mockMvc.perform(post("/create/user")
//        mockMvc.perform(get("/create/user")
//                        .with(token)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(om.writeValueAsString(userCreateDTO)));
////                .andExpect(status().isCreated())
////                .andExpect((ResultMatcher) jsonPath("$.firstName").value("FirstName"))
////                .andExpect((ResultMatcher) jsonPath("$.lastName").value("LastName"));
//        var user = userRepository.findByEmailWithEagerUpload(userCreateDTO.getEmail()).orElseThrow();
//
//        Assertions.assertThat(user).isNotNull();
//        Assertions.assertThat(user.getFirstName()).isEqualTo(userCreateDTO.getFirstName());
//        Assertions.assertThat(user.getLastName()).isEqualTo(userCreateDTO.getLastName());
//    }
//
////    @Test
////    public void testCreateTask() throws Exception {
////        mockMvc.perform(post("/create/task")
////                        .contentType(MediaType.APPLICATION_JSON)
////                        .content(om.writeValueAsString(taskCreateDTO)))
////                .andExpect(status().isCreated())
////                .andExpect(jsonPath("$.title").value("Task Title"))
////                .andExpect(jsonPath("$.content").value("Task Content"));
////    }
//
//    @Test
//    public void testJSON() throws JSONException, JsonProcessingException {
//        UserCreateDTO testUser = new UserCreateDTO();
//        testUser.setFirstName("FirstName");
//        testUser.setLastName("LastName");
//        testUser.setEmail("test.user@example.com");
//        testUser.setPassword("password");
//
//        User user = userMapper.map(testUser);
//        userRepository.save(user); // user.id = 2;
//        token = jwt().jwt(builder -> builder.subject(user.getEmail()));
//        System.out.println("1 SAVE USER: " + user);
//
//        TaskCreateDTO testTask = new TaskCreateDTO();
//        testTask.setTitle("Task Title");
//        testTask.setContent("Task Content");
//        testTask.setStatus("draft");
//        testTask.setAssigneeId(user.getId());
//
//        Task task = taskMapper.map(testTask);
//        taskRepository.save(task); // task.id = 1;
//        System.out.println("SAVE TASK: " + task);
//        // merge
//        user.addTask(task);
//        userRepository.save(user);
//        System.out.println("SAVE + MERGE USERTASK: " + user);
//
//        userTaskDTO = new UserWithTaskDTO();
//        UserDTO userDTO = userMapper.map(user);
//        userTaskDTO.setId(userDTO.getId());
//        userTaskDTO.setFirstName(userDTO.getFirstName());
//        userTaskDTO.setLastName("***");
//        userTaskDTO.setEmail(userDTO.getEmail());
//
//        TaskDTO taskDTO = taskMapper.map(task);
//        taskDTO.setTitle("***");
//        userTaskDTO.setTaskDTO(taskDTO);
//
//        String jsonString = om.writeValueAsString(userTaskDTO); // JSON
//        // -> {"id":2,"email":"test.user@example.com","firstName":"FirstName",
//        // "lastName":"***","taskDTO":{"id":1,"createdAt":"2024-08-18","title":"***",
//        // "content":"Task Content","status":"draft","taskLabelIds":[],"assignee_id":2}}
//
//        String expectedJson = "{"
//                + "\"id\": 2,"
//                + "\"email\": \"test.user@example.com\","
//                + "\"firstName\": \"FirstName\","
//                + "\"lastName\": \"***\","
//                + "\"taskDTO\": {"
//                + "    \"id\": 1,"
//                + "    \"createdAt\": \"2024-08-18\","
//                + "    \"title\": \"***\","
//                + "    \"content\": \"Task Content\","
//                + "    \"status\": \"draft\","
//                + "    \"taskLabelIds\": [ ],"
//                + "    \"assignee_id\": 2"
//                + "}}";
//
//        JSONAssert.assertEquals(expectedJson, jsonString, false);
//    }
//
//    // ПАДАЕТ С 404
//    @Test
//    public void testUpdateUserAndTask() throws Exception {
//        UserCreateDTO testUser = new UserCreateDTO();
//        testUser.setFirstName("FirstName");
//        testUser.setLastName("LastName");
//        testUser.setEmail("test.user@example.com");
//        testUser.setPassword("password");
//
//        User user = userMapper.map(testUser);
//        userRepository.save(user); // user.id = 2;
//        token = jwt().jwt(builder -> builder.subject(user.getEmail()));
//
//        TaskCreateDTO testTask = new TaskCreateDTO();
//        testTask.setTitle("Task Title");
//        testTask.setContent("Task Content");
//        testTask.setStatus("draft");
//        testTask.setAssigneeId(user.getId());
//
//        Task task = taskMapper.map(testTask);
//        taskRepository.save(task); // task.id = 1;
//
//        // merge
//        user.addTask(task);
//        userRepository.save(user);
//
////        // Если используется UserWithTaskDTO:
////        userTaskDTO = new UserWithTaskDTO();
////        userTaskDTO.setId(user.getId());
////        userTaskDTO.setFirstName(user.getFirstName());
////        userTaskDTO.setLastName("***");
////        userTaskDTO.setEmail(user.getEmail());
////
////        TaskDTO taskDTO = taskMapper.map(task);
////        taskDTO.setTitle("***");
////        userTaskDTO.setTaskDTO(taskDTO);
//
//        // Если используется UserTaskUpdateDTO:
//        UserTaskUpdateDTO userTaskUpdateDTO = new UserTaskUpdateDTO();
//        TaskUpdateDTO taskUpdateDTO = new TaskUpdateDTO();
//        taskUpdateDTO.setTitle(JsonNullable.of("***"));
//        userTaskUpdateDTO.setTaskUpdateDTO(taskUpdateDTO);
//        userTaskUpdateDTO.setLastName(JsonNullable.of("***"));
//
//
//
//        mockMvc.perform(put("/user/2/task/1")
//                        .with(token)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        //.content(om.writeValueAsString(userTaskDTO)));
//                        .content(om.writeValueAsString(userTaskUpdateDTO)));
//                //.andExpect(status().isOk());
//
//        // Проверка обновления пользователя
//        User updatedUser = userRepository.findByIdWithEagerUpload(2L).orElseThrow();
//        assertThat(updatedUser.getLastName()).isEqualTo("***");
//
//        // Проверка обновления задачи
//        Task updatedTask = taskRepository.findByIdWithEagerUpload(task.getId()).orElseThrow();
//        assertThat(updatedTask.getName()).isEqualTo("***");
//
//        // Проверка связи между пользователем и задачей
//        assertThat(updatedUser.getTasks().size()).isEqualTo(1);
//        assertThat(updatedTask.getAssignee()).isEqualTo(updatedUser);
//    }
//}
