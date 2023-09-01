package com.marcosimon.autosurvey.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.marcosimon.autosurvey.organization.Organization;

public record UserOrgResponseDTO(
                                 @JsonProperty("userId")
                                 String userId,
                                 @JsonProperty("username")
                                 String username,
                                 @JsonProperty
                                 String email,
                                 @JsonProperty
                                 String roles,
                                 @JsonProperty
                                 String status
          ) {
}
