package TimeSheet.NXActivityTracker.Model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "timesheet_entries")
public class TimeSheetEntry
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private  String department;//
    private String bowp;//
    @Column(name="FTE transfer From (Cost center code)")
    private String fteCostTransferFrom;
    @Column(name="FTE transfer To (Cost center code)")
    private String fteCostTransferTo;
    @Column(name="project_activity",columnDefinition = "TEXT")
    private String projectActivity;//
    @Column(name = "entry_date",nullable = false)
    private LocalDate entryDate;
    private String status;//
    private String userId;


    public TimeSheetEntry() {
    }

    public TimeSheetEntry(Long id, String department, String bowp, String fteCostTransferFrom, String fteCostTransferTo, String projectActivity, LocalDate entryDate, String status, String userId) {
        this.id = id;
        this.department = department;
        this.bowp = bowp;
        this.fteCostTransferFrom = fteCostTransferFrom;
        this.fteCostTransferTo = fteCostTransferTo;
        this.projectActivity = projectActivity;
        this.entryDate = entryDate;
        this.status = status;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getBowp() {
        return bowp;
    }

    public void setBowp(String bowp) {
        this.bowp = bowp;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
