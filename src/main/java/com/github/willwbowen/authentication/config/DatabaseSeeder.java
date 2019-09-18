package com.github.willwbowen.authentication.config;

import com.github.willwbowen.authentication.model.AuthClientDetails;
import com.github.willwbowen.authentication.model.Authorities;
import com.github.willwbowen.authentication.model.User;
import com.github.willwbowen.authentication.repository.AuthClientRepository;
import com.github.willwbowen.authentication.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DatabaseSeeder {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseSeeder.class);
    private UserRepository userRepository;
    private AuthClientRepository authClientRepository;
    private JdbcTemplate jdbcTemplate;
    private PasswordEncoder passwordEncoder;

    @Value("${seed.data.password}")
    private String defaultPassword;
    @Value("${seed.data.username}")
    private String username;
    @Value("${seed.data.browser-client}")
    private String browserClient;
    @Value("${seed.data.account-client}")
    private String accountClient;

    @Autowired
    public DatabaseSeeder(
            UserRepository userRepository,
            AuthClientRepository authClientRepository,
            JdbcTemplate jdbcTemplate,
            PasswordEncoder passwordEncoder) {
        this.authClientRepository = authClientRepository;
        this.userRepository = userRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
    }
    @EventListener
    public void seed(ContextRefreshedEvent event) {
        seedUsersTable();
        seedClientsTable();
    }

    private void seedUsersTable() {
        String sql = "SELECT username FROM users WHERE username = \""+username+"\" LIMIT 1";
        List<User> u = jdbcTemplate.query(sql, (resultSet, rowNum) -> null);
        if(u == null || u.size() <= 0) {
            User user = new User();
            user.setActivated(true);
            user.setPassword(passwordEncoder.encode(defaultPassword));
            Set<Authorities> authorities = new HashSet<>();
            authorities.add(Authorities.ROLE_USER);
            user.setAuthorities(authorities);
            user.setUsername(username);
            userRepository.save(user);
            logger.info("Users Seeded");
        } else {
            logger.trace("User Seeding Not Required");
        }
    }

    private void seedClientsTable() {
        String sql = "SELECT client_id FROM clients WHERE client_id = \""+browserClient+"\" LIMIT 1";
        List<AuthClientDetails> acd = jdbcTemplate.query(sql, (resultSet, rowNum) -> null);
        if (acd == null || acd.size() <= 0) {
            AuthClientDetails details = new AuthClientDetails();
            details.setClientId(browserClient);
            details.setClientSecret(passwordEncoder.encode(defaultPassword));
            details.setScopes("ui");
            details.setGrantTypes("refresh_token,password");
            authClientRepository.save(details);

            details = new AuthClientDetails();
            details.setClientId(accountClient);
            details.setClientSecret(passwordEncoder.encode(defaultPassword));
            details.setScopes("server");
            details.setGrantTypes("refresh_token,client_credentials");
            authClientRepository.save(details);
            logger.info("Client Details Seeded");
        } else {
            logger.trace("Client Details Seeding Not Required");
        }
    }
}
