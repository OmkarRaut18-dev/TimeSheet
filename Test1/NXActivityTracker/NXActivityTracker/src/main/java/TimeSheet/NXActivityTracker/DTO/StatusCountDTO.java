package TimeSheet.NXActivityTracker.DTO;

public class StatusCountDTO
{
    private int workingDays;
    private int leaveDays;
    private int holidays;

    public StatusCountDTO() {
    }

    public StatusCountDTO(int workingDays, int leaveDays, int holidays) {
        this.workingDays = workingDays;
        this.leaveDays = leaveDays;
        this.holidays = holidays;
    }

    public int getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(int workingDays) {
        this.workingDays = workingDays;
    }

    public int getLeaveDays() {
        return leaveDays;
    }

    public void setLeaveDays(int leaveDays) {
        this.leaveDays = leaveDays;
    }

    public int getHolidays() {
        return holidays;
    }

    public void setHolidays(int holidays) {
        this.holidays = holidays;
    }
}
