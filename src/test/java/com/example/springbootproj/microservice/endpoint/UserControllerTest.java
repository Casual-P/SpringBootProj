package com.example.springbootproj.microservice.endpoint;

import com.example.springbootproj.dto.UserDto;
import com.example.springbootproj.entity.User;
import com.example.springbootproj.exception.UserNotFoundException;
import com.example.springbootproj.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class UserControllerTest {

    @Autowired MockMvc mockMvc;

    @Autowired UserRepository userRepository;

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:14-alpine"))
                    .withDatabaseName("testDB")
                    .withUsername("postgres")
                    .withPassword("123")
                    .withClasspathResourceMapping("/SQL/schema.sql", "/docker-entrypoint-initdb.d/create_schema.sql", BindMode.READ_WRITE);


    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        postgreSQLContainer.start();
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.liquibase.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.liquibase.user", postgreSQLContainer::getUsername);
        registry.add("spring.liquibase.password", postgreSQLContainer::getPassword);
    }


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @WithMockUser(username = "mock", roles = {"ADMIN", "USER"}, password = "mock")
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"/SQL/clear.sql"})
    @SuppressWarnings("unchecked")
    void getUsers() throws Exception {
        userRepository.save(User.builder().username("Test").email("test@test.test").password("$2y$10$IjpvhJZ9r/U9anrcRm38GOUtBP5nMBp/3jJAGAVVNgWXZfNQEegYO").build());
        ObjectMapper objectMapper = new ObjectMapper();
        MvcResult mvcResult = mockMvc.perform(get("/api/users/pageable/{page}", 0))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        List<UserDto> list = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), List.class);
        assertEquals(1, list.size());
    }

    @Test
    @WithMockUser(username = "mock", roles = {"ADMIN", "USER"}, password = "mock")
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"/SQL/clear.sql"})
    void createOne() throws Exception {
        UserDto dto = UserDto.builder().username("Test").email("test@test.test").password("$2y$10$IjpvhJZ9r/U9anrcRm38GOUtBP5nMBp/3jJAGAVVNgWXZfNQEegYO").build();
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writer().writeValueAsString(dto);
        MvcResult mvcResult = mockMvc.perform(put("/api/users/new").content(json).contentType(MediaType.APPLICATION_JSON).with(csrf()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        UserDto createdUser = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), UserDto.class);
        assertDoesNotThrow(() -> userRepository.findById(createdUser.getId()).orElseThrow());
    }

    @Test
    @WithMockUser(username = "mock", roles = {"ADMIN", "USER"}, password = "mock")
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"/SQL/clear.sql"})
    void updateOne() throws Exception {
        User tempUser = userRepository.save(User.builder().username("Test").email("test@test.test").password("$2y$10$IjpvhJZ9r/U9anrcRm38GOUtBP5nMBp/3jJAGAVVNgWXZfNQEegYO").build());
        UserDto userDto = UserDto.builder().id(tempUser.getId()).username("Updated").email("update@update.update").password(tempUser.getPassword()).build();
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writer().writeValueAsString(userDto);
        mockMvc.perform(patch("/api/users/update").content(json).contentType(MediaType.APPLICATION_JSON).with(csrf()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        User updatedUser = userRepository.findById(tempUser.getId()).orElseThrow();
        assertEquals(userDto.getEmail(), updatedUser.getEmail());
        assertEquals(userDto.getUsername(), updatedUser.getUsername());
    }

    @Test
    @WithMockUser(username = "mock", roles = {"ADMIN", "USER"}, password = "mock")
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"/SQL/clear.sql"})
    void deleteOneByEmail() throws Exception {
        User tempUser = userRepository.save(User.builder().username("Test").email("test@test.test").password("$2y$10$IjpvhJZ9r/U9anrcRm38GOUtBP5nMBp/3jJAGAVVNgWXZfNQEegYO").build());
        mockMvc.perform(delete("/api/users/delete/email").content(tempUser.getEmail()).contentType(MediaType.APPLICATION_JSON).with(csrf()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertThrows(UserNotFoundException.class, () -> userRepository.findById(tempUser.getId()).orElseThrow(UserNotFoundException::new));
    }

    @Test
    @WithMockUser(username = "mock", roles = {"ADMIN", "USER"}, password = "mock")
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"/SQL/clear.sql"})
    void deleteOneByUsername() throws Exception {
        User tempUser = userRepository.save(User.builder().username("Test").email("test@test.test").password("$2y$10$IjpvhJZ9r/U9anrcRm38GOUtBP5nMBp/3jJAGAVVNgWXZfNQEegYO").build());
        mockMvc.perform(delete("/api/users/delete/username").content(tempUser.getUsername()).contentType(MediaType.APPLICATION_JSON).with(csrf()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertThrows(UserNotFoundException.class, () -> userRepository.findById(tempUser.getId()).orElseThrow(UserNotFoundException::new));
    }
}