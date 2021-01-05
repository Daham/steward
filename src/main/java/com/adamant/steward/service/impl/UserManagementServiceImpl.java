package com.adamant.steward.service.impl;

import com.adamant.steward.dto.user_management.GroupResponseDTO;
import com.adamant.steward.dto.user_management.GroupsResponseDTO;
import com.adamant.steward.dto.user_management.RoleResponseDTO;
import com.adamant.steward.dto.user_management.RolesResponseDTO;
import com.adamant.steward.dto.user_management.UserResponseDTO;
import com.adamant.steward.dto.user_management.UsersResponseDTO;
import com.adamant.steward.entity.couch_db.CouchDBFindAllResponse;
import com.adamant.steward.entity.user_management.Group;
import com.adamant.steward.entity.user_management.Role;
import com.adamant.steward.entity.user_management.User;
import com.adamant.steward.exception.DataConflictException;
import com.adamant.steward.exception.DataRemovalException;
import com.adamant.steward.exception.DataSavingException;
import com.adamant.steward.exception.DataUpdatingException;
import com.adamant.steward.exception.ResourceNotFoundException;
import com.adamant.steward.exception.enums.ErrorDetailCode;
import com.adamant.steward.service.CouchDBService;
import com.adamant.steward.service.UserManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import com.adamant.steward.util.ModelUtility;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserManagementServiceImpl implements UserManagementService {

    private final CouchDBService couchDBService;

    /**
     * Find user by the email.
     *
     * @param id - id
     * @return UserResponseDTO
     * @throws ResourceNotFoundException If the user doesn't exist.
     */
    @Override
    public UserResponseDTO findUser(String id) {
        User user = couchDBService.find(User.class, id);

        if (ObjectUtils.isEmpty(user)) {
            log.info("No user exists with the id: {}", id);
            throw new ResourceNotFoundException()
                    .addDetail("User not found for the provided id", id, ErrorDetailCode.USER_NOT_FOUND);
        }
        return ModelUtility.mapDtoToEntity(user, UserResponseDTO.class);
    }

    /**
     * Find all users
     *
     * @param rowsPerPage - rows per page
     * @param param       - param
     * @return UsersResponseDTO
     */
    @Override
    public UsersResponseDTO findAllUsers(int rowsPerPage, String param) {
        CouchDBFindAllResponse<User> response = couchDBService.findAll(User.class, rowsPerPage, param);


        return UsersResponseDTO.builder()
                .isHasNext(response.isHasNext())
                .isHasPrevious(response.isHasPrevious())
                .nextParam(response.getNextParam())
                .previousParam(response.getPreviousParam())
                .pageNumber(response.getPageNumber())
                .totalResults(response.getTotalResults())
                .items(response.getResultList().stream()
                        .map(user -> ModelUtility.mapDtoToEntity(user, UserResponseDTO.class))
                        .collect(Collectors.toList()))
                .build();
    }

    /**
     * Resolve user roles(i.e merge custom roles with inherited roles from assigned groups)
     * @param user - user
     * @return User
     */
    private User resolveUserRoles(User user){

        //Already assigned roles
        Set<String> customRoles = Set.copyOf(user.getAssignedRoles());

        //Inherited roles from assigned groups
        Set<String> inheritedRoles = new HashSet<>();

        user.getAssignedGroups().forEach((String groupId) -> {
            List<String> roles = findGroup(groupId).getAssignedRoles();
            inheritedRoles.addAll(roles);
        });

        Set<String> mergedRoleSet = new HashSet<>();

        //Merge assigned roles and roles from assigned groups
        mergedRoleSet.addAll(customRoles);
        mergedRoleSet.addAll(inheritedRoles);

        user.setAssignedRoles(List.copyOf(mergedRoleSet));

        return user;
    }

    /**
     * Resolve user roles when updating (i.e merge roles in the change request with existing roles)
     * @param user - user
     * @param existingUser - existing user
     * @return User
     */
    private User resolveUserRoles(User user, User existingUser){

        //Already assigned roles from user
        Set<String> customRolesFromUser = Set.copyOf(user.getAssignedRoles());

        //Already assigned roles from existing user
        Set<String> customRolesFromExistingUser = Set.copyOf(existingUser.getAssignedRoles());

        //Inherited roles from assigned groups of user
        Set<String> inheritedRolesOfUser = new HashSet<>();

        user.getAssignedGroups().forEach((String groupId) -> {
            List<String> roles = findGroup(groupId).getAssignedRoles();
            inheritedRolesOfUser.addAll(roles);
        });

        //Inherited roles from assigned groups of existing user
        Set<String> inheritedRolesOfExistingUser = new HashSet<>();

        user.getAssignedGroups().forEach((String groupId) -> {
            List<String> roles = findGroup(groupId).getAssignedRoles();
            inheritedRolesOfExistingUser.addAll(roles);
        });

        Set<String> mergedRoleSet = new HashSet<>();

        //Merge assigned roles and roles from assigned groups
        mergedRoleSet.addAll(customRolesFromUser);
        mergedRoleSet.addAll(customRolesFromExistingUser);
        mergedRoleSet.addAll(inheritedRolesOfUser);
        mergedRoleSet.addAll(inheritedRolesOfExistingUser);

        user.setAssignedRoles(List.copyOf(mergedRoleSet));

        return user;
    }

    /**
     * Find group by the id.
     *
     * @param id - id
     * @return GroupResponseDTO
     * @throws ResourceNotFoundException If the group doesn't exist.
     */
    @Override
    public GroupResponseDTO findGroup(String id) {
        Group group = couchDBService.find(Group.class, id);

        if (ObjectUtils.isEmpty(group)) {
            log.info("No group exists with the id: {}", id);
            throw new ResourceNotFoundException()
                    .addDetail("Group not found for the provided id", id, ErrorDetailCode.GROUP_NOT_FOUND);
        }
        return ModelUtility.mapDtoToEntity(group, GroupResponseDTO.class);
    }

    /**
     * Find all groups
     *
     * @param rowsPerPage - rows per page
     * @param param       - param
     * @return GroupsResponseDTO
     */
    @Override
    public GroupsResponseDTO findAllGroups(int rowsPerPage, String param) {
        CouchDBFindAllResponse<Group> response = couchDBService.findAll(Group.class, rowsPerPage, param);


        return GroupsResponseDTO.builder()
                .isHasNext(response.isHasNext())
                .isHasPrevious(response.isHasPrevious())
                .nextParam(response.getNextParam())
                .previousParam(response.getPreviousParam())
                .pageNumber(response.getPageNumber())
                .totalResults(response.getTotalResults())
                .items(response.getResultList().stream()
                        .map(group -> ModelUtility.mapDtoToEntity(group, GroupResponseDTO.class))
                        .collect(Collectors.toList()))
                .build();
    }

    /**
     * Find role by the id.
     *
     * @param id - id
     * @return RoleResponseDTO
     * @throws ResourceNotFoundException If the group doesn't exist.
     */
    @Override
    public RoleResponseDTO findRole(String id) {
        Role role = couchDBService.find(Role.class, id);

        if (ObjectUtils.isEmpty(role)) {
            log.info("No role exists with the id: {}", id);
            throw new ResourceNotFoundException()
                    .addDetail("Role not found for the provided id", id, ErrorDetailCode.ROLE_NOT_FOUND);
        }
        return ModelUtility.mapDtoToEntity(role, RoleResponseDTO.class);
    }

    /**
     * Find all groups
     *
     * @param rowsPerPage - rows per page
     * @param param       - param
     * @return RolesResponseDTO
     */
    @Override
    public RolesResponseDTO findAllRoles(int rowsPerPage, String param) {
        CouchDBFindAllResponse<Role> response = couchDBService.findAll(Role.class, rowsPerPage, param);


        return RolesResponseDTO.builder()
                .isHasNext(response.isHasNext())
                .isHasPrevious(response.isHasPrevious())
                .nextParam(response.getNextParam())
                .previousParam(response.getPreviousParam())
                .pageNumber(response.getPageNumber())
                .totalResults(response.getTotalResults())
                .items(response.getResultList().stream()
                        .map(role -> ModelUtility.mapDtoToEntity(role, RoleResponseDTO.class))
                        .collect(Collectors.toList()))
                .build();
    }

    /**
     * Save role
     *
     * @param id   - id
     * @param role - role
     * @return Saved role
     * @throws DataSavingException   If a failure occurred during the save.
     * @throws DataConflictException If a conflict is detected during the save.
     */
    @Override
    public RoleResponseDTO saveRole(String id, Role role) {

        if (ObjectUtils.isEmpty(couchDBService.find(Group.class, id))) {

            role.set_id(id);
            role.setType(Role.class.getName().toLowerCase(Locale.ENGLISH));
            boolean isSaveSuccess = couchDBService.save(role);

            if (isSaveSuccess) {
                log.info("Successfully saved the role: {}", role.toString());
                Role savedRole = couchDBService.find(Role.class, id);
                return ModelUtility.mapDtoToEntity(savedRole, RoleResponseDTO.class);
            }

            log.error("Error occurred while saving the role: {}", role.toString());
            throw new DataSavingException()
                    .addDetail("Error occurred while saving the group", role.toString(), ErrorDetailCode.ERROR_SAVING_ROLE);
        }

        log.error("Role with id: {} already exists", id);
        throw new DataConflictException()
                .addDetail("Role already exists", id, ErrorDetailCode.ROLE_ALREADY_EXISTS);
    }

    /**
     * Update role
     *
     * @param id   - id
     * @param role - role
     * @return Updated role
     * @throws DataUpdatingException     If a failure occurred during the update.
     * @throws ResourceNotFoundException If the user doesn't exist.
     */
    @Override
    public RoleResponseDTO updateRole(String id, Role role) {

        Role existingRole = couchDBService.find(Role.class, id);

        if (ObjectUtils.isEmpty(existingRole)) {
            log.info("No role exists with the id: {}", id);
            throw new ResourceNotFoundException()
                    .addDetail("Role not found for the provided id", id, ErrorDetailCode.ROLE_NOT_FOUND);
        }

        role.set_id(id);
        role.set_rev(existingRole.get_rev());
        boolean isUpdateSuccess = couchDBService.update(role);

        if (isUpdateSuccess) {
            log.info("Successfully updated the role: {}", role.toString());
            Role updatedRole = couchDBService.find(Role.class, id);
            return ModelUtility.mapDtoToEntity(updatedRole, RoleResponseDTO.class);
        }

        log.error("Error occurred while updating the role: {}", role.toString());
        throw new DataUpdatingException()
                .addDetail("Error occurred while saving the role", role.toString(), ErrorDetailCode.ERROR_UPDATING_ROLE);
    }

    /**
     * Remove role
     *
     * @param id - id
     */
    @Override
    public void removeRole(String id) {

        Role existingRole = couchDBService.find(Role.class, id);

        if (ObjectUtils.isEmpty(existingRole)) {
            log.info("No role exists with the id: {}", id);
            throw new ResourceNotFoundException()
                    .addDetail("Role not found for the provided id", id, ErrorDetailCode.ROLE_NOT_FOUND);
        }

        String rev = existingRole.get_rev();
        boolean isRemovalSuccess = couchDBService.remove(id, rev);

        if (isRemovalSuccess) {
            log.info("Successfully removed the role with id: {} and rev: {}", id, rev);
            return;
        }

        log.error("Error occurred while removing the role id: {} and rev: {}", id, rev);
        throw new DataRemovalException()
                .addDetail("Error occurred while removing the group", id, ErrorDetailCode.ERROR_REMOVING_ROLE);
    }


}
