# Gerenciador de Orçamentos Kaspper

## Visão Geral
O **Gerenciador de Orçamentos Kaspper** é um sistema desenvolvido em **Java** com **Spring Boot** para auxiliar no gerenciamento de demandas, tarefas e orçamentos. Possui uma interface web interativa e responsiva construída com **Thymeleaf**, proporcionando uma experiência prática e intuitiva.

## Funcionalidades
- **Gestão de Orçamentos**:
  - Criação, atualização e exclusão de orçamentos.
  - Visualização detalhada e listagem de orçamentos.
- **Integração com Demandas e Clientes**:
  - Associação de demandas a clientes.
  - Gerenciamento de tarefas vinculadas a cada demanda.
- **Interface Web Responsiva**:
  - Renderização dinâmica com **Thymeleaf**.
  - Suporte a operações CRUD via formulários interativos.
- **Filtros Avançados**:
  - Pesquisa de orçamentos com base em status, valor ou prazo.
- **Documentação de API**:
  - Endpoints documentados com **Swagger**, facilitando a exploração e o consumo.

## Pré-Requisitos
- **Java** 17 ou superior.
- **Maven** para gerenciamento de dependências.
- **Banco de Dados** H2 (em memória) configurado para desenvolvimento.

## Estrutura do Projeto
```
projeto/
├── src/main/java/com/orcamentos/kaspper
│   ├── config/           # Configurações (CORS, Swagger, etc.)
│   ├── controller/       # Controladores responsáveis pelos endpoints
│   ├── dto/              # Objetos para transferência de dados
│   ├── exception/        # Tratamento e personalização de erros
│   ├── model/            # Definições de modelos de dados
│   ├── repository/       # Interfaces JPA para acesso ao banco
│   ├── service/          # Lógica de negócios
│   └── KaspperApplication.java  # Classe principal para inicialização
├── src/main/resources/
│   ├── templates/        # Arquivos de template Thymeleaf
│   ├── application.properties  # Configurações da aplicação
│   ├── schema.sql        # Script de criação do esquema do banco
│   └── sql/triggers.sql  # Definições de triggers no banco
├── pom.xml               # Arquivo de configuração do Maven
└── README.md             # Documentação do projeto
```

## Configuração e Execução

1. **Clone o Repositório**:
   ```bash
   git clone <url-do-repositorio>
   cd projeto
   ```

2. **Configuração do Banco de Dados**:
   - O projeto utiliza o banco de dados H2 (em memória) como padrão.
   - Caso prefira usar outro banco, ajuste as configurações no arquivo `application.properties`.

3. **Execute a Aplicação**:
   ```bash
   mvn spring-boot:run
   ```

4. **Acesse o Sistema**:
   - Interface Web: [http://localhost:8080](http://localhost:8080)
   - Documentação Swagger: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Principais Endpoints

### Orçamentos
- **Listar todos**: `GET /api/orcamentos`
- **Buscar por ID**: `GET /api/orcamentos/{id}`
- **Criar novo**: `POST /api/orcamentos`
- **Atualizar existente**: `PUT /api/orcamentos/{id}`
- **Excluir**: `DELETE /api/orcamentos/{id}`
- **Filtrar**: `GET /api/orcamentos/filtrar`

### Demandas
- **Listar todas**: `GET /api/demandas`
- **Buscar por ID**: `GET /api/demandas/{id}`

### Clientes
- **Listar todos**: `GET /api/clientes`

## Tecnologias Utilizadas
- **Java** com **Spring Boot**.
- **Thymeleaf** para renderização do frontend.
- **H2 Database** para persistência no ambiente de desenvolvimento.
- **Maven** para gerenciamento de dependências.
- **Swagger** para documentação da API.

## 🤝 Contribuidores
Este projeto foi desenvolvido por:
- [@jmoraes-92](https://github.com/jmoraes-92)
- [@Isaacboniii](https://github.com/Isaacboniii)
- [@Idalvo](https://github.com/Idalvo)
- [@contagiovaneines](https://github.com/Contagiovaneines)

---

## 🖋️ Autor e Finalidade
Este sistema foi desenvolvido como parte do Trabalho de Conclusão de Curso da equipe **Kaspper**, com o objetivo de aplicar práticas modernas de desenvolvimento web e resolver um problema de gerenciamento de orçamentos.

---

## 🛠️ Licença
© Kaspper, 2025. Todos os direitos reservados.
Este software é proprietário e seu uso está sujeito às condições expressas no contrato de licença.

