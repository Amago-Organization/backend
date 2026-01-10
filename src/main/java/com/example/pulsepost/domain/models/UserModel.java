package com.example.pulsepost.domain.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.pulsepost.presentation.validations.GroupValidation;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Table(name = "users")
@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    @Column(name = "id")
    private String id;

    @NotBlank(groups = GroupValidation.Create.class)
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @NotBlank(groups = { GroupValidation.Create.class, GroupValidation.Login.class })
    @Size(max = 255)
    @Email
    @Column(name = "email", length = 255, nullable = false, unique = true)
    private String email;

    @NotBlank(groups = { GroupValidation.Create.class, GroupValidation.Login.class })
    @Size(min = 6)
    @Column(name = "password", nullable = false, columnDefinition = "TEXT")
    private String password;

    @Column(name = "bio", nullable = true, columnDefinition = "TEXT")
    private String bio;

    @Column(name = "image", nullable = true, columnDefinition = "TEXT")
    private String image;

    @NotNull(groups = GroupValidation.Create.class)
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = true)
    private LocalDateTime updatedAt;

    @JsonIgnore
    @OneToMany(mappedBy = "userId")
    List<PostModel> posts = new ArrayList<>();

}