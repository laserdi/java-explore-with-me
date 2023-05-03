package ru.practicum.explore_with_me.model;

public enum StateAction {
    PUBLISH_EVENT,
    REJECT_EVENT,
    /**
     * Отправлено на утверждение.
     */
    SEND_TO_REVIEW,
    /**
     * Отменено после
     */
    CANCEL_REVIEW
}
