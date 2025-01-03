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
@WebServlet("/deleteBook")
public class DeleteBookServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database connection manager instance
    ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
    DBConnectionManager dbManager = context.getBean(DBConnectionManager.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve book ID from the request
        String bookId = request.getParameter("id");

        // Prepare response writer to send feedback to the client
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            // Open database connection
            dbManager.openConnection();
            Connection conn = dbManager.getConnection();

            // Prepare the SQL query to delete the task
            String query = "DELETE FROM Books WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);

            // Set query parameter
            stmt.setInt(1, Integer.parseInt(bookId));

            // Execute the query
            int rowsAffected = stmt.executeUpdate();

            // Respond with a success message
            if (rowsAffected > 0) {
                out.println("<h2>Book with ID " + bookId + " deleted successfully!</h2>");
            } else {
                out.println("<h2>No book found with ID " + bookId + ".</h2>");
            }

            // Close the statement
            stmt.close();
        } catch (Exception e) {
            // Handle exceptions and respond with an error message
            e.printStackTrace();
            out.println("<h2>An error occurred while deleting the book.</h2>");
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
