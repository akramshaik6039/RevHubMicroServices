package com.revhub.user.dto;

import lombok.Data;

@Data
public class ProfileUpdateRequest {
    private String bio;
    private String profilePicture;
    private Boolean isPrivate;
}
