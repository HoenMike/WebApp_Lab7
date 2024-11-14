<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, com.example.beans.ProductBean, com.example.beans.AccountBean, com.example.beans.OrderBean" %>
<!DOCTYPE html>
<html>
<head>
    <title>Checkout</title>
</head>
<body>
    <h1>Checkout</h1>
    <% List<ProductBean> cart = (List<ProductBean>) request.getAttribute("cart");
       AccountBean account = (AccountBean) request.getAttribute("account");
       OrderBean order = (OrderBean) request.getAttribute("order"); %>
    <table>
        <tr><th>Product</th><th>Quantity</th><th>Price</th></tr>
        <% for (ProductBean product : cart) { %>
        <tr>
            <td><%= product.getName() %></td>
            <td><%= request.getParameter("quantity-" + product.getProductID()) %></td>
            <td><%= product.getPrice() %></td>
        </tr>
        <% } %>
        <tr><td colspan="2">Total:</td><td><%= order.getTotalAmount() %></td></tr>
    </table>
    <p>Logged in as: <%= account.getName() %></p>
    <p>Visa Number: <%= account.getVisaNumber() %></p>
    <p>Address: <%= account.getAddress() %></p>
    <form action="shopping" method="post">
        <input type="hidden" name="action" value="complete">
        <input type="submit" value="Complete Order">
    </form>
</body>
</html>