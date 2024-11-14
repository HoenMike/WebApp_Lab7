/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.lab7;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ShoppingServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        switch (action) {
            case "account":
                processAccount(request, response);
                break;
            case "cart":
                processCart(request, response);
                break;
            case "checkout":
                processCheckout(request, response);
                break;
            case "complete":
                processComplete(request, response);
                break;
            default:
                response.sendRedirect("account.jsp");
        }
    }

    private void processAccount(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AccountBean account = new AccountBean();
        account.setName(request.getParameter("name"));
        account.setVisaNumber(request.getParameter("visaNumber"));
        account.setAddress(request.getParameter("address"));
        request.setAttribute("account", account);
        request.getRequestDispatcher("products.jsp").forward(request, response);
    }

    private void processCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AccountBean account = (AccountBean) request.getAttribute("account");
        List<ProductBean> products = new ArrayList<>();
        // Retrieve products from database and add to products list
        request.setAttribute("products", products);
        request.setAttribute("account", account);
        request.getRequestDispatcher("products.jsp").forward(request, response);
    }

    private void processCheckout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AccountBean account = (AccountBean) request.getAttribute("account");
        List<ProductBean> cart = new ArrayList<>();
        // Retrieve selected products from request and add to cart list
        OrderBean order = new OrderBean();
        order.setTotalAmount(calculateTotal(cart));
        request.setAttribute("cart", cart);
        request.setAttribute("account", account);
        request.setAttribute("order", order);
        request.getRequestDispatcher("checkout.jsp").forward(request, response);
    }

    private void processComplete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
