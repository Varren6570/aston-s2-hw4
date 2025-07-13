package aston_s2_hw4.swagger;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Информация о гиперссылке HATEOAS")
public class LinkInfo {
    @Schema(description = "URL для действия", example = "http://localhost:8080/users/1")
    private String href;

    @Schema(description = "HTTP-метод", example = "GET")
    private String type;

    public LinkInfo(String href, String type) {
        this.href = href;
        this.type = type;
    }

    public String getHref() {
        return href;
    }

    public String getType() {
        return type;
    }
}
