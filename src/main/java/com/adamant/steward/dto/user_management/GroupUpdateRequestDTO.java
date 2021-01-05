package com.adamant.steward.dto.user_management;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupUpdateRequestDTO {

    @JsonProperty("assigned_roles")
    private List<String> assignedRoles;

    @JsonProperty("created_by")
    private String createdBy;
}
