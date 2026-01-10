package com.example.pulsepost.presentation.messages;

public class ExceptionMessage {
    public static final String invalidAuthentication = "Autenticação inválida!";

    public static final String unauthorizedAccess = "Acesso não autorizado!";
    
    public static final String uploadFileError = "Erro ao fazer upload do arquivo!";
    
    public static final String deleteFileError = "Erro ao apagar o arquivo!";
    
    public static final String invalidFileType = "Tipo de arquivo inválido!";

    public static final String attributeUsed(String attribute) {
        return attribute + " já em uso!";
    }

    public static final String notFound(String name) {
        return name + " não encontrado!";
    }

    public static final String token(String erro) {
        return "Erro ao gerar o token! Motivo de erro: " + erro;
    }

}
