--
-- Файл сгенерирован с помощью SQLiteStudio v3.3.3 в Вт авг 23 16:18:40 2022
--
-- Использованная кодировка текста: System
--
PRAGMA foreign_keys = off;
BEGIN TRANSACTION;

-- Таблица: brands
DROP TABLE IF EXISTS brands;
CREATE TABLE brands (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, logo_path TEXT NOT NULL, name TEXT NOT NULL);
INSERT INTO brands (id, logo_path, name) VALUES (1, 'file:///android_asset/image/brand_1.webp', 'Victoria''s Secret');
INSERT INTO brands (id, logo_path, name) VALUES (2, 'file:///android_asset/image/brand_2.png', 'Intimissimi');
INSERT INTO brands (id, logo_path, name) VALUES (3, 'file:///android_asset/image/brand_3.jpg', 'MilaVitsa');
INSERT INTO brands (id, logo_path, name) VALUES (4, 'file:///android_asset/image/brand_4.gif', 'La Perla');
INSERT INTO brands (id, logo_path, name) VALUES (5, 'file:///android_asset/image/brand_5.jpg', 'Fleur du Mal');
INSERT INTO brands (id, logo_path, name) VALUES (6, 'file:///android_asset/image/brand_6.png', 'Maison Lejaby');
INSERT INTO brands (id, logo_path, name) VALUES (7, 'file:///android_asset/image/brand_7.png', 'Lui et Elle');
INSERT INTO brands (id, logo_path, name) VALUES (8, 'file:///android_asset/image/brand_8.jpg', 'Lowry');
INSERT INTO brands (id, logo_path, name) VALUES (9, 'file:///android_asset/image/brand_9.jpg', 'MiNiMi');
INSERT INTO brands (id, logo_path, name) VALUES (10, 'file:///android_asset/image/brand_10.png', 'SiSi');
INSERT INTO brands (id, logo_path, name) VALUES (11, 'file:///android_asset/image/brand_11.png', 'Vis-A-Vis');
INSERT INTO brands (id, logo_path, name) VALUES (12, 'file:///android_asset/image/brand_12.jpg', 'Fiore');
INSERT INTO brands (id, logo_path, name) VALUES (13, 'file:///android_asset/image/brand_13.jpg', 'Incanto');
INSERT INTO brands (id, logo_path, name) VALUES (14, 'file:///android_asset/image/brand_14.jpg', 'Mark Formelle');
INSERT INTO brands (id, logo_path, name) VALUES (15, 'file:///android_asset/image/brand_15.png', 'Infinity lingerie');
INSERT INTO brands (id, logo_path, name) VALUES (16, 'file:///android_asset/image/brand_16.jpg', 'Luce Del Sole');
INSERT INTO brands (id, logo_path, name) VALUES (17, 'file:///android_asset/image/brand_17.webp', 'Aveline');
INSERT INTO brands (id, logo_path, name) VALUES (18, 'file:///android_asset/image/brand_18.webp', 'ORIGINAL SIN');
INSERT INTO brands (id, logo_path, name) VALUES (19, 'file:///android_asset/image/brand_20.webp', 'Moor Moor');
INSERT INTO brands (id, logo_path, name) VALUES (20, 'file:///android_asset/image/brand_19.webp', 'VITORICCI');

