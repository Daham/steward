package com.adamant.steward.service;

import com.adamant.steward.dto.user_management.*;
import com.adamant.steward.entity.user_management.Role;

public interface UserManagementService {

    UserResponseDTO findUser(String id);

    UsersResponseDTO findAllUsers(int rowsPerPage, String param);

    GroupResponseDTO findGroup(String id);

    GroupsResponseDTO findAllGroups(int rowsPerPage, String param);

    RoleResponseDTO findRole(String id);

    RolesResponseDTO findAllRoles(int rowsPerPage, String param);

    RoleResponseDTO saveRole(String id, Role role);

    RoleResponseDTO updateRole(String id, Role role);

    void removeRole(String id);
}
