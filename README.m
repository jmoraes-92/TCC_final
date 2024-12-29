# Gerenciador de Orçamentos Kaspper

## Visão Geral
O projeto Gerenciador de Orçamentos Kaspper é um sistema desenvolvido em **Java** utilizando **Spring Boot** para gerenciar demandas, tarefas e orçamentos. Ele conta com um frontend integrado ao **Thymeleaf**, fornecendo uma interface responsiva para interação com os dados.

## Funcionalidades
- **Gerenciamento de Orçamentos**:
  - Criação de orçamentos baseados em demandas.
  - Atualização e exclusão de orçamentos existentes.
  - Listagem e detalhamento de orçamentos gerados.
- **Integração com Demandas e Clientes**:
  - Associar demandas a clientes.
  - Gerenciar tarefas relacionadas a cada demanda.
- **Frontend Interativo**:
  - Visualização de dados com **Thymeleaf**.
  - Formulários interativos para operações CRUD.
- **Filtros Avançados**:
  - Buscar orçamentos com filtros por status, valor ou prazo.
- **Documentação de API**:
  - Configurado com **Swagger** para exploração de endpoints.

## Requisitos
- **Java** 17 ou superior.
- **Maven** para gerenciamento de dependências.
- **Banco de Dados** H2 (em memória) para ambiente de desenvolvimento.

## Estrutura do Projeto

```
projeto/
├── src/main/java/com/orcamentos/kaspper
│   ├── config/           # Configurações gerais do sistema (CORS, Swagger)
│   ├── controller/       # Controladores para exposição de endpoints
│   ├── dto/              # Objetos de transferência de dados
│   ├── exception/        # Tratamento de exceções
│   ├── model/            # Modelos de dados
│   ├── repository/       # Interfaces de repositórios para JPA
│   ├── service/          # Lógica de negócios
│   └── KaspperApplication.java  # Classe principal da aplicação
├── src/main/resources/
│   ├── templates/        # Arquivos Thymeleaf
│   ├── application.properties  # Configurações de aplicação
│   ├── schema.sql        # Script de criação de tabelas
│   └── sql/triggers.sql  # Definições de triggers do banco de dados
├── pom.xml               # Configurações do Maven
└── README.md             # Documentação do projeto
```

## Configuração

1. **Clonar o Repositório**:
   ```bash
   git clone <url-do-repositorio>
   cd projeto
   ```

2. **Configurar o Banco de Dados**:
   O banco de dados H2 está configurado como padrão em `application.properties`. Caso deseje usar outro banco, atualize as credenciais no mesmo arquivo.

3. **Executar a Aplicação**:
   ```bash
   mvn spring-boot:run
   ```

4. **Acessar a Aplicação**:
   - Interface Web: [http://localhost:8080](http://localhost:8080)
   - Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Endpoints Principais

### Orçamentos
- **Listar Todos**: `GET /api/orcamentos`
- **Buscar por ID**: `GET /api/orcamentos/{id}`
- **Criar**: `POST /api/orcamentos`
- **Atualizar**: `PUT /api/orcamentos/{id}`
- **Excluir**: `DELETE /api/orcamentos/{id}`
- **Filtrar**: `GET /api/orcamentos/filtrar`

### Demandas
- **Listar Todas**: `GET /api/demandas`
- **Buscar por ID**: `GET /api/demandas/{id}`

### Clientes
- **Listar Todos**: `GET /api/clientes`

## Tecnologias Utilizadas
- **Java** com **Spring Boot**.
- **Maven** para gerenciamento de dependências.
- **H2 Database** para persistência.
- **Thymeleaf** para renderização de templates.
- **Swagger** para documentação de API.

## Contribuições
Contribuições são bem-vindas! Por favor, crie um fork do repositório e envie um pull request com suas modificações.

## Licença
Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.


