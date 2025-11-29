package TimeSheet.NXActivityTracker.DTO;

import java.time.LocalDate;

public class getEntryDatesWithStatusDTO
{
    private LocalDate entrydates;
    private String status;

    public getEntryDatesWithStatusDTO() {
    }

    public getEntryDatesWithStatusDTO(LocalDate entrydates, String status) {
        this.entrydates = entrydates;
        this.status = status;
    }

    public LocalDate getEntrydates() {
        return entrydates;
    }

    public void setEntrydates(LocalDate entrydates) {
        this.entrydates = entrydates;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
