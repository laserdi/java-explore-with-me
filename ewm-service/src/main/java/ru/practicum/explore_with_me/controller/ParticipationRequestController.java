package ru.practicum.explore_with_me.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.request.ParticipationRequestDto;
import ru.practicum.explore_with_me.service.request.ParticipationRequestService;

import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/requests")
public class ParticipationRequestController {
    private final ParticipationRequestService participationRequestService;

    /**
     * GET /users/{userId}/requests
     * <p>Получение информации о заявках текущего пользователя на участие в чужих событиях.</p>
     * <p>В случае, если по заданным фильтрам не найдено ни одной заявки, возвращает пустой список.</p>
     * @param userId ID пользователя.
     * @return список заявок.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getRequests(@PathVariable Long userId) {
        log.info("Получение информации о заявках текущего пользователя на участие в чужих событиях." +
                "GET /users/{}/requests userId", userId);
        return participationRequestService.getRequestsByUserId(userId);
    }

    /**
     * POST /users/{userId}/requests
     * <p>Добавление запроса от текущего пользователя на участие в событии.</p>
     * Обратите внимание:
     * <p>нельзя добавить повторный запрос (Ожидается код ошибки 409)</p>
     * инициатор события не может добавить запрос на участие в своём событии (Ожидается код ошибки 409)
     * <p>нельзя участвовать в неопубликованном событии (Ожидается код ошибки 409)</p>
     * если у события достигнут лимит запросов на участие - необходимо вернуть ошибку (Ожидается код ошибки 409)
     * <p>если для события отключена пре-модерация запросов на участие, то запрос должен автоматически перейти
     * в состояние подтвержденного</p>
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addRequest(@PathVariable Long userId,
                                              @RequestParam Long eventId) {
        log.info("POST  /users/{userId}/requests userId {}, eventId {}", userId, eventId);
        return participationRequestService.create(userId, eventId);
    }

    /**
     * PATCH /users/{userId}/requests/{requestId}/cancel
     * <p>Отмена своего запроса на участие в событии.</p>
     */
    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable("userId") @Positive long userId,
                                                 @PathVariable(name = "requestId") @Positive long requestId) {
        return participationRequestService.cancelRequest(userId, requestId);
    }

}
