package com.example.amago.features.post.dto.response;

import java.time.LocalDateTime;

import com.example.amago.features.post.enums.PostTypeEnum;
import com.example.amago.features.user.dto.response.UserSummaryDto;

public record PostDetailDto(String id, String title, String description, PostTypeEnum postType, String file,
                LocalDateTime createdAt,
                LocalDateTime updatedAt, UserSummaryDto user) {

}
