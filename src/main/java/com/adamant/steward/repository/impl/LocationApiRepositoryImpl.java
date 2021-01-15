package com.adamant.steward.repository.impl;

import com.adamant.steward.dto.user_management.*;
import com.adamant.steward.entity.user_management.Group;
import com.adamant.steward.entity.user_management.User;
import com.adamant.steward.exception.DataLoadingException;
import com.adamant.steward.exception.enums.ErrorDetailCode;
import com.adamant.steward.repository.LocationApiRepository;
import com.adamant.steward.util.ModelUtility;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static com.adamant.steward.util.UrlConstants.*;

@Repository
@Slf4j
@RequiredArgsConstructor
public class LocationApiRepositoryImpl implements LocationApiRepository {

    @Value("${location.service.host.endpoint}")
    private String locationServiceHost;

    private final RestTemplate restTemplate;

    /**
     * Create user
     *
     * @param userCreateRequestDTO - user create request
     * @return Optional<User>
     */
    @Override
    public Optional<User> createUser(UserCreateRequestDTO userCreateRequestDTO) {

        if (ObjectUtils.isEmpty(userCreateRequestDTO)) {
            log.error("User is null");
            throw new IllegalArgumentException("User is null");
        }

        UserResponseDTO userResponseDTO;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            HttpEntity<String> request =
                    new HttpEntity<>(objectMapper.writeValueAsString(userCreateRequestDTO), null);

            userResponseDTO = restTemplate.postForObject(locationServiceHost.concat(CREATE_USER), request, UserResponseDTO.class);

        } catch (HttpClientErrorException.NotFound e) {
            log.error("Http client error occurred while creating user", e);
            return Optional.empty();

        } catch (Exception e) {
            log.error("Error occurred while creating user", e);
            throw new DataLoadingException()
                    .addDetail("Error occurred while creating user", userCreateRequestDTO.getEmail(), ErrorDetailCode.LOCATION_SERVICE_NOT_AVAILABLE);
        }

        if (ObjectUtils.isEmpty(userResponseDTO)) {
            return Optional.empty();
        }

