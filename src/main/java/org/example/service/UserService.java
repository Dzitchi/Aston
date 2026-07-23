package org.example.service;

import org.example.dto.UserRequestDto;
import org.example.dto.UserResponseDto;

import java.util.List;

public interface UserService {

    UserResponseDto create(UserRequestDto request);

    UserResponseDto getById(Long id);

    List<UserResponseDto> getAll();

    UserResponseDto update(Long id, UserRequestDto request);

    void delete(Long id);
}
