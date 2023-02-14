delete from custom_schema.user_role;
delete from custom_schema.users;
alter sequence custom_schema.users_id_seq restart with 1;
