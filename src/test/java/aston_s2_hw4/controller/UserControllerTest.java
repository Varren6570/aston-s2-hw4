package aston_s2_hw4.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import aston_s2_hw4.dto.UserCreateRequest;
import aston_s2_hw4.dto.UserDto;
import aston_s2_hw4.model.User;
import aston_s2_hw4.service.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

/**
 * Тесты для UserController
 */
@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserServiceImpl userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private UserDto userDto;

    @BeforeEach
    public void init() {
        user = User.builder()
                .name("name")
                .email("mail")
                .age(12)
                .createdAt(LocalDateTime.now())
                .build();

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("name");
        userDto.setEmail("mail");
        userDto.setAge(12);
    }

    /**
     * Проверяет успешное получение списка пользователей
     */
    @Test
    void getAllUsers_ReturnsStatusOk() throws Exception {
        // Given
        when(userService.getAllUsers()).thenReturn(List.of());

        // When / Then
        mockMvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    /**
     * Проверяет успешное создание пользователя
     */
    @Test
    void addUser_ReturnsStatusCreated() throws Exception {
        // Given
        when(userService.addUser(ArgumentMatchers.any())).thenReturn(userDto);

        // When / Then
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(userDto.getName()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()))
                .andExpect(jsonPath("$.age").value(userDto.getAge()));
    }

    /**
     * Проверяет успешное получение пользователя по ID
     */
    @Test
    void getUserById_ReturnStatusOk() throws Exception {
        // Given
        when(userService.getUser(1L)).thenReturn(userDto);

        // When
        ResultActions response = mockMvc.perform(get("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", CoreMatchers.is(userDto.getName())))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()))
                .andExpect(jsonPath("$.age").value(userDto.getAge()));
    }

    /**
     * Проверяет успешное обновление пользователя
     */
    @Test
    void updateUser_ReturnsStatusOk() throws Exception {
        // Given
        UserCreateRequest userCreateRequest = new UserCreateRequest("name", "mail", 12);
        when(userService.updateUser(1L, userCreateRequest)).thenReturn(userDto);

        // When
        ResultActions response = mockMvc.perform(patch("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)));

        // Then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", CoreMatchers.is(userDto.getName())))
                .andExpect(jsonPath("$.email", CoreMatchers.is(userDto.getEmail())))
                .andExpect(jsonPath("$.age").value(userDto.getAge()));
    }

    /**
     * Проверяет успешное удаление пользователя
     */
    @Test
    void deleteUser_ReturnsStatusOk() throws Exception {
        // Given
        doNothing().when(userService).deleteUser(1L);

        // When
        ResultActions response = mockMvc.perform(delete("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Пользователь удален"));
    }
}
