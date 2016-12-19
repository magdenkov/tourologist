package tech.bubbl.tourologist.service.dto.tour;

import com.google.maps.model.LatLng;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Denis Magdenkov on 19.12.2016.
 */
public class RecalculateRoutePointsDTO implements Serializable{

    private LatLng origin;

    private LatLng destination;

    private List<LatLng> bubblsToCover;

    public RecalculateRoutePointsDTO() {
    }

    public LatLng getOrigin() {
        return origin;
    }

    public void setOrigin(LatLng origin) {
        this.origin = origin;
    }

    public LatLng getDestination() {
        return destination;
    }

    public void setDestination(LatLng destination) {
        this.destination = destination;
    }

    public List<LatLng> getBubblsToCover() {
        return bubblsToCover;
    }

    public void setBubblsToCover(List<LatLng> bubblsToCover) {
        this.bubblsToCover = bubblsToCover;
    }
}
