from flask import Flask, render_template, request, redirect

app = Flask(__name__)

products = [
    {"id": 1, "name": "Ali Yazbek", "image": "images/ali_yazbek.jpeg", "details": "Computer Science Student"},
    {"id": 2, "name": "Ali Suliman", "image": "images/ali_suliman.jpeg", "details": "Computer Science Student"},
    {"id": 3, "name": "Sara Nabhan", "image": "images/sara_nabhan.jpeg", "details": "Computer Science Student"}
]

@app.route('/')
def display_products():
    total = len(products)
    return render_template('products.html', products=products, total=total)

@app.route('/add', methods=['GET', 'POST'])
def add_product():
    method = request.method
    name = request.form.get('name', '')
    image = request.form.get('image', '')
    details = request.form.get('details', '')
    new_id = len(products) + 1
    new_product = {"id": new_id, "name": name, "image": image, "details": details}
    products.append(new_product)
    return render_template('add_product.html', method=method)

@app.route('/product/<int:id>')
def product_detail(id):
    index = id - 1
    product = products[index]
    return render_template('product_detail.html', product=product)

@app.route('/delete/<int:id>')
def delete_product(id):
    index = id - 1
    product = products[index]
    return render_template('delete_confirm.html', product=product)

@app.route('/confirm-delete/<int:id>', methods=['POST'])
def confirm_delete(id):
    index = id - 1
    product = products[index]
    products.remove(product)
    return redirect('/')
