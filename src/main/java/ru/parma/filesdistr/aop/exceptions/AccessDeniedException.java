package ru.parma.filesdistr.aop.exceptions;

import org.springframework.util.Assert;

public class AccessDeniedException extends BaseException{
    public AccessDeniedException(String message) {
        super(message);
    }

    public AccessDeniedException(String message, Object id){
        this(formatMessage(message, id));
    }

    private static String formatMessage(String message, Object id) {
        Assert.hasText(message, "Сообщение не может быть пустым");
        Assert.notNull(id, "Идентификатор объекта не может быть null");
        Assert.hasText(id.toString(), "Идентификатор не может быть пустым");
        return String.format("%s с ключом %d не найден", message, id);
    }
}
