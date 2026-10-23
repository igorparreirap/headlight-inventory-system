-- =============================================================================
-- PROJETO   : Gerenciador de Estoque para Loja de Farois Automotivos (CMP1611)
-- ALUNO     : Igor Parreira de Paula
-- ARQUIVO   : schema.sql     -  Script de criacao do banco e dados de teste
-- SGBD      : MySQL (padrao MySQL, charset utf8mb4)
-- -----------------------------------------------------------------------------
-- DADOS DE ACESSO AO BANCO (usados pela classe util.ConexaoBD):
--   Nome do banco : db_farois
--   Usuario       : root
--   Senha         : (defina a senha do seu MySQL)
--   Host/Porta    : localhost : 3306
--   URL JDBC      : jdbc:mysql://localhost:3306/db_farois
-- =============================================================================

DROP DATABASE IF EXISTS db_farois;
CREATE DATABASE db_farois
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_general_ci;
USE db_farois;

-- -----------------------------------------------------------------------------
-- Tabela: usuario
-- -----------------------------------------------------------------------------
CREATE TABLE usuario (
    id    INT          NOT NULL AUTO_INCREMENT,
    login VARCHAR(50)  NOT NULL,
    senha VARCHAR(255) NOT NULL, -- hash PBKDF2 (nunca texto puro)
    CONSTRAINT pk_usuario PRIMARY KEY (id),
    CONSTRAINT uq_usuario_login UNIQUE (login)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------------------------
-- Tabela: marca
-- -----------------------------------------------------------------------------
CREATE TABLE marca (
    id   INT          NOT NULL AUTO_INCREMENT,
    nome VARCHAR(60)  NOT NULL,
    CONSTRAINT pk_marca PRIMARY KEY (id),
    CONSTRAINT uq_marca_nome UNIQUE (nome)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------------------------
-- Tabela: farol
-- -----------------------------------------------------------------------------
CREATE TABLE farol (
    id         INT           NOT NULL AUTO_INCREMENT,
    modelo     VARCHAR(80)   NOT NULL,
    ano        INT           NOT NULL,
    tipo       VARCHAR(40)   NOT NULL,
    quantidade INT           NOT NULL DEFAULT 0,
    preco      DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    id_marca   INT           NOT NULL,
    CONSTRAINT pk_farol PRIMARY KEY (id),
    CONSTRAINT uq_farol UNIQUE (modelo, ano, tipo, id_marca),
    CONSTRAINT fk_farol_marca FOREIGN KEY (id_marca)
        REFERENCES marca (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============================================================================
-- DADOS DE TESTE (POPULADOS COM MAIS DE 100 VEÍCULOS)
-- =============================================================================

-- Usuario administrador para acesso inicial (login: admin / senha: admin)
-- A senha e armazenada como hash PBKDF2-HMAC-SHA256 (ver util.HashSenha).
INSERT INTO usuario (login, senha) VALUES
    ('admin', 'pbkdf2$210000$PWkqgOosEEHEFCI0NWZj6A==$y2GQ0faOGqWXMKfFww2dbjXmamtG/OEeSUiSzA5/9Pk=');

-- Cadastro de Marcas Ampliado
INSERT INTO marca (nome) VALUES
    ('Ford'),
    ('Chevrolet'),
    ('Volkswagen'),
    ('Fiat'),
    ('Toyota'),
    ('Honda'),
    ('Hyundai'),
    ('Renault'),
    ('Jeep'),
    ('Nissan'),
    ('Mitsubishi'),
    ('RAM'),
    ('BMW');

-- Cadastro de Faróis (110 registros variados)
INSERT INTO farol (modelo, ano, tipo, quantidade, preco, id_marca) VALUES
    -- FORD (10)
    ('Ka',          2018, 'Dianteiro', 12, 320.00, (SELECT id FROM marca WHERE nome='Ford')),
    ('Ka',          2018, 'Traseiro',   8, 210.00, (SELECT id FROM marca WHERE nome='Ford')),
    ('Fiesta',      2015, 'Traseiro',   8, 280.50, (SELECT id FROM marca WHERE nome='Ford')),
    ('Fiesta',      2015, 'Dianteiro', 14, 340.00, (SELECT id FROM marca WHERE nome='Ford')),
    ('Ranger',      2020, 'Dianteiro',  6, 890.00, (SELECT id FROM marca WHERE nome='Ford')),
    ('Ranger',      2020, 'Traseiro',   5, 420.00, (SELECT id FROM marca WHERE nome='Ford')),
    ('Ranger',      2023, 'Dianteiro',  4, 1450.00, (SELECT id FROM marca WHERE nome='Ford')),
    ('EcoSport',    2019, 'Dianteiro', 11, 520.00, (SELECT id FROM marca WHERE nome='Ford')),
    ('EcoSport',    2019, 'Traseiro',   7, 310.00, (SELECT id FROM marca WHERE nome='Ford')),
    ('Focus',       2017, 'Dianteiro',  5, 680.00, (SELECT id FROM marca WHERE nome='Ford')),

    -- CHEVROLET (12)
    ('Onix',        2020, 'Dianteiro', 20, 410.90, (SELECT id FROM marca WHERE nome='Chevrolet')),
    ('Onix',        2020, 'Traseiro',  15, 290.00, (SELECT id FROM marca WHERE nome='Chevrolet')),
    ('Prisma',      2017, 'Dianteiro',  5, 395.00, (SELECT id FROM marca WHERE nome='Chevrolet')),
    ('Prisma',      2017, 'Traseiro',   9, 240.00, (SELECT id FROM marca WHERE nome='Chevrolet')),
    ('S10',         2019, 'Dianteiro',  8, 750.00, (SELECT id FROM marca WHERE nome='Chevrolet')),
    ('S10',         2019, 'Traseiro',   6, 380.00, (SELECT id FROM marca WHERE nome='Chevrolet')),
    ('S10',         2022, 'Dianteiro',  4, 1200.00, (SELECT id FROM marca WHERE nome='Chevrolet')),
    ('Tracker',     2021, 'Dianteiro', 10, 890.00, (SELECT id FROM marca WHERE nome='Chevrolet')),
    ('Tracker',     2021, 'Traseiro',   8, 460.00, (SELECT id FROM marca WHERE nome='Chevrolet')),
    ('Cruze',       2018, 'Dianteiro',  7, 720.00, (SELECT id FROM marca WHERE nome='Chevrolet')),
    ('Cruze',       2018, 'Traseiro',   5, 410.00, (SELECT id FROM marca WHERE nome='Chevrolet')),
    ('Spin',        2016, 'Dianteiro', 12, 340.00, (SELECT id FROM marca WHERE nome='Chevrolet')),

    -- VOLKSWAGEN (13)
    ('Gol',         2016, 'Dianteiro', 15, 250.00, (SELECT id FROM marca WHERE nome='Volkswagen')),
    ('Gol',         2016, 'Traseiro',  12, 180.00, (SELECT id FROM marca WHERE nome='Volkswagen')),
    ('Polo',        2021, 'Traseiro',  10, 460.75, (SELECT id FROM marca WHERE nome='Volkswagen')),
    ('Polo',        2021, 'Dianteiro', 14, 620.00, (SELECT id FROM marca WHERE nome='Volkswagen')),
    ('Golf',        2015, 'Dianteiro',  6, 850.00, (SELECT id FROM marca WHERE nome='Volkswagen')),
    ('Golf',        2015, 'Traseiro',   4, 510.00, (SELECT id FROM marca WHERE nome='Volkswagen')),
    ('Nivus',       2022, 'Dianteiro',  8, 1150.00, (SELECT id FROM marca WHERE nome='Volkswagen')),
    ('Nivus',       2022, 'Traseiro',   6, 640.00, (SELECT id FROM marca WHERE nome='Volkswagen')),
    ('T-Cross',     2020, 'Dianteiro',  9, 980.00, (SELECT id FROM marca WHERE nome='Volkswagen')),
    ('T-Cross',     2020, 'Traseiro',   7, 530.00, (SELECT id FROM marca WHERE nome='Volkswagen')),
    ('Saveiro',     2018, 'Dianteiro', 11, 310.00, (SELECT id FROM marca WHERE nome='Volkswagen')),
    ('Virtus',      2019, 'Dianteiro', 13, 580.00, (SELECT id FROM marca WHERE nome='Volkswagen')),
    ('Amarok',      2017, 'Dianteiro',  5, 920.00, (SELECT id FROM marca WHERE nome='Volkswagen')),

    -- FIAT (13)
    ('Uno',         2014, 'Dianteiro',  7, 190.00, (SELECT id FROM marca WHERE nome='Fiat')),
    ('Uno',         2014, 'Traseiro',  10, 130.00, (SELECT id FROM marca WHERE nome='Fiat')),
    ('Argo',        2022, 'Dianteiro', 18, 520.00, (SELECT id FROM marca WHERE nome='Fiat')),
    ('Argo',        2022, 'Traseiro',  12, 310.00, (SELECT id FROM marca WHERE nome='Fiat')),
    ('Toro',        2019, 'Dianteiro',  9, 850.00, (SELECT id FROM marca WHERE nome='Fiat')),
    ('Toro',        2019, 'Traseiro',   8, 420.00, (SELECT id FROM marca WHERE nome='Fiat')),
    ('Toro',        2021, 'Dianteiro',  5, 1100.00, (SELECT id FROM marca WHERE nome='Fiat')),
    ('Strada',      2020, 'Dianteiro', 14, 490.00, (SELECT id FROM marca WHERE nome='Fiat')),
    ('Strada',      2020, 'Traseiro',  11, 290.00, (SELECT id FROM marca WHERE nome='Fiat')),
    ('Strada',      2022, 'Dianteiro',  7, 890.00, (SELECT id FROM marca WHERE nome='Fiat')),
    ('Palio',       2015, 'Dianteiro', 16, 240.00, (SELECT id FROM marca WHERE nome='Fiat')),
    ('Cronos',      2021, 'Dianteiro',  8, 540.00, (SELECT id FROM marca WHERE nome='Fiat')),
    ('Mobi',        2018, 'Dianteiro', 22, 280.00, (SELECT id FROM marca WHERE nome='Fiat')),

    -- TOYOTA (10)
    ('Corolla',     2019, 'Dianteiro',  9, 680.00, (SELECT id FROM marca WHERE nome='Toyota')),
    ('Corolla',     2019, 'Traseiro',   6, 390.00, (SELECT id FROM marca WHERE nome='Toyota')),
    ('Corolla',     2022, 'Dianteiro',  5, 1250.00, (SELECT id FROM marca WHERE nome='Toyota')),
    ('Hilux',       2023, 'Traseiro',   6, 740.30, (SELECT id FROM marca WHERE nome='Toyota')),
    ('Hilux',       2023, 'Dianteiro',  4, 1600.00, (SELECT id FROM marca WHERE nome='Toyota')),
    ('Hilux',       2018, 'Dianteiro',  8, 890.00, (SELECT id FROM marca WHERE nome='Toyota')),
    ('Yaris',       2020, 'Dianteiro', 11, 610.00, (SELECT id FROM marca WHERE nome='Toyota')),
    ('Yaris',       2020, 'Traseiro',   9, 340.00, (SELECT id FROM marca WHERE nome='Toyota')),
    ('SW4',         2021, 'Dianteiro',  3, 1950.00, (SELECT id FROM marca WHERE nome='Toyota')),
    ('Etios',       2017, 'Dianteiro', 12, 320.00, (SELECT id FROM marca WHERE nome='Toyota')),

    -- HONDA (9)
    ('Civic',       2018, 'Dianteiro',  8, 790.00, (SELECT id FROM marca WHERE nome='Honda')),
    ('Civic',       2018, 'Traseiro',   6, 450.00, (SELECT id FROM marca WHERE nome='Honda')),
    ('Civic',       2021, 'Dianteiro',  5, 1350.00, (SELECT id FROM marca WHERE nome='Honda')),
    ('Fit',         2016, 'Dianteiro', 10, 410.00, (SELECT id FROM marca WHERE nome='Honda')),
    ('Fit',         2016, 'Traseiro',   8, 260.00, (SELECT id FROM marca WHERE nome='Honda')),
    ('HR-V',        2019, 'Dianteiro',  7, 880.00, (SELECT id FROM marca WHERE nome='Honda')),
    ('HR-V',        2019, 'Traseiro',   6, 490.00, (SELECT id FROM marca WHERE nome='Honda')),
    ('City',        2020, 'Dianteiro',  9, 590.00, (SELECT id FROM marca WHERE nome='Honda')),
    ('WR-V',        2018, 'Dianteiro',  4, 520.00, (SELECT id FROM marca WHERE nome='Honda')),

    -- HYUNDAI (9)
    ('HB20',        2019, 'Dianteiro', 18, 380.00, (SELECT id FROM marca WHERE nome='Hyundai')),
    ('HB20',        2019, 'Traseiro',  14, 230.00, (SELECT id FROM marca WHERE nome='Hyundai')),
    ('HB20',        2022, 'Dianteiro',  8, 790.00, (SELECT id FROM marca WHERE nome='Hyundai')),
    ('HB20S',       2020, 'Dianteiro',  9, 420.00, (SELECT id FROM marca WHERE nome='Hyundai')),
    ('Creta',       2018, 'Dianteiro', 11, 640.00, (SELECT id FROM marca WHERE nome='Hyundai')),
    ('Creta',       2018, 'Traseiro',   8, 370.00, (SELECT id FROM marca WHERE nome='Hyundai')),
    ('Creta',       2022, 'Dianteiro',  5, 1180.00, (SELECT id FROM marca WHERE nome='Hyundai')),
    ('Tucson',      2015, 'Dianteiro',  6, 490.00, (SELECT id FROM marca WHERE nome='Hyundai')),
    ('IX35',        2016, 'Dianteiro',  4, 680.00, (SELECT id FROM marca WHERE nome='Hyundai')),

    -- RENAULT (9)
    ('Sandero',     2017, 'Dianteiro', 15, 290.00, (SELECT id FROM marca WHERE nome='Renault')),
    ('Sandero',     2017, 'Traseiro',  12, 190.00, (SELECT id FROM marca WHERE nome='Renault')),
    ('Logan',       2018, 'Dianteiro', 10, 310.00, (SELECT id FROM marca WHERE nome='Renault')),
    ('Duster',      2020, 'Dianteiro',  8, 580.00, (SELECT id FROM marca WHERE nome='Renault')),
    ('Duster',      2020, 'Traseiro',   7, 340.00, (SELECT id FROM marca WHERE nome='Renault')),
    ('Kwid',        2019, 'Dianteiro', 20, 260.00, (SELECT id FROM marca WHERE nome='Renault')),
    ('Kwid',        2019, 'Traseiro',  16, 160.00, (SELECT id FROM marca WHERE nome='Renault')),
    ('Oroch',       2018, 'Dianteiro',  5, 540.00, (SELECT id FROM marca WHERE nome='Renault')),
    ('Captur',      2021, 'Dianteiro',  4, 820.00, (SELECT id FROM marca WHERE nome='Renault')),

    -- JEEP (7)
    ('Renegade',    2018, 'Dianteiro', 12, 590.00, (SELECT id FROM marca WHERE nome='Jeep')),
    ('Renegade',    2018, 'Traseiro',  10, 380.00, (SELECT id FROM marca WHERE nome='Jeep')),
    ('Renegade',    2021, 'Dianteiro',  6, 920.00, (SELECT id FROM marca WHERE nome='Jeep')),
    ('Compass',     2019, 'Dianteiro',  8, 840.00, (SELECT id FROM marca WHERE nome='Jeep')),
    ('Compass',     2019, 'Traseiro',   7, 460.00, (SELECT id FROM marca WHERE nome='Jeep')),
    ('Compass',     2022, 'Dianteiro',  4, 1390.00, (SELECT id FROM marca WHERE nome='Jeep')),
    ('Commander',   2022, 'Dianteiro',  3, 1750.00, (SELECT id FROM marca WHERE nome='Jeep')),

    -- NISSAN (7)
    ('Kicks',       2019, 'Dianteiro', 11, 620.00, (SELECT id FROM marca WHERE nome='Nissan')),
    ('Kicks',       2019, 'Traseiro',   9, 390.00, (SELECT id FROM marca WHERE nome='Nissan')),
    ('Kicks',       2022, 'Dianteiro',  5, 1150.00, (SELECT id FROM marca WHERE nome='Nissan')),
    ('March',       2016, 'Dianteiro', 14, 280.00, (SELECT id FROM marca WHERE nome='Nissan')),
    ('Versa',       2018, 'Dianteiro', 10, 340.00, (SELECT id FROM marca WHERE nome='Nissan')),
    ('Frontier',    2020, 'Dianteiro',  4, 980.00, (SELECT id FROM marca WHERE nome='Nissan')),
    ('Frontier',    2022, 'Dianteiro',  3, 1420.00, (SELECT id FROM marca WHERE nome='Nissan')),

    -- MITSUBISHI (5)
    ('L200 Triton', 2019, 'Dianteiro',  6, 890.00, (SELECT id FROM marca WHERE nome='Mitsubishi')),
    ('L200 Triton', 2022, 'Dianteiro',  4, 1480.00, (SELECT id FROM marca WHERE nome='Mitsubishi')),
    ('ASX',         2018, 'Dianteiro',  5, 670.00, (SELECT id FROM marca WHERE nome='Mitsubishi')),
    ('Outlander',   2017, 'Dianteiro',  3, 820.00, (SELECT id FROM marca WHERE nome='Mitsubishi')),
    ('Pajero',      2015, 'Dianteiro',  4, 750.00, (SELECT id FROM marca WHERE nome='Mitsubishi')),

    -- RAM (4)
    ('Rampage',     2024, 'Dianteiro',  5, 1650.00, (SELECT id FROM marca WHERE nome='RAM')),
    ('1500',        2022, 'Dianteiro',  2, 2400.00, (SELECT id FROM marca WHERE nome='RAM')),
    ('2500',        2021, 'Dianteiro',  3, 2650.00, (SELECT id FROM marca WHERE nome='RAM')),
    ('3500',        2023, 'Dianteiro',  2, 2900.00, (SELECT id FROM marca WHERE nome='RAM')),

    -- BMW (2)
    ('Serie 3',     2020, 'Dianteiro',  4, 1850.00, (SELECT id FROM marca WHERE nome='BMW')),
    ('X1',          2019, 'Dianteiro',  3, 1420.00, (SELECT id FROM marca WHERE nome='BMW'));

-- =============================================================================
-- CONFERENCIA RAPIDA
--   SELECT * FROM usuario;
--   SELECT * FROM marca;
--   SELECT f.id, f.modelo, f.ano, f.tipo, f.quantidade, f.preco, m.nome AS marca
--     FROM farol f JOIN marca m ON m.id = f.id_marca ORDER BY m.nome, f.modelo;
-- =============================================================================