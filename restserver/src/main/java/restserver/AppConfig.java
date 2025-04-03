package restserver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import persistence.UsersHandler;

/**
 * Configuration class for setting up application beans.
 * 
 * This class is used to define beans that will be managed by the Spring container, 
 * allowing for dependency injection and easy configuration of the application's components.
 * 
 * <ul>
 * <li>{@link #usersHandler()}: Creates and configures a {@link UsersHandler}
 *  bean for managing user data.</li>
 * </ul>
 */
@Configuration
public class AppConfig {
    
    @Bean
    public UsersHandler usersHandler() {
        return new UsersHandler();
    }
}
