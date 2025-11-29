package TimeSheet.NXActivityTracker.DTO;

import java.time.LocalDateTime;

public class UserListForAdminDTO {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private LocalDateTime createdAt;
    private String statusOfEntries;
    private entryDatesLeaveCountDTO leaveCount;


    public UserListForAdminDTO() {
    }

    public UserListForAdminDTO(String userId, String firstName, String lastName, String email, String role, LocalDateTime createdAt, String statusOfEntries, entryDatesLeaveCountDTO leaveCount) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
        this.statusOfEntries = statusOfEntries;
        this.leaveCount = leaveCount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatusOfEntries() {
        return statusOfEntries;
    }

    public void setStatusOfEntries(String statusOfEntries) {
        this.statusOfEntries = statusOfEntries;
    }

    public entryDatesLeaveCountDTO getLeaveCount() {
        return leaveCount;
    }

    public void setLeaveCount(entryDatesLeaveCountDTO leaveCount) {
        this.leaveCount = leaveCount;
    }
}
