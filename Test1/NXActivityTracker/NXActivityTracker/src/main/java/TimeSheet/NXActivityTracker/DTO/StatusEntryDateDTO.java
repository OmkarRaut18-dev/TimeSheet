package TimeSheet.NXActivityTracker.DTO;

import java.time.LocalDate;

public class StatusEntryDateDTO
{
    private LocalDate entryDate;
    private String status;

    public StatusEntryDateDTO() {
    }

    public StatusEntryDateDTO(LocalDate entryDate, String status) {
        this.entryDate = entryDate;
        this.status = status;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
