package lt.snatovich.demo.service;

import lt.snatovich.demo.model.User;

import java.util.Optional;


public interface UsersService {
    User getUserCreateIfNone(String userId);

    Optional<User> getUser(String userId);
}
