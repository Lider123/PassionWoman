from flask import Flask, flash, redirect, render_template, request, url_for
from validation import price_is_valid, rating_is_valid

import db
import json
import sys


STATIC_IMAGE_FOLDER_PATH = "static/image/"
app = Flask(__name__)
app.config['SECRET_KEY'] = 'Qb5acHSjOhxP9wRuuBU8u1waVqYPbL5Z'


@app.route("/")
def index():
    return render_template("index.html")


@app.route("/categories")
def categories():
    data = db.get_categories()
    return render_template("categories.html", categories=data)


@app.route("/products")
def products():
    data = [dict(p) for p in db.get_products()]
    for d in data:
        d['category_name'] = db.get_category_by_id(d['category_id'])['name']
        if d['brand_id'] is not None:
            d['brand_name'] = db.get_brand_by_id(d['brand_id'])['name']
        else:
            d['brand_name'] = None

    return render_template("products.html", products=data)


@app.route("/products/new", methods=('GET', 'POST'))
def create_product():
    if request.method == 'POST':
        print(request.form, file=sys.stderr)  # TODO: remove
        print(request.form.getlist("product_item_color[]"), file=sys.stderr)  # TODO: remove
        name = request.form['name']
        image = request.files['image']
        category_id = request.form['category_id']
        price_str = request.form['price']
        price_with_discount_str = request.form['price_with_discount']
        rating_str = request.form['rating']

        if not name:
            flash('Name is required!')
        elif not image:
            flash('Image is required!')
        elif not category_id:
            flash('Category is required!')
        elif not price_str:
            flash('Price is required!')
        elif not price_is_valid(price_str):
            flash('Price has incorrect format!')
        elif not price_with_discount_str:
            flash('Price with discount is required!')
        elif not price_is_valid(price_with_discount_str):
            flash('Price with discount has incorrect format!')
        elif not rating_str:
            flash('Rating is required!')
        elif not rating_is_valid(rating_str):
            flash('Rating has incorrect format!')

        # TODO: insert new product into database

    _categories = db.get_categories()
    colors = db.get_colors()
    return render_template("create_product.html", categories=_categories, colors=colors)


@app.route("/categories/new", methods=('GET', 'POST'))
def create_category():
    if request.method == 'POST':
        name = request.form['name']
        image = request.files['image']

        if not name:
            flash('Name is required!')
        elif not image:
            flash('Image is required!')
        else:
            new_category_id = db.insert_category(name, image.filename)
            image_name = f"category_{new_category_id}_image_path.{image.filename.split('.')[1]}"
            image_path = STATIC_IMAGE_FOLDER_PATH + image_name
            image.save(image_path)
            db.update_category_image_path(new_category_id, image_path)
            return redirect(url_for('categories'))

    return render_template("create_category.html")
