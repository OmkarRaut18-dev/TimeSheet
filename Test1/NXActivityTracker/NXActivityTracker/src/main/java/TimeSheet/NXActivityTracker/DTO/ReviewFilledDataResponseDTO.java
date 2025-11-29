package TimeSheet.NXActivityTracker.DTO;

import java.time.LocalDate;

public class ReviewFilledDataResponseDTO
{
    private String department;
    private String bowpNumber;
    private String FTETransferToCostCode;
    private String projectActivity;
    private LocalDate date;
    private String status;

    public ReviewFilledDataResponseDTO() {
    }

    public ReviewFilledDataResponseDTO(String department, String bowpNumber, String FTETransferToCostCode, String projectActivity, LocalDate date, String status) {
        this.department = department;
        this.bowpNumber = bowpNumber;
        this.FTETransferToCostCode = FTETransferToCostCode;
        this.projectActivity = projectActivity;
        this.date = date;
        this.status = status;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getBowpNumber() {
        return bowpNumber;
    }

    public void setBowpNumber(String bowpNumber) {
        this.bowpNumber = bowpNumber;
    }

    public String getFTETransferToCostCode() {
        return FTETransferToCostCode;
    }

    public void setFTETransferToCostCode(String FTETransferToCostCode) {
        this.FTETransferToCostCode = FTETransferToCostCode;
    }

    public String getProjectActivity() {
        return projectActivity;
    }

    public void setProjectActivity(String projectActivity) {
        this.projectActivity = projectActivity;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
