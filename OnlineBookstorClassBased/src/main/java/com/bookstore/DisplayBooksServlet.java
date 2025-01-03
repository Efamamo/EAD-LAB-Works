package com.bookstore;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
@WebServlet("/displayBooks")
public class DisplayBooksServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database connection manager instance
    ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
    DBConnectionManager dbManager = context.getBean(DBConnectionManager.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Set response content type
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            // Open database connection
            dbManager.openConnection();
            Connection conn = dbManager.getConnection();

            // Execute SQL query to retrieve books
            String query = "SELECT * FROM Books";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // Start generating the HTML table
            out.println("<html>");
            out.println("<head><title>Task List</title></head>");
            out.println("<body>");
            out.println("<h2>Task List</h2>");
            out.println("<table border='1' cellspacing='0' cellpadding='5'>");
            out.println("<tr><th>ID</th><th>Title</th><th>Author</th><th>Price</th></tr>");

            // Iterate through the result set and populate the table
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                int price = rs.getInt("price");

                out.println("<tr>");
                out.println("<td>" + id + "</td>");
                out.println("<td>" + title + "</td>");
                out.println("<td>" + author + "</td>");
                out.println("<td>" + price + "</td>");
                out.println("</tr>");
            }

            // End the HTML table and document
            out.println("</table>");
            out.println("</body>");
            out.println("</html>");

            // Close the result set and statement
            rs.close();
            stmt.close();
        } catch (Exception e) {
            // Handle exceptions and respond with an error message
            e.printStackTrace();
            out.println("<h2>An error occurred while retrieving books.</h2>");
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
