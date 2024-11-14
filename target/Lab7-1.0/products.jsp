<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.mycompany.lab7.DBConnection, java.util.List, com.mycompany.lab7.ProductBean, com.mycompany.lab7.AccountBean, com.mycompany.lab7.OrderBean" %>
<!DOCTYPE html>
<html>
<head>
    <title>Product Selection</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-5">
        <h1 class="mb-4">Select Products</h1>
        <% List<ProductBean> products = (List<ProductBean>) request.getAttribute("products"); %>
        <form action="shop" method="post">
            <input type="hidden" name="action" value="cart">
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>Product</th>
                        <th>Manufacturer</th>
                        <th>Country</th>
                        <th>Price</th>
                        <th>Quantity</th>
                        <th>Add/Remove</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (ProductBean product : products) { %>
                    <tr>
                        <td><%= product.getName() %></td>
                        <td><%= product.getManufacturer() %></td>
                        <td><%= product.getCountry() %></td>
                        <td><%= product.getPrice() %></td>
                        <td><input type="number" class="form-control" name="quantity-<%= product.getProductID() %>" value="0" min="0"></td>
                        <td><button type="submit" class="btn btn-danger" name="action" value="<%= product.getProductID() %>">Remove</button></td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
            <button type="submit" class="btn btn-primary" name="action" value="checkout">Checkout</button>
            <a href="account.jsp" class="btn btn-secondary">Logout</a>
        </form>
        <% AccountBean account = (AccountBean) request.getAttribute("account"); %>
        <p class="mt-3">Logged in as: <%= account.getName() %></p>
    </div>
</body>
</html>