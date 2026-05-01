# Pulse Post

O Pulse Post é um aplicativo de feed de postagens inspirado no Instagram, desenvolvido como parte de um teste técnico, com o objetivo de demonstrar meus conhecimentos em Spring Boot, arquitetura de software e boas práticas de desenvolvimento.

O projeto foi concebido e implementado em um período de 5 dias, priorizando organização, escalabilidade e clareza arquitetural.

Ao explorar as branches do repositório, é possível acompanhar a evolução do sistema e entender as decisões técnicas adotadas ao longo do desenvolvimento.

## Tecnologias usadas

- Spring Boot - Java
- MySql - SQL
- Cloudinary - Banco de Imagens e Vídeos

## Configurações em localhost
    
  - instale o JDK17; 
  - Instale o Maven na sua máquina;
  - Instale o MySQL na sua máquina;
  - Crie um usuário chamado postgres no postgreSQL, se não houver;
  - Altere a senha de usuário para postgres também;
  - Crie um banco de dados chamado "pulse_post_db";
  - No arquivo "aplication.properties", altere o valor de "JWT_SECRET" na chave secreta para qualquer palavra que você achar secreta
  - No arquivo "aplication.properties", adicione os dados enviados por e-mail da cloudinary.

## Dependências implantadas

- Spring Web
- Spring Data JPA
- Lombok
- Spring Boot DevTools
- PostgreSQL Driver
- Spring Security
- Validation
- Java JWT

## Inicialização

Execute o comando para limpar e reconstruir o projeto spring:
    - mvn clean package
    
Execute o comando para rodar o projeto na web:
    - mvn spring-boot:run

## Endpoints

[endpoints.yaml](./endpoints.yaml)
    