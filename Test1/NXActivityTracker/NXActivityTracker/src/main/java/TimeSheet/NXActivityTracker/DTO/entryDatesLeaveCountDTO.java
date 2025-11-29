package TimeSheet.NXActivityTracker.DTO;

import java.time.LocalDate;
import java.util.List;

public class entryDatesLeaveCountDTO
{
    private List<LocalDate> entryDate;
    private int countOfLeave;

    public entryDatesLeaveCountDTO() {
    }

    public entryDatesLeaveCountDTO(List<LocalDate> entryDate, int countOfLeave) {
        this.entryDate = entryDate;
        this.countOfLeave = countOfLeave;
    }

    public List<LocalDate> getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(List<LocalDate> entryDate) {
        this.entryDate = entryDate;
    }

    public int getCountOfLeave() {
        return countOfLeave;
    }

    public void setCountOfLeave(int countOfLeave) {
        this.countOfLeave = countOfLeave;
    }
}
