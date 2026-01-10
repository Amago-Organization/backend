package com.example.pulsepost.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PostTypeEnum {
    TEXT("TEXT"),
    IMAGE("IMAGE"),
    VIDEO("VIDEO");

    private String typePost;
}
