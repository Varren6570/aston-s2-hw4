package aston_s2_hw4.swagger;

import aston_s2_hw4.dto.UserDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

@Schema(description = "Пользователь с HATEOAS-ссылками")
public class UserWithLinks {

    @Schema(description = "Данные пользователя")
    private UserDto user;

    @Schema(description = "Ссылки HATEOAS для данного пользователя")
    private Map<String, LinkInfo> _links;

    public UserWithLinks(UserDto user, Map<String, LinkInfo> links) {
        this.user = user;
        this._links = links;
    }

    public UserDto getUser() {
        return user;
    }

    public Map<String, LinkInfo> get_links() {
        return _links;
    }
}
