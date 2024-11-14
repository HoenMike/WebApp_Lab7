<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, com.mycompany.lab7.ProductBean, com.mycompany.lab7.AccountBean, com.mycompany.lab7.OrderBean" %>
<!DOCTYPE html>
<html>
<head>
    <title>Checkout</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-5">
        <h1 class="mb-4">Checkout</h1>
        <% List<ProductBean> cart = (List<ProductBean>) request.getAttribute("cart");
           AccountBean account = (AccountBean) request.getAttribute("account");
           OrderBean order = (OrderBean) request.getAttribute("order"); %>
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>Product</th>
                    <th>Quantity</th>
                    <th>Price</th>
                </tr>
            </thead>
            <tbody>
                <% for (ProductBean product : cart) { %>
                <tr>
                    <td><%= product.getName() %></td>
                    <td><%= request.getParameter("quantity-" + product.getProductID()) %></td>
                    <td><%= product.getPrice() %></td>
                </tr>
                <% } %>
                <tr>
                    <td colspan="2">Total:</td>
                    <td><%= order.getTotalAmount() %></td>
                </tr>
            </tbody>
        </table>
        <p>Logged in as: <%= account.getName() %></p>
        <p>Visa Number: <%= account.getVisaNumber() %></p>
        <p>Address: <%= account.getAddress() %></p>
        <form action="shop" method="post">
            <input type="hidden" name="action" value="complete">
            <button type="submit" class="btn btn-primary">Complete Order</button>
            <a href="products.jsp" class="btn btn-secondary">Shop Some More!</a>
        </form>
    </div>
</body>
</html>