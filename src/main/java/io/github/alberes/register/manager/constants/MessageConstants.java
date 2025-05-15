package io.github.alberes.register.manager.constants;

public interface MessageConstants {

    public static final String DATE_TIME_FORMATTER_PATTERN = "dd/MM/yyyy HH:mm:ss";

    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String HAS_ROLE_ADMIN = "hasRole('ADMIN')";
    public static final String HAS_ROLE_ADMIN_USER = "hasRole('ADMIN') || hasRole('USER')";

    public static  final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";

    public static final String UNAUTHORIZED = "UNAUTHORIZED";
    public static final String UNAUTHORIZED_MESSAGE = "The user can only access resources that belong to him.";
    public static final String AUTHORIZATION_FAILURE = "Authorization failure!";

    public static final String UTF_8 = "UTF-8";
    public static final String APPLICATION_JSON = "application/json";

    public static final String SLASH = "/";

    public static final String HMACSHA256 = "HmacSHA256";

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String PROFILES = "profiles";
    public static final String REGISTRATION_DATE = "registrationDate";

    public static final String REGISTRATION_WITH_E_MAIL = "Registration with e-mail ";
    public static final String HAS_ALREADY_BEEN_REGISTERED = " has already been registered!";
    public static final String OBJECT_NOT_FOUND_ID = "Object not found! Id: ";
    public static final String TYPE = ", Type: ";
    public static final String OBJECT_NOT_FOUND = "Object not found!";
}
