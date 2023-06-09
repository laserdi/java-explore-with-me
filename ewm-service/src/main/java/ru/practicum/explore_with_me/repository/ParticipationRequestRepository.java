package ru.practicum.explore_with_me.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explore_with_me.model.ParticipationRequest;

import java.util.List;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
    /**
     * <p>Получение информации о заявках текущего пользователя на участие в чужих событиях.</p>
     * <p>В случае, если по заданным фильтрам не найдено ни одной заявки, возвращает пустой список.</p>
     * @param userId ID пользователя.
     * @return список заявок.
     */
    @Query("select p from ParticipationRequest p where p.requester.id = ?1 order by p.id")
    List<ParticipationRequest> findAllByRequesterIdOrderByIdAsc(Long userId);

    /**
     * Проверить участвует ли пользователь в этом событии.
     */
    @Query("select count(p) from ParticipationRequest p where p.requester.id = ?1 and p.event.id = ?2")
    int countAllByRequester_IdAndEvent_Id(Long userId, Long eventId);

    /**
     * Запрос списка подтверждённых запросов на участие в определённом событии (для определения лимита запросов).
     */
    @Query("select p from ParticipationRequest p where p.event.id = ?1 and p.statusRequest = 'CONFIRMED'")
    List<ParticipationRequest> findConfirmedRequests(Long eventId);


    /**
     * Поиск списка заявок на участие в событиях из списка.
     * @param ids список идентификаторов событий.
     * @return список подтверждённых заявок в каждом событии.
     * @statusRequest статус заявки = подтверждён.
     */
    @Query("select p from ParticipationRequest p where p.statusRequest = 'CONFIRMED' and p.event.id in ?1")
    List<ParticipationRequest> findConfirmedRequests(List<Long> ids);

    /**
     * Найти список всех заявок переданных в списке.
     * @param requestIds список ID заявок на участие.
     * @return список заявок.
     */
    List<ParticipationRequest> findByIdInOrderByIdAsc(List<Long> requestIds);

    List<ParticipationRequest> findAllByEvent_Id(Long eventId);
}
