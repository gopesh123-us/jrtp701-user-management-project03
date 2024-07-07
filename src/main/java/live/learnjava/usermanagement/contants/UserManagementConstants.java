package live.learnjava.usermanagement.contants;

public enum UserManagementConstants {
	
	REGISTER_SUCCESS("register-success"),
	REGISTER_FAILURE("register-failure"),
	SAVE_SUCCESS("save-success"),
	SAVE_FAILURE("save-failure"),
	UPDATE_SUCCESS("update-success"),
	UPDATE_FAILURE("update-failure"),
	DELETE_SUCCESS("delete-success"),
	DELETE_FAILURE("delete-failure"),
	LOGIN_SUCCESS("login-success"),
	LOGIN_FAILURE("login-failure"),
	INVALID_CREDENTIALS("invalid-credentials"),
	FROM_EMAIL("from-email"),
	FIND_USER_BYID_SUCCESS("find-user-by-id-success"),
	FIND_USER_BYID_FAILURE("find-user-by-id-failure"),
	PASSWORD_NOT_MATCH("password-not-match"),
	PASSWORD_RECOVERED("password-recovered"),
	PASSWORD_NOT_RECOVERED("password-not-recovered"),
	ACTIVATE_ACCOUNT_URL("activate-account-url"),
	ACCOUNT_ACTIVE_STATUS("account-active-status"),
	STATUS_CHANGE_SUCCESS("status-change-success"),
	STATUS_CHANGE_FAILURE("status-change-failure");
	

	private final String keyValue;

	UserManagementConstants(String theKeyValue) {
		this.keyValue = theKeyValue;
	}
	
	@Override
	public String toString() {
		return this.keyValue;
	}
}
