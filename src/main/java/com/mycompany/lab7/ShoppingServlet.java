package com.mycompany.lab7;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/ShoppingServlet")
public class ShoppingServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/web_lab7";
    private static final String DB_USERNAME = "hoenmike";
    private static final String DB_PASSWORD = "Crtm123123";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action != null) {
            switch (action) {
                case "products":
                    showProducts(request, response);
                    break;
                case "checkout":
                    showCheckout(request, response);
                    break;
                case "delete":
                    deleteFromCart(request, response);
                    break;
                case "logout":
                    logout(request, response);
                    break;
                default:
                    showAccount(request, response);
                    break;
            }
        } else {
            showAccount(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action != null) {
            switch (action) {
                case "account":
                    processAccount(request, response);
                    break;
                case "add":
                    addToCart(request, response);
                    break;
                case "checkout":
                    processCheckout(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    break;
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void showAccount(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/account.jsp");
        dispatcher.forward(request, response);
    }

    private void processAccount(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String visaNumber = request.getParameter("visaNumber");
        String address = request.getParameter("address");

        AccountBean account = new AccountBean();
        account.setName(name);
        account.setVisaNumber(visaNumber);
        account.setAddress(address);

        // Save the account information to the database
        saveAccountToDatabase(account);

        // Store the account information in the session
        HttpSession session = request.getSession();
        session.setAttribute("account", account);

        showProducts(request, response);
    }

    private void showProducts(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<ProductBean> products = getProductsFromDatabase();
        List<CartItem> cart = (List<CartItem>) request.getSession().getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            request.getSession().setAttribute("cart", cart);
        }

        request.setAttribute("products", products);
        request.setAttribute("cart", cart);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/products.jsp");
        dispatcher.forward(request, response);
    }

    private void addToCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int productId = Integer.parseInt(request.getParameter("productId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        ProductBean product = getProductFromDatabase(productId);
        CartItem item = new CartItem(product, quantity);

        @SuppressWarnings("unchecked")
        List<CartItem> cart = (List<CartItem>) request.getSession().getAttribute("cart");
        cart.add(item);

        showProducts(request, response);
    }

    private void deleteFromCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int itemId = Integer.parseInt(request.getParameter("itemId"));

        @SuppressWarnings("unchecked")
        List<CartItem> cart = (List<CartItem>) request.getSession().getAttribute("cart");
        cart.removeIf(item -> item.getItemId() == itemId);

        showProducts(request, response);
    }

    private void showCheckout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        @SuppressWarnings("unchecked")
        List<CartItem> cart = (List<CartItem>) request.getSession().getAttribute("cart");
        AccountBean account = (AccountBean) request.getSession().getAttribute("account");

        double total = 0;
        for (CartItem item : cart) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }

        request.setAttribute("cart", cart);
        request.setAttribute("account", account);
        request.setAttribute("total", total);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/checkout.jsp");
        dispatcher.forward(request, response);
    }

    private void processCheckout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Process the order and save it to the database
        @SuppressWarnings("unchecked")
        List<CartItem> cart = (List<CartItem>) request.getSession().getAttribute("cart");
        AccountBean account = (AccountBean) request.getSession().getAttribute("account");

        saveOrderToDatabase(account, cart);

        // Clear the cart
        request.getSession().setAttribute("cart", new ArrayList<>());

        response.sendRedirect("ShoppingServlet?action=products");
    }

    private void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Clear the session
        request.getSession().invalidate();
        showAccount(request, response);
    }

    private List<ProductBean> getProductsFromDatabase() {
        List<ProductBean> products = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD); PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Product"); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int productId = rs.getInt("productID");
                String name = rs.getString("name");
                String manufacturer = rs.getString("manufacturer");
                String country = rs.getString("country");
                double price = rs.getDouble("price");
                products.add(new ProductBean(productId, name, manufacturer, country, price));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    private ProductBean getProductFromDatabase(int productId) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD); PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Product WHERE productID = ?")) {
            stmt.setInt(1, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    String manufacturer = rs.getString("manufacturer");
                    String country = rs.getString("country");
                    double price = rs.getDouble("price");
                    return new ProductBean(productId, name, manufacturer, country, price);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void saveAccountToDatabase(AccountBean account) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD); PreparedStatement stmt = conn.prepareStatement("INSERT INTO Customer (name, visaNumber, address) VALUES (?, ?, ?)")) {
            stmt.setString(1, account.getName());
            stmt.setString(2, account.getVisaNumber());
            stmt.setString(3, account.getAddress());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void saveOrderToDatabase(AccountBean account, List<CartItem> cart) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            conn.setAutoCommit(false);

            // Insert the customer
            PreparedStatement customerStmt = conn.prepareStatement("INSERT INTO Customer (name, visaNumber, address) VALUES (?, ?, ?)");
            customerStmt.setString(1, account.getName());
            customerStmt.setString(2, account.getVisaNumber());
            customerStmt.setString(3, account.getAddress());
            customerStmt.executeUpdate();

            // Get the customer ID
            int customerId;
            try (ResultSet rs = customerStmt.getGeneratedKeys()) {
                if (rs.next()) {
                    customerId = rs.getInt(1);
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }

            // Insert the order
            PreparedStatement orderStmt = conn.prepareStatement("INSERT INTO `Order` (customerID, orderDate, totalAmount) VALUES (?, CURDATE(), ?)");
            double total = 0;
            for (CartItem item : cart) {
                total += item.getProduct().getPrice() * item.getQuantity();
            }
            orderStmt.setInt(1, customerId);
            orderStmt.setDouble(2, total);
            orderStmt.executeUpdate();

            // Get the order ID
            int orderId;
            try (ResultSet rs = orderStmt.getGeneratedKeys()) {
                if (rs.next()) {
                    orderId = rs.getInt(1);
                } else {
                    throw new SQLException("Creating order failed, no ID obtained.");
                }
            }

            // Insert the order items
            PreparedStatement orderItemStmt = conn.prepareStatement("INSERT INTO OrderItems (orderID, productID, itemPrice, quantity) VALUES (?, ?, ?, ?)");
            for (CartItem item : cart) {
                orderItemStmt.setInt(1, orderId);
                orderItemStmt.setInt(2, item.getProduct().getProductID());
                orderItemStmt.setDouble(3, item.getProduct().getPrice());
                orderItemStmt.setInt(4, item.getQuantity());
                orderItemStmt.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
