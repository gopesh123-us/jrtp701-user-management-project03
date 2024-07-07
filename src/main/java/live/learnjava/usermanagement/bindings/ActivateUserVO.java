package live.learnjava.usermanagement.bindings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivateUserVO {
	
	private String email;
	
	private String newPassword;
	
	private String confirmPassword;
	
	private String tempPassword;	
}
