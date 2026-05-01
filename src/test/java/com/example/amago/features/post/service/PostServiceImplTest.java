package com.example.amago.features.post.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import com.example.amago.core.exceptions.DomainException;
import com.example.amago.core.services.upload.CloudinaryUploadService;
import com.example.amago.features.post.dto.request.PostRegisterDto;
import com.example.amago.features.post.dto.request.PostUpdateDto;
import com.example.amago.features.post.dto.response.PostDetailDto;
import com.example.amago.features.post.enums.PostTypeEnum;
import com.example.amago.features.post.model.PostModel;
import com.example.amago.features.post.repository.PostRepository;
import com.example.amago.features.user.mapper.PostMapper;
import com.example.amago.features.user.model.UserModel;

@ExtendWith(MockitoExtension.class)
public class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private CloudinaryUploadService cloudinaryUploadService;

    @Mock
    private PostMapper postMapper;

    @Mock
    private MultipartFile file;

    @InjectMocks
    private PostServiceImpl service;

    private UserModel user;
    private PostModel post;

    @BeforeEach
    void setup() {
        user = new UserModel();
        user.setId("1");

        post = new PostModel();
        post.setId("post1");
        post.setTitle("titulo");
        post.setDescription("desc");
        post.setCreatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Deve registrar post com sucesso sem arquivo")
    void shouldRegisterPostWithoutFile() {

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null));

        PostRegisterDto dto = new PostRegisterDto(user, "titulo", "desc", null);

        when(postMapper.toEntityRegister(dto)).thenReturn(post);
        when(postRepository.save(any())).thenReturn(post);

        PostDetailDto result = service.register(dto);

        assertNotNull(result);
        verify(postRepository, atLeastOnce()).save(any());
    }

    @Test
    @DisplayName("Deve registrar post com upload de imagem")
    void shouldRegisterPostWithImage() {

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null));

        PostRegisterDto dto = new PostRegisterDto(user, "titulo", "desc", file);

        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("image/png");

        when(postMapper.toEntityRegister(dto)).thenReturn(post);
        when(postRepository.save(any())).thenReturn(post);
        when(cloudinaryUploadService.uploadFile(any(), any()))
                .thenReturn("url");

        PostDetailDto result = service.register(dto);

        assertNotNull(result);
        verify(cloudinaryUploadService).uploadFile(any(), any());
    }

        @Test
    @DisplayName("Deve registrar post com upload de vídeo")
    void shouldRegisterPostWithVideo() {

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null));

        PostRegisterDto dto = new PostRegisterDto(user, "titulo", "desc", file);

        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("video/mp4");

        when(postMapper.toEntityRegister(dto)).thenReturn(post);
        when(postRepository.save(any())).thenReturn(post);
        when(cloudinaryUploadService.uploadFile(any(), any()))
                .thenReturn("url");

        PostDetailDto result = service.register(dto);

        assertNotNull(result);
        verify(cloudinaryUploadService).uploadFile(any(), any());
    }

    @Test
    @DisplayName("Deve falhar ao registrar sem autenticação")
    void shouldFailRegisterWithoutAuth() {

        SecurityContextHolder.clearContext();

        PostRegisterDto dto = new PostRegisterDto(user, "titulo", "desc", null);

        assertThrows(DomainException.class,
                () -> service.register(dto));
    }

    @Test
    @DisplayName("Deve retornar detalhe com sucesso")
    void shouldReturnDetail() {

        when(postRepository.findById("1"))
                .thenReturn(Optional.of(post));

        PostDetailDto result = service.detail("1");

        assertNotNull(result);
    }

    @Test
    @DisplayName("Deve falhar quando post não existe")
    void shouldFailDetailNotFound() {

        when(postRepository.findById("1"))
                .thenReturn(Optional.empty());

        assertThrows(DomainException.class,
                () -> service.detail("1"));
    }

    @Test
    @DisplayName("Deve atualizar post com sucesso")
    void shouldUpdatePost() {

        when(postRepository.findById("1"))
                .thenReturn(Optional.of(post));

        when(postRepository.save(any())).thenReturn(post);

        PostUpdateDto dto = new PostUpdateDto("novo", "desc", null, null);

        PostDetailDto result = service.update("1", dto);

        assertNotNull(result);
    }

 @Test
    @DisplayName("Deve atualizar post com novo arquivo de imagem")
    void shouldUpdateWithImage() {

        when(postRepository.findById("1"))
                .thenReturn(Optional.of(post));

        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("image/png");

        when(cloudinaryUploadService.uploadFile(any(), any()))
                .thenReturn("url");

        when(postRepository.save(any())).thenReturn(post);

        PostUpdateDto dto = new PostUpdateDto("novo", "desc", PostTypeEnum.IMAGE, file);

        PostDetailDto result = service.update("1", dto);

        assertNotNull(result);
        verify(cloudinaryUploadService).uploadFile(any(), any());
    }

    @Test
    @DisplayName("Deve atualizar post com novo arquivo de vídeo")
    void shouldUpdateWithVideo() {

        when(postRepository.findById("1"))
                .thenReturn(Optional.of(post));

        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("video/mp4");

        when(cloudinaryUploadService.uploadFile(any(), any()))
                .thenReturn("url");

        when(postRepository.save(any())).thenReturn(post);

        PostUpdateDto dto = new PostUpdateDto("novo", "desc", PostTypeEnum.VIDEO, file);

        PostDetailDto result = service.update("1", dto);

        assertNotNull(result);
        verify(cloudinaryUploadService).uploadFile(any(), any());
    }

    @Test
    @DisplayName("Deve falhar update quando post não existe")
    void shouldFailUpdateNotFound() {

        when(postRepository.findById("1"))
                .thenReturn(Optional.empty());

        PostUpdateDto dto = new PostUpdateDto("novo", "desc", null, null);

        assertThrows(DomainException.class,
                () -> service.update("1", dto));
    }

    @Test
    @DisplayName("Deve deletar post com sucesso")
    void shouldDeletePost() {

        when(postRepository.findById("1"))
                .thenReturn(Optional.of(post));

        doNothing().when(postRepository).delete(any());

        service.delete("1");

        verify(postRepository).delete(any());
    }

    @Test
    @DisplayName("Deve deletar post com arquivo")
    void shouldDeletePostWithFile() {

        post.setFile("url");
        post.setPostType(PostTypeEnum.IMAGE);

        when(postRepository.findById("1"))
                .thenReturn(Optional.of(post));

        service.delete("1");

        verify(cloudinaryUploadService).deleteFile(any(), eq(false));
    }

    @Test
    @DisplayName("Deve falhar delete quando não existe")
    void shouldFailDeleteNotFound() {

        when(postRepository.findById("1"))
                .thenReturn(Optional.empty());

        assertThrows(DomainException.class,
                () -> service.delete("1"));
    }

    @Test
    @DisplayName("Deve listar todos os posts com sucesso")
    void shouldListAllPosts() {

        when(postRepository.findAllByOrderByCreatedAtDesc())
                .thenReturn(List.of(post));

        assertNotNull(service.list());
    }

    @Test
    @DisplayName("Deve listar posts do tipo 'TEXT' com sucesso")
    void shouldListByTypeText() {

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null));

        when(postRepository.findAllByPostTypeAndUserIdOrderByCreatedAtDesc(
                any(), any()))
                .thenReturn(List.of(post));

        assertNotNull(service.listByPostType("TEXT"));
    }

    @Test
    @DisplayName("Deve listar posts do tipo 'IMAGE' com sucesso")
    void shouldListByTypeImage() {

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null));

        when(postRepository.findAllByPostTypeAndUserIdOrderByCreatedAtDesc(
                any(), any()))
                .thenReturn(List.of(post));

        assertNotNull(service.listByPostType("IMAGE"));
    }


    @Test
    @DisplayName("Deve listar posts do tipo 'VIDEO' com sucesso")
    void shouldListByTypeVideo() {

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null));

        when(postRepository.findAllByPostTypeAndUserIdOrderByCreatedAtDesc(
                any(), any()))
                .thenReturn(List.of(post));

        assertNotNull(service.listByPostType("VIDEO"));
    }


    @Test
    @DisplayName("Deve falhar listByType sem autenticação")
    void shouldFailListByTypeWithoutAuth() {

        SecurityContextHolder.clearContext();

        assertThrows(DomainException.class,
                () -> service.listByPostType("TEXT"));
    }
}
