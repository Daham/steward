package com.adamant.steward.repository;

import com.adamant.steward.dto.user_management.GroupCreateRequestDTO;
import com.adamant.steward.dto.user_management.GroupUpdateRequestDTO;
import com.adamant.steward.dto.user_management.UserCreateRequestDTO;
import com.adamant.steward.dto.user_management.UserUpdateRequestDTO;
import com.adamant.steward.entity.user_management.Group;
import com.adamant.steward.entity.user_management.User;

import java.util.Optional;

public interface LocationApiRepository {

    Optional<User> createUser(UserCreateRequestDTO userCreateRequestDTO);

    Optional<User> updateUser(String id, UserUpdateRequestDTO userUpdateRequestDTO);

    void deleteUser(String id);

    Optional<Group> createGroup(GroupCreateRequestDTO groupCreateRequestDTO);

    Optional<Group> updateGroup(String id, GroupUpdateRequestDTO groupUpdateRequestDTO);

    void deleteGroup(String id);
}
