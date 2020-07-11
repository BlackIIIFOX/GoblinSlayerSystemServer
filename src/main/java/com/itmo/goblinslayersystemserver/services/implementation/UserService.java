package com.itmo.goblinslayersystemserver.services.implementation;

import com.itmo.goblinslayersystemserver.exceptions.BadRequestException;
import com.itmo.goblinslayersystemserver.exceptions.NotFoundException;
import com.itmo.goblinslayersystemserver.models.User;
import com.itmo.goblinslayersystemserver.repositories.UserRepository;
import com.itmo.goblinslayersystemserver.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService implements IUserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ArrayList<User> getUsersList() {
        return (ArrayList<User>) userRepository.findAll();
    }

    @Override
    public User createUser(User user) {
        User userWithSameUsername = userRepository.findByUsername(user.getUsername());

        if (userWithSameUsername != null) {
            throw new BadRequestException("A user with this username already exists");
        }

        // Щифруем пароль в BCrypt
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.saveAndFlush(user);
    }

    @Override
    public User updateUserById(Integer id, User user) {
        User updatableUser;

        try {
            updatableUser = userRepository.findById(id).get();
        } catch (Exception e) {
            throw new NotFoundException();
        }

        updatableUser.setName(user.getName());
        updatableUser.setAddress(user.getAddress());
        updatableUser.setRoles(user.getRoles());
        updatableUser.setBlocked(user.isBlocked());
        updatableUser.setAdventurerStatus(user.getAdventurerStatus());
        updatableUser.setAdventurerExperience(user.getAdventurerExperience());
        updatableUser.setAdventurerRank(user.getAdventurerRank());
        userRepository.save(updatableUser);

        return updatableUser;
    }

    @Override
    public User getUserById(Integer id) {
        try {
            return userRepository.findById(id).get();
        } catch (Exception e) {
            throw new NotFoundException();
        }
    }

    @Override
    public User getUserByUsername(String username) {
        User result = userRepository.findByUsername(username);
        return result;
    }

    @Override
    public String deleteUserById(Integer id) {
        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            throw new NotFoundException();
        }

        return "";
    }
}
