package com.image.imageclientsdk;

import com.image.imageclientsdk.client.ImageClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author JXY
 * @version 1.0
 * @since 2024/5/23
 */
@Configuration
@ConfigurationProperties("image.client")
@Data
@Component
public class ImageClientConfig {
    private String accessKey;
    private String secretKey;

    @Bean
    public ImageClient imageClient() {
        return new ImageClient(accessKey, secretKey);
    }
}
