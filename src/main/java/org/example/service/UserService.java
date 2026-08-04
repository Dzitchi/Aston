package org.example.service;

import org.example.dto.UserModel;
import org.example.dto.UserRequestDto;

import java.util.List;

public interface UserService {

    UserModel create(UserRequestDto request);

    UserModel getById(Long id);

    List<UserModel> getAll();

    UserModel update(Long id, UserRequestDto request);

    void delete(Long id);
}
