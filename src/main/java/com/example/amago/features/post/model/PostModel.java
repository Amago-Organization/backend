package com.example.amago.features.post.model;

import java.time.LocalDateTime;

import com.example.amago.core.utils.validations.GroupValidation;
import com.example.amago.features.post.enums.PostTypeEnum;
import com.example.amago.features.user.model.UserModel;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Table(name = "posts")
@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PostModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    @Column(name = "id")
    private String id;

    @NotNull(groups = GroupValidation.Create.class)
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonProperty(access = Access.WRITE_ONLY)
    private UserModel userId;

    @NotBlank(groups = GroupValidation.Create.class)
    @Size(max = 255)
    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @NotBlank(groups = GroupValidation.Create.class)
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "post_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PostTypeEnum postType;

    @Column(name = "file", nullable = true, columnDefinition = "TEXT")
    private String file;

    @NotNull(groups = GroupValidation.Create.class)
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = true)
    private LocalDateTime updatedAt;
}
