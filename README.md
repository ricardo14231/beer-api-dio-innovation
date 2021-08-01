# Desenvolvimento de testes unitários para validar uma API REST de gerenciamento estoques de cerveja


# Sobre o projeto 

Está API foi proposto a partir do projeto de desenvolvimento de testes unitários realizado durante a Bootcamp GFT START #2 Java, oferecida pela plataforma Digital Innovation One.

Foram feitas algumas alterações na atividade sugerida.   

## Pré-requisitos para executar o projeto:

Java: 11 ou superior
Maven: 3.8.1
JUnit 5.

## Para executar a aplicação:

No diretório do projeto execute o seguinte comando no terminal:
mvn spring-boot:run 

## Endpoints:

Método | Endpoint
:-----:|:--------:
 GET   | http://localhost:8080/beer/list
 POST  | http://localhost:8080/beer/create
 GET   | http://localhost:8080/beer/{id}
 GET   | http://localhost:8080/beer/findName/{name}
 PUT   | http://localhost:8080/beer/update/{id}
 DELETE| http://localhost:8080/beer/delete/{id}
 PATCH | http://localhost:8080/beer/{id}/increment/{quantity}

## Modelo Json para criar um curriculum:
Para adicionar uma cerveja, envie uma requisição com o modelo a seguir para o endpoint http://localhost:8080/beer/create
``` JSON
{
	"name":"Brahma",
	"brand":"Ambev",
	"max":50,
	"quantity":20,
	"type":"ALE",
	"consumptionTemperature": 5,
	"alcoholicStrenght" : 60,
	"description" : "description..."
}
```
## Autor:

Ricardo Farias.

https://www.linkedin.com/in/ricardo-farias-04069359/

## Licença:

[![NPM](http://img.shields.io/npm/l/react)](https://github.com/ricardo14231/beer-api-dio-innovation/blob/main/LICENSE)
