# Spring Boot Tweet API

## Descrição do Projeto

Este projeto é uma API simplificada para simular o funcionamento de um sistema de Tweets. Utiliza Spring Boot como framework principal, juntamente com Spring Security 6 e Java 17. Além disso, implementa o OAuth2 Resource Server para autenticação e autorização.

O objetivo principal é demonstrar o uso de controle de acesso granular utilizando tokens JWT e roles (perfis de acesso), bem como a utilização de annotations de segurança, tais como @PreAuthorize, nas controllers.

## Tecnologias Utilizadas
* Java 17
* Spring Boot
* JPA / Hibernate
* Maven
* Spring Security 6
* OAuth2 Resource Server
* JWT (JSON Web Tokens)

## Funcionalidades
* Registro de usuários
* Autenticação via OAuth2 e geração de token JWT
* Autorização baseada em roles (perfis de acesso)
* Postagem de tweets
* Listagem de tweets
* Controle de acesso granular utilizando annotations de segurança

## Como Executar

### Pré-requisitos: Java 17

```bash
# 1 - Clonar o repositório:
$ git clone https://github.com/Davi-Bandeira/Twitter-Simplified.git

# 2 - Entrar na pasta do projeto:
$ cd Twitter-Simplified

# 3 - Executar o projeto
$ ./mvnw spring-boot:run
```

## Autor
Davi Monteiro Bandeira

https://www.linkedin.com/in/davi-monteiro-bandeira/


