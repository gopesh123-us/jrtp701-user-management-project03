package live.learnjava.usermanagement.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "JRTP_USER_MASTER")
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;

	@Column(length = 30)
	private String userName;

	@Column(length = 50, unique = true)
	private String email;

	@Column(length = 50)
	private Long contactNumber;

	@Column(length = 10)
	private String gender;
	
	private LocalDate dateOfBirth;
	
	private Long aadharSsnNo;
	
	@Column(length = 20)
	private String password;

	@Column(length = 10)
	private String active_sw;

	@CreationTimestamp
	@Column(insertable = true, updatable = false)
	private LocalDateTime createdOn;

	@UpdateTimestamp
	@Column(insertable = false, updatable = true)
	private LocalDateTime updatedOn;

	@Column(length = 20)
	private String createdBy;

	@Column(length = 20)
	private String updatedBy;

}
