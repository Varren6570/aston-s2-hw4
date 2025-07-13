package aston_s2_hw4.swagger;


import aston_s2_hw4.dto.UserDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

@Schema(description = "Пользователь с гиперссылками HATEOAS")
public class UserResponseModel {
    private UserDto user;

    @Schema(description = "HATEOAS ссылки")
    private Map<String, LinkInfo> _links;

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public Map<String, LinkInfo> get_links() {
        return _links;
    }

    public void set_links(Map<String, LinkInfo> _links) {
        this._links = _links;
    }
}
