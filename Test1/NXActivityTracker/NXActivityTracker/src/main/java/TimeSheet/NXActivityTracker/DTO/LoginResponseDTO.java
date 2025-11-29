package TimeSheet.NXActivityTracker.DTO;

import java.time.LocalDateTime;
import java.util.UUID;

public class LoginResponseDTO
{
    private Long userId;
    private String username;
    private String role;
    private String rnNumber;
    private UUID projectId;
    private String firstName;
    private String lastName;
    private String email;
    private String designation;
    private LocalDateTime createdAt;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(Long userId, String username, String role, String rnNumber, UUID projectId, String firstName, String lastName, String email, String designation, LocalDateTime createdAt) {
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.rnNumber = rnNumber;
        this.projectId = projectId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.designation = designation;
        this.createdAt = createdAt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRnNumber() {
        return rnNumber;
    }

    public void setRnNumber(String rnNumber) {
        this.rnNumber = rnNumber;
    }

    public UUID getProjectId() {
        return projectId;
    }

    public void setProjectId(UUID projectId) {
        this.projectId = projectId;
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

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
