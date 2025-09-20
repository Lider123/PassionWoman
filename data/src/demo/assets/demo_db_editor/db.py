import sqlite3


def get_db_connection():
    conn = sqlite3.connect("passionwoman.db")
    conn.row_factory = sqlite3.Row
    return conn


def get_colors():
    conn = get_db_connection()
    colors = conn.execute("SELECT * FROM colors").fetchall()
    conn.close()
    return colors


def get_categories():
    conn = get_db_connection()
    categories = conn.execute("SELECT * FROM categories").fetchall()
    conn.close()
    return categories


def get_category_by_id(category_id):
    conn = get_db_connection()
    category = conn.execute(f"SELECT * FROM categories WHERE id = {category_id}").fetchone()
    conn.close()
    return category


def get_brand_by_id(brand_id):
    conn = get_db_connection()
    brand = conn.execute(f"SELECT * FROM brands WHERE id = {brand_id}")
    conn.close()
    return brand


def get_products():
    conn = get_db_connection()
    products = conn.execute("SELECT * FROM products").fetchall()
    conn.close()
    return products


def insert_category(name, image_path):
    conn = get_db_connection()
    cursor = conn.cursor()
    cursor.execute('INSERT INTO categories (name, image_path) VALUES (?, ?)', (name, image_path))
    category_id = cursor.lastrowid
    conn.commit()
    conn.close()
    return category_id


def update_category_image_path(category_id, image_path):
    conn = get_db_connection()
    conn.execute("UPDATE categories SET image_path = ? WHERE id = ?", (image_path, category_id))
    conn.commit()
    conn.close()

