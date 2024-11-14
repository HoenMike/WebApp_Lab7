<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.mycompany.lab7.AccountBean" %>
<!DOCTYPE html>
<html>
	<head>
		<title>Account Information</title>
		<link
			rel="stylesheet"
			href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"
		/>
	</head>
	<body>
		<div class="container mt-5">
			<h1 class="mb-4">Enter Account Information</h1>
			<form action="shop" method="post">
				<input type="hidden" name="action" value="account" />
				<div class="form-group">
					<label for="name">Name:</label>
					<input type="text" class="form-control" id="name" name="name" required />
				</div>
				<div class="form-group">
					<label for="visaNumber">Visa Number:</label>
					<input type="text" class="form-control" id="visaNumber" name="visaNumber" required />
				</div>
				<div class="form-group">
					<label for="address">Address:</label>
					<textarea class="form-control" id="address" name="address" required></textarea>
				</div>
				<button type="submit" class="btn btn-primary">Continue</button>
			</form>
		</div>
	</body>
</html>
