package com.bookstore;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@WebServlet("/searchBooks")
public class SearchBookServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database connection manager instance
    ApplicationContext context = new ClassPathXmlApplicationContext("springApplicationContext.xml");
    DBConnectionManager dbManager = context.getBean("dbConnectionManager", DBConnectionManager.class);


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve the search query from the request
        String searchQuery = request.getParameter("title");
        System.out.println(searchQuery);

        // Prepare response writer to send feedback to the client
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            // Open database connection
            dbManager.openConnection();
            Connection conn = dbManager.getConnection();

            // Prepare the SQL query to search for books by title
            String query = "SELECT * FROM Books WHERE title LIKE ?";
            PreparedStatement stmt = conn.prepareStatement(query);

            // Set query parameter (use % for partial matches)
            stmt.setString(1, "%" + searchQuery + "%");

            // Execute the query
            ResultSet rs = stmt.executeQuery();

            // Start generating the HTML response
            out.println("<html>");
            out.println("<head><title>Search Results</title></head>");
            out.println("<body>");
            out.println("<h2>Search Results for: \"" + (searchQuery == null ? "All Books" : searchQuery) + "\"</h2>");

            // Check if there are results
            if (!rs.isBeforeFirst()) { // No data in ResultSet
                out.println("<p>No tasks found matching the search query.</p>");
            } else {
                // Generate an HTML table for the results
                out.println("<table border='1' cellspacing='0' cellpadding='5'>");
                out.println("<tr><th>ID</th><th>Title</th><th>Author</th><th>Price</th></tr>");

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

                out.println("</table>");
            }

            out.println("</body>");
            out.println("</html>");

            // Close the result set and statement
            rs.close();
            stmt.close();
        } catch (Exception e) {
            // Handle exceptions and respond with an error message
            e.printStackTrace();
            out.println("<h2>An error occurred while searching for books.</h2>");
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
