package ru.practicum.explore_with_me.util;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@NoArgsConstructor
public class QPredicates {
    private List<Predicate> predicates = new ArrayList<>();

    /**
     * Для объединения предикатов.
     * @param object объект или поле, по которому будет идти выборка. Может быть любого типа.
     * @param function функция, может быть игнорировать регистр и т.п.
     * @return возвращает себя же.
     * @param <T> тип может быть любым.
     */
    public <T> QPredicates add(T object, Function<T, Predicate> function) {
        if (object != null) {
            predicates.add(function.apply(object));
        }
        return this;
    }

    /**
     * Для объединения их через 'AND'
     * @return один объединённый предикат.
     */
    public Predicate buildAnd() {
        return ExpressionUtils.allOf(predicates);
    }

    /**
     * Для объединения их через 'OR'
     * @return один объединённый предикат.
     */
    public Predicate buildOr() {
        return ExpressionUtils.anyOf(predicates);
    }

    public static QPredicates builder() {
        return new QPredicates();
    }
}
