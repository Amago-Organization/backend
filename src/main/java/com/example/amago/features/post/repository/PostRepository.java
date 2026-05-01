package com.example.amago.features.post.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.example.amago.features.post.enums.PostTypeEnum;
import com.example.amago.features.post.model.PostModel;
import com.example.amago.features.user.model.UserModel;

@CrossOrigin(origins = "*")
public interface PostRepository extends JpaRepository<PostModel, String> {
    List<PostModel> findAllByOrderByCreatedAtDesc();

    List<PostModel> findAllByPostTypeAndUserIdOrderByCreatedAtDesc(PostTypeEnum postType, UserModel userId);
}
