package aston_s2_hw4.service;

import aston_s2_hw4.dto.UserDto;
import aston_s2_hw4.exceptions.UserNotFoundException;
import aston_s2_hw4.kafka.KafkaSender;
import aston_s2_hw4.model.User;
import aston_s2_hw4.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервисный слой для работы с пользователями.
 * <p>
 * Реализует CRUD-операции через UserRepository.
 * Реализованы методы для маппинга между User и UserDto.
 *
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    KafkaSender sender;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, KafkaSender sender) {
        this.userRepository = userRepository;
        this.sender = sender;
    }
    /**
     * Создаёт нового пользователя и возвращает его DTO, а также отправляет сообщение о создании по электронной почте.
     */
    @Override
    public UserDto addUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setAge(userDto.getAge());
        user.setCreatedAt(LocalDateTime.now());

        User newUser = userRepository.save(user);

        Message<String> message = MessageBuilder
                .withPayload(newUser.getEmail())
                .setHeader(KafkaHeaders.TOPIC, "notification-topic")
                .setHeader(KafkaHeaders.KEY, newUser.getEmail())
                .setHeader("eventType", "CREATE")
                .build();

        sender.sendMessage(message);

        UserDto userResponse = new UserDto();
        userResponse.setId(newUser.getId());
        userResponse.setName(newUser.getName());
        userResponse.setEmail(newUser.getEmail());
        userResponse.setAge(newUser.getAge());
        return userResponse;
    }
    /**
     * Возвращает DTO пользователя по ID.
     * Бросает UserNotFoundException, если пользователь не найден.
     */
    @Override
    public UserDto getUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        return mapToDto(user);
    }
    /**
     * Возвращает список всех пользователей в виде DTO.
     */
    @Override
    public List<UserDto> getAllUsers() {
        List<User> user = userRepository.findAll();
        return user.stream()
                .map(u -> mapToDto(u))
                .collect(Collectors.toList());
    }
    /**
     * Обновляет существующего пользователя по ID.
     * Обновляет те поля, что есть в json'e.
     * Бросает UserNotFoundException, если пользователь не найден.
     */
    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getAge() != 0) {
            user.setAge(userDto.getAge());
        }
        User updatedUser = userRepository.save(user);
        return mapToDto(updatedUser);

    }
    /**
     * Удаляет пользователя по ID.
     * Бросает UserNotFoundException, если пользователь не найден, а также отправляет сообщение о создании по электронной почте.
     */
    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        userRepository.delete(user);

        Message<String> message = MessageBuilder
                .withPayload(user.getEmail())
                .setHeader(KafkaHeaders.TOPIC, "notification-topic")
                .setHeader(KafkaHeaders.KEY, user.getEmail())
                .setHeader("eventType", "DELETE")
                .build();

        sender.sendMessage(message);
    }
    /**
     * Маппинг User в UserDto.
     */
    private UserDto mapToDto(User user) {
        if (user == null) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setAge(user.getAge());

        return userDto;
    }
    /**
     * Маппинг UserDto в User(не понадобился).
     */
    private User mapToUser(UserDto userDto) {
        if (userDto == null) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.id(userDto.getId());
        user.name(userDto.getName());
        user.email(userDto.getEmail());
        user.age(userDto.getAge());

        return user.build();
    }
}
