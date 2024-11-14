/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.lab7;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class ShoppingServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        Connection conn = (Connection) getServletContext().getAttribute("connection");

        switch (action) {
            case "account":
                processAccount(request, response, conn);
                break;
            case "cart":
                processCart(request, response, conn);
                break;
            case "checkout":
                processCheckout(request, response, conn);
                break;
            case "complete":
                processComplete(request, response, conn);
                break;
            default:
                response.sendRedirect("account.jsp");
        }
    }

    private void processAccount(HttpServletRequest request, HttpServletResponse response, Connection conn) throws ServletException, IOException {
        String name = request.getParameter("name");
        String visaNumber = request.getParameter("visaNumber");
        String address = request.getParameter("address");

        if (name == null || name.trim().isEmpty()
                || visaNumber == null || visaNumber.trim().isEmpty()
                || address == null || address.trim().isEmpty()) {
            request.setAttribute("error", "All fields are required");
            request.getRequestDispatcher("account.jsp").forward(request, response);
            return;
        }

        AccountBean account = new AccountBean();
        account.setName(name);
        account.setVisaNumber(visaNumber);
        account.setAddress(address);

        // Store account in session
        HttpSession session = request.getSession();
        session.setAttribute("account", account);

        // Save to database and redirect
        try {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Customer (name, visaNumber, address) VALUES (?, ?, ?)");
            stmt.setString(1, name);
            stmt.setString(2, visaNumber);
            stmt.setString(3, address);
            stmt.executeUpdate();

            // Load products and forward to products page
            processCart(request, response, conn);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Database error occurred");
            request.getRequestDispatcher("account.jsp").forward(request, response);
        }
    }

    private void processCart(HttpServletRequest request, HttpServletResponse response, Connection conn) throws ServletException, IOException {
        HttpSession session = request.getSession();
        AccountBean account = (AccountBean) session.getAttribute("account");

        if (account == null) {
            response.sendRedirect("account.jsp");
            return;
        }

        List<ProductBean> products = new ArrayList<>();

        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Product");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ProductBean product = new ProductBean();
                product.setProductID(rs.getInt("productID"));
                product.setName(rs.getString("name"));
                product.setManufacturer(rs.getString("manufacturer"));
                product.setCountry(rs.getString("country"));
                product.setPrice(rs.getDouble("price"));
                products.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        request.setAttribute("products", products);
        request.setAttribute("account", account);
        request.getRequestDispatcher("products.jsp").forward(request, response);
    }

    private void processCheckout(HttpServletRequest request, HttpServletResponse response, Connection conn) throws ServletException, IOException {
        HttpSession session = request.getSession();
        AccountBean account = (AccountBean) session.getAttribute("account");

        if (account == null) {
            response.sendRedirect("account.jsp");
            return;
        }

        List<ProductBean> cart = new ArrayList<>();

        // Retrieve selected products from request and add to cart list
        OrderBean order = new OrderBean();
        order.setTotalAmount(calculateTotal(cart));

        request.setAttribute("cart", cart);
        request.setAttribute("account", account);
        request.setAttribute("order", order);
        request.getRequestDispatcher("checkout.jsp").forward(request, response);
    }

    private void processComplete(HttpServletRequest request, HttpServletResponse response, Connection conn) throws ServletException, IOException {
        HttpSession session = request.getSession();
        AccountBean account = (AccountBean) session.getAttribute("account");

        if (account == null) {
            response.sendRedirect("account.jsp");
            return;
        }

        // Save order to database
        response.sendRedirect("thankyou.jsp");
    }

    private double calculateTotal(List<ProductBean> cart) {
        double total = 0.0;
        for (ProductBean product : cart) {
            total += product.getPrice();
        }
        return total;
    }
}
