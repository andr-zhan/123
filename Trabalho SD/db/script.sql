-- Apagar tabelas antigas, se existirem (útil para testes)
DROP TABLE IF EXISTS emprestimos;
DROP TABLE IF EXISTS livros;
DROP TABLE IF EXISTS utilizadores;

-- ==========================
-- Tabela: Utilizadores
-- ==========================
CREATE TABLE utilizadores (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(120) UNIQUE NOT NULL,
    tipo VARCHAR(20) CHECK (tipo IN ('normal', 'administrador')),
    estado_operacional VARCHAR(20)
        CHECK (estado_operacional IN ('ativo', 'suspenso', 'bloqueado'))
        DEFAULT 'ativo',
    estado_admin VARCHAR(20)
        CHECK (estado_admin IN ('aprovado', 'nao_aprovado'))
        DEFAULT 'nao_aprovado'
);

-- ==========================
-- Tabela: Livros
-- ==========================
CREATE TABLE livros (
    id SERIAL PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    autor VARCHAR(100) NOT NULL,
    categoria VARCHAR(50),
    estado_operacional VARCHAR(20)
        CHECK (estado_operacional IN ('disponivel', 'reservado', 'emprestado', 'manutencao'))
        DEFAULT 'disponivel',
    estado_admin VARCHAR(20)
        CHECK (estado_admin IN ('aprovado', 'nao_aprovado'))
        DEFAULT 'nao_aprovado'
);

-- ==========================
-- Tabela: Empréstimos
-- ==========================
CREATE TABLE emprestimos (
    id SERIAL PRIMARY KEY,
    utilizador_id INT REFERENCES utilizadores(id),
    livro_id INT REFERENCES livros(id),
    data_emprestimo DATE NOT NULL,
    data_devolucao DATE,
    estado VARCHAR(20)
        CHECK (estado IN ('ativo', 'concluido', 'atrasado'))
        DEFAULT 'ativo'
);
