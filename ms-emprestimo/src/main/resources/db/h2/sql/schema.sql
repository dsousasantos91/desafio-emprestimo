-- Apagar as tabelas se já existirem
drop table if exists emprestimo cascade;
drop table if exists pessoa cascade;

-- Criar a tabela pessoa
create table pessoa
(
    id                   bigint auto_increment,
    identificador        varchar(14)    not null,
    nome                 varchar(50)    not null,
    data_nascimento      date           not null,
    tipo_identificador   int        not null check (tipo_identificador between 0 and 3),
    valor_max_emprestimo numeric(38, 2) not null,
    valor_min_mensal     numeric(38, 2) not null,
    primary key (id),
    constraint UniqueAssociado unique (identificador)
);

-- Criar a tabela emprestimo
create table emprestimo
(
    id               bigint auto_increment,
    id_pessoa        bigint         not null,
    data_criacao     date           not null,
    numero_parcelas  int            not null,
    status_pagamento int        not null check (status_pagamento between 0 and 1),
    valor_emprestimo numeric(38, 2) not null,
    primary key (id)
);

-- Adicionar a constraint de chave estrangeira na tabela emprestimo
alter table emprestimo add constraint FKeujhum1ufew05uykqc54c9dg foreign key (id_pessoa) references pessoa(id);
