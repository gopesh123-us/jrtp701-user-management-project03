package live.learnjava.usermanagement.bindings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecoverPasswordVO {
	
	private String userName;
	
	private String email;
}
