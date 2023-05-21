package com.segroup2023.lab.service;

import com.segroup2023.lab.database.entity.User;
import com.segroup2023.lab.database.repository.UserRepository;
import com.segroup2023.lab.exception.type.LoginFailureException;
import com.segroup2023.lab.exception.type.FieldConflictException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService {
    private static UserRepository userRepository;

    @Autowired
    private UserService(UserRepository userRepository) {
        UserService.userRepository = userRepository;
    }

    @Autowired
    private void init(UserRepository userRepository){
        List<User> admins = userRepository.findByIdentity(User.Identity.ADMIN);
        User admin;
        if(admins.isEmpty()) {
            admin = new User("admin", "null", "null", "null", "wylkernb666", User.Identity.ADMIN);
            userRepository.save(admin);
        } else
            admin = admins.get(0);
        AccountService.checkAdminAccount(admin);
    }

    private static void checkConflict(User user) throws FieldConflictException {
        if(!userRepository.findByUsername(user.getUsername()).isEmpty())
            throw new FieldConflictException("Username");
        if(!userRepository.findByPhone(user.getPhone()).isEmpty())
            throw new FieldConflictException("Phone number");
        if(!userRepository.findByIdCard(user.getIdCard()).isEmpty())
            throw new FieldConflictException("Identity card number");
        if(!userRepository.findByEmail(user.getEmail()).isEmpty())
            throw new FieldConflictException("Email");
    }

    public static void register(User user) throws FieldConflictException {
        checkConflict(user);
        userRepository.save(user);
        AccountService.createPersonalAccount(user);
    }

    public static void modify(User user, String username, String phone, String email, String password) throws FieldConflictException {
        List<User> usernameList = userRepository.findByUsername(username);
        if(!usernameList.isEmpty() && !user.equals(usernameList.get(0)))
            throw new FieldConflictException("Username");
        List<User> phoneList = userRepository.findByUsername(phone);
        if(!phoneList.isEmpty() && !user.equals(phoneList.get(0)))
            throw new FieldConflictException("Phone number");
        List<User> emailList = userRepository.findByUsername(email);
        if(!emailList.isEmpty() && !user.equals(emailList.get(0)))
            throw new FieldConflictException("Email");
        user.setUsername(username);
        user.setPhone(phone);
        user.setEmail(email);
        if(password != null)
            user.setPassword(password);
        userRepository.save(user);
    }

    public static User login(String username, String password) throws LoginFailureException {
        List<User> rows = userRepository.findByUsername(username);
        if(rows.isEmpty())
            throw new LoginFailureException(LoginFailureException.Cause.USERNAME);
        User user = rows.get(0);
        if(!user.getPassword().equals(password))
            throw new LoginFailureException(LoginFailureException.Cause.PASSWORD);
        return user;
    }

    public static Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public static User getUser(Long user) {
        Optional<User> optional = userRepository.findById(user);
        if (optional.isEmpty()) {
            log.warn("User not found.");
            return null;
        }
        return optional.get();
    }

}
