package aston_s2_hw4.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Запрос на создание пользователя")
public class UserCreateRequest {

    @Schema(description = "Имя", example = "Евгений")
    private String name;
    @Schema(description = "Электронная почта", example = "mail@mail.com")
    private String email;
    @Schema(description = "Возраст", example = "27")
    private int age;

    public UserCreateRequest() {
    }

    public UserCreateRequest(String name, String email, int age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}