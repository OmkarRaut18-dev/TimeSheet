package TimeSheet.NXActivityTracker.DTO;

import java.util.List;

public class TimeSheetActivityRequestDTO
{
    private String department;
    private String bowpNumber;
    private String fteCostTransferFrom;
    private String fteCostTransferTo;
    private String projectActivity;
    private List<String> dates;
    private String status;
    private String userId;

    public TimeSheetActivityRequestDTO() {
    }

    public TimeSheetActivityRequestDTO(String department, String bowpNumber, String fteCostTransferFrom, String fteCostTransferTo, String projectActivity, List<String> dates, String status, String userId) {
        this.department = department;
        this.bowpNumber = bowpNumber;
        this.fteCostTransferFrom = fteCostTransferFrom;
        this.fteCostTransferTo = fteCostTransferTo;
        this.projectActivity = projectActivity;
        this.dates = dates;
        this.status = status;
        this.userId = userId;
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

    public String getFteCostTransferFrom() {
        return fteCostTransferFrom;
    }

    public void setFteCostTransferFrom(String fteCostTransferFrom) {
        this.fteCostTransferFrom = fteCostTransferFrom;
    }

    public String getFteCostTransferTo() {
        return fteCostTransferTo;
    }

    public void setFteCostTransferTo(String fteCostTransferTo) {
        this.fteCostTransferTo = fteCostTransferTo;
    }

    public String getProjectActivity() {
        return projectActivity;
    }

    public void setProjectActivity(String projectActivity) {
        this.projectActivity = projectActivity;
    }

    public List<String> getDates() {
        return dates;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