        User userResponse = ModelUtility.mapDtoToEntity(userResponseDTO, User.class);
        return Optional.of(userResponse);
    }

    /**
     * Create user
     *
     * @param id   - user id
     * @param userUpdateRequestDTO - user update request
     * @return Optional<User>
     */
    @Override
    public Optional<User> updateUser(String id, UserUpdateRequestDTO userUpdateRequestDTO) {

        if (!ObjectUtils.allNotNull(id, userUpdateRequestDTO)) {
            log.error("User id or User details for update is null");
            throw new IllegalArgumentException("User id or User details for update is null");
        }

        String contextPath = String.format(UPDATE_USER, id);
        UserResponseDTO userResponseDTO;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            HttpEntity<String> request =
                    new HttpEntity<>(objectMapper.writeValueAsString(userUpdateRequestDTO), null);

            userResponseDTO = restTemplate.postForObject(locationServiceHost.concat(contextPath), request, UserResponseDTO.class);

        } catch (HttpClientErrorException.NotFound e) {
            log.error("Http client error occurred while updating user", e);
            return Optional.empty();

        } catch (Exception e) {
            log.error("Error occurred while updating user", e);
            throw new DataLoadingException()
                    .addDetail("Error occurred while updating user", id, ErrorDetailCode.LOCATION_SERVICE_NOT_AVAILABLE);
        }

        if (ObjectUtils.isEmpty(userResponseDTO)) {
            return Optional.empty();
        }

        User userResponse = ModelUtility.mapDtoToEntity(userResponseDTO, User.class);
        return Optional.of(userResponse);
    }

    /**
     * Delete user
     *
     * @param id   - user id
     * @return Optional<User>
     */
    @Override
    public void deleteUser(String id) {

        if (ObjectUtils.isEmpty(id)) {
            log.error("User id is null");
            throw new IllegalArgumentException("User id is null");
        }

        String contextPath = String.format(DELETE_USER, id);

        try {

            restTemplate.delete(locationServiceHost.concat(contextPath));

        } catch (HttpClientErrorException.NotFound e) {
            log.error("Http client error occurred while deleting user", e);

        } catch (Exception e) {
            log.error("Error occurred while deleting user", e);
            throw new DataLoadingException()
                    .addDetail("Error occurred while deleting user", id, ErrorDetailCode.LOCATION_SERVICE_NOT_AVAILABLE);
        }
    }

    /**
     * Create user
     *
     * @param groupCreateRequestDTO - group create request
     * @return Optional<User>
     */
    @Override
    public Optional<Group> createGroup(GroupCreateRequestDTO groupCreateRequestDTO) {

        if (ObjectUtils.isEmpty(groupCreateRequestDTO)) {
            log.error("Group is null");
            throw new IllegalArgumentException("Group is null");
        }

        GroupResponseDTO groupResponseDTO;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            HttpEntity<String> request =
                    new HttpEntity<>(objectMapper.writeValueAsString(groupCreateRequestDTO), null);

            groupResponseDTO = restTemplate.postForObject(locationServiceHost.concat(CREATE_GROUP), request, GroupResponseDTO.class);

        } catch (HttpClientErrorException.NotFound e) {
            log.error("Http client error occurred while creating group", e);
            return Optional.empty();

        } catch (Exception e) {
            log.error("Error occurred while creating group", e);
            throw new DataLoadingException()
                    .addDetail("Error occurred while creating group", groupCreateRequestDTO.getName(), ErrorDetailCode.LOCATION_SERVICE_NOT_AVAILABLE);
        }

        if (ObjectUtils.isEmpty(groupResponseDTO)) {
            return Optional.empty();
        }

        Group groupResponse = ModelUtility.mapDtoToEntity(groupResponseDTO, Group.class);
        return Optional.of(groupResponse);
    }

    /**
     * Create user
     *
     * @param id   - group id
     * @param groupUpdateRequestDTO - group update request
     * @return Optional<User>
     */
    @Override
    public Optional<Group> updateGroup(String id, GroupUpdateRequestDTO groupUpdateRequestDTO) {

        if (!ObjectUtils.allNotNull(id, groupUpdateRequestDTO)) {
            log.error("Group id or Group details for update is null");
            throw new IllegalArgumentException("Group id or Group details for update is null");
        }

        String contextPath = String.format(UPDATE_GROUP, id);
        GroupResponseDTO groupResponseDTO;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            HttpEntity<String> request =
                    new HttpEntity<>(objectMapper.writeValueAsString(groupUpdateRequestDTO), null);

            groupResponseDTO = restTemplate.postForObject(locationServiceHost.concat(contextPath), request, GroupResponseDTO.class);

        } catch (HttpClientErrorException.NotFound e) {
            log.error("Http client error occurred while updating group", e);
            return Optional.empty();

        } catch (Exception e) {
            log.error("Error occurred while updating group", e);
            throw new DataLoadingException()
                    .addDetail("Error occurred while updating group", id, ErrorDetailCode.LOCATION_SERVICE_NOT_AVAILABLE);
        }

        if (ObjectUtils.isEmpty(groupResponseDTO)) {
            return Optional.empty();
        }

        Group groupResponse = ModelUtility.mapDtoToEntity(groupResponseDTO, Group.class);
        return Optional.of(groupResponse);
    }

    /**
     * Delete group
     *
     * @param id   - group id
     * @return Optional<Group>
     */
    @Override
    public void deleteGroup(String id) {
        if (ObjectUtils.isEmpty(id)) {
            log.error("Group id is null");
            throw new IllegalArgumentException("Group id is null");
        }

        String contextPath = String.format(DELETE_GROUP, id);

        try {

            restTemplate.delete(locationServiceHost.concat(contextPath));

        } catch (HttpClientErrorException.NotFound e) {
            log.error("Http client error occurred while deleting group", e);

        } catch (Exception e) {
            log.error("Error occurred while deleting group", e);
            throw new DataLoadingException()
                    .addDetail("Error occurred while deleting group", id, ErrorDetailCode.LOCATION_SERVICE_NOT_AVAILABLE);
        }
    }
}
