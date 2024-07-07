package live.learnjava.usermanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import live.learnjava.usermanagement.config.ApplicationConfigProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = ApplicationConfigProperties.class)
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
