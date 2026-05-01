package com.example.amago.core.services.update;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.example.amago.core.exceptions.DomainException;
import com.example.amago.core.services.upload.CloudinaryUploadServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CloudinaryUploadServiceImplTest {
    @Mock
    private Cloudinary cloudinary;

    @Mock
    private Uploader uploader;

    @Mock
    private MultipartFile file;

    @InjectMocks
    private CloudinaryUploadServiceImpl service;

    @BeforeEach
    void setup() {
        service = new CloudinaryUploadServiceImpl(cloudinary);
    }

    @Test
    @DisplayName("Deve fazer upload de imagem com sucesso")
    void shouldUploadImageSuccessfully() throws Exception {

        when(file.getContentType()).thenReturn("image/png");
        when(file.getBytes()).thenReturn("img".getBytes());

        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(any(), any()))
                .thenReturn(Map.of("secure_url", "url"));

        String result = service.uploadFile(file, "id");

        assertEquals("url", result);
        verify(uploader).upload(any(), any());
    }

    @Test
    @DisplayName("Deve fazer upload de vídeo com sucesso")
    void shouldUploadVideoSuccessfully() throws Exception {

        when(file.getContentType()).thenReturn("video/mp4");
        when(file.getBytes()).thenReturn("video".getBytes());

        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(any(), any()))
                .thenReturn(Map.of("secure_url", "url-video"));

        String result = service.uploadFile(file, "id");

        assertEquals("url-video", result);
    }

    @Test
    @DisplayName("Deve falhar quando tipo de arquivo é inválido")
    void shouldFailWhenFileTypeIsInvalid() {

        when(file.getContentType()).thenReturn("application/pdf");

        assertThrows(DomainException.class,
                () -> service.uploadFile(file, "id"));
    }

    @Test
    @DisplayName("Deve falhar quando contentType é null")
    void shouldFailWhenContentTypeIsNull() {

        when(file.getContentType()).thenReturn(null);

        assertThrows(DomainException.class,
                () -> service.uploadFile(file, "id"));
    }

    @Test
    @DisplayName("Deve falhar quando ocorre IOException no upload")
    void shouldFailWhenUploadThrowsIOException() throws Exception {

        when(file.getContentType()).thenReturn("image/png");
        when(file.getBytes()).thenThrow(new IOException());

        assertThrows(DomainException.class,
                () -> service.uploadFile(file, "id"));
    }

    @Test
    @DisplayName("Deve deletar a imagem com sucesso")
    void shouldDeleteImageSuccessfully() throws Exception {

        when(cloudinary.uploader()).thenReturn(uploader);

        service.deleteFile("file-id", false);

        verify(uploader).destroy(contains("image/file-id"), any());
    }

    @Test
    @DisplayName("Deve deletar o vídeo com sucesso")
    void shouldDeleteVideoSuccessfully() throws Exception {

        when(cloudinary.uploader()).thenReturn(uploader);

        service.deleteFile("file-id", true);

        verify(uploader).destroy(contains("video/file-id"), any());
    }

    @Test
    @DisplayName("Deve falhar ao deletar uma imagem quando ocorre IOException")
    void shouldFailWhenDeleteImageThrowsIOException() throws Exception {

        when(cloudinary.uploader()).thenReturn(uploader);
        doThrow(new IOException())
                .when(uploader)
                .destroy(anyString(), any());

        assertThrows(DomainException.class,
                () -> service.deleteFile("file-id", false));
    }

    @Test
    @DisplayName("Deve falhar ao deletar um vídeo quando ocorre IOException")
    void shouldFailWhenDeleteVideoThrowsIOException() throws Exception {

        when(cloudinary.uploader()).thenReturn(uploader);
        doThrow(new IOException())
                .when(uploader)
                .destroy(anyString(), any());

        assertThrows(DomainException.class,
                () -> service.deleteFile("file-id", true));
    }
}
