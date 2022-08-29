import sqlite3


def get_db_connection():
    conn = sqlite3.connect("passionwoman.db")
    conn.row_factory = sqlite3.Row
    return conn


def get_categories():
    conn = get_db_connection()
    categories = conn.execute("SELECT * FROM categories").fetchall()
    conn.close()
    return categories


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

