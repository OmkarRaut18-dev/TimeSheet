package TimeSheet.NXActivityTracker.Repo;

import TimeSheet.NXActivityTracker.DTO.ReviewFilledDataResponseDTO;
import TimeSheet.NXActivityTracker.DTO.StatusEntryDateDTO;
import TimeSheet.NXActivityTracker.DTO.entryDatesLeaveCountDTO;
import TimeSheet.NXActivityTracker.DTO.getEntryDatesWithStatusDTO;
import TimeSheet.NXActivityTracker.Model.TimeSheetEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TimeSheetEntryRepo extends JpaRepository<TimeSheetEntry,Long> {


    @Query("Select new TimeSheet.NXActivityTracker.DTO.getEntryDatesWithStatusDTO(e.entryDate, e.status) FROM TimeSheetEntry e WHERE e.userId = :userId")
    List<getEntryDatesWithStatusDTO> getUsedDatesByUserid(@Param("userId") String userId);

    @Query("Select new TimeSheet.NXActivityTracker.DTO.StatusEntryDateDTO(e.entryDate, e.status)FROM TimeSheetEntry e WHERE e.userId = :userId")
    List<StatusEntryDateDTO> getStatusAndEntryDatesByUserId(@Param("userId") String userId);

    @Query("Select new TimeSheet.NXActivityTracker.DTO.ReviewFilledDataResponseDTO(e.department,  e.bowp,e.fteCostTransferTo, e.projectActivity, e.entryDate,  e.status) FROM TimeSheetEntry e WHERE e.userId = :userId")
    List<ReviewFilledDataResponseDTO> fetchProjectActivityDetails(@Param("userId") String userId);

    Optional<TimeSheetEntry> findByUserIdAndEntryDate(String userId, LocalDate dateOfEntry);

    @Transactional
    @Modifying
    @Query("SELECT t FROM TimeSheetEntry t WHERE EXTRACT(YEAR FROM t.entryDate) = :year AND EXTRACT(MONTH FROM t.entryDate) = :month AND t.userId = :userId AND t.status = 'Working'")
    List<TimeSheetEntry> findByUserIdAndMonthYear(@Param("year") int year, @Param("month") int month, @Param("userId") String userId);

    @Transactional
    @Query("SELECT COUNT(e.status) FROM TimeSheetEntry e WHERE EXTRACT(YEAR FROM e.entryDate) = :year AND EXTRACT(MONTH FROM e.entryDate) = :month AND e.userId = :userId AND e.status = 'Working' AND e.department = :department")
    int getTotalWorkingDays(@Param("year") int year, @Param("month") int month, @Param("userId") String userId, @Param("department") String department);

    @Transactional
    @Modifying
    @Query("DELETE FROM TimeSheetEntry t WHERE EXTRACT(YEAR FROM t.entryDate) = :year AND EXTRACT(MONTH FROM t.entryDate) = :month")
    void deleteOlderThan(@Param("year") int cutoffYear, @Param("month") int cutoffMonth);

    @Transactional
    @Query("SELECT COUNT(e) FROM TimeSheetEntry e WHERE EXTRACT(YEAR FROM e.entryDate) = :year AND EXTRACT(MONTH FROM e.entryDate) = :month AND e.userId = :userId")
    int getEntriesByUser(@Param("year")int currentYear, @Param("month") int currentMonth, @Param("userId") String userId);

    @Transactional
    @Query("SELECT COUNT(e.status) FROM TimeSheetEntry e WHERE EXTRACT(YEAR FROM e.entryDate) = :year AND EXTRACT(MONTH FROM e.entryDate) = :month AND e.userId = :userId AND e.status = 'Leave'")
    int leaveCount (@Param("year")int currentYear, @Param("month") int currentMonth, @Param("userId") String userId);

    @Transactional
    @Query("SELECT e.entryDate FROM TimeSheetEntry e WHERE EXTRACT(YEAR FROM e.entryDate) = :year AND EXTRACT(MONTH FROM e.entryDate) = :month AND e.userId = :userId AND e.status = 'Leave'")
    List<LocalDate> datesOfLeave(int year, int month, String userId);
}
