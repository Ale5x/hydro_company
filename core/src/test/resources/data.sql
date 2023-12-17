insert into roles (id_roles, name) values(1, 'CEO'),
(2, 'ADMIN'), (3, 'MANAGER'), (4, 'CUSTOMER'), (5, 'USER');

insert into companies (name, address) values('Company 1', 'USA'),
('Company 2', 'BY'), ('Company 3', 'UK');

insert into users (first_name, last_name, password, path, id_companies, registration, id_roles)
values('Alex', 'Human', '123456', 'large-path', 1, '2020-01-01', 1),
('Donald', 'Hammer', '232edf', 'large-path 1', 1, '2010-01-01', 1),
('Gringo', 'Axe', 'dsdfsd', 'large-path 33', 1, '2020-07-25', 2);


