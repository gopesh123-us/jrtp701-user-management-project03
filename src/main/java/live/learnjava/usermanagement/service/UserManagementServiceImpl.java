package live.learnjava.usermanagement.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import live.learnjava.usermanagement.bindings.ActivateUserVO;
import live.learnjava.usermanagement.bindings.RecoverPasswordVO;
import live.learnjava.usermanagement.bindings.UserAccountVO;
import live.learnjava.usermanagement.bindings.UserLoginVO;
import live.learnjava.usermanagement.config.ApplicationConfigProperties;
import live.learnjava.usermanagement.contants.UserManagementConstants;
import live.learnjava.usermanagement.entity.UserEntity;
import live.learnjava.usermanagement.repository.IUserRepository;
import live.learnjava.usermanagement.utils.Password;
import live.learnjava.usermanagement.utils.UserManagementMailService;

@Service
public class UserManagementServiceImpl implements IUserManagementService {

	Logger logger = LoggerFactory.getLogger(UserManagementServiceImpl.class);

	@Autowired
	IUserRepository userRepo;

	@Autowired
	UserManagementMailService mailService;

	@Autowired
	Configuration config;
	

	@Autowired
	private ApplicationConfigProperties properties;

	@Override
	public String registerUser(UserAccountVO registerUserAccountVO) {
		// convert userAccountVO to User
		UserEntity user = new UserEntity();

		// copy properties from form user data to UserEntity object
		BeanUtils.copyProperties(registerUserAccountVO, user);

		// generate random password
		user.setPassword(Password.generateRandomPassword());
		user.setActive_sw("inActive");

		// save
		UserEntity savedUser = userRepo.save(user);

		if (savedUser != null) {
			// send email
			// TODO to create a dynamic email body and provide the link from the controller
			// class
			new Thread(() -> {
				String toEmail = registerUserAccountVO.getEmail();
				String subject = "Please active your account";
//				String emailBody = "Your account is created, please activate your account via this link "
//						+ "http://localhost:8080/usermanagement/api/showUserById/" + savedUser.getUserId();
				Map<String, Object> placeholdersMap = new HashMap<>();
				placeholdersMap.put("name", registerUserAccountVO.getUserName());
				placeholdersMap.put("userId", savedUser.getUserId());
				try {
					Template template = config.getTemplate("register-user-template.ftl");
					mailService.sendEmail(toEmail, subject, placeholdersMap, template);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}).start();

			return properties.getMessages().get(UserManagementConstants.REGISTER_SUCCESS.toString()) + " "
					+ savedUser.getUserId();
		} else {
			return properties.getMessages().get(UserManagementConstants.REGISTER_FAILURE.toString());
		}
	}

	// We do we do not really have to show all the information in the response.
	@Override
	public UserAccountVO showUserById(Integer userId) {
		Optional<UserEntity> opt = userRepo.findById(userId);
		UserAccountVO userAccountVO = null;
		if (opt.isPresent()) {
			userAccountVO = new UserAccountVO();
			BeanUtils.copyProperties(opt.get(), userAccountVO);
			logger.info("Found user with id " + userId);
			logger.info(properties.getMessages().get(UserManagementConstants.FIND_USER_BYID_SUCCESS.toString()) + " "
					+ userId);
		}
		return userAccountVO;
	}

	@Override
	public String activateUser(ActivateUserVO activateUserData) {
		// check if the password is correct.
		if (!activateUserData.getNewPassword().equals(activateUserData.getConfirmPassword())) {
			return properties.getMessages().get(UserManagementConstants.PASSWORD_NOT_MATCH.toString());
		}
		// update record here and we have to query so we have to create an entity object
		// convert
		UserEntity user = new UserEntity();
		BeanUtils.copyProperties(activateUserData, user); // this will not work

		// set email-id and temp password on the user object so that we can look up the
		// user
		user.setEmail(activateUserData.getEmail());
		user.setPassword(activateUserData.getTempPassword());

		Example<UserEntity> example = Example.of(user);

		List<UserEntity> list = userRepo.findAll(example);

		// if valid email and temporary password is given, then set end-user supplied
		// real password to update the record
		String response = "";
		if (list.size() != 0) {
			// get entity object from database
			UserEntity entity = list.get(0); // email id is unique
			entity.setPassword(activateUserData.getNewPassword());
			entity.setActive_sw("Active");
			// update object
			UserEntity updatedUser = userRepo.save(entity);
			response = properties.getMessages().get(UserManagementConstants.UPDATE_SUCCESS.toString()) + ": "
					+ updatedUser.getEmail();

			// send email to the user that the account has been activated
			new Thread(() -> {
				String toEmail = updatedUser.getEmail();
				String subject = "Account activated";
				//String emailBody = "Your account has been activated and password updated.";
				Map<String, Object> placeHoldersMap = new HashMap<>();
				placeHoldersMap.put("name", updatedUser.getUserName());
				try {
					Template template = config.getTemplate("activate-user-template.ftl");
					mailService.sendEmail(toEmail, subject, placeHoldersMap, template);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}).start();

		} else {

			response = properties.getMessages().get(UserManagementConstants.UPDATE_FAILURE.toString());
		}

		return response;
	}

	@Override
	public String login(UserLoginVO userLoginVO) {
		// convert userLogin = we will use email and password
		UserEntity user = new UserEntity();
		BeanUtils.copyProperties(userLoginVO, user);

		// prepare example object
		Example<UserEntity> example = Example.of(user);

		List<UserEntity> listUsers = userRepo.findAll(example);

		String response = "";

		if (listUsers.size() == 0) {
			response = properties.getMessages().get(UserManagementConstants.INVALID_CREDENTIALS.toString());
		} else {
			// get entity object
			UserEntity userEntity = listUsers.get(0);
			// check active status
			if (userEntity.getActive_sw().equalsIgnoreCase("Active")) {
				response = properties.getMessages().get(UserManagementConstants.LOGIN_SUCCESS.toString());
			} else {
				response = properties.getMessages().get(UserManagementConstants.LOGIN_FAILURE.toString());
			}
		}
		return response;
	}

	@Override
	public String recoverPassword(RecoverPasswordVO recoverPasswordData) {
		// get data from user
		UserEntity entity = new UserEntity();
		String response = "";

		if (recoverPasswordData.getUserName().equals("") || recoverPasswordData.getEmail().equals("")) {
			response = "User name or Email not provided.";
			response = response + "\n";
			response = response
					+ properties.getMessages().get(UserManagementConstants.PASSWORD_NOT_RECOVERED.toString());
			return response;
		}
		// convert into entity
		BeanUtils.copyProperties(recoverPasswordData, entity);

		// wrap entity into Example obj
		Example<UserEntity> example = Example.of(entity);

		// find user with example
		List<UserEntity> listUsers = userRepo.findAll(example);

		// if user is found
		if (listUsers.size() != 0) {
			UserEntity foundUser = listUsers.get(0);

			// generate response
			response = properties.getMessages().get(UserManagementConstants.PASSWORD_RECOVERED.toString());
			response = response + "\n";
			response = response + "Your password is: " + foundUser.getPassword();

			// send email to the user for the account password
			new Thread(() -> {
				String toEmail = foundUser.getEmail();
				String subject = "Password recovered successfully";
				Map<String, Object> placeHolders = new HashMap<>();
				placeHolders.put("name", foundUser.getUserName());
				placeHolders.put("password", foundUser.getPassword());
				
				try {
					Template template = config.getTemplate("recover-password-template.ftl");
					mailService.sendEmail(toEmail, subject, placeHolders, template);
				} catch (Exception e) {
					e.printStackTrace();
				}				

			}).start();

		} else {

			// if user is not found, do not send email
			response = properties.getMessages().get(UserManagementConstants.PASSWORD_NOT_RECOVERED.toString());
		}

		return response;
	}

	@Override
	public List<UserAccountVO> showAllUsers() {
		List<UserAccountVO> allUsers = userRepo.findAll().stream().map((entity) -> {
			UserAccountVO userAccountVO = new UserAccountVO();
			BeanUtils.copyProperties(entity, userAccountVO);
			return userAccountVO;
		}).toList();
		return allUsers;
	}

	@Override
	public String deleteUserById(Integer userId) {
		Optional<UserEntity> opt = userRepo.findById(userId);
		String response = "";
		if (opt.isPresent()) {
			userRepo.deleteById(userId);
			response = properties.getMessages().get(UserManagementConstants.DELETE_SUCCESS.toString());
			return response;
		} else {

			response = "User not found";
			response = response + "\n";
			response = response + properties.getMessages().get(UserManagementConstants.DELETE_FAILURE.toString());
			return response;
		}

	}

	@Override
	public String editUser(UserAccountVO userAccountVO) {
		// get id and find the user by id
		Integer userId = userAccountVO.getUserId();
		String response = "";
		Optional<UserEntity> opt = userRepo.findById(userId);
		if (opt.isPresent()) {

			// if found get the entity and copy the properties form userAccountVO to entity
			UserEntity entity = opt.get();
			BeanUtils.copyProperties(userAccountVO, entity);

			// save entity
			userRepo.save(entity);

			// generate response
			response = properties.getMessages().get(UserManagementConstants.UPDATE_SUCCESS.toString());

			// return response
			return response;
		} else {
			// if not found return message that user could not be found
			response = "User could not be found with user name as " + userAccountVO.getUserName();
			response = response + " with user-id " + userAccountVO.getUserId();
			response = response + "\n";
			response = response
					+ properties.getMessages().get(UserManagementConstants.FIND_USER_BYID_FAILURE.toString());
			response = response + "\n";
			response = response + properties.getMessages().get(UserManagementConstants.UPDATE_FAILURE.toString());
			return response;
		}
	}

	@Override
	public String changeUserStatus(Integer userId, String status) {
		Optional<UserEntity> opt = userRepo.findById(userId);
		String response = "";
		if (opt.isPresent()) {
			UserEntity entity = opt.get();
			entity.setActive_sw(status);
			userRepo.save(entity);
			
			response = "Status changed to " + status;
			response = response + "\n";
			response = response
					+ properties.getMessages().get(UserManagementConstants.STATUS_CHANGE_SUCCESS.toString());
			return response;
		} else {
			response = properties.getMessages().get(UserManagementConstants.STATUS_CHANGE_FAILURE.toString());
			return response;
		}

	}

	@Override
	public UserAccountVO showUserByNameAndEmail(String userName, String email) {
		
		UserAccountVO userAccountVO = null;
		UserEntity entity = userRepo.findByUserNameAndEmail(userName, email);
		if(entity != null) {
			userAccountVO = new UserAccountVO();
			BeanUtils.copyProperties(entity, userAccountVO);
		}		
		
		return userAccountVO;
	}
	
	
}
