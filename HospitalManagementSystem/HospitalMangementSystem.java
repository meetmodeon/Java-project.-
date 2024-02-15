package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalMangementSystem {
    private static final String url="jdbc:mysql://localhost:3306/hospitalManagementSystem";
    private static final String username="root";
    private static final String password="123456";

    public static void main(String[] args){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        Scanner sc=new Scanner(System.in);
        try{
            Connection con= DriverManager.getConnection(url,username,password);
            Patient patient=new Patient(con,sc);
            Doctor doctor=new Doctor(con);
            while(true){
                System.out.println("HOSPITAL MANAGEMENT SYSTEM ");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patients");
                System.out.println("3. View Doctors");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit");
                System.out.println("Enter your choice: ");
                int choice=sc.nextInt();

                switch(choice){
                    case 1:
                        //Add patient
                        patient.addPatient();
                        System.out.println();
                        break;
                    case 2:
                        //View Patient
                        doctor.viewDoctors();
                        System.out.println();
                        break;
                    case 3:
                        // View Doctors
                        doctor.viewDoctors();
                        System.out.println();
                        break;
                    case 4:
                        // Book Appointment
                        bookAppointment(patient,doctor,con,sc);
                    case 5:
                        System.out.println("THANK YOU! FOR USING HOSPITAL MANGEMENT SYSTEM!");
                        return;
                    default:
                        System.out.println("Enter valid choice!!!");
                        break;
                }

            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public static void bookAppointment(Patient patient,Doctor doctor,Connection con,Scanner scanner){
        System.out.println("Enter Patient Id: ");
        int patientId=scanner.nextInt();
        System.out.println("Enter Doctor Id: ");
        int doctorId=scanner.nextInt();
        System.out.println("Enter appointment dat (YYYY-MM-DD): ");
        String appointmentDate=scanner.next();
        if(patient.getPatientById(patientId)&&doctor.getDoctorById(doctorId)){
            if(checkDoctorAvailability(doctorId,appointmentDate,con)){
                String appointmentQuery="insert into appointments(patient_id,doctor_id,appointment_date) values(?,?,?)";
                try{
                    PreparedStatement preparedStatement=con.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1,patientId);
                    preparedStatement.setInt(2,doctorId);
                    preparedStatement.setString(3,appointmentDate);
                    int rowsAffected=preparedStatement.executeUpdate();
                    if(rowsAffected>0){
                        System.out.println("Appoointment Booked!");
                    }else{
                        System.out.println("Failed to Book Appointment!");
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }else{
                System.out.println("Doctor not available on this date!!");
            }
        }else{
            System.out.println("Either doctor or patient doesn't exist!!!");
        }
    }
    public static boolean checkDoctorAvailability(int doctorId,String appointmentData,Connection connection){
        String query="select count(*) from appointments where doctor_id=? and appointment_date=?";
        try{
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setInt(1,doctorId);
            preparedStatement.setString(2,appointmentData);
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                int count=resultSet.getInt(1);
                if(count==0){
                    return true;
                }else{
                    return false;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
