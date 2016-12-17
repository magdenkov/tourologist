package tech.bubbl.tourologist.web.rest.vm;

import org.hibernate.validator.constraints.NotBlank;
import tech.bubbl.tourologist.service.dto.TransportObject;


/**
 * Created by Denis Magdenkov on 17.12.2016.
 */
public class SocialLoginDTO implements TransportObject {

    @NotBlank
    private String token;

    private String lanKey;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLanKey() {
        return lanKey == null ? "en" : lanKey;
    }

    public void setLanKey(String lanKey) {
        this.lanKey = lanKey;
    }
}
