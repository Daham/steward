package com.adamant.steward.service;

import com.adamant.steward.dto.user_management.GroupCreateRequestDTO;
import com.adamant.steward.dto.user_management.GroupUpdateRequestDTO;
import com.adamant.steward.dto.user_management.UserCreateRequestDTO;
import com.adamant.steward.dto.user_management.UserUpdateRequestDTO;
import com.adamant.steward.entity.user_management.Group;
import com.adamant.steward.entity.user_management.User;

public interface LocationApiService {

    User createUser(UserCreateRequestDTO userCreateRequestDTO);

    User updateUser(String id, UserUpdateRequestDTO userUpdateRequestDTO);

    void deleteUser(String id);

    Group createGroup(GroupCreateRequestDTO groupCreateRequestDTO);

    Group updateGroup(String id, GroupUpdateRequestDTO groupUpdateRequestDTO);

    void deleteGroup(String id);
}
