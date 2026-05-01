# Âmago

O Âmago é uma rede social criada para compartilhar memórias de momentos íntimos já vividos, despertando aquele gostinho do passado, profundo e marcante, que merece ser revivido mais uma vez.

O projeto, antes chamado de *Pulse Post*, foi refatorado para que o aplicativo não seja apenas uma rede social de criação de posts, mas uma plataforma que traga mais significado aos usuários, priorizando práticas de gestão de projeto, qualidade de software, uso de IA e clareza arquitetural.

Ao explorar as branches do repositório, é possível acompanhar a evolução do sistema e entender as decisões técnicas adotadas ao longo do desenvolvimento.

## Tecnologias e Abordagens

O backend foi desenvolvido utilizando Spring Boot, seguindo uma abordagem de MVC Modular (Feature-Based), onde cada funcionalidade é organizada de forma independente, promovendo maior coesão e escalabilidade.

A estrutura do projeto foi pensada para garantir separação clara de responsabilidades, baixo acoplamento e alta testabilidade, mantendo as regras de negócio centralizadas na camada de service.

Para persistência de dados, foi utilizado o PostgreSQL, integrado ao projeto por meio do Spring Data JPA com Hibernate, facilitando o acesso ao banco de dados de forma eficiente e declarativa.

A autenticação da aplicação é baseada em JWT (JSON Web Token), garantindo segurança nas rotas protegidas e controle de acesso dos usuários.

Para o upload e gerenciamento de arquivos (imagens e vídeos), foi integrado o Cloudinary, permitindo armazenamento escalável e otimizado de mídia.

O projeto também conta com:

Validações aplicadas nos controllers e na camada de serviço
Tratamento centralizado de exceções com DomainException
Conversão de dados utilizando mappers (DTO ↔ Model)
Testes unitários com JUnit e Mockito, focados na camada de regras de negócio

Além disso, foi adotada uma organização por features, permitindo que cada módulo do sistema evolua de forma independente, mantendo a clareza arquitetural mesmo com o crescimento do projeto.

## Organização do Projeto

A estrutura do projeto segue o padrão de modularização por domínio:

```text
amago/
├── features/
│   ├── post/
│   │   ├── controller/
│   │   ├── dto/
│   │   ├── enums/
│   │   ├── repository/
│   │   ├── mapper/
│   │   ├── service/
│   │   └── model/
│   ├── user/
│   │   ├── controller/
│   │   ├── dto/
│   │   ├── repository/
│   │   ├── mapper/
│   │   ├── service/
│   │   └── model/
├── core/
│   ├── config/
│   ├── exceptions/
│   ├── services/
└── └── utils/
```

Essa organização facilita a manutenção, melhora a legibilidade do código e permite escalar o sistema de forma mais controlada.

## Dependências Implantadas

- Spring Web
- Spring Data JPA
- Lombok
- Spring Boot DevTools
- PostgreSQL Driver
- Spring Security
- Validation
- Java JWT
- Cloudinary HTTP44

## Incialização
    
  - instale o JDK17; 
  - Instale o Maven na sua máquina;
  - Instale o PostgreSQL na sua máquina;
  - Crie um usuário, senha e banco de dados referente ao projeto no postgreSQL na sua máquina, se não houver;
  - Duplique o arquivo  [.env.example](.env.example), renome-o para *.env* e adicione os valores das chaves;
  - Obs:
    - Para rodar em um dispositivo diferente, abra o terminal do seu S.O e indentifique o endereço ip com:
        - ifconfig
    - Depois coloque esse endereço ip no valor da chave *SERVER_ADDRESS* no arquivo *.env* recém criado.
  - Copie e cole a linha abaixo no terminal para reconstruir o projeto:
    - mvn clean
  - Copie e cole a linha abaixo no terminal para executar o projeto:
    - mvn spring-boot:run

## Endpoints

Importe o seguinte arquivo de endpoit abaixo para testar as requisições http no Insomnia ou Postman:

[endpoints.yaml](./endpoints.yaml)
    