/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author K Joel Joyson
 */
public class Student {
    private String name;
    private int rollNo;
    private String attendance;

    // Constructor with parameters
    public Student(String name, int rollNo, String attendance) {
        this.name = name;
        this.rollNo = rollNo;
        this.attendance = attendance;
    }

    // Getter methods
    public String getName() {
        return name;
    }

    public int getRollNo() {
        return rollNo;
    }

    public String getAttendance() {
        return attendance;
    }
}
