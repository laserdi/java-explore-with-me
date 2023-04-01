package ru.practicum.explore_with_me.service.request;

import ru.practicum.explore_with_me.dto.request.ParticipationRequestDto;

import java.util.List;

public interface ParticipationRequestService {
    /**
     * <p>Получение информации о заявках текущего пользователя на участие в чужих событиях.</p>
     * <p>В случае, если по заданным фильтрам не найдено ни одной заявки, возвращает пустой список.</p>
     * @param userId ID пользователя.
     * @return список заявок.
     */
    List<ParticipationRequestDto> getRequestsByUserId(Long userId);

    /**
     * <p>Добавление запроса от текущего пользователя на участие в событии.</p>
     * Обратите внимание:
     * <p>нельзя добавить повторный запрос (Ожидается код ошибки 409)</p>
     * инициатор события не может добавить запрос на участие в своём событии (Ожидается код ошибки 409)
     * <p>нельзя участвовать в неопубликованном событии (Ожидается код ошибки 409)</p>
     * если у события достигнут лимит запросов на участие - необходимо вернуть ошибку (Ожидается код ошибки 409)
     * <p>если для события отключена пре-модерация запросов на участие, то запрос должен автоматически перейти
     * в состояние подтвержденного</p>
     * @param userId  ID пользователя, добавляющего запрос.
     * @param eventId ID события
     * @return сохранённый запрос.
     */
    ParticipationRequestDto create(Long userId, Long eventId);

    /**
     * <p>Отмена своего запроса на участие в событии.</p>
     * @param userId ID пользователя, отменяющего запрос на участие.
     * @param eventId ID события.
     * @return информация о событии.
     */
    ParticipationRequestDto cancelRequest(Long userId, Long eventId);
}
