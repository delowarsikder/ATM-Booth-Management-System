import java.text.SimpleDateFormat;
import java.util.Date;

public class DateAndTime {

    String currentTime, currentDate;
    public DateAndTime() {
        SimpleDateFormat DateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat TimeFormatter = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        this.currentDate = DateFormatter.format(date);
        this.currentTime = TimeFormatter.format(date);
    }

    public static void main(String[] args) {
        DateAndTime dateAndTime = new DateAndTime();
        System.out.println("Date: " + dateAndTime.currentDate);
        System.out.println("Time: " + dateAndTime.currentTime);
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public String getCurrentDate() {
        return currentDate;
    }

}