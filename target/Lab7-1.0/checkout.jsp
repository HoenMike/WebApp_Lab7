<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8" />
		<title>Checkout</title>
		<link
			rel="stylesheet"
			href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"
		/>
	</head>
	<body>
		<div class="container my-5">
			<h1 class="mb-4">Checkout</h1>

			<div class="card mb-4">
				<div class="card-body">
					<h5 class="card-title">Customer Information</h5>
					<p class="card-text">Name: ${account.name}</p>
					<p class="card-text">VISA Number: ${account.visaNumber}</p>
					<p class="card-text">Address: ${account.address}</p>
				</div>
			</div>

			<h2 class="mb-4">Order Summary</h2>
			<table class="table">
				<thead>
					<tr>
						<th>Product</th>
						<th>Manufacturer</th>
						<th>Price</th>
						<th>Quantity</th>
						<th>Total</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${cart}" var="item">
						<tr>
							<td>${item.product.name}</td>
							<td>${item.product.manufacturer}</td>
							<td>${item.product.price}</td>
							<td>${item.quantity}</td>
							<td>${item.quantity * item.product.price}</td>
						</tr>
					</c:forEach>
				</tbody>
				<tfoot>
					<tr>
						<th colspan="4" class="text-right">Total:</th>
						<th>${total}</th>
					</tr>
				</tfoot>
			</table>

			<div class="text-center">
				<a href="ShoppingServlet?action=products" class="btn btn-primary">Shop Some More!</a>
				<a href="ShoppingServlet?action=logout" class="btn btn-danger">Logout</a>
			</div>
		</div>
	</body>
</html>
