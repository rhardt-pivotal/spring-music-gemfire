package io.pivotal.fe.samples.music.springmusicgemfire;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

@SpringBootApplication
@EnableGemfireRepositories(mappingContextRef = "myMappingContext")
@EntityScan(basePackages = "io.pivotal.fe.samples.music.springmusicgemfire.domain")
public class SpringMusicGemfireApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringMusicGemfireApplication.class, args);
	}
}
