insert into roles (id_roles, name) values(1, 'CEO'),
(2, 'ADMIN'), (3, 'MANAGER'), (4, 'CUSTOMER'), (5, 'USER');

insert into products_connection (size) values('1/2'),
('1'), ('1/8'), ('1/16'), ('2');

insert into products_type (name) values('pipeline'),
('side'), ('wall'), ('into');

insert into product_companies (name) values('BOSH'),
('REXHROTH'), ('GY'), ('IMages');

insert into countries (name) values('USA'),
('Belarus'), ('UK'), ('Bela'), ('IT'), ('Belar');

insert into user_company (name, address, countries_id) values('Company 1', 'Street 1', 1),
('Company 2', 'Street 2', 2), ('Company 3', 'Street 3', 3);

insert into products_type (name) values('Valve 1'),
('Valve 2'), ('Valve 3'), ('Valve 4'), ('Valve 5'), ('Valve 6');

insert into companies_has_countries (id_companies, id_countries) values(1, 2),
(2, 2), (3, 1);

insert into users (first_name, last_name, email, password, path, id_user_company, registration, id_roles)
values('Alex', 'Human', 'email@email.com', '123456', 'large-path', 1, '2020-01-01', 1),
('Donald', 'Hammer', 'email1@email.com', '232edf', 'large-path 1', 1, '2010-01-01', 1),
('Gringo', 'Axe', 'email2@email.com', 'dsdfsd', 'large-path 33', 1, '2020-07-25', 2);

insert into products (count, pressure, pressure_max, weight, path_hydraulic_scheme, additional_inf, sku,
flow_rate, id_products_type, id_products_connection, id_product_companies, id_countries)
values (25, 335, 375, 1, 'large-path', '2020-additional_inf-01', 'sku #1', 150, 2, 1, 1, 1),
(11, 415, 450, 1.5, 'large-path 1', '2020-additional_inf-02', 'sku #2', 200, 3, 1, 1, 2),
(158, 176, 150, 0.9, 'large-path 2', '2020-additional_inf-03', 'sku #3', 250, 2, 2, 2, 3),
(325, 184, 210, 5, 'large-path 3', '2020-additional_inf-04', 'sku #4', 270, 3, 2, 1, 4),
(75, 125, 250, 10.5, 'large-path 4', '2020-additional_inf-01_5', 'sku #5', 130, 2, 3, 3, 5);

insert into pictures (path, id_products) values('path_product-1', 1),
('path_product-1', 1), ('path_product-2', 2), ('path_product-2', 2), ('path_product-3', 3);

insert into storage_racks_products (id_products, id_storage_racks) values(1, 1),
(2, 2), (5, 1), (3, 1), (4, 2);

insert into shelf (name) values('#1'), ('#2'), ('#3'), ('#4'), ('#5'), ('#6');

insert into storage_racks (name, shelf_id) values('storage_racks-name-1', 1),
('storage_racks-name-2', 2), ('storage_racks-name-3', 3), ('storage_racks-name-4', 4), ('storage_racks-name-5', 5);

insert into countries_has_product_companies (id_countries, id_product_companies) values(1, 1),
(1, 2), (1, 3);
