package tech.bubbl.tourologist.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Denis Magdenkov on 24.11.2016.
 */
public class IdsDto implements Serializable {

        private List<Long> interests =  new ArrayList<>();

        public List<Long> getInterests() {
            return interests;
        }

        public void setInterests(List<Long> interests) {
            this.interests = interests;
        }

        public IdsDto() {}


}

