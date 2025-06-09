# 🗳️ Sistema de Votação - Spring Boot + Kafka + Quartz

Este projeto é um sistema de votação construído com foco em escalabilidade, performance e arquitetura limpa. Ele permite o registro de pautas, abertura de sessões de votação com tempo limitado e publicação de resultados via Kafka após o encerramento automático.

---

## Como executar o projeto

### Pré-requisitos

- Docker + Docker Compose instalados

### Rodando tudo com Docker Compose

```bash
docker compose up --build
```
### 📗 O projeto conta com swagger para melhor consulta dos endpoints e documentação
Após rodar o projeto basta acessar:
```
http://localhost:8080/swagger-ui/index.html#/
```

## Este comando irá:
- Construir a imagem da API Spring Boot

- Subir um banco de dados PostgreSQL persistente

- Iniciar o Kafka + Zookeeper

- Inicializar a API configurada para se conectar com todos os serviços
---
## 🧱 Arquitetura

Optei por uma arquitetura em camadas (layered architecture) com influência da Clean Architecture, separando bem as responsabilidades entre controladores, serviços, persistência, DTOs, integrações externas (via Feign) e jobs assíncronos. Essa estrutura garante organização, legibilidade e facilidade para evoluir o sistema sem a complexidade completa do DDD, o que considerei desnecessário para esse escopo.

---
## 🕗 Por que utilizei o Quartz?
Optei por utilizar o Quartz para garantir o agendamento confiável do encerramento das sessões de votação. Como cada sessão tem uma duração personalizada, precisei de uma ferramenta robusta que permitisse o agendamento dinâmico e com persistência, mesmo após reinícios da aplicação. O Quartz atende perfeitamente esse cenário, possibilitando o controle preciso da execução dos jobs e a integridade da lógica de negócio mesmo em ambiente de produção.

---
## 🏎️ Controle de Race Condition
Mesmo que neste projeto o risco real de race condition seja baixo — dado que o voto só pode ser computado uma vez por associado e as requisições costumam ser rápidas —, optei por garantir integridade total com o uso de @Transactional no momento da votação. Essa decisão reforça a segurança da operação em cenários concorrentes, evitando qualquer possibilidade de inconsistência nos dados caso múltiplas requisições de voto sejam processadas simultaneamente para a mesma pauta. É uma boa prática que previne problemas em ambientes de alta carga e torna a aplicação mais resiliente.

## 🧪 CPFs disponíveis para teste
Você pode utilizar os seguintes CPFs cadastrados previamente no banco para realizar testes de votação:

12345678901

98765432100

11122233344

55566677788

00011122233

Esses CPFs já estão registrados na tabela associado e podem ser usados diretamente nos endpoints da API.





