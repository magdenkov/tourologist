package tech.bubbl.tourologist.service.dto;

/**
 * Created by Denis Magdenkov on 22.11.2016.
 */
public class ErrorDTO extends SuccessTransportObject {

    private String error;

    public  ErrorDTO(String error) {
        this.error = error;
        setSuccess(false);
    }

    public ErrorDTO error(String errorMessage) {
        setError(errorMessage);
        return this;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
