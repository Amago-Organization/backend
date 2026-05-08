### Critérios de Aceitação - Login de Usuário

**Dado** que possuo uma conta cadastrada,  
**Quando** informo email e senha corretos via API,  
**Então** o sistema retorna um token de autenticação válido (JWT).  

**Dado** que informo senha incorreta ou email não cadastrado,  
**Quando** tento fazer login,  
**Então** o sistema retorna erro "E-mail ou senha inválidos".  

**Dado** que informo uma senha sem ao menos 1 caractere maiúsculo, 1 caractere minúsculo, 1 número e 1 caractere especial,  
**Quando** tento fazer login,  
**Então** o sistema exibe erro "A senha deve conter ao menos 1 caractere maiúsculo, 1 caractere minúsculo, 1 número e 1 caractere especial!". 

**Dado** que tento fazer login sem informar email ou senha,  
**Quando** envio a requisição,  
**Então** o sistema retorna erro de campo obrigatório.
