package com.memori.memori_service.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.memori.memori_data.repositories.simple.UserRepository;
import com.memori.memori_domain.User;
import com.memori.memori_service.dtos.UserDto;
import com.memori.memori_service.mappers.UserMapper;

import jakarta.persistence.EntityExistsException;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Transactional
    public UserDto createUser(UserDto userDto) {
        User user = userMapper.dtoToEntity(userDto);
        if (user.getId() == null)
            throw new IllegalArgumentException("User id must not be null");
        @SuppressWarnings("null")
        Optional<User> userFromDb = userRepository.findById(user.getId());
        if (userFromDb.isPresent())
            throw new EntityExistsException("User already exist");
        user = userRepository.save(user);
        UserDto output = userMapper.entityToDto(user);
        return output;
    }

    @Transactional
    public UserDto updateUser(UserDto userDto) {
        User user = userMapper.dtoToEntity(userDto);
        if (user.getId() == null)
            throw new IllegalArgumentException("User id must not be null");
        @SuppressWarnings("null")
        Optional<User> userFromDb = userRepository.findById(user.getId());
        if (userFromDb.isEmpty())
            throw new EntityExistsException("User does not exist");
        user = userRepository.save(user);
        UserDto output = userMapper.entityToDto(user);
        return output;
    }

    public Optional<UserDto> getUserById(String id) {
        if (id == null || id.isBlank())
            throw new IllegalArgumentException("Id must not be null");
        Optional<User> userOptional = userRepository.findById(id);
        Optional<UserDto> userDtoOptional = userOptional.map(user -> 
            userMapper.entityToDto(user));
        return userDtoOptional;
    }

    public Optional<UserDto> getUserByEmail(String email) {
        if (email == null || email.isBlank())
            throw new IllegalArgumentException("Email must not be null");
        Optional<User> userOptional = userRepository.findByEmail(email);
        Optional<UserDto> userDtoOptional = userOptional.map(user -> 
            userMapper.entityToDto(user));
        return userDtoOptional;
    }
}
