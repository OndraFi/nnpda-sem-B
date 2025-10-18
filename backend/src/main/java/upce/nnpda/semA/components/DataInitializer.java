package upce.nnpda.semA.components;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import upce.nnpda.semA.domain.User;
import upce.nnpda.semA.repository.UserRepository;

import java.util.LinkedList;
import java.util.List;

@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;

    public DataInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Data init running...");
        User user = new User("ondra","fialka.ondra@gmail.com","heslo");
        if(!userRepository.existsByEmail(user.getEmail())) {
            log.info("Creating user {}", user.getEmail());
            userRepository.save(user);
        }

        List<User> users = userRepository.findAll();
        log.info("Found {} users", users.size());
        log.info("First user {}", users.getFirst());
    }

}
