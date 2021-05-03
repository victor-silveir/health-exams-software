# CRUD-Spring-Next.Js

## Descrição do projeto:
 
Uma aplicação back-end para realizar cadastro, deleção e atualização de exames e instituições de saúde, contendo regras de negócio específicas!

Sumário
=================
<!--ts-->
   * [Descrição do Projeto](#descrição-do-projeto)
   * [Sumário](#sumário)
   * [Como instalar e rodar o projeto](#Como-instalar-e-rodar-o-projeto)
      * [Pré Requisitos](#pré-requisitos)
      * [Build do Projeto](#build-do-projeto)
   * [Uso e Testes](#uso-e-testes)
        * [Uso e Endpoints Liberados](#uso-e-endpoints-liberados)
* [Considerações Finais](#considerações-finais)
<!--te-->



## Tecnologias utilizadas: 

* Spring Boot - A aplicação foi construída inteiramente em java utilizando-se dos recursos do framework para construir uma API REST.

* H2 - Banco de dados em memória para facilitar os testes da persistência de dados dos clientes.

* Junit - Biblioteca Java para realização de testes na aplicação, com códigos pré-determinados e sem a necessidade de ter que ingerir diferentes dados para realizar testes.

# Como instalar e rodar o projeto: 

## Pré Requisitos:

Para poder rodar o projeto na sua máquina é necessário ter instalado [Maven](https://maven.apache.org/download.cgi) e [Java](https://www.oracle.com/technetwork/pt/java/javase/downloads/index.html) (Sendo necessário pelo o menos a versão 8.0 do Java), caso utilizem o docker-compose é necessário ter instalado também o [Docker](https://www.docker.com/products/docker-desktop).


## Build do projeto:

Após baixar o projeto, abra o terminal de comando na raíz do projeto e execute o seguinte comando:

```sh
mvn clean package
```

Este comando irá gerar um arquivo .jar compilado na pasta targetque pode ser executado com o seguinte comando: 

```sh
java -jar health-software-1.jar
```

Também é possível subir a aplicação para um container via Docker, para isso utilizar o comando:

```sh
docker-compose up
```
Ou, caso deseje deixar em background e continuar usando o terminal,

```sh 
docker-compose up -d
```
Após subir a aplicação, acesse http://localhost:8080/healthcareinstitutions para conferir se não houve nenhum erro durante a execução da aplicação, foi ingerido alguns dados iniciais e esse endpont irá mostrar esses dados.

# Uso e Testes

## Uso e endpoints liberados

Após conferir o funcionamento da API é possível testar a aplicação, para testá-la basta executar as classes de testes, que ficam dentro da pasta src/test. Além disso foi desenvolvida uma aplicação, incompleta, para realizar os testes adequados para essa API.


# Considerações Finais

* Não foi pedido para utilizar um banco de dados, por isso utilizei o h2, além de ser mais prático para implementar e rodar em modo de testes, mas, caso fosse continuar desenvolvendo uma aplicação como esta implementaria o uso do PostgreSQL para armazenar os dados dos clientes.
* Seria interessante, na minha opinião, criar classes separadas para representar as entidades paciente e médicos.
* Uma sugestão de atualização da aplicação seria implementar um serviço de e-mail para envios de e-mail para os pacientes de confirmação de cadastro de exames, entre outros.
*Uma possível evolução para o sistema é implementar uma autenticação, para que a aplicação tenha mais segurança e controle dos usuários.
* Foi pedido para listar os exames e eu optei fazer isso, seguindo as lógicas requeridas, buscando os exames cadastrados em uma determinada instituição de saúde, liberando apenas os dados do nome do paciente e do nome do procedimento.



