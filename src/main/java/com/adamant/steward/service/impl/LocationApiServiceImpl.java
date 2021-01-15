package com.adamant.steward.service.impl;

import com.adamant.steward.dto.user_management.GroupCreateRequestDTO;
import com.adamant.steward.dto.user_management.GroupUpdateRequestDTO;
import com.adamant.steward.dto.user_management.UserCreateRequestDTO;
import com.adamant.steward.dto.user_management.UserUpdateRequestDTO;
import com.adamant.steward.entity.user_management.Group;
import com.adamant.steward.entity.user_management.User;
import com.adamant.steward.repository.LocationApiRepository;
import com.adamant.steward.service.LocationApiService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class LocationApiServiceImpl implements LocationApiService {

    private final LocationApiRepository locationApiRepository;

    @Override
    public User createUser(UserCreateRequestDTO userCreateRequestDTO) {
        Optional<User> optionalUser = locationApiRepository.createUser(userCreateRequestDTO);
        return optionalUser.orElse(null);
    }

    @Override
    public User updateUser(String id, UserUpdateRequestDTO user) {
        Optional<User> optionalUser = locationApiRepository.updateUser(id, user);
        return optionalUser.orElse(null);
    }

    @Override
    public void deleteUser(String id) {
        locationApiRepository.deleteUser(id);
    }

    @Override
    public Group createGroup(GroupCreateRequestDTO groupCreateRequestDTO) {
        Optional<Group> optionalGroup = locationApiRepository.createGroup(groupCreateRequestDTO);
        return optionalGroup.orElse(null);
    }

    @Override
    public Group updateGroup(String id, GroupUpdateRequestDTO groupUpdateRequestDTO) {
        Optional<Group> optionalGroup = locationApiRepository.updateGroup(id, groupUpdateRequestDTO);
        return optionalGroup.orElse(null);
    }

    @Override
    public void deleteGroup(String id) {
        locationApiRepository.deleteUser(id);
    }
}
