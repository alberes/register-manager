# Projeto: Gerenciamento de Usuários
Este projeto é responsável por gerenciar usuários, incluindo a criação, edição, exclusão e validação de dados, como a restrição de unicidade no campo de email.

Funcionalidades
* Adicionar novos usuários e endereços.
* Editar informações de usuários e endereços existentes.
* Excluir usuários e endereços.
* Garantir que o campo de email seja único no banco de dados.

### Estrutura do Banco de Dados
O projeto utiliza uma tabela chamada user_account. A seguinte restrição foi adicionada para garantir a unicidade do campo de email:
alter table if exists user_account
add constraint unique_user_account_email unique (email)

Tabelas user_account_role e address.

### Tecnologias Utilizadas
* Linguagem: Java e SQL
* Banco de Dados: [Postgres](https://www.postgresql.org/)
* Frameworks: [Spring Boot 3.4.5](https://start.spring.io/)
* Dependencias: Spring Security, jjwt, JPA, lombok, mapstruct, jackson, openfeign
* JDK: 17
* IDE: [Intellij](https://www.jetbrains.com/idea/)
* Gerenciado de dependencias: [Apache Maven 3.9.9](https://maven.apache.org/)
* Container: [Docker](https://www.docker.com/) e [Docker Hub](https://hub.docker.com/)
* Ferramentas: [Postman](https://www.postman.com/) [Google Chrome
   Versão 136.0.7103.93 (Versão oficial) 64 bits](https://www.google.com/intl/pt-BR/chrome/)

### Como Executar
1. Clone o repositório: git clone https://github.com/alberes/register-manager
2. Configure o banco de dados:
- Banco de dados: register_manager
- Tabelas: user_account, user_account_role e address
Certifique-se de que o banco de dados está configurado corretamente.
A aplicação irá criar as tabelas automaticamento caso não exista ou execute o script antes que se encontra no projeto.
Localizar o arquivo [SUB_DIRETORIOS]/register-manager/DDL.sql
3. Usando uma imagem Docker (Opcional)

 Um opção é criar um container docker com a imagem do Postgres, abaixo um exemplo que configurar usuário, senha e cria o banco de dados.
```
docker run --name postgresdb -p 5432:5432 -e POSTGRES_PASSWORD=postgres -e POSTGRES=postgres -e POSTGRES_DB=register_manager -d postgres:16.3
```
4. Executar o projeto
- Abrir o terminal na raiz do projeto [SUB_DIRETORIOS]/register-manager e exeuctar o comando abaixo para gerar o pacote.
```
mvn -DskipTests=true clean package
```
- No termial entrar no diretório [SUB_DIRETORIOS]/register-manager/target
```
java -jar register-manager-0.0.1-SNAPSHOT.jar
```
A aplicação subirá na porta 8081

### Testes
1. Carga inicial:
   - A aplicação faz uma carga inicial para facilitar os testes.
   - Perfil:
     - ADMIN tem acesso a todos os recursos
     - USER apenas ao próprio recurso e não tem tem permissão para criar usuário :-D
2. Testes usando Postman
    - Localize a collection que se encontra no diretório [SUB_DIRETORIOS]/register-manager/register-manager.postman_collection
    - Importar no Postman
    - A aplicação criou alguns usuários:
      - admin@admin.com com
      - manager@manager.com
      - user@user.com
      - 
3. Exemplos:
   - Recuros
     - [Login](#Login) - /api/v1/login
     - Users - /api/v1/users
       - [Criar](Users Criar)
       - [Consultar](Users Consultar )
       - [Atualizar](Users Atualizar)
       - [Excluir](Users Excluir)
     - Addresses - /api/v1/users/(userId)/addresses
        - [Criar](Addresses Criar)
        - [Consultar](Addresses Consultar )
        - [Atualizar](Addresses Atualizar)
        - [Excluir](Addresses Excluir)

## Postman
    <a id="login_id"></a>
   - Login
```
curl --location 'http://localhost:8081/api/v1/login' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username": "user@user.com",
    "password": "password"
}'
```
   - Users - Criar
```
curl --location 'http://localhost:8081/api/v1/users' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer [TOKEN_OBTIDO_LOGIN]' \
--data-raw '{
    "name": "Posteman User",
    "email": "postman@postman.com",
    "password": "postman123456",
    "role": "USER"
}'
```
   - Users - Consultar
```
curl --location 'http://localhost:8081/api/v1/users/(ID)' \
--header 'Authorization: Bearer [TOKEN_OBTIDO_LOGIN]'
```
   - Users - Consultar usuários com paginação
```
curl --location 'http://localhost:8081/api/v1/users/(ID)/addresses?page=0&linesPerPage=10&orderBy=publicArea&direction=ASC' \
--header 'Authorization: Bearer [TOKEN_OBTIDO_LOGIN]'
```
   - Users - Atualizar
```
curl --location --request PUT 'http://localhost:8081/api/v1/users/(ID)' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer [TOKEN_OBTIDO_LOGIN]' \
--data '{
    "name": "Name updated"
}'
```
   - Users - Excluir
```
curl --location --request DELETE 'http://localhost:8081/api/v1/users/(ID)' \
--header 'Authorization: Bearer [TOKEN_OBTIDO_LOGIN]'
```
   - Addresses - Criar
```
curl --location 'http://localhost:8081/api/v1/users/(UserId))/addresses' \
--header 'Content-Type: application/json' \
--header 'Authorization: [TOKEN_OBTIDO_LOGIN]'\
--data '{
    "publicArea": "Avenida Principal",
    "number": 15,
    "additionalAddress": "",
    "neighborhood": "Centro",
    "city": "São Paulo",
    "state": "SP",
    "zipCode": "06185987"
}'
```
   - Addresses - Consultar endereço do usuário
```
curl --location 'http://localhost:8081/api/v1/users/(userId)/addresses/(addressId)' \
--header 'Authorization: Bearer [TOKEN_OBTIDO_LOGIN]'
```
- Addresses - Consultar endereço VIA CEP para preencher o endereço do usuário
```
curl --location 'http://localhost:8081/api/v1/users/(userId)/addresses/zipcode/(CEP)' \
--header 'Authorization: Bearer [TOKEN_OBTIDO_LOGIN]'
```
   - Addresses - Consultar usuários com paginação
```
curl --location 'http://localhost:8081/api/v1/users/(userId)/addresses?page=0&linesPerPage=10&orderBy=publicArea&direction=ASC' \
--header 'Authorization: Bearer [TOKEN_OBTIDO_LOGIN]'
```
   - Addresses - Atualizar
```
curl --location --request PUT 'http://localhost:8081/api/v1/users/(userId)/addresses/(addressId)' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer [TOKEN_OBTIDO_LOGIN]' \
--data '{
    "publicArea": "Avenida Principal Atualizada",
    "number": 16,
    "additionalAddress": "updated",
    "neighborhood": "Centro  updated",
    "city": "Osasco  updated",
    "state": "SC",
    "zipCode": "89625741"
}'
```
   - Addresses - Excluir
```
curl --location --request DELETE 'http://localhost:8081/api/v1/users/(userId)/addresses/(addressId)' \
--header 'Authorization: Bearer [TOKEN_OBTIDO_LOGIN]'
```

## Docker
No projeto já existe uma imagem versionada no Docker Hub e precisa apenas ter o ambiente Docker.
Abrir um terminal no mesmo diretório do arquivo docker-compose.yaml e execute o comando abaixo.
```
docker-compose up -d
```
Baixar o ambiente
```
docker-compose down
```