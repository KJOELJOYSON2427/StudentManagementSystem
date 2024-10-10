import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/AttendanceServlet")
public class AttendanceServlet extends HttpServlet {
    
    // List to store student attendance data temporarily
    private final List<Student> attendanceList = new ArrayList<>();

    // JDBC connection details for the MySQL database
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/StudentAttendance"; // Replace with your database name
    private static final String JDBC_USER = "root"; // Replace with your database username
    private static final String JDBC_PASSWORD = "joel.2427"; // Replace with your database password

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get the submitted form data
        String studentName = request.getParameter("studentName");
        int rollNo = Integer.parseInt(request.getParameter("rollNo"));
        String attendance = request.getParameter("attendance");

        // Create a new student record and add it to the temporary attendance list
        Student student = new Student(studentName, rollNo, attendance);
        attendanceList.add(student);

        // Insert the record into the database
        if (insertAttendanceToDatabase(student)) {
            // Redirect to the same page to show updated attendance after successful insertion
            response.sendRedirect("AttendanceServlet");
        } else {
            // Handle failure (optional)
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to insert record.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            // HTML structure and Attendance Table
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<meta charset=\"UTF-8\">");
            out.println("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
            out.println("<title>Student Attendance Management</title>");
            out.println("<style>table, th, td {border: 1px solid black; border-collapse: collapse; padding: 10px;} th, td {text-align: left;}</style>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Student Attendance Management</h1>");
            out.println("<h2>Attendance Records</h2>");
            out.println("<table>");
            out.println("<thead>");
            out.println("<tr>");
            out.println("<th>Student Name</th>");
            out.println("<th>Roll No</th>");
            out.println("<th>Attendance</th>");
            out.println("</tr>");
            out.println("</thead>");
            out.println("<tbody>");

            // Loop through the attendance list and display it in the table
            for (Student student : attendanceList) {
                out.println("<tr>");
                out.println("<td>" + student.getName() + "</td>");
                out.println("<td>" + student.getRollNo() + "</td>");
                out.println("<td>" + student.getAttendance() + "</td>");
                out.println("</tr>");
            }

            out.println("</tbody>");
            out.println("</table>");
            out.println("<br><a href=\"attendance.html\">Add more attendance</a>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // Method to insert the attendance record into the database
    private boolean insertAttendanceToDatabase(Student student) {
        boolean isSuccess = false;

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String insertSQL = "INSERT INTO student_attendance (name, rollno, attendance) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setString(1, student.getName());
            preparedStatement.setInt(2, student.getRollNo());
            preparedStatement.setString(3, student.getAttendance());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                isSuccess = true; // Insertion was successful
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isSuccess;
    }
}
