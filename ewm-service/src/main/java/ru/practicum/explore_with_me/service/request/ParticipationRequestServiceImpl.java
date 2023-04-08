package ru.practicum.explore_with_me.service.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore_with_me.dto.request.ParticipationRequestDto;
import ru.practicum.explore_with_me.handler.exceptions.NotFoundRecordInBD;
import ru.practicum.explore_with_me.handler.exceptions.OperationFailedException;
import ru.practicum.explore_with_me.mapper.ParticipationRequestMapper;
import ru.practicum.explore_with_me.model.*;
import ru.practicum.explore_with_me.repository.EventRepository;
import ru.practicum.explore_with_me.repository.ParticipationRequestRepository;
import ru.practicum.explore_with_me.service.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParticipationRequestServiceImpl implements ParticipationRequestService {

    private final ParticipationRequestRepository participationRequestRepository;

    private final UserService userService;
    private final ParticipationRequestMapper requestMapper;
    private final EventRepository eventRepository;

    /**
     * <p>Получение информации о заявках текущего пользователя на участие в чужих событиях.</p>
     * <p>В случае, если по заданным фильтрам не найдено ни одной заявки, возвращает пустой список.</p>
     * @param userId ID пользователя.
     * @return список заявок.
     */
    @Override
    public List<ParticipationRequestDto> getRequestsByUserId(Long userId) {
        User userFromDb = userService.getUserOrThrow(userId, "При получении информации о заявках на " +
                "участие в событиях не найден пользователь ID = %d.");
        List<ParticipationRequestDto> result = participationRequestRepository.findAllByRequesterIdOrderByIdAsc(userId).stream()
                .map(requestMapper::mapToDto).collect(Collectors.toList());
        log.info("Выдан ответ на запрос участия пользователя (ID = {}) в чужих событиях.", userId);
        return result;
    }

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
    @Override
    public ParticipationRequestDto create(Long userId, Long eventId) {
        User userFromDb = userService.getUserOrThrow(userId, "При создании заявки на участие в событии " +
                "не найден пользователь ID = %d.");
        Event eventFromDb = getEventOrThrow(eventId, "При создании заявки на участие " +
                "в событии не найдено событие ID = %d.");
        if (userId.equals(eventFromDb.getInitiator().getId())) {
            throw new OperationFailedException(String.format("Инициатор с ID = %d события не может создать " +
                    "запрос на участие в своём событии с ID = %d.", userId, eventId));
        }
        if (eventFromDb.getEventState() != EventState.PUBLISHED) {
            throw new OperationFailedException(
                    "Нельзя участвовать в неопубликованном событии."
            );
        }

        if (participationRequestRepository.countAllByRequester_IdAndEvent_Id(userId, eventId) != 0) {
            throw new OperationFailedException(
                    String.format("Нельзя добавить повторный запрос на участие в событии ID = %d.", eventId));
        }

        //Определяем количество подтверждённых запросов на участие в событии.
        List<ParticipationRequest> confirmedRequests =
                participationRequestRepository.findConfirmedRequests(eventId);

        if (confirmedRequests.size() == eventFromDb.getParticipantLimit()) {
            throw new OperationFailedException(
                    String.format("Нельзя добавить запрос на участие в событии с ID = %d, поскольку достигнут " +
                            "лимит запросов.", eventId));
        }

        ParticipationRequest participationRequest = new ParticipationRequest();
        participationRequest.setRequester(userFromDb);

        if (eventFromDb.getRequestModeration() && eventFromDb.getParticipantLimit() != 0) {
            //Если требуется модерация и есть ограничение на количество участников...
            participationRequest.setStatusRequest(StatusRequest.PENDING);
        } else {
            participationRequest.setStatusRequest(StatusRequest.CONFIRMED);
        }

        participationRequest.setEvent(eventFromDb);
        participationRequest.setCreated(LocalDateTime.now());
        participationRequest = participationRequestRepository.save(participationRequest);
        log.info("Сохранена заявка на участие в событии с ID = {} пользователя с ID = {}.", eventId, userId);
        return requestMapper.mapToDto(participationRequest);
    }

    /**
     * Проверка наличия события в БД.
     * @param eventId ID события.
     * @param message сообщение для исключения, которое должно содержать поле с ID или быть пустым.
     * @return найденное событие.
     */
    private Event getEventOrThrow(Long eventId, String message) {
        if (message == null || message.isBlank()) {
            message = "В БД не найдено событие с ID = %d.";
        }
        String finalMessage = message;
        return eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundRecordInBD(String.format(finalMessage, eventId)));
    }

    /**
     * <p>Отмена своего запроса на участие в событии.</p>
     * @param userId    ID пользователя, отменяющего запрос на участие.
     * @param requestId ID события.
     * @return информация о событии.
     */
    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        userService.check(userId, "При отмене заявки на участие в событии " +
                "не найден пользователь ID = %d.");
        Event eventFromDb = getEventOrThrow(requestId, "При отмене заявки на участие " +
                "в событии не найдено событие ID = %d.");
        ParticipationRequest participationRequest = participationRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundRecordInBD(String.format("При отмене заявки на участие " +
                        "в событии не найдена заявка ID = %d.", requestId)));

        //Проверка того, что заявка принадлежит этому пользователю.
        if (!Objects.equals(participationRequest.getRequester().getId(), userId)) {
            throw new OperationFailedException(String.format(
                    "Отменить запрос ID = %d нельзя, поскольку Вы не являетесь инициатором запроса.", requestId));
        }
        participationRequest.setStatusRequest(StatusRequest.CANCELED);
        log.info("Выполнена отмена заявки на событие ID = {}, пользователем ID = {}.", requestId, userId);
        return requestMapper.mapToDto(participationRequest);
    }


    /**
     * <p>Получение информации о запросах на участие в событии текущего пользователя.</p>
     * <p>В случае, если по заданным фильтрам не найдено ни одной заявки, возвращает пустой список.</p>
     * @param eventId ID события.
     * @return список запросов.
     */
    @Override
    public List<ParticipationRequestDto> getRequestsForEvent(Long eventId) {
        List<ParticipationRequest> result = participationRequestRepository.findAllByEvent_Id(eventId);
        return requestMapper.mapListToDtoList(result);
    }

    /**
     * Найти запросы на участие по ID.
     * @param requestIds список ID заявок.
     * @return список запросов.
     */
    @Override
    public List<ParticipationRequestDto> findRequestByIds(List<Long> requestIds) {
        List<ParticipationRequest> result = participationRequestRepository.findByIdInOrderByIdAsc(requestIds);
        List<ParticipationRequestDto> requestDtos = requestMapper.mapListToDtoList(result);
        log.info("Выдан список заявок на участие в событиях по переданному списку ID заявок.");
        return requestDtos;
    }

    /**
     * Метод обновления заявки на участие в событии.
     * <p>Обновляется только статус.</p>
     * @param idRequest ID заявки.
     * @param status    статус заявки (отклонено, одобрено и тп.).
     */
    @Override
    @Transactional
    public ParticipationRequestDto updateRequest(Long idRequest, StatusRequest status) {
        Optional<ParticipationRequest> request = participationRequestRepository.findById(idRequest);
        ParticipationRequest result = null;
        if (request.isPresent()) {
            request.get().setStatusRequest(status);
            result = participationRequestRepository.save(request.get());
        }
        return requestMapper.mapToDto(result);
    }

    /**
     * Вернуть запрос или сгенерировать исключение.
     * @param reqId   ID запроса на участие.
     * @param message сообщение об ошибке для сгенерированного исключения.
     *                Сообщение должно содержать поле для ID или быть null.
     */
    @Override
    public ParticipationRequestDto getRequestOrThrow(Long reqId, String message) {
        if (message == null || message.isBlank()) {
            message = "Не найден запрос на участие с ID = %d.";
        }
        String finalMessage = message;
        ParticipationRequest request = participationRequestRepository.findById(reqId)
                .orElseThrow(() -> new NotFoundRecordInBD(String.format(finalMessage, reqId)));
        return requestMapper.mapToDto(request);
    }
}
