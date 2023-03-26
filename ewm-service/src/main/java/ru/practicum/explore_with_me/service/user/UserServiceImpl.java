package ru.practicum.explore_with_me.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore_with_me.dto.user.UserDto;
import ru.practicum.explore_with_me.handler.exceptions.NotFoundRecordInBD;
import ru.practicum.explore_with_me.mapper.UserMapper;
import ru.practicum.explore_with_me.model.User;
import ru.practicum.explore_with_me.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Получить всех пользователей из списка с ID.
     * @param from количество категорий, которые нужно пропустить для формирования текущего набора.
     * @param size количество категорий в наборе.
     * @return список пользователей.
     */
    @Override
    public List<UserDto> findByIds(List<Long> ids, int from, int size) {
        Pageable pageable = PageRequest.of(from, size, Sort.by("name").ascending());
        List<User> users;
        if (ids == null || ids.isEmpty()) {
            //Если параметр пуст или его нет, то получить список всех пользователей.
            users = userRepository.findAll(pageable).getContent();
            log.info("Выдан список всех пользователей, поскольку список ids пуст.");
        } else {
            users = userRepository.findAllByIdIn(ids, pageable).getContent();
            log.info("Выдан список, состоящий из {} пользователей.", ids.size());
        }
        return users.stream()
                .map(userMapper::mapToUserDto).collect(Collectors.toList());
    }

    /**
     * <p>Добавление нового пользователя.</p>
     * POST
     * <p>/admin/users</p>
     */
    @Override
    public UserDto save(UserDto userDto) {
        User newUser = userMapper.mapToUser(userDto);
        User savedUser = userRepository.save(newUser);
        UserDto result = userMapper.mapToUserDto(savedUser);
        log.info("Выполнено сохранение нового пользователя в БД ID = {}, name = {}.", result.getId(), result.getName());
        return result;
    }

    /**
     * <p>Удаление пользователя.</p>
     * Delete
     * <p>/admin/users/{userId}</p>
     * @param userId ID удаляемого пользователя.
     */
    @Override
    public void delete(Long userId) {
        UserDto oldUser = check(userId, "При удалении из БД пользователь с ID = %d не найден.");
        // TODO: 26.03.2023 Проверить отсутствие "хвостов" удаляемого пользователя.
        userRepository.deleteById(userId);
        log.info("Выполнено удаление пользователя с ID = {} и  name = {}", userId, oldUser.getName());
    }

    /**
     * Проверка наличия пользователя в БД.
     * @param userId  ID пользователя.
     * @param message сообщение для исключения.
     */
    @Override
    public UserDto check(Long userId, String message) {
        if (message == null || message.isBlank()) {
            message = "В БД не найден пользователь с ID = %d.";
        }

        String finalMessage = message;
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundRecordInBD(String.format(finalMessage, userId)));

        return userMapper.mapToUserDto(user);
    }
}
