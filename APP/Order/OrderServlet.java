import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class OrderServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Set response type
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        // Retrieve form parameters
        String orderId = request.getParameter("orderId");
        String emailId = request.getParameter("emailId");

        // Validate input
        if (orderId == null || emailId == null || orderId.isEmpty() || emailId.isEmpty()) {
            out.println("<h3>Invalid input. Please enter Order ID and Email ID correctly.</h3>");
            return;
        }

        // Database credentials (replace with your own)
        String url = "jdbc:mysql://localhost:3306/orderdb"; // Change to your DB URL
        String user = "root"; // Change to your DB username
        String password = "password"; // Change to your DB password

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // Establish a database connection
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);

            // Prepare SQL query to fetch the order
            String sql = "SELECT status FROM orders WHERE order_id = ? AND email = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, orderId);
            stmt.setString(2, emailId);

            // Execute query and get the result
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String status = rs.getString("status");
                out.println("<h3>Order Status: " + status + "</h3>");
            } else {
                out.println("<h3>Order not found or email does not match.</h3>");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h3>Error connecting to the database.</h3>");
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
