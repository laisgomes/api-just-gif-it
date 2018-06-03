package com.apijustgifit;

import com.apijustgifit.domain.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
		FileStorageProperties.class
})
public class ApiJustGifItApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiJustGifItApplication.class, args);
	}
}
