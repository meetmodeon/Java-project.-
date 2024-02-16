import java.sql.*;
import java.util.Scanner;

public class HotelReservationSystem {
    private static final String url="jdbc:mysql://localhost:3306/hotel_db";
    private static final String username="root";
    private static final String password="123456";

    public static void main(String[] args)throws ClassNotFoundException, SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        Scanner sc;
        Connection con;
        try {
            con = DriverManager.getConnection(url, username, password);
            while (true) {
                System.out.println();
                System.out.println("HOTEL MANAGEMENT SYSTEM");
                sc = new Scanner(System.in);
                System.out.println("1.Reservation a Room");
                System.out.println("2.View Reservations");
                System.out.println("3.Get Room Number");
                System.out.println("4.Update Reservations");
                System.out.println("5.Delete Reservations");
                System.out.println("0.Exit");
                System.out.println("Choose an Option:");
                int choice = sc.nextInt();
                switch (choice) {
                    case 1:
                        reserveRoom(con,sc);
                        break;
                    case 2:
                        viewReservation(con);
                        break;
                    case 3:
                        getRoomNumber(con,sc);
                        break;
                    case 4:
                        updateReservation(con,sc);
                        break;
                    case 5:
                        deleteReservation(con,sc);
                        break;
                    case 0:
                        exit();
                        sc.close();
                        return;
                    default:
                        System.out.println("Invalid Choice.Try again.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private static void deleteReservation(Connection con, Scanner sc) {
        try{
            System.out.println("Enter reservationID to delete");
            int reservationID=sc.nextInt();
            if(!reservationExists(con,reservationID)){
                System.out.println("Reservation not found for the given ID");
            }
            String sql="delete from reservation where reservation_id="+reservationID+"";
            try(Statement stmt=con.createStatement()){
                int affectedRows=stmt.executeUpdate(sql);
                if(affectedRows>0){
                    System.out.println("Reservation deleted Sucessfully!!!");
                }else{
                    System.out.println("Delection of given id failed!!!");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    private static void updateReservation(Connection con, Scanner sc) {
        try{
            System.out.println("Enter reservation ID to update: ");
            int reservationID=sc.nextInt();
            sc.nextLine();
            if(!reservationExists(con,reservationID)){
                System.out.println("Reservation not found for the "+reservationID+" Reservation Id");
                return;
            }
            System.out.println("Enter new guest name: ");
            String new_guestName=sc.nextLine();
            System.out.println("Enter new room Number: ");
            int newRoomNumber=sc.nextInt();
            System.out.println("Enter new contact number: ");
            String newContactNumber=sc.next();

            String sql="update reservation set guest_name='"+new_guestName+"',"+"room_number="+newRoomNumber+","+"contact_number='"+newContactNumber+"'"+" "+"where reservation_id="+reservationID+"";
            try(Statement stmt=con.createStatement()){
                int affectedRows=stmt.executeUpdate(sql);
                if(affectedRows>0){
                    System.out.println("Reservation updated Sucessfully!!!");
                }else
                    System.out.println("Reservation update failed!!!");
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    private static void getRoomNumber(Connection con, Scanner sc) {
        try
        {
            System.out.println("Enter reservation ID: ");
            int reservationID=sc.nextInt();
            System.out.println("Enter guest name: ");
            String guestName=sc.next();
            String sql="select reservation_id,guest_name,room_number from reservation "+"where reservation_id="+reservationID+" "+"and guest_name='"+guestName+"'";
            try(Statement stmt= con.createStatement();
                ResultSet rs=stmt.executeQuery(sql);
            ){
                if(rs.next()){
                    int roomNumber=rs.getInt("room_number");
                    System.out.println("Room Number for Reservation ID"+
                            reservationID+"and Guest: "+guestName +"is: "+roomNumber
                            );
                }else {
                    System.out.println("Reservation Not found for the "+reservationID+" ID and Guest "+guestName);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    private static void reserveRoom(Connection con, Scanner sc) {
        try {
            System.out.println("Enter guest name:");
            String guestName = sc.next();
            sc.nextLine();
            System.out.println("Enter room number: ");
            int roomNumber = sc.nextInt();
            System.out.println("Enter contact number: ");
            String contactNumber = sc.next();

            String sql = "insert into reservation (guest_name,room_number,contact_number) values ('"+guestName+"',"+roomNumber+",'"+contactNumber+"')";

            try(Statement stmt=con.createStatement()){
                int affectedRows=stmt.executeUpdate(sql);
                if(affectedRows>0){
                    System.out.println("Reservation Sucessfully!!");
                }else
                    System.out.println("Reservation Failed...");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    private static void viewReservation(Connection con) throws SQLException{
        String sql="select * from reservation";
        try(Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery(sql)
        ){
            System.out.println("Current Reservations: ");
            System.out.println("+-----------------------+-----------------+--------------------+----------------------+---------------------+");
            System.out.println("| Reservation ID        | Guest           | Room Number        | Contact Number       | Reservation Date     ");
            System.out.println("+-----------------------+-----------------+--------------------+----------------------+---------------------+");
            while(rs.next()){
                int reservationid=rs.getInt("reservation_id");
                String guestname=rs.getString("guest_name");
                int roomNumber=rs.getInt("room_number");
                String contactNumber=rs.getString("contact_number");
                String reservationDate=rs.getTimestamp("reservation_date").toString();

                System.out.printf("| %-20d | %-15s | %-13d | %-20s | %-19s |\n",reservationid,guestname,roomNumber,contactNumber,reservationDate);
            }
            System.out.println("+---------------------+-----------------+---------------------+-----------------------+--------------------+");
            System.out.println("Closed all statement!!!");
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    private static boolean reservationExists(Connection con,int reservationID){
        try{
            String sql="select reservation_id from reservation where reservation_id="+reservationID+"";
            try(Statement stmt=con.createStatement();
                ResultSet rs=stmt.executeQuery(sql);
            ){
                return rs.next();
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
            return false;
        }

    }
    public static void exit()throws InterruptedException{
        System.out.println("Exiting System");
        int i=5;
        while(i!=0){
            System.out.println(".");
            Thread.sleep(450);
            i--;
        }
        System.out.println();
        System.out.println("Thank You for using Hotel Reservation System!!!");
    }

}
