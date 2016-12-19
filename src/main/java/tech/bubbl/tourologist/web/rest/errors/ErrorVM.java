package tech.bubbl.tourologist.web.rest.errors;

import tech.bubbl.tourologist.service.dto.ErrorDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * View Model for transferring error message with a list of field errors.
 */
public class ErrorVM extends ErrorDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String description;

    private List<FieldErrorVM> fieldErrors;

    public ErrorVM(String message) {
        this(message, null);
    }

    public ErrorVM(String message, String description) {
        super(message);
        this.description = description;
    }

    public ErrorVM(String message, String description, List<FieldErrorVM> fieldErrors) {
        super(message);
        this.description = description;
        this.fieldErrors = fieldErrors;
    }

    public void add(String objectName, String field, String message) {
        if (fieldErrors == null) {
            fieldErrors = new ArrayList<>();
        }
        fieldErrors.add(new FieldErrorVM(objectName, field, message));
    }


    public String getDescription() {
        return description;
    }

    public List<FieldErrorVM> getFieldErrors() {
        return fieldErrors;
    }
}
