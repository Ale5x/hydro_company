INSERT INTO countries (name) VALUES
('Belarus'), ('USA'), ('Germany'), ('China'), ('Brazil');

INSERT INTO user_companies (name, address, country_id) VALUES
('TechCorp', '123 Silicon Ave', 1), ('AutoMakers', '456 Autobahn St', 2), ('MegaTrade', '789 Commerce Rd', 3),
('GreenEnergy', '101 Forest Ln', 4), ('SpaceXplore', '202 Cosmo Blvd', 1);

INSERT INTO roles (name) VALUES ('Admin'), ('User'), ('Manager'), ('Guest'), ('Support');

INSERT INTO user_statuses (status) VALUES ('Active'), ('Inactive'), ('Pending'), ('Banned'), ('Deleted');

INSERT INTO users (first_name, last_name, password, path, user_company_id, registration, role_id, email,
user_status_id) VALUES
('John', 'Doe', 'pass123', NULL, 1, CURRENT_TIMESTAMP, 1, 'john.doe@example.com', 1),
('Anna', 'Smith', 'pass234', NULL, 2, CURRENT_TIMESTAMP, 2, 'anna.smith@example.com', 1),
('Peter', 'Brown', 'pass345', NULL, NULL, CURRENT_TIMESTAMP, 3, 'peter.brown@example.com', 2),
('Linda', 'Taylor', 'pass456', NULL, 3, CURRENT_TIMESTAMP, 4, 'linda.taylor@example.com', 1),
('James', 'Wilson', 'pass567', NULL, 4, CURRENT_TIMESTAMP, 5, 'james.wilson@example.com', 3);

INSERT INTO products_type (name) VALUES ('Valve'), ('Pump'), ('Filter'), ('Compressor'), ('Tank');

INSERT INTO products_connections (size) VALUES
('Small'), ('Medium'), ('Large'), ('Extra Large'), ('Custom');

INSERT INTO product_companies (name) VALUES
('HydroFlow'), ('PumpTech'), ('FilterPro'), ('AirComp'), ('TankWorks');

INSERT INTO products (flow_rate, pressure, weight, path_hydraulic_scheme, pressure_max, additional_inf,
product_type_id, count, product_connection_id, product_company_id) VALUES
(100, 200, 15.5, '/schemes/valve1.png', 250, 'High durability', 1, 10, 1, 1),
(150, 300, 20.0, '/schemes/pump1.png', 350, NULL, 2, 5, 2, 2),
(80, 180, 10.0, '/schemes/filter1.png', 200, 'Low maintenance', 3, 15, 3, 3),
(200, 400, 30.0, '/schemes/compressor1.png', 450, NULL, 4, 3, 4, 4),
(120, 250, 25.0, '/schemes/tank1.png', 300, 'For industrial use', 5, 7, 5, 5);

INSERT INTO pictures (path, product_id) VALUES
('/images/prod1_pic1.jpg', 1), ('/images/prod2_pic1.jpg', 2), ('/images/prod3_pic1.jpg', 3),
('/images/prod4_pic1.jpg', 4), ('/images/prod5_pic1.jpg', 5);

INSERT INTO storage_racks (name) VALUES
('Rack A'), ('Rack B'), ('Rack C'), ('Rack D'), ('Rack E');

INSERT INTO shelves (name, storage_racks_id) VALUES
('S1', 1), ('S2', 2), ('S3', 3), ('S4', 4), ('S5', 5);

INSERT INTO countries_has_product_companies (country_id, product_company_id) VALUES
(1, 1), (2, 2), (3, 3), (4, 4), (5, 5);

INSERT INTO product_sku_status (status) VALUES
('In Stock'), ('Out of Stock'), ('Discontinued'), ('Backordered'), ('Reserved');

INSERT INTO product_sku (sku_code, product_id, status_id, country_id, shelf_id) VALUES
('SKU1001', 1, 1, 1, 1), ('SKU1002', 2, 2, 2, 2), ('SKU1003', 3, 3, 3, 3),('SKU1004', 4, 4, 4, 4), ('SKU1005', 5, 5, 5, 5);
