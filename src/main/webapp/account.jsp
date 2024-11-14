<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> <%@ page
import="com.example.beans.AccountBean" %>
<!DOCTYPE html>
<html>
	<head>
		<title>Account Information</title>
	</head>
	<body>
		<h1>Enter Account Information</h1>
		<form action="shopping" method="post">
			<input type="hidden" name="action" value="account" />
			<label for="name">Name:</label>
			<input type="text" id="name" name="name" required /><br />
			<label for="visaNumber">Visa Number:</label>
			<input type="text" id="visaNumber" name="visaNumber" required /><br />
			<label for="address">Address:</label>
			<textarea id="address" name="address" required></textarea><br />
			<input type="submit" value="Continue" />
		</form>
	</body>
</html>
