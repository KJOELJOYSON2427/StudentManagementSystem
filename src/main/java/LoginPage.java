import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/LoginPage")
public class LoginPage extends HttpServlet {

    // JDBC connection details
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/StudentAttendance";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "joel.2427";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    public void init() throws ServletException {
    super.init();
    try {
        // Load the MySQL JDBC driver
        Class.forName("com.mysql.cj.jdbc.Driver");
        System.out.println("MySQL JDBC Driver Registered!");
    } catch (ClassNotFoundException e) {
        System.out.println("MySQL JDBC Driver not found. Include it in your library path.");
        e.printStackTrace();
    }
}

   protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    response.setContentType("text/html;charset=UTF-8");
    
    // Fetching the roll number and password from the request
    String rollNumber = request.getParameter("rollno");
    String password = request.getParameter("password");
    
    if (rollNumber == null || rollNumber.isEmpty() || password == null || password.isEmpty()) {
        try (PrintWriter out = response.getWriter()) {
            out.println("<html><body>");
            out.println("<h2>Login failed. Please fill in all fields.</h2>");
            out.println("<a href=\"index.html\">Try again</a>");
            out.println("</body></html>");
        }
        return; // Exit early if validation fails
    }
    
    
   
    // Validate the roll number and password using JDBC
    try (PrintWriter out = response.getWriter()) {
        if (validateLogin(rollNumber, password)) {
            // Redirect to the next page if the login is successful
            response.sendRedirect("attendance.html");
        } else {
            // Display error message if login fails
            out.println("<html><body>");
            out.println("<h2>Login failed. Invalid roll number or password.</h2>");
            out.println("<a href=\"index.html\">Try again</a>");
            out.println("</body></html>");
        }
    }
}


    private boolean validateLogin(String rollNumber, String password) {
        boolean isValid = false;

        // Establish JDBC connection and query the database
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String query = "SELECT * FROM students WHERE rollno= ? AND password= ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, rollNumber);
            preparedStatement.setString(2, password);  // Ideally, password should be hashed and checked.

            ResultSet resultSet = preparedStatement.executeQuery();

            // If a record is found, the credentials are valid
            if (resultSet.next()) {
                isValid = true;
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Log exception for debugging
        }

        return isValid;
    }

    @Override
    public String getServletInfo() {
        return "LoginPage Servlet for handling roll number-based login";
    }
}