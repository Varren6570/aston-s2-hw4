package aston_s2_hw4.swagger;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;

@Schema(description = "Коллекция пользователей с HATEOAS-ссылками")
public class UserListResponseModel {

  @Schema(description = "Список пользователей с их ссылками")
  private List<UserResponseModel> _embedded;

  @Schema(description = "Ссылки на действия над коллекцией")
  private Map<String, LinkInfo> _links;

  public UserListResponseModel(List<UserResponseModel> embedded, Map<String, LinkInfo> links) {
    this._embedded = embedded;
    this._links = links;
  }

  public List<UserResponseModel> get_embedded() {
    return _embedded;
  }

  public Map<String, LinkInfo> get_links() {
    return _links;
  }
}
