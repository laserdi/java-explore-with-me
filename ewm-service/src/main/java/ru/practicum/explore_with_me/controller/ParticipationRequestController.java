package ru.practicum.explore_with_me.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.request.ParticipationRequestDto;
import ru.practicum.explore_with_me.service.request.ParticipationRequestService;

import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/requests")
@Validated
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
    public List<ParticipationRequestDto> getRequests(@PathVariable @Positive Long userId) {
        log.info("Получение информации о заявках текущего пользователя на участие в чужих событиях." +
                "GET /users/{}/requests userId", userId);
        return participationRequestService.getRequestsByUserId(userId);
    }

    /**
     * Добавление запроса от текущего пользователя на участие в событии.
     * <p>POST /users/{userId}/requests</p>
     * Обратите внимание:
     * <p>нельзя добавить повторный запрос.</p>
     * инициатор события не может добавить запрос на участие в своём событии.
     * <p>нельзя участвовать в неопубликованном событии.</p>
     * если у события достигнут лимит запросов на участие - необходимо вернуть ошибку.
     * <p>если для события отключена пре-модерация запросов на участие, то запрос должен автоматически перейти
     * в состояние подтвержденного</p>
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addRequest(@PathVariable @Positive Long userId,
                                              @RequestParam @Positive Long eventId) {
        log.info("Добавление запроса от текущего пользователя на участие в событии.\t\tPOST  /users/{userId}/requess userId {}, eventId {}", userId, eventId);
        return participationRequestService.create(userId, eventId);
    }

    /**
     * <p>Отмена своего запроса на участие в событии.</p>
     * PATCH /users/{userId}/requests/{requestId}/cancel
     */
    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable("userId") @Positive Long userId,
                                                 @PathVariable(name = "requestId") @Positive Long requestId) {
        log.info(String.format("Отмена своего запроса с ID = %d на участие в событии пользователя с ID = %d.\n" +
                "PATCH /users/{userId}/requests/{requestId}/cancel", requestId, userId));
        return participationRequestService.cancelRequest(userId, requestId);
    }

}
