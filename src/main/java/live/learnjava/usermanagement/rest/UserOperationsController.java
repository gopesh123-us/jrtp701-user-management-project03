package live.learnjava.usermanagement.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import live.learnjava.usermanagement.bindings.ActivateUserVO;
import live.learnjava.usermanagement.bindings.RecoverPasswordVO;
import live.learnjava.usermanagement.bindings.UserAccountVO;
import live.learnjava.usermanagement.bindings.UserLoginVO;
import live.learnjava.usermanagement.service.IUserManagementService;

/* @formatter:off */
@OpenAPIDefinition(
		info = @Info(title = "User Management API", 
					 version = "1.0", 
		             description = "User Management API to add, edit, delete users with other tasks including password management", 
		             license = @License(
		            		 name = "Gopesh Sharma", 
		            		 url = "https://www.acmesoftware.com"), 
		             contact = @Contact(
		            		 url = "http://www.acmesoftwaregt.com", 
		            		 name = "Gopesh Sharma", 
		            		 email = "gopesh.sharma@gmail.com")))
/* @formatter:on */
@RestController
@RequestMapping("/usermanagement/api")
public class UserOperationsController {

	@Autowired
	IUserManagementService service;

	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@RequestBody UserAccountVO userAccountVO) {
		String failuerMessage = "";
		try {
			String message = service.registerUser(userAccountVO);
			failuerMessage = message;
			return new ResponseEntity<String>(message, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(failuerMessage, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/showUser/{userName}/{email}")
	public ResponseEntity<?> showUserByNameAndEmail(@PathVariable String userName, @PathVariable String email) {
		UserAccountVO userAccountVO = null;
		try {
			userAccountVO = service.showUserByNameAndEmail(userName, email);
			return new ResponseEntity<UserAccountVO>(userAccountVO, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/showUserById/{userId}")
	public ResponseEntity<?> showUserById(@PathVariable Integer userId) {
		try {
			UserAccountVO userAccount = service.showUserById(userId);
			return new ResponseEntity<UserAccountVO>(userAccount, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/activateUser")
	public ResponseEntity<String> activateUser(@RequestBody ActivateUserVO activateUserVO) {

		try {
			String message = service.activateUser(activateUserVO);
			return new ResponseEntity<String>(message, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody UserLoginVO userLoginVO) {
		try {
			String message = service.login(userLoginVO);
			return new ResponseEntity<String>(message, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@PostMapping("/recoverPassword")
	public ResponseEntity<String> recoverPassword(@RequestBody RecoverPasswordVO recoverPasswordVO) {
		try {

			String message = service.recoverPassword(recoverPasswordVO);
			return new ResponseEntity<String>(message, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/showAllUsers")
	public ResponseEntity<?> showAllUsers() {
		try {
			List<UserAccountVO> allUsers = service.showAllUsers();
			return new ResponseEntity<List<UserAccountVO>>(allUsers, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			String message = "Problem getting all users";
			message = message + "\n";
			message = message + e.getMessage();
			return new ResponseEntity<String>(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/deleteUser/{userId}")
	public ResponseEntity<String> deleteUser(@PathVariable Integer userId) {
		try {

			String message = service.deleteUserById(userId);
			return new ResponseEntity<String>(message, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			String message = "User not found for deletion";
			message = message + "\n";
			message = message + e.getMessage();
			return new ResponseEntity<String>(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/editUser")
	public ResponseEntity<String> editUser(@RequestBody UserAccountVO userAccountVO) {
		try {
			// user service
			String message = service.editUser(userAccountVO);
			return new ResponseEntity<String>(message, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PatchMapping("/changeUserStatus/{userId}/{status}")
	public ResponseEntity<String> changeUserStatus(@PathVariable Integer userId, @PathVariable String status) {
		// use service
		try {

			String message = service.changeUserStatus(userId, status);
			return new ResponseEntity<String>(message, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

//	public String activateUser(ActivateUserPageInfo activateData);

}
