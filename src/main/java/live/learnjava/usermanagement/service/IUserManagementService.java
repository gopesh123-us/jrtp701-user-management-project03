package live.learnjava.usermanagement.service;

import java.util.List;

import live.learnjava.usermanagement.bindings.ActivateUserVO;
import live.learnjava.usermanagement.bindings.RecoverPasswordVO;
import live.learnjava.usermanagement.bindings.UserAccountVO;
import live.learnjava.usermanagement.bindings.UserLoginVO;

public interface IUserManagementService {
	
	public String registerUser(UserAccountVO registerUserAccountVO);
	
	public UserAccountVO showUserById(Integer userId) throws Exception;
	
	public String activateUser(ActivateUserVO activateUserData);
	
	public String login(UserLoginVO userLoginVO);
	
	public String recoverPassword(RecoverPasswordVO recoverPasswordData);
	
	public List<UserAccountVO> showAllUsers();
	
	public String editUser(UserAccountVO userAccountVO);	
	
	public String deleteUserById(Integer userId);
	
	public String changeUserStatus(Integer userId, String status); //active, inactive
		
	public UserAccountVO showUserByNameAndEmail(String userName, String email);	
	
}
