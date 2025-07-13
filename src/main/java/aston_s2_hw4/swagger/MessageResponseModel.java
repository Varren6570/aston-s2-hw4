package aston_s2_hw4.swagger;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Сообщение-ответ сервера")
public class MessageResponseModel {

    @Schema(description = "Текстовое сообщение", example = "User deleted successfully")
    private String message;

    public MessageResponseModel(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}