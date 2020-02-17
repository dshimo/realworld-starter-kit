package application.errors;

import org.json.JSONObject;

public class ValidationMessages {
    public static final String REGISTRATION_REQUIREMENTS_BLANK = "username, email, and password must not be blank";
    public static final String DUPLICATE_USERNAME = "username is taken";
    public static final String DUPLICATE_EMAIL = "email is taken";
    public static final String LOGIN_REQUIREMENTS_BLANK = "email, and password must not be blank";
    public static final String EMAIL_NOT_FOUND = "provided email not found";
    public static final String LOGIN_FAIL = "matching email and password not found";
    public static final String PROFILE_NOT_FOUND = "profile not found";

    public static String throwError(String message) {
        JSONObject body = new JSONObject();

        body.put("body", new String[]{message});
        return new JSONObject().put("errors", body).toString();
    }
}