package lt.snatovich.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lt.snatovich.demo.model.User;
import lt.snatovich.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final UserRepository userRepository;

    @Override
    public User getUserCreateIfNone(String userId) {
        final UUID userUUID = UUID.fromString(userId);
        return userRepository.findById(userUUID).orElseGet(() -> {
            log.warn("No user with id '{}' found in the database, adding...", userUUID);
            return userRepository.save(new User(userUUID));
        });
    }

    @Override
    public Optional<User> getUser(String userId) {
        final UUID userUUIO = UUID.fromString(userId);
        return userRepository.findById(userUUIO);
    }
}
