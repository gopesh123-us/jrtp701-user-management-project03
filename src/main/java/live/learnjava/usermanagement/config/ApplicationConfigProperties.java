package live.learnjava.usermanagement.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "user.module")
@Data
public class ApplicationConfigProperties {
	private Map<String, String> messages;
	private Map<String, String> mail;
	
	
}