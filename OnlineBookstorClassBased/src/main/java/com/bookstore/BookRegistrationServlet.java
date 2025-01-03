package com.bookstore;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@WebServlet("/register")
public class BookRegistrationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database connection manager instance

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve Book details from the request
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        int price = Integer.parseInt(request.getParameter("price"));
        

        // Prepare response writer to send feedback to the client
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        DBConnectionManager dbManager = context.getBean(DBConnectionManager.class);

        try {
            // Validate input
            if (title == null || title.trim().isEmpty() ||
                author == null || author.trim().isEmpty() ) {
                out.println("<h2>All fields are required. Please provide valid inputs.</h2>");
                return;
            }

            if (price < 0){
                out.println("<h2>Price Can not be negative.</h2>");
                return;
            }

            // Open database connection
            dbManager.openConnection();
            Connection conn = dbManager.getConnection();

            // Prepare the SQL query to insert book details
            String query = "INSERT INTO Books (title, author, price) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);

            // Set query parameters
            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.setInt(3, price);

            // Execute the query
            int rowsAffected = stmt.executeUpdate();

            // Respond with a success message
            if (rowsAffected > 0) {
                response.sendRedirect("/OnlineBookStore/displayBooks");            } else {
                out.println("<h2>Book registration failed. Please try again.</h2>");
            }

            // Close the statement
            stmt.close();
        } catch (Exception e) {
            // Log the exception and respond with an error message
            e.printStackTrace();
            out.println("<h2>An error occurred while registering the book. Please try again later.</h2>");
        } finally {
            try {
                // Close the database connection
                dbManager.closeConnection();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
