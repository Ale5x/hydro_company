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

INSERT INTO products_type (name) VALUES ('Valve'), ('Pump');

INSERT INTO products_connections (size) VALUES
('Small'), ('Medium'), ('Large'), ('Extra Large'), ('Custom');

INSERT INTO product_companies (name) VALUES
('HydroFlow'), ('PumpTech'), ('FilterPro'), ('AirComp'), ('TankWorks');

INSERT INTO products (
    flow_rate, pressure, weight, path_hydraulic_scheme, pressure_max, additional_inf,
    product_type_id, count, product_connection_id, product_company_id
) VALUES
(100, 200, 15.5, '/schemes/valve1.png', 250, 'High durability', 1, 10, 1, 1),
(150, 300, 20.0, '/schemes/pump1.png', 350, NULL, 2, 5, 2, 2),
(80, 180, 10.0, '/schemes/filter1.png', 200, 'Low maintenance', 1, 15, 3, 3),
(200, 400, 30.0, '/schemes/compressor1.png', 450, NULL, 2, 3, 4, 4),
(120, 250, 25.0, '/schemes/tank1.png', 300, 'For industrial use', 1, 7, 5, 5),
(90, 170, 12.0, '/schemes/valve2.png', 220, 'Standard model', 2, 12, 1, 1),
(160, 310, 21.5, '/schemes/pump2.png', 360, NULL, 1, 6, 2, 2),
(85, 190, 11.0, '/schemes/filter2.png', 210, 'Compact design', 2, 14, 3, 3),
(210, 410, 31.0, '/schemes/compressor2.png', 460, NULL, 1, 4, 4, 4),
(125, 260, 26.0, '/schemes/tank2.png', 310, 'High capacity', 2, 8, 5, 5),
(95, 175, 13.0, '/schemes/valve3.png', 230, 'Improved seal', 1, 9, 1, 1),
(155, 305, 22.0, '/schemes/pump3.png', 355, NULL, 2, 7, 2, 2),
(82, 185, 10.5, '/schemes/filter3.png', 205, 'Easy replacement', 1, 13, 3, 3),
(205, 405, 29.0, '/schemes/compressor3.png', 455, NULL, 2, 5, 4, 4),
(118, 245, 24.5, '/schemes/tank3.png', 295, 'Corrosion resistant', 1, 6, 5, 5),
(105, 215, 16.0, '/schemes/valve4.png', 255, 'Heavy-duty', 2, 11, 1, 1),
(165, 320, 23.0, '/schemes/pump4.png', 370, NULL, 1, 6, 2, 2),
(87, 195, 12.0, '/schemes/filter4.png', 215, 'Environment friendly', 2, 12, 3, 3),
(215, 420, 32.0, '/schemes/compressor4.png', 470, NULL, 1, 4, 4, 4),
(130, 270, 27.0, '/schemes/tank4.png', 320, 'Large volume', 2, 9, 5, 5),
(98, 178, 14.0, '/schemes/valve5.png', 235, 'Compact valve', 1, 10, 1, 1),
(158, 308, 22.5, '/schemes/pump5.png', 358, NULL, 2, 5, 2, 2),
(83, 183, 10.8, '/schemes/filter5.png', 203, 'Disposable cartridge', 1, 13, 3, 3),
(208, 408, 30.5, '/schemes/compressor5.png', 458, NULL, 2, 3, 4, 4),
(122, 255, 25.5, '/schemes/tank5.png', 305, 'Robust material', 1, 7, 5, 5),
(170, 330, 24.0, '/schemes/pump6.png', 380, NULL, 2, 6, 2, 2),
(89, 198, 11.2, '/schemes/filter6.png', 218, 'Washable filter', 1, 11, 3, 3),
(220, 430, 33.0, '/schemes/compressor6.png', 480, NULL, 2, 5, 4, 4),
(135, 275, 28.0, '/schemes/tank6.png', 325, 'Insulated design', 1, 6, 5, 5),
(110, 210, 16.5, '/schemes/valve7.png', 260, 'Quick response', 2, 8, 1, 1),
(102, 205, 15.8, '/schemes/valve6.png', 252, 'Smooth control', 1, 10, 1, 1);

INSERT INTO pictures (path, product_id) VALUES
('/images/prod1_pic1.jpg', 1), ('/images/prod2_pic1.jpg', 2), ('/images/prod3_pic1.jpg', 3),
('/images/prod4_pic1.jpg', 4), ('/images/prod5_pic1.jpg', 5);

INSERT INTO storage_racks (name) VALUES
('Rack A'), ('Rack B');

INSERT INTO shelves (name, rack_id) VALUES
('S1', 2), ('S2', 2);

INSERT INTO countries_has_product_companies (country_id, product_company_id) VALUES
(1, 1), (2, 2), (3, 3), (4, 4), (5, 5);

INSERT INTO product_sku_status (status) VALUES
('In Stock'), ('Out of Stock');

INSERT INTO product_sku (sku_code, product_id, status_id, country_id, shelf_id) VALUES
('SKU1001', 1, 1, 1, 1), ('SKU1002', 2, 2, 2, 2), ('SKU1003', 3, 1, 3, 1), ('SKU1004', 4, 2, 4, 2),
('SKU1005', 5, 1, 5, 1), ('SKU1006', 6, 2, 1, 2), ('SKU1007', 7, 1, 2, 1), ('SKU1008', 8, 2, 3, 2),
('SKU1009', 9, 1, 4, 1), ('SKU1010', 10, 2, 5, 2), ('SKU1011', 11, 1, 2, 1), ('SKU1012', 12, 2, 3, 2),
('SKU1013', 13, 1, 4, 1), ('SKU1014', 14, 2, 5, 2), ('SKU1015', 15, 1, 1, 1), ('SKU1016', 16, 2, 3, 2),
('SKU1017', 17, 1, 4, 1), ('SKU1018', 18, 2, 5, 2), ('SKU1019', 19, 1, 1, 1), ('SKU1020', 20, 2, 2, 2),
('SKU1021', 21, 1, 3, 1), ('SKU1022', 22, 2, 4, 2), ('SKU1023', 23, 1, 5, 1), ('SKU1024', 24, 2, 1, 2),
('SKU1025', 25, 1, 2, 1), ('SKU1026', 26, 2, 3, 2), ('SKU1027', 27, 1, 4, 1), ('SKU1028', 28, 2, 5, 2),
('SKU1029', 29, 1, 1, 1), ('SKU1030', 30, 2, 2, 2);

