package ru.practicum.explore_with_me.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.explore_with_me.repository.UserRepository;

@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl {
    private final UserRepository userRepository;
}
