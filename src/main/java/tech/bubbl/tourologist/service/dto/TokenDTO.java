package tech.bubbl.tourologist.service.dto;

import tech.bubbl.tourologist.domain.User;

/**
 * Created by Denis Magdenkov on 22.11.2016.
 */
public class TokenDTO {

    private String id_token;

    public String getId_token() {
        return id_token;
    }

    public void setId_token(String id_token) {
        this.id_token = id_token;
    }
}
