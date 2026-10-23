===============================================================================
 BANCO DE DADOS - Gerenciador de Estoque para Loja de Farois Automotivos
 Disciplina CMP1611 - Mini-Projeto de Software
 Aluno: Igor Parreira de Paula
===============================================================================

1) DADOS DE ACESSO
   - SGBD          : MySQL (padrao MySQL)
   - Nome do banco : db_farois
   - Charset       : utf8mb4
   - Usuario       : root
   - Senha         : (defina a senha do seu MySQL)
   - Host / Porta  : localhost / 3306
   - URL JDBC      : jdbc:mysql://localhost:3306/db_farois

   As credenciais NAO ficam no codigo-fonte. Copie o arquivo
   db.properties.example (raiz do projeto) para db.properties e preencha
   usuario e senha, ou defina as variaveis de ambiente DB_URL, DB_USER e
   DB_PASSWORD.

2) ARQUIVO DO SCRIPT
   - schema.sql : cria o banco, as tabelas e insere os dados de teste.

3) COMO EXECUTAR (HeidiSQL ou ferramenta semelhante)
   a) Abra o HeidiSQL e conecte-se ao servidor MySQL.
   b) Menu Arquivo > Carregar arquivo SQL... e selecione schema.sql.
   c) Execute o script (F9 ou botao Executar).
   d) O proprio script faz DROP/CREATE do banco db_farois, portanto pode ser
      executado em uma base limpa quantas vezes for necessario.

   Alternativa por linha de comando:
      mysql -u root -p < database/schema.sql

4) ESTRUTURA DAS TABELAS
   usuario(id*, login, senha)
   marca(id*, nome)
   farol(id*, modelo, ano, tipo, quantidade, preco, id_marca -> marca.id)

   (*) chave primaria AUTO_INCREMENT
   - usuario.login e marca.nome sao UNIQUE (evitam duplicidade).
   - farol possui UNIQUE composto (modelo, ano, tipo, id_marca).
   - farol.id_marca e chave estrangeira para marca.id.

5) DADOS DE TESTE INCLUSOS
   - 1 usuario administrador  -> login: admin  /  senha: admin
   - 5 marcas (Ford, Chevrolet, Volkswagen, Fiat, Toyota)
   - 10 farois variados vinculados as marcas.

===============================================================================
