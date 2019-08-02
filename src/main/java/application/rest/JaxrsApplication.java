package application.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.eclipse.microprofile.auth.LoginConfig;

@LoginConfig(authMethod = "MP-JWT", realmName = "jwt-jaspi")
@ApplicationPath("/api")
public class JaxrsApplication extends Application {

}
