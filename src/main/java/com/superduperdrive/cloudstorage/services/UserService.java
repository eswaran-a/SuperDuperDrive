package com.superduperdrive.cloudstorage.services;

import com.superduperdrive.cloudstorage.mapper.UserMapper;
import com.superduperdrive.cloudstorage.model.User;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class UserService {
    private HashService hashService;
    private UserMapper userMapper;

    public UserService(HashService hashService, UserMapper userMapper) {
        this.hashService = hashService;
        this.userMapper = userMapper;
    }
    public int createUser(User user) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = hashService.getHashedValue(user.getPassword(), encodedSalt);
        return userMapper.insertUser(new User(null, user.getUsername(), encodedSalt,
                hashedPassword, user.getFirstName(), user.getLastName()));
    }

    public User getUserByUsername(String username) {
        return userMapper.findUserByUsername(username);
    }

    public boolean isUserExists(String username) {
        return userMapper.findUserByUsername(username) != null;
    }
}
