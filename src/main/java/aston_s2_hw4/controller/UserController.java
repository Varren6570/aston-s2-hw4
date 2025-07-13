package aston_s2_hw4.controller;


import aston_s2_hw4.dto.UserCreateRequest;
import aston_s2_hw4.dto.UserDto;
import aston_s2_hw4.service.UserService;
import aston_s2_hw4.swagger.MessageResponseModel;
import aston_s2_hw4.swagger.UserListResponseModel;
import aston_s2_hw4.swagger.UserResponseModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * REST‑контроллер, предоставляющий CRUD‑API для пользователей.
 * <p>
 * Особенности:
 *
 * <li>Базовый префикс URL — /api</li>
 * <li>Работает только с DTO, не раскрывая сущности</li>
 * <li>Возвращает ResponseEntity, задает HTTP‑статусы</li>
 */
@RestController
@RequestMapping("/api/")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Получить список всех пользователей")
    @ApiResponse(
            responseCode = "200",
            description = "Список пользователей с HATEOAS-ссылками",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserListResponseModel.class),
                    examples = @ExampleObject(
                            value = """
                                    {
                                      "_embedded": {
                                        "userDtoList": [
                                          {
                                            "id": 1,
                                            "name": "nadddme",
                                            "email": "mailddddargaregfrer",
                                            "age": 13,
                                            "_links": {
                                              "self": {
                                                "href": "/users/1",
                                                "type": "GET"
                                              },
                                              "patch": {
                                                "href": "/user/1",
                                                "type": "PATCH"
                                              },
                                              "delete": {
                                                "href": "/users/1",
                                                "type": "DELETE"
                                              }
                                            }
                                          },
                                          {
                                            "id": 2,
                                            "name": "4",
                                            "email": "3",
                                            "age": 3,
                                            "_links": {
                                              "self": {
                                                "href": "/users/2",
                                                "type": "GET"
                                              },
                                              "patch": {
                                                "href": "/user/2",
                                                "type": "PATCH"
                                              },
                                              "delete": {
                                                "href": "/users/2",
                                                "type": "DELETE"
                                              }
                                            }
                                          },
                                          {
                                            "id": 6,
                                            "name": "aaaaa",
                                            "email": "aaaaa",
                                            "age": 12,
                                            "_links": {
                                              "self": {
                                                "href": "/users/6",
                                                "type": "GET"
                                              },
                                              "patch": {
                                                "href": "/user/6",
                                                "type": "PATCH"
                                              },
                                              "delete": {
                                                "href": "/users/6",
                                                "type": "DELETE"
                                              }
                                            }
                                          }
                                        ]
                                      },
                                      "_links": {
                                        "self": {
                                          "href": "/users/",
                                          "type": "GET"
                                        }
                                      }
                                    }
                                    """)))
    @GetMapping("/users")
    public ResponseEntity<CollectionModel<EntityModel<UserDto>>> getAllUsers() {

        List<UserDto> users = userService.getAllUsers();

        List<EntityModel<UserDto>> models = users
                .stream()
                .map(user -> EntityModel.of(user)
                        .add(Link.of("/users/" + user.getId()).withSelfRel().withType("GET"))
                        .add(Link.of("/user/" + user.getId()).withRel("patch").withType("PATCH"))
                        .add(Link.of("/users/" + user.getId()).withRel("delete").withType("DELETE")))
                .toList();

        CollectionModel<EntityModel<UserDto>> collection = CollectionModel.of(models)
                .add(Link.of("/users/").withSelfRel().withType("GET"));

        return new ResponseEntity<>(collection, HttpStatus.OK);
    }

    @Operation(summary = "Создать пользователя")
    @ApiResponse(
            responseCode = "201",
            description = "Пользователь с HATEOAS-ссылками",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseModel.class),
                    examples = @ExampleObject(
                            value = """
                                    {
                                      "id": 1,
                                      "name": "Евгений",
                                      "email": "mail@mail.com",
                                      "age": 27,
                                      "_links": {
                                        "self": {
                                          "href": "/users/12",
                                          "type": "GET"
                                        },
                                        "patch": {
                                          "href": "/user/12",
                                          "type": "PATCH"
                                        },
                                        "delete": {
                                          "href": "/users/12",
                                          "type": "DELETE"
                                        }
                                      }
                                    }
                                    """)))
    @PostMapping("/users")
    public ResponseEntity<EntityModel<UserDto>> addUser(
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Пользователь без ID",
                    required = true, content = @Content(schema = @Schema(implementation = UserCreateRequest.class)))
            UserCreateRequest userCreateRequest) {

        EntityModel<UserDto> model = EntityModel.of(userService.addUser(userCreateRequest));
        Long id = model.getContent().getId();
        model
                .add(Link.of("/users/" + id).withSelfRel().withType("GET"))
                .add(Link.of("/user/" + id).withRel("patch").withType("PATCH"))
                .add(Link.of("/users/" + id).withRel("delete").withType("DELETE"));

        return new ResponseEntity<>(model, HttpStatus.CREATED);
    }

    @Operation(summary = "Получить пользователя по ID")
    @ApiResponse(
            responseCode = "200",
            description = "Пользователь с HATEOAS-ссылками",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseModel.class),
                    examples = @ExampleObject(value = """
                            {
                                 "id": 1,
                                 "name": "Евгений",
                                 "email": "mail@mail.com",
                                 "age": 13,
                                 "_links": {
                                     "self": {
                                         "href": "/users/1",
                                         "type": "GET"
                                     },
                                     "patch": {
                                         "href": "/user/1",
                                         "type": "PATCH"
                                     },
                                     "delete": {
                                         "href": "/users/1",
                                         "type": "DELETE"
                                     }
                                 }
                             }
                            """)))
    @GetMapping("/users/{id}")
    public ResponseEntity<EntityModel<UserDto>> getUserById(@PathVariable Long id) {

        EntityModel<UserDto> model = EntityModel.of(userService.getUser(id))
                .add(Link.of("/users/" + id).withSelfRel().withType("GET"))
                .add(Link.of("/user/" + id).withRel("patch").withType("PATCH"))
                .add(Link.of("/users/" + id).withRel("delete").withType("DELETE"));

        return ResponseEntity.ok().body(model);
    }

    @Operation(summary = "Обновить данные пользователя по ID")
    @ApiResponse(responseCode = "200",
            description = "Пользователь с HATEOAS-ссылками",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseModel.class),
                    examples = @ExampleObject(value = """
                            {
                                "id": 1,
                                "name": "Евгений",
                                "email": "mail@mail.com",
                                "age": 13,
                                "_links": {
                                    "self": {
                                        "href": "/users/1",
                                        "type": "GET"
                                    },
                                    "patch": {
                                        "href": "/user/1",
                                        "type": "PATCH"
                                    },
                                    "delete": {
                                        "href": "/users/1",
                                        "type": "DELETE"
                                    }
                                }
                            }
                            """)))
    @PatchMapping("users/{id}")
    public ResponseEntity<EntityModel<UserDto>> updateUser(
            @PathVariable Long id,
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Пользователь без ID",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UserCreateRequest.class)))
            UserCreateRequest userCreateRequest) {

        EntityModel<UserDto> model = EntityModel.of(userService.updateUser(id, userCreateRequest))
                .add(Link.of("/users/" + id).withSelfRel().withType("GET"))
                .add(Link.of("/user/" + id).withRel("patch").withType("PATCH"))
                .add(Link.of("/users/" + id).withRel("delete").withType("DELETE"));

        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @Operation(summary = "Удалить пользователя по ID")
    @ApiResponse(
            responseCode = "200",
            description = "Сообщение об успешном удалении пользователя",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MessageResponseModel.class),
                    examples = @ExampleObject(
                            value = """
                                    {
                                    "message": "Пользователь успешно удален"
                                    }
                                    """)))
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(Map.of("message", "Пользователь успешно удален"));
    }

}