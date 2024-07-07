package live.learnjava.usermanagement.bindings;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAccountVO {
	
	private Integer userId; 

	private String userName;

	private String email;

	private Long contactNumber;

	private String gender;
	
	private LocalDate dateOfBirth;
	
	private Long aadharSsnNo;
	
	private String password;
	
	private String active_sw;

}
