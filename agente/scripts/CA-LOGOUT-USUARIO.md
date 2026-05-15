### Critérios de Aceitação - Logout de Usuário

**Dado** que estou autenticado,  
**Quando** solicito logout via API,  
**Então** o token é invalidado e a sessão é encerrada com sucesso.  

**Dado** que tento fazer logout sem estar autenticado,  
**Quando** envio a requisição,  
**Então** o sistema retorna erro "Usuário não autenticado".