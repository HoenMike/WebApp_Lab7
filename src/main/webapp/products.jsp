<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, com.example.beans.ProductBean, com.example.beans.AccountBean" %>
<!DOCTYPE html>
<html>
<head>
    <title>Product Selection</title>
</head>
<body>
    <h1>Select Products</h1>
    <% List<ProductBean> products = (List<ProductBean>) request.getAttribute("products"); %>
    <form action="shopping" method="post">
        <input type="hidden" name="action" value="cart">
        <table>
            <tr><th>Product</th><th>Manufacturer</th><th>Country</th><th>Price</th><th>Quantity</th><th>Add/Remove</th></tr>
            <% for (ProductBean product : products) { %>
            <tr>
                <td><%= product.getName() %></td>
                <td><%= product.getManufacturer() %></td>
                <td><%= product.getCountry() %></td>
                <td><%= product.getPrice() %></td>
                <td><input type="number" name="quantity-<%= product.getProductID() %>" value="0" min="0"></td>
                <td><button type="submit" name="action" value="<%= product.getProductID() %>">Add/Remove</button></td>
            </tr>
            <% } %>
        </table>
        <button type="submit" name="action" value="checkout">Checkout</button>
    </form>
    <% AccountBean account = (AccountBean) request.getAttribute("account"); %>
    <p>Logged in as: <%= account.getName() %></p>
</body>
</html>