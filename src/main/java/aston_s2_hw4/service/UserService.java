package aston_s2_hw4.service;

import aston_s2_hw4.dto.UserCreateRequest;
import aston_s2_hw4.dto.UserDto;
import java.util.List;

public interface UserService {
  UserDto addUser(UserCreateRequest userCreateRequest);

  UserDto getUser(Long id);

  List<UserDto> getAllUsers();

  UserDto updateUser(Long id, UserCreateRequest userCreateRequest);

  void deleteUser(Long id);
}
