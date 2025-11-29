package TimeSheet.NXActivityTracker.DTO;

import jakarta.persistence.Column;

import java.time.LocalDateTime;

public class CreateUserDTO
{
    private Long id;
    public String userId;
    public String firstName;
    private String lastName;
    private String email;
    private String rnNumber;
    private String role;
    private LocalDateTime createdAt;
    private String password;
    private String designation;

    public CreateUserDTO() {
    }

    public CreateUserDTO(Long id, String userId, String firstName, String lastName, String email, String rnNumber, String role, LocalDateTime createdAt, String password, String designation) {
        this.id = id;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.rnNumber = rnNumber;
        this.role = role;
        this.createdAt = createdAt;
        this.password = password;
        this.designation = designation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getRnNumber() {
        return rnNumber;
    }

    public void setRnNumber(String rnNumber) {
        this.rnNumber = rnNumber;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }
}
