package tech.bubbl.tourologist.config;

import com.google.maps.GeoApiContext;
import org.apache.commons.lang3.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.concurrent.TimeUnit;

@Configuration
public class GoogleMapsConfiguration {

    @SuppressWarnings("unused")
    private final Logger log = LoggerFactory.getLogger(GoogleMapsConfiguration.class);

    @Bean
    @Description("Api context for directions and destination matrix.")
    public GeoApiContext geoApiContext() {
        GeoApiContext geoApiContext = new GeoApiContext();

        // TODO: 28.11.2016 Move api key to env variable!!! 
        geoApiContext
            .setApiKey("AIzaSyD5QFvaweY2CFU-U62nHrNOWCkENUM_YTo")
            .setQueryRateLimit(3)
            .setConnectTimeout(10, TimeUnit.SECONDS)
            .setReadTimeout(10, TimeUnit.SECONDS)
            .setWriteTimeout(10, TimeUnit.SECONDS);

        return geoApiContext;
    }
}
