package com.adamant.steward.controller;

import com.adamant.steward.dto.user_management.GroupCreateRequestDTO;
import com.adamant.steward.dto.user_management.GroupResponseDTO;
import com.adamant.steward.dto.user_management.GroupUpdateRequestDTO;
import com.adamant.steward.dto.user_management.GroupsResponseDTO;
import com.adamant.steward.dto.user_management.RoleResponseDTO;
import com.adamant.steward.dto.user_management.RolesResponseDTO;
import com.adamant.steward.dto.user_management.UserCreateRequestDTO;
import com.adamant.steward.dto.user_management.UserResponseDTO;
import com.adamant.steward.dto.user_management.UserUpdateRequestDTO;
import com.adamant.steward.dto.user_management.UsersResponseDTO;
import com.adamant.steward.entity.error_data.ErrorData;
import com.adamant.steward.entity.user_management.Group;
import com.adamant.steward.entity.user_management.User;
import com.adamant.steward.service.LocationApiService;
import com.adamant.steward.service.UserManagementService;
import com.adamant.steward.util.ApiSpecConstants;
import com.adamant.steward.util.ModelUtility;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Validated
@RequiredArgsConstructor
public class UserManagementController {

    private final UserManagementService userManagementService;

    private final LocationApiService locationApiService;

