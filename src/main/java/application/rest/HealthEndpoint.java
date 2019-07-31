package application.rest;

import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

@Health
@ApplicationScoped
public class HealthEndpoint implements HealthCheck {

    // TODO: Write a method to test connection to Derby

    @Override
    public HealthCheckResponse call() {
        return HealthCheckResponse.named("test").up().build();
    }

}
