package tech.bubbl.tourologist.service.dto;

public class SuccessTransportObject implements TransportObject {

    private Boolean success = true;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
