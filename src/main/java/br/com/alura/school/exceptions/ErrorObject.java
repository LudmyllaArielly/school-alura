package br.com.alura.school.exceptions;

public class ErrorObject {

    private final String message;
    private final String field;

    public ErrorObject(String message, String field) {
        this.message = message;
        this.field = field;

    }

    public String getMessage() {
        return message;
    }

    public String getField() {
        return field;
    }


}