-- Таблица: categories
DROP TABLE IF EXISTS categories;
CREATE TABLE categories (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name TEXT NOT NULL, image_path TEXT NOT NULL);
INSERT INTO categories (id, name, image_path) VALUES (1, 'Bra', 'file:///android_asset/image/category_bra.jpg');
INSERT INTO categories (id, name, image_path) VALUES (2, 'Panties', 'file:///android_asset/image/category_panties.jpg');
INSERT INTO categories (id, name, image_path) VALUES (3, 'Lingerie', 'file:///android_asset/image/category_lingerie.jpg');
INSERT INTO categories (id, name, image_path) VALUES (4, 'Erotic', 'file:///android_asset/image/category_erotic.jpg');
INSERT INTO categories (id, name, image_path) VALUES (5, 'Swim', 'file:///android_asset/image/category_swim.jpg');
INSERT INTO categories (id, name, image_path) VALUES (6, 'Corsets', 'file:///android_asset/image/category_corsets.jpg');
INSERT INTO categories (id, name, image_path) VALUES (7, 'Garter belts', 'file:///android_asset/image/category_garter_belts.jpg');
INSERT INTO categories (id, name, image_path) VALUES (8, 'Babydolls', 'file:///android_asset/image/category_babydolls.jpg');
INSERT INTO categories (id, name, image_path) VALUES (9, 'Stockings', 'file:///android_asset/image/category_stockings.jpg');
INSERT INTO categories (id, name, image_path) VALUES (10, 'Pantyhose', 'file:///android_asset/image/category_pantyhose.jpg');

-- Таблица: colors
DROP TABLE IF EXISTS colors;
CREATE TABLE colors (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ui_name TEXT NOT NULL, hex TEXT NOT NULL);
INSERT INTO colors (id, ui_name, hex) VALUES (1, 'white', '#FFFFFF');
INSERT INTO colors (id, ui_name, hex) VALUES (2, 'black', '#000000');
INSERT INTO colors (id, ui_name, hex) VALUES (3, 'red', '#FF0000');
INSERT INTO colors (id, ui_name, hex) VALUES (4, 'light blue', '#72BCD4');
INSERT INTO colors (id, ui_name, hex) VALUES (5, 'purple', '#800080');
INSERT INTO colors (id, ui_name, hex) VALUES (6, 'yellow', '#E5E500');
INSERT INTO colors (id, ui_name, hex) VALUES (7, 'blue', '#4169E1');
INSERT INTO colors (id, ui_name, hex) VALUES (8, 'beige', '#F5F5DC');
INSERT INTO colors (id, ui_name, hex) VALUES (9, 'hot pink', '#FF69B4');
INSERT INTO colors (id, ui_name, hex) VALUES (10, 'pink', '#FFC0CB');
INSERT INTO colors (id, ui_name, hex) VALUES (11, 'brown', '#966919');
INSERT INTO colors (id, ui_name, hex) VALUES (12, 'multicolor', 'multicolor');

