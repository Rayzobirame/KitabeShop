create sequence produit_id_seq start with 1 increment by 50;

create table produits
(
    id bigint default nextval('produit_id_seq') not null,
    code               text not null unique ,
    nom                 text not null ,
    description         text,
    images              text,
    prix                numeric not null ,
    primary key         (id)
)