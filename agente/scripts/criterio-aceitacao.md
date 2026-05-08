### Critérios de Aceitação

**Dado** que sou um usuário não autenticado,  
**Quando** informo name, email válido e único e password entre 6 e 10 caracteres,  

**Então** o sistema cria a conta com sucesso e retorna todos os dados do usuário (exceto password), com bio, image e updated_at podendo ser nulos.  

**Dado** que informo um email já cadastrado,  
**Quando** tento registrar a conta,  
**Então** o sistema retorna erro "E-mail já existe".  

**Dado** que informo uma senha com menos de 6 ou mais de 10 caracteres,  
**Quando** tento registrar,  
**Então** o sistema retorna erro "Senha deve ter entre 6 e 10 caracteres".  

**Dado** que informo um email em formato inválido,  
**Quando** tento registrar,  
**Então** o sistema retorna erro "Formato de e-mail inválido".