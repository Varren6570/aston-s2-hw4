package aston_s2_hw4.swagger;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;

@Schema(description = "Коллекция пользователей с HATEOAS-ссылками")
public class UserListResponseModel {

    @Schema(description = "Список пользователей с их ссылками")
    private List<UserWithLinks> _embedded;

    @Schema(description = "Ссылки на действия над коллекцией")
    private Map<String, LinkInfo> _links;

    public UserListResponseModel(List<UserWithLinks> embedded, Map<String, LinkInfo> links) {
        this._embedded = embedded;
        this._links = links;
    }

    public List<UserWithLinks> get_embedded() {
        return _embedded;
    }

    public Map<String, LinkInfo> get_links() {
        return _links;
    }
}