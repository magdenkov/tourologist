package tech.bubbl.tourologist.config;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.transfer.TransferManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSConfiguration {

    @SuppressWarnings("unused")
    private final Logger log = LoggerFactory.getLogger(AWSConfiguration.class);

    @Autowired
    private AmazonS3 s3;

    @Bean
    public TransferManager transferManager() {
        return new TransferManager(s3);
    }
}
