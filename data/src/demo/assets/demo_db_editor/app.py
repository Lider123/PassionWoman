from flask import Flask, flash, redirect, render_template, request, url_for
import db


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