    @Operation(
            summary = "List all users",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully listing all users",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorData.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Example success response for listing all users",
                                                    value = ApiSpecConstants.LIST_ALL_USERS_SUCCESS_RESPONSE
                                            )}
                            ))
            }
    )
    @GetMapping(value = "/users")
    public ResponseEntity<UsersResponseDTO> listAllUsers(
            @Parameter(description = "Rows per page", example = "10")
            @RequestParam(name = "rowsPerPage") int rowsPerPage,

            @Parameter(description = "Search param")
            @RequestParam(name = "param", required = false) String param) {

        UsersResponseDTO usersResponseDTO = userManagementService.findAllUsers(rowsPerPage, param);

        return new ResponseEntity<>(usersResponseDTO, HttpStatus.OK);
    }


    @Operation(
            summary = "Create a user",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Successfully creating a user",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserResponseDTO.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Example success response for creating a user",
                                                    value = ApiSpecConstants.CREATE_USER_SUCCESS_RESPONSE
                                            )}
                            ))
            }
    )
    @PostMapping(value = "/users")
    public ResponseEntity<UserResponseDTO> createUser(
            @Parameter(
                    description = "User to add. Cannot null or empty.",
                    required = true,
                    schema = @Schema(
                            implementation = UserCreateRequestDTO.class
                    ))
            @Valid @RequestBody UserCreateRequestDTO userCreateRequestDTO) {

        User user = locationApiService.createUser(userCreateRequestDTO);

        UserResponseDTO userResponseDTO = ModelUtility.mapDtoToEntity(user, UserResponseDTO.class);

        return new ResponseEntity<>(userResponseDTO, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Retrieve a user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieving a user",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorData.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Example success response for retrieving a user",
                                                    value = ApiSpecConstants.RETRIEVE_USER_SUCCESS_RESPONSE
                                            )}
                            ))
            }
    )
    @GetMapping(value = "/users/{id}")
    public ResponseEntity<UserResponseDTO> retrieveUser(

            @Parameter(description = "User ID", example = "john@gmail.com")
            @PathVariable(name = "id") String id) {

        UserResponseDTO userResponseDTO = userManagementService.findUser(id);

        return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "Update a user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully updating a user",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorData.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Example success response for updating a user",
                                                    value = ApiSpecConstants.UPDATE_USER_SUCCESS_RESPONSE
                                            )}
                            ))
            }
    )
    @PutMapping(value = "/users/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @Parameter(description = "User ID", example = "1232134")
            @PathVariable(name = "id") String id,
            @Parameter(
                    description = "User details to update. Cannot null or empty.",
                    required = true,
                    schema = @Schema(
                            implementation = UserUpdateRequestDTO.class
                    ))
            @Valid
            @RequestBody UserUpdateRequestDTO userUpdateRequestDTO) {

        User user = locationApiService.updateUser(id, userUpdateRequestDTO);

        UserResponseDTO userResponseDTO = ModelUtility.mapDtoToEntity(user, UserResponseDTO.class);

        return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete a user",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Successfully deleting a user"
                    )
            }
    )
    @DeleteMapping(value = "/users/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "User ID", example = "1232134")
            @PathVariable(name = "id") String id
    ) {

        locationApiService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "List all groups",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully listing all groups",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorData.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Example success response for listing all groups",
                                                    value = ApiSpecConstants.LIST_ALL_GROUPS_SUCCESS_RESPONSE
                                            )}
                            ))
            }
    )
    @GetMapping(value = "/groups")
    public ResponseEntity<GroupsResponseDTO> listAllGroups(
            @Parameter(description = "Rows per page", example = "10")
            @RequestParam(name = "rowsPerPage") int rowsPerPage,

            @Parameter(description = "Search param")
            @RequestParam(name = "param", required = false) String param) {

        GroupsResponseDTO groupsResponseDTO = userManagementService.findAllGroups(rowsPerPage, param);

        return new ResponseEntity<>(groupsResponseDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "Create a group",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Successfully creating a group",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorData.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Example success response for creating a group",
                                                    value = ApiSpecConstants.CREATE_GROUP_SUCCESS_RESPONSE
                                            )}
                            ))
            }
    )
    @PostMapping(value = "/group")
    public ResponseEntity<GroupResponseDTO> createGroup(
            @Parameter(
                    description = "Group to add. Cannot null or empty.",
                    required = true,
                    schema = @Schema(
                            implementation = GroupCreateRequestDTO.class
                    ))
            @Valid @RequestBody GroupCreateRequestDTO groupCreateRequestDTO) {

        Group group = locationApiService.createGroup(groupCreateRequestDTO);

        GroupResponseDTO groupResponseDTO = ModelUtility.mapDtoToEntity(group, GroupResponseDTO.class);

        return new ResponseEntity<>(groupResponseDTO, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Retrieve a group",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieving a group",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorData.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Example success response for retrieving a group",
                                                    value = ApiSpecConstants.RETRIEVE_GROUP_SUCCESS_RESPONSE
                                            )}
                            ))
            }
    )
    @GetMapping(value = "/groups/{id}")
    public ResponseEntity<GroupResponseDTO> retrieveGroup(

            @Parameter(description = "Group ID", example = "12")
            @PathVariable(name = "id") String id) {

        GroupResponseDTO groupResponseDTO = userManagementService.findGroup(id);

        return new ResponseEntity<>(groupResponseDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "Update a group",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully updating a group",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorData.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Example success response for updating a group",
                                                    value = ApiSpecConstants.UPDATE_GROUP_SUCCESS_RESPONSE
                                            )}
                            ))
            }
    )
    @PutMapping(value = "/groups/{id}")
    public ResponseEntity<GroupResponseDTO> updateGroup(
            @Parameter(description = "Group ID", example = "12")
            @PathVariable(name = "id") String id,
            @Parameter(
                    description = "Group details to update. Cannot null or empty.",
                    required = true,
                    schema = @Schema(
                            implementation = GroupUpdateRequestDTO.class
                    ))
            @Valid
            @RequestBody GroupUpdateRequestDTO groupUpdateRequestDTO) {

        Group group = locationApiService.updateGroup(id, groupUpdateRequestDTO);

        GroupResponseDTO groupResponseDTO = ModelUtility.mapDtoToEntity(group, GroupResponseDTO.class);

        return new ResponseEntity<>(groupResponseDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete a group",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully deleting a group"
                    )
            }
    )
    @DeleteMapping(value = "/groups/{id}")
    public ResponseEntity<Void> deleteGroup(
            @Parameter(description = "Group ID", example = "12")
            @PathVariable(name = "id") String id
    ) {

        locationApiService.deleteGroup(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "List all roles",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully listing all roles",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorData.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Example success response for listing all roles",
                                                    value = ApiSpecConstants.LIST_ALL_ROLES_SUCCESS_RESPONSE
                                            )}
                            ))
            }
    )
    @GetMapping(value = "/roles")
    public ResponseEntity<RolesResponseDTO> listAllRoles(
            @Parameter(description = "Rows per page", example = "10")
            @RequestParam(name = "rowsPerPage") int rowsPerPage,

            @Parameter(description = "Search param")
            @RequestParam(name = "param", required = false) String param) {

        RolesResponseDTO rolesResponseDTO = userManagementService.findAllRoles(rowsPerPage, param);

        return new ResponseEntity<>(rolesResponseDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "Retrieve a role",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieving a role",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorData.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Example success response for retrieving a user",
                                                    value = ApiSpecConstants.RETRIEVE_ROLE_SUCCESS_RESPONSE
                                            )}
                            ))
            }
    )
    @GetMapping(value = "/roles/{id}")
    public ResponseEntity<RoleResponseDTO> retrieveRole(

            @Parameter(description = "Role ID", example = "2")
            @PathVariable(name = "id") String id) {

        RoleResponseDTO roleResponseDTO = userManagementService.findRole(id);

        return new ResponseEntity<>(roleResponseDTO, HttpStatus.OK);
    }
}
