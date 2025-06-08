create role local;
alter user local password 'local';

create database local;
alter database local owner to local;
-- grant all on schema public to local;
-- grant all privileges on database local to local;
alter role local with login;