-- Таблица: country_to_product
DROP TABLE IF EXISTS country_to_product;
CREATE TABLE country_to_product (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, product_country_code TEXT REFERENCES product_countries (code) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL, product_id INTEGER REFERENCES products (id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL);
INSERT INTO country_to_product (id, product_country_code, product_id) VALUES (1, 'italy', 2);

-- Таблица: model_to_product
DROP TABLE IF EXISTS model_to_product;
CREATE TABLE model_to_product (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, product_model_code TEXT REFERENCES product_models (code) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL, product_id INTEGER REFERENCES products (id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL);
INSERT INTO model_to_product (id, product_model_code, product_id) VALUES (1, 'balconette', 2);

-- Таблица: product_countries
DROP TABLE IF EXISTS product_countries;
CREATE TABLE product_countries (code TEXT PRIMARY KEY NOT NULL, ui_name TEXT NOT NULL);
INSERT INTO product_countries (code, ui_name) VALUES ('belarus', 'Belarus');
INSERT INTO product_countries (code, ui_name) VALUES ('hungary', 'Hungary');
INSERT INTO product_countries (code, ui_name) VALUES ('italy', 'Italy');
INSERT INTO product_countries (code, ui_name) VALUES ('china', 'China');

-- Таблица: product_images
DROP TABLE IF EXISTS product_images;
CREATE TABLE product_images (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, image_path TEXT NOT NULL, product_item_id INTEGER REFERENCES product_items (id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL);
INSERT INTO product_images (id, image_path, product_item_id) VALUES (1, 'file:///android_asset/image/product_bra_1_item_1_1.jpg', 1);
INSERT INTO product_images (id, image_path, product_item_id) VALUES (2, 'file:///android_asset/image/product_bra_1_item_1_2.jpg', 1);
INSERT INTO product_images (id, image_path, product_item_id) VALUES (3, 'file:///android_asset/image/product_bra_1_item_1_3.jpg', 1);
INSERT INTO product_images (id, image_path, product_item_id) VALUES (4, 'file:///android_asset/image/product_bra_1_item_1_4.jpg', 1);
INSERT INTO product_images (id, image_path, product_item_id) VALUES (5, 'file:///android_asset/image/product_bra_1_item_2_1.jpg', 2);
INSERT INTO product_images (id, image_path, product_item_id) VALUES (6, 'file:///android_asset/image/product_bra_1_item_2_2.jpg', 2);
INSERT INTO product_images (id, image_path, product_item_id) VALUES (7, 'file:///android_asset/image/product_bra_1_item_2_3.jpg', 2);
INSERT INTO product_images (id, image_path, product_item_id) VALUES (8, 'file:///android_asset/image/product_bra_1_item_2_4.jpg', 2);
INSERT INTO product_images (id, image_path, product_item_id) VALUES (9, 'file:///android_asset/image/product_bra_2_pink_1.webp', 3);

-- Таблица: product_items
DROP TABLE IF EXISTS product_items;
CREATE TABLE product_items (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, color_id INTEGER REFERENCES colors (id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL, product_id INTEGER REFERENCES products (id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL);
INSERT INTO product_items (id, color_id, product_id) VALUES (1, 1, 1);
INSERT INTO product_items (id, color_id, product_id) VALUES (2, 2, 1);
INSERT INTO product_items (id, color_id, product_id) VALUES (3, 10, 2);

-- Таблица: product_materials
DROP TABLE IF EXISTS product_materials;
CREATE TABLE product_materials (code TEXT PRIMARY KEY NOT NULL, ui_name TEXT NOT NULL);
INSERT INTO product_materials (code, ui_name) VALUES ('lace', 'lace');
INSERT INTO product_materials (code, ui_name) VALUES ('nylon', 'nylon');
INSERT INTO product_materials (code, ui_name) VALUES ('spandex', 'spandex');
INSERT INTO product_materials (code, ui_name) VALUES ('polyester', 'polyester');
INSERT INTO product_materials (code, ui_name) VALUES ('net', 'net');
INSERT INTO product_materials (code, ui_name) VALUES ('microfiber', 'microfiber');

-- Таблица: product_models
DROP TABLE IF EXISTS product_models;
CREATE TABLE product_models (code TEXT PRIMARY KEY NOT NULL, ui_name TEXT NOT NULL);
INSERT INTO product_models (code, ui_name) VALUES ('slip', 'slip');
INSERT INTO product_models (code, ui_name) VALUES ('brazilian', 'brazilian');
INSERT INTO product_models (code, ui_name) VALUES ('thong', 'thong');
INSERT INTO product_models (code, ui_name) VALUES ('push-up', 'push-up');
INSERT INTO product_models (code, ui_name) VALUES ('balconette', 'balconette');

-- Таблица: product_seasons
DROP TABLE IF EXISTS product_seasons;
CREATE TABLE product_seasons (code TEXT PRIMARY KEY NOT NULL, ui_name TEXT NOT NULL);
INSERT INTO product_seasons (code, ui_name) VALUES ('all_season', 'all season');

-- Таблица: product_sizes
DROP TABLE IF EXISTS product_sizes;
CREATE TABLE product_sizes (code TEXT PRIMARY KEY NOT NULL, ui_name TEXT NOT NULL);
INSERT INTO product_sizes (code, ui_name) VALUES ('110', '110');
INSERT INTO product_sizes (code, ui_name) VALUES ('108', '108');
INSERT INTO product_sizes (code, ui_name) VALUES ('106', '106');
INSERT INTO product_sizes (code, ui_name) VALUES ('105C', '105C');
INSERT INTO product_sizes (code, ui_name) VALUES ('105B', '105B');
INSERT INTO product_sizes (code, ui_name) VALUES ('104', '104');
INSERT INTO product_sizes (code, ui_name) VALUES ('102', '102');
INSERT INTO product_sizes (code, ui_name) VALUES ('100C', '100C');
INSERT INTO product_sizes (code, ui_name) VALUES ('100B', '100B');
INSERT INTO product_sizes (code, ui_name) VALUES ('100', '100');
INSERT INTO product_sizes (code, ui_name) VALUES ('98', '98');
INSERT INTO product_sizes (code, ui_name) VALUES ('96', '96');
INSERT INTO product_sizes (code, ui_name) VALUES ('95C', '95C');
INSERT INTO product_sizes (code, ui_name) VALUES ('95B', '95B');
INSERT INTO product_sizes (code, ui_name) VALUES ('94', '94');
INSERT INTO product_sizes (code, ui_name) VALUES ('92', '92');
INSERT INTO product_sizes (code, ui_name) VALUES ('90C', '90C');
INSERT INTO product_sizes (code, ui_name) VALUES ('90B', '90B');
INSERT INTO product_sizes (code, ui_name) VALUES ('90', '90');
INSERT INTO product_sizes (code, ui_name) VALUES ('88', '88');
INSERT INTO product_sizes (code, ui_name) VALUES ('86', '86');
INSERT INTO product_sizes (code, ui_name) VALUES ('85D', '85D');
INSERT INTO product_sizes (code, ui_name) VALUES ('85C', '85C');
INSERT INTO product_sizes (code, ui_name) VALUES ('85B', '85B');
INSERT INTO product_sizes (code, ui_name) VALUES ('85A', '85A');
INSERT INTO product_sizes (code, ui_name) VALUES ('84', '84');
INSERT INTO product_sizes (code, ui_name) VALUES ('82', '82');
INSERT INTO product_sizes (code, ui_name) VALUES ('80C', '80C');
INSERT INTO product_sizes (code, ui_name) VALUES ('80B', '80B');
INSERT INTO product_sizes (code, ui_name) VALUES ('80A', '80A');
INSERT INTO product_sizes (code, ui_name) VALUES ('80', '80');
INSERT INTO product_sizes (code, ui_name) VALUES ('75D', '75D');
INSERT INTO product_sizes (code, ui_name) VALUES ('75C', '75C');
INSERT INTO product_sizes (code, ui_name) VALUES ('75B', '75B');
INSERT INTO product_sizes (code, ui_name) VALUES ('75A', '75A');
INSERT INTO product_sizes (code, ui_name) VALUES ('70D', '70D');
INSERT INTO product_sizes (code, ui_name) VALUES ('70C', '70C');
INSERT INTO product_sizes (code, ui_name) VALUES ('70B', '70B');
INSERT INTO product_sizes (code, ui_name) VALUES ('70A', '70A');
INSERT INTO product_sizes (code, ui_name) VALUES ('65B', '65B');
INSERT INTO product_sizes (code, ui_name) VALUES ('XXXL', 'XXXL');
INSERT INTO product_sizes (code, ui_name) VALUES ('XXL', 'XXL');
INSERT INTO product_sizes (code, ui_name) VALUES ('XL', 'XL');
INSERT INTO product_sizes (code, ui_name) VALUES ('L', 'L');
INSERT INTO product_sizes (code, ui_name) VALUES ('M', 'M');
INSERT INTO product_sizes (code, ui_name) VALUES ('S', 'S');
INSERT INTO product_sizes (code, ui_name) VALUES ('XS', 'XS');

-- Таблица: product_styles
DROP TABLE IF EXISTS product_styles;
CREATE TABLE product_styles (code TEXT PRIMARY KEY NOT NULL, ui_name TEXT NOT NULL);
INSERT INTO product_styles (code, ui_name) VALUES ('wedding', 'wedding');
INSERT INTO product_styles (code, ui_name) VALUES ('gift', 'gift');
INSERT INTO product_styles (code, ui_name) VALUES ('vintage', 'vintage');
INSERT INTO product_styles (code, ui_name) VALUES ('evening', 'evening');
INSERT INTO product_styles (code, ui_name) VALUES ('classic', 'classic');
INSERT INTO product_styles (code, ui_name) VALUES ('cocktail', 'cocktail');
INSERT INTO product_styles (code, ui_name) VALUES ('party', 'party');
INSERT INTO product_styles (code, ui_name) VALUES ('casual', 'casual');

-- Таблица: product_types
DROP TABLE IF EXISTS product_types;
CREATE TABLE product_types (code TEXT PRIMARY KEY NOT NULL, ui_name TEXT NOT NULL);
INSERT INTO product_types (code, ui_name) VALUES ('underwear_set', 'underwear set');
INSERT INTO product_types (code, ui_name) VALUES ('bra', 'bra');
INSERT INTO product_types (code, ui_name) VALUES ('bustier', 'bustier');

-- Таблица: products
DROP TABLE IF EXISTS products;
CREATE TABLE products (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, category_id INTEGER REFERENCES categories (id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL, name TEXT NOT NULL, preview_path TEXT NOT NULL, price DOUBLE NOT NULL, price_with_discount DOUBLE NOT NULL, rating DOUBLE NOT NULL, description TEXT, brand_id INTEGER REFERENCES brands (id) ON DELETE SET NULL ON UPDATE CASCADE);
INSERT INTO products (id, category_id, name, preview_path, price, price_with_discount, rating, description, brand_id) VALUES (1, 1, 'Cute bra', 'file:///android_asset/image/product_bra_1_preview.jpg', 55.0, 49.5, 4.0, NULL, NULL);
INSERT INTO products (id, category_id, name, preview_path, price, price_with_discount, rating, description, brand_id) VALUES (2, 1, 'Balconette bra Prima Vista', 'file:///android_asset/image/product_bra_2_preview.webp', 19.23, 17.21, 0.0, NULL, NULL);

-- Таблица: promotions
DROP TABLE IF EXISTS promotions;
CREATE TABLE promotions (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, image_path TEXT NOT NULL);
INSERT INTO promotions (id, image_path) VALUES (1, 'file:///android_asset/image/promotion_1.jpg');
INSERT INTO promotions (id, image_path) VALUES (2, 'file:///android_asset/image/promotion_2.webp');
INSERT INTO promotions (id, image_path) VALUES (3, 'file:///android_asset/image/promotion_3.jpeg');

-- Таблица: season_to_product
DROP TABLE IF EXISTS season_to_product;
CREATE TABLE season_to_product (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, product_season_code TEXT NOT NULL REFERENCES product_seasons (code) ON DELETE CASCADE ON UPDATE CASCADE, product_id INTEGER NOT NULL REFERENCES products (id) ON DELETE CASCADE ON UPDATE CASCADE);
INSERT INTO season_to_product (id, product_season_code, product_id) VALUES (1, 'all_season', 2);

-- Таблица: size_to_product_item
DROP TABLE IF EXISTS size_to_product_item;
CREATE TABLE size_to_product_item (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, product_size_code TEXT REFERENCES product_sizes (code) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL, product_item_id INTEGER REFERENCES product_items (id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL, is_available INTEGER NOT NULL);
INSERT INTO size_to_product_item (id, product_size_code, product_item_id, is_available) VALUES (1, '70B', 3, 0);
INSERT INTO size_to_product_item (id, product_size_code, product_item_id, is_available) VALUES (2, '70D', 3, 1);

-- Таблица: type_to_product
DROP TABLE IF EXISTS type_to_product;
CREATE TABLE type_to_product (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, product_type_code TEXT REFERENCES product_types (code) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL, product_id INTEGER REFERENCES products (id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL);
INSERT INTO type_to_product (id, product_type_code, product_id) VALUES (1, 'bra', 2);

-- Таблица: users
DROP TABLE IF EXISTS users;
CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name TEXT, surname TEXT, phone TEXT NOT NULL, avatar TEXT);
INSERT INTO users (id, name, surname, phone, avatar) VALUES (1, NULL, NULL, '+79100000000', 'file:///android_asset/image/avatar.jpg');

COMMIT TRANSACTION;
PRAGMA foreign_keys = on;
