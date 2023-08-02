package ru.parma.filesdistr.aop.exceptions;

import org.springframework.util.Assert;

public class EntityNotFoundException extends BaseException{
    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String type, Object id){
        this(formatMessage(type, id));
    }

    private static String formatMessage(String type, Object id) {
        Assert.hasText(type, "Тип не может быть пустым");
        Assert.notNull(id, "Идентификатор объекта не может быть null");
        Assert.hasText(id.toString(), "Идентификатор не может быть пустым");
        return String.format("%s с id: %d не найден", type, id);
    }
}
