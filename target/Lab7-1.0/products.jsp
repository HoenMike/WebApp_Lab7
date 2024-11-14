<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8" />
		<title>Products</title>
		<link
			rel="stylesheet"
			href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"
		/>
	</head>
	<body>
		<div class="container my-5">
			<h1 class="mb-4">Select Products</h1>
			<form action="ShoppingServlet" method="post">
				<input type="hidden" name="action" value="add" />
				<div class="form-group">
					<label for="product">Product:</label>
					<select class="form-control" id="product" name="productId" required>
						<option value="">Select a product</option>
						<c:forEach items="${products}" var="product">
							<option value="${product.productID}">
								${product.name} - ${product.manufacturer} (${product.price})
							</option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label for="quantity">Quantity:</label>
					<input
						type="number"
						class="form-control"
						id="quantity"
						name="quantity"
						min="1"
						value="1"
						required
					/>
				</div>
				<button type="submit" class="btn btn-primary">Add to Cart</button>
				<a href="ShoppingServlet?action=checkout" class="btn btn-success">Checkout</a>
			</form>

			<h2 class="mt-5">Cart</h2>
			<table class="table">
				<thead>
					<tr>
						<th>Product</th>
						<th>Manufacturer</th>
						<th>Price</th>
						<th>Quantity</th>
						<th>Action</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${cart}" var="item">
						<tr>
							<td>${item.product.name}</td>
							<td>${item.product.manufacturer}</td>
							<td>${item.product.price}</td>
							<td>${item.quantity}</td>
							<td>
								<a
									href="ShoppingServlet?action=delete&itemId=${item.itemId}"
									class="btn btn-danger btn-sm"
									>Delete</a
								>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</body>
</html>
