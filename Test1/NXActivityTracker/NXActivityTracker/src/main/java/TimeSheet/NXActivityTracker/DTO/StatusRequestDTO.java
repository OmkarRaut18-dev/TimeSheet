package TimeSheet.NXActivityTracker.DTO;

public class StatusRequestDTO {
    private int year;
    private int month;
    private String userId;

    public StatusRequestDTO() {
    }

    public StatusRequestDTO(int year, int month, String userId) {
        this.year = year;
        this.month = month;
        this.userId = userId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
