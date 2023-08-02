package ru.parma.filesdistr.aop.exceptions;

import org.springframework.util.Assert;

public class EntityIllegalArgumentException extends BaseException{
    public EntityIllegalArgumentException(String message) {
        super(message);
    }
    public EntityIllegalArgumentException(String type, String error){
        this(formatMessage(type, error));
    }

    private static String formatMessage(String type, String error) {
        Assert.hasText(type, "Тип не может быть пустым");
        Assert.hasText(error, "Ошибка не может быть пустой");
        return String.format("%s, текст ошибки: %s", type, error);
    }
}
