package me.strongwhisk;

import lombok.extern.slf4j.Slf4j;
import me.strongwhisk.blcking.RestTemplateEx;
import me.strongwhisk.nonblocking.WebClientEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@SpringBootApplication
@Slf4j
public class WebclientApplication {

	@Autowired
	private RestTemplateEx restTemplateEx;

	@Autowired
	private WebClientEx webClientEx;

	public static void main(String[] args) {
		SpringApplication.run(WebclientApplication.class, args);
	}

	@Bean
	public ApplicationRunner applicationRunner(){
		return args -> {
			restTemplateEx.startBlockApi();
			webClientEx.startNonblockingAPI();
			webClientEx.doOnNextEx();
		};
	}
}
