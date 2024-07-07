package live.learnjava.usermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import live.learnjava.usermanagement.entity.UserEntity;

public interface IUserRepository extends JpaRepository<UserEntity, Integer>{
	
     public UserEntity findByUserNameAndEmail(String userName, String email);
			
}
