package TimeSheet.NXActivityTracker.Repo;

import TimeSheet.NXActivityTracker.DTO.UserListResponseDTO;
import TimeSheet.NXActivityTracker.Model.TimeSheetEntry;
import TimeSheet.NXActivityTracker.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimeSheetRepo extends JpaRepository<User,Long> {

    Optional<User> findByUserIdAndPassword(String userId, String password);

    Optional<User> findByUserId(String userId);

//    @Query("Select DISTINCT e.userId,e.firstName,e.lastName,e.email,e.role,e.createdAt FROM User e")
//    List<UserListResponseDTO> getDeveloperList();

    @Query("SELECT new TimeSheet.NXActivityTracker.DTO.UserListResponseDTO(e.userId, e.firstName, e.lastName, e.email, e.role, e.createdAt) " +
            "FROM User e")
    List<UserListResponseDTO> getDeveloperList();

    @Transactional
    @Modifying
    @Query("DELETE FROM User e WHERE e.userId = :userId")
    void deleteByUserId(String userId);

    @Transactional
    @Modifying
    @Query("UPDATE User e SET password = :password WHERE e.userId = :userId")
    void updatePasswordForUserId(@Param("userId") String userId, @Param("password") String password);


}
