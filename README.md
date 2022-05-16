# Projeto de Album de Figurinhas (OAuth2 Resource Server)

Projeto backend de uma API REST para gerencimento de Album de Figurinhas. Este projeto é utilizado no módulo de segurança com Spring Security e OAuth2, servindo como Resource Server para a atividade.

### Atenção:
> Branch com Spring Security OAuth2 Resource Server configurado, porém com testes de integração **quebrando com HTTP Status Code 401/403 propositalmente**.

## Tecnologias utilizadas:

Algumas das principais tecnologias utilizadas neste projeto:

1. Java 11;
2. Spring Boot 2.6.7;
3. Spring Data JPA com Hibernate;
4. Bean Validation;
5. Banco em memoria H2;
6. jUnit e Spring Testing;

## Ambiente de desenvolvimento

Para configurar e rodar a aplicação em ambiente local basta seguir os passos:

1. Clonar o repositório ou fazer seu download:

```shell
git clone git@github.com:zup-academy/oauth2-resourceserver-minhas-figurinhas.git
```

2. Importar o projeto na IDE IntelliJ;

3. Iniciar a aplicação via IDE ou linha de comando:

```shell
./mvnw spring-boot:run
``` 

4. Iniciar o Keycloack via Docker-Compose:

```shell
docker-compose -f docker-compose-keycloak.yml up -d
```

5. Acesse o Keycloack usando login `admin` e senha `admin`: http://localhost:18080/auth/admin/;
6. Crie o Realm `minhas-figurinhas`;
7. No Realm criado:
    - 7.1. crie os Users: `rafael.ponte` e `jordi.silva`;
    - 7.2. crie os Scopes: `albuns:read` e `albuns:write`;
    - 7.3. crie o Client: `minhas-figurinhas-client`;
        - configure _Access Type_ como `confidential`;
        - configure o _Standard Flow Enabled_ como `ON`;
        - configure o _Direct Access Grants_ Enabled como `ON`;
        - adicione os Scopes ao Client criado como escopos opcionais;

## Consumindo a API REST da aplicação

Aqui demonstramos através de alguns exemplos como você pode consumir a API REST exposta pela aplicação. Estamos utilizando o comando `cURL` como cliente HTTP mas você pode usar qualquer outro de sua preferência, como POSTman ou Insomnia. 

Dado que a aplicação esteja rodando, basta executar os comandos abaixo para exercitar os endpoints públicos da aplicação.

### Atenção:
> Lembre-se de passar o `access_token` no cabeçalho HTTP de cada requisição;

### Criando novo album

```shell
curl --request POST \
  --url http://localhost:8080/oauth2-resourceserver-minhas-figurinhas/api/albuns \
  --header 'Authorization: Bearer <access_token>' \
  --header 'Content-Type: application/json' \
  --data '{
	"titulo": "Dragon Ball Z",
	"descricao": "Personagens do anime Dragon Ball Z",
	"figurinhas": [
		{ "descricao": "Goku", "enderecoDaImagem": "http://animes.com/dbz/goku.png" },
		{ "descricao": "Picollo", "enderecoDaImagem": "http://animes.com/dbz/picollo.png" }
	]
}'
```

### Adicionando figurinha no album

```shell
curl --request POST \
  --url http://localhost:8080/oauth2-resourceserver-minhas-figurinhas/api/albuns/1/figurinhas \
  --header 'Authorization: Bearer <access_token>' \
  --header 'Content-Type: application/json' \
  --data '{ 
	"descricao": "Kuririn", 
	"enderecoDaImagem": "http://animes.com/dbz/kuririn.png" 
}'
```

### Detalhando um album existente
```shell
curl --request GET \
  --url http://localhost:8080/oauth2-resourceserver-minhas-figurinhas/api/albuns/1 \
  --header 'Authorization: Bearer <access_token>'
```

### Listando todos os albuns
```shell
curl --request GET \
  --url http://localhost:8080/oauth2-resourceserver-minhas-figurinhas/api/albuns \
  --header 'Authorization: Bearer <access_token>'
```

## Duvidas e suporte

Basta entrar em contato com a equipe da Zup Edu responsável pelo Bootcamp no horário comercial.