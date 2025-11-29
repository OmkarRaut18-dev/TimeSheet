package TimeSheet.NXActivityTracker.DTO;

import java.time.LocalDate;
import java.util.Objects;

public class reportGenerationDTO
{
    private String month;
    private String rnId;
    private String nameOfEmployee;
    private String designation;
    private String fteTransferFromCostCode;
    private String fteTransferToCostCode;
    private String numberOfWorkingDays;
    private String verticalFrom;
    private String verticalTo;
    private String submittedBy;
    private LocalDate submittedOn;
    private String remarks;
    private String numberOfWorkingHours;
    private String bowp;

    public reportGenerationDTO(String month, String rnId, String nameOfEmployee, String designation, String fteTransferFromCostCode, String fteTransferToCostCode, String numberOfWorkingDays, String verticalFrom, String verticalTo, String submittedBy, LocalDate submittedOn, String remarks, String numberOfWorkingHours, String bowp) {
        this.month = month;
        this.rnId = rnId;
        this.nameOfEmployee = nameOfEmployee;
        this.designation = designation;
        this.fteTransferFromCostCode = fteTransferFromCostCode;
        this.fteTransferToCostCode = fteTransferToCostCode;
        this.numberOfWorkingDays = numberOfWorkingDays;
        this.verticalFrom = verticalFrom;
        this.verticalTo = verticalTo;
        this.submittedBy = submittedBy;
        this.submittedOn = submittedOn;
        this.remarks = remarks;
        this.numberOfWorkingHours = numberOfWorkingHours;
        this.bowp = bowp;
    }

    public reportGenerationDTO() {
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getRnId() {
        return rnId;
    }

    public void setRnId(String rnId) {
        this.rnId = rnId;
    }

    public String getNameOfEmployee() {
        return nameOfEmployee;
    }

    public void setNameOfEmployee(String nameOfEmployee) {
        this.nameOfEmployee = nameOfEmployee;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getFteTransferFromCostCode() {
        return fteTransferFromCostCode;
    }

    public void setFteTransferFromCostCode(String fteTransferFromCostCode) {
        this.fteTransferFromCostCode = fteTransferFromCostCode;
    }

    public String getFteTransferToCostCode() {
        return fteTransferToCostCode;
    }

    public void setFteTransferToCostCode(String fteTransferToCostCode) {
        this.fteTransferToCostCode = fteTransferToCostCode;
    }

    public String getNumberOfWorkingDays() {
        return numberOfWorkingDays;
    }

    public void setNumberOfWorkingDays(String numberOfWorkingDays) {
        this.numberOfWorkingDays = numberOfWorkingDays;
    }

    public String getVerticalFrom() {
        return verticalFrom;
    }

    public void setVerticalFrom(String verticalFrom) {
        this.verticalFrom = verticalFrom;
    }

    public String getVerticalTo() {
        return verticalTo;
    }

    public void setVerticalTo(String verticalTo) {
        this.verticalTo = verticalTo;
    }

    public String getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(String submittedBy) {
        this.submittedBy = submittedBy;
    }

    public LocalDate getSubmittedOn() {
        return submittedOn;
    }

    public void setSubmittedOn(LocalDate submittedOn) {
        this.submittedOn = submittedOn;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getNumberOfWorkingHours() {
        return numberOfWorkingHours;
    }

    public void setNumberOfWorkingHours(String numberOfWorkingHours) {
        this.numberOfWorkingHours = numberOfWorkingHours;
    }

    public String getBowp() {
        return bowp;
    }

    public void setBowp(String bowp) {
        this.bowp = bowp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        reportGenerationDTO that = (reportGenerationDTO) o;
        return Objects.equals(month, that.month) &&
                Objects.equals(rnId, that.rnId) &&
                Objects.equals(nameOfEmployee, that.nameOfEmployee) &&
                Objects.equals(fteTransferFromCostCode, that.fteTransferFromCostCode) &&
                Objects.equals(fteTransferToCostCode, that.fteTransferToCostCode) &&
                Objects.equals(bowp, that.bowp); // Include other fields as needed
    }

    @Override
    public int hashCode() {
        return Objects.hash(month, rnId, nameOfEmployee, fteTransferFromCostCode, fteTransferToCostCode, bowp);
    }

}
