package aston_s2_hw4.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Пользователь")
@Data
public class UserDto {
  @Schema(description = "ID пользователя", example = "1")
  private Long id;

  @Schema(description = "Имя", example = "Евгений")
  private String name;

  @Schema(description = "Электронная почта", example = "mail@mail.com")
  private String email;

  @Schema(description = "Возраст", example = "27")
  private int age;
}
