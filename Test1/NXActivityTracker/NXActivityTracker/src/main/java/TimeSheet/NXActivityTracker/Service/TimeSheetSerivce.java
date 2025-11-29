package TimeSheet.NXActivityTracker.Service;

import TimeSheet.NXActivityTracker.DTO.*;
import TimeSheet.NXActivityTracker.Model.TimeSheetEntry;
import TimeSheet.NXActivityTracker.Model.User;
import TimeSheet.NXActivityTracker.Repo.TimeSheetEntryRepo;
import TimeSheet.NXActivityTracker.Repo.TimeSheetRepo;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class TimeSheetSerivce {

    @Autowired
    public TimeSheetRepo timeSheetRepo;

    @Autowired
    public TimeSheetEntryRepo sheetEntryRepo;

    @Scheduled(cron = "0 0 1 1 * ?")
    public void deleteOldReports() {
        YearMonth cutoff = YearMonth.now().minusMonths(4);
        int cutoffYear = cutoff.getYear();
        int cutoffMonth = cutoff.getMonthValue();
        sheetEntryRepo.deleteOlderThan(cutoffYear, cutoffMonth);
    }

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    public User storeUserCredentials(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return  timeSheetRepo.save(user);
    }

    public User VerifyUser(String userId, String password) {

        User user = timeSheetRepo.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid userId or password"));
        if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
            return user;
        } else {
            throw new IllegalArgumentException("Invalid userId or password");
        }
    }

    public List<UserListForAdminDTO> fetchDetails() {
        List<UserListForAdminDTO> userListForAdminDTOList = new ArrayList<>();
        LocalDate today = LocalDate.now();
        int currentYear = today.getYear();
        int currentMonth = today.getMonthValue();
        //Give Total Number of working days (Week Days) in current month.
        int totalWeekDaysInMonth = getWeekdaysInCurrentMonth();

        List<UserListResponseDTO> userListResponseDTOs = timeSheetRepo.getDeveloperList();

        for (UserListResponseDTO user : userListResponseDTOs) {
            int totalEntries = sheetEntryRepo.getEntriesByUser(currentYear, currentMonth, user.getUserId());

            String status;
            if (totalEntries == totalWeekDaysInMonth) {
                status = "No Pending Entries \uD83D\uDFE2";
            } else {
                int pending = totalWeekDaysInMonth - totalEntries;
                status = "Pending entries for " + pending + (pending == 1 ? " day \uD83D\uDD34" : " days \uD83D\uDD34");
            }

            List<LocalDate> entries = sheetEntryRepo.datesOfLeave(currentYear,currentMonth,user.getUserId());
            entryDatesLeaveCountDTO leaveCountDTO = new entryDatesLeaveCountDTO(entries,entries.size());

            UserListForAdminDTO dto = new UserListForAdminDTO();
            dto.setUserId(user.getUserId());
            dto.setEmail(user.getEmail());
            dto.setRole(user.getRole());
            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastName());
            dto.setCreatedAt(user.getCreatedAt());
            dto.setStatusOfEntries(status);
            dto.setLeaveCount(leaveCountDTO);

            userListForAdminDTOList.add(dto);
        }

        return userListForAdminDTOList;
    }


    public static int getWeekdaysInCurrentMonth() {
        YearMonth currentMonth = YearMonth.now();
        LocalDate start = currentMonth.atDay(1);
        LocalDate end = currentMonth.atEndOfMonth();

        int weekdayCount = 0;
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            DayOfWeek day = date.getDayOfWeek();
            if (day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY) {
                weekdayCount++;
            }
        }

        return weekdayCount;
    }

    public CreateUserDTO addUser(CreateUserDTO createUserDTO){
        Optional<User> userList = timeSheetRepo.findByUserId(createUserDTO.getUserId());
        if(userList.isPresent()){
            throw  new IllegalArgumentException("User with ID " + createUserDTO.getUserId() + " already exists.");
        }
        else{
            // Create a new User entity
            User user = new User();
            user.setUserId(createUserDTO.getUserId());
            user.setRnNumber(createUserDTO.getRnNumber());
            user.setFirstName(createUserDTO.getFirstName());
            user.setLastName(createUserDTO.getLastName());
            user.setEmail(createUserDTO.getEmail());
            user.setRole(createUserDTO.getRole());
            user.setPassword(createUserDTO.getPassword());
            user.setDesignation(createUserDTO.getDesignation());
            user.setCreatedAt(LocalDateTime.now()); // set createdAt manually

            // Encode the password before saving
//            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//            String encodedPassword = encoder.encode(createUserDTO.getPassword());
            user.setPassword(bCryptPasswordEncoder.encode(createUserDTO.getPassword()));

            // Save the user
            User savedUser = timeSheetRepo.save(user);
            return new CreateUserDTO(
                    savedUser.getId(),
                    savedUser.getUserId(),
                    savedUser.getFirstName(),
                    savedUser.getLastName(),
                    savedUser.getEmail(),
                    savedUser.getRnNumber(),
                    savedUser.getRole(),
                    savedUser.getCreatedAt(),
                    savedUser.getPassword(),
                    savedUser.getDesignation()
            );
        }

    }

    public String removeUserService(String userId) {
        Optional<User> user = timeSheetRepo.findByUserId(userId);
        if(user.isPresent()){
            timeSheetRepo.deleteByUserId(userId);
            return "User Id successfully deleted";
        }
        else{
            return "User ID not present in database";
        }
    }

    public String uploadProjectActivity(TimeSheetActivityRequestDTO activityRequestDTO)
    {
        for(String date : activityRequestDTO.getDates()){
            TimeSheetEntry entry = new TimeSheetEntry();
            entry.setDepartment(activityRequestDTO.getDepartment());
            entry.setBowp(activityRequestDTO.getBowpNumber());
            entry.setFteCostTransferFrom(activityRequestDTO.getFteCostTransferFrom());
            entry.setFteCostTransferTo(activityRequestDTO.getFteCostTransferTo());
            entry.setProjectActivity(activityRequestDTO.getProjectActivity());
            entry.setStatus(activityRequestDTO.getStatus());
            entry.setEntryDate(LocalDate.parse(date));
            entry.setUserId(activityRequestDTO.getUserId());
            sheetEntryRepo.save(entry);
        }
        return "Activity saved";
    }

    public List<getEntryDatesWithStatusDTO> findUsedDates(String userId) {
        List<getEntryDatesWithStatusDTO> savedDates = sheetEntryRepo.getUsedDatesByUserid(userId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return savedDates;
    }

    public StatusCountDTO getStatus(int year, int month, String userId){
        int workingDaysCounts =0;
        int leaveCounts =0;
        int holidayCounts =0;

        List<StatusEntryDateDTO> statusAndEntryDates = sheetEntryRepo.getStatusAndEntryDatesByUserId(userId);
        for(StatusEntryDateDTO obj : statusAndEntryDates){
            LocalDate date = obj.getEntryDate();
            if(date.getMonthValue()==(month) && date.getYear() == year){
                String status = obj.getStatus().toUpperCase();
                if(status.equals("WORKING")){
                    workingDaysCounts++;
                }
                else if(status.equals("LEAVE")){
                    leaveCounts++;
                }
                else if(status.equals("HOLIDAY")){
                    holidayCounts++;
                }
            }
        }
        return new StatusCountDTO(workingDaysCounts,leaveCounts,holidayCounts);
    }

    public List<ReviewFilledDataResponseDTO> getProjectActivityInfo(int year, int month, String userId) {
        List<ReviewFilledDataResponseDTO> responseDTOS = sheetEntryRepo.fetchProjectActivityDetails(userId);
        List<ReviewFilledDataResponseDTO> reviewDataForSpecificMonth = new ArrayList<>();

        for(ReviewFilledDataResponseDTO obj : responseDTOS){
               LocalDate date = obj.getDate();
               if(date.getMonthValue() == month && date.getYear()==year){
                   reviewDataForSpecificMonth.add(obj);
               }
        }
        reviewDataForSpecificMonth.sort(Comparator.comparing(ReviewFilledDataResponseDTO::getDate));
        return reviewDataForSpecificMonth;
    }

    public String editProjectActivity(TimeSheetActivityRequestDTO activityRequestDTO) {
        String userId = activityRequestDTO.getUserId();
        List<String> dates = activityRequestDTO.getDates();
        if (dates == null || dates.isEmpty()) {
            throw new IllegalArgumentException("Dates list is empty or null");
        }

        LocalDate dateOfEntry = LocalDate.parse(dates.get(0));

        Optional<TimeSheetEntry> optionalEntry = sheetEntryRepo.findByUserIdAndEntryDate(userId, dateOfEntry);
        if(optionalEntry.isPresent()){
            TimeSheetEntry timeSheetEntry = optionalEntry.get();
            timeSheetEntry.setProjectActivity(activityRequestDTO.getProjectActivity());
            timeSheetEntry.setBowp(activityRequestDTO.getBowpNumber());
            timeSheetEntry.setFteCostTransferFrom(activityRequestDTO.getFteCostTransferFrom());
            timeSheetEntry.setFteCostTransferTo(activityRequestDTO.getFteCostTransferTo());
            timeSheetEntry.setStatus(activityRequestDTO.getStatus());
            timeSheetEntry.setDepartment(activityRequestDTO.getDepartment());
            sheetEntryRepo.save(timeSheetEntry);
        }
        else{
            TimeSheetEntry newEntry = new TimeSheetEntry();
            newEntry.setUserId(activityRequestDTO.getUserId());
            newEntry.setDepartment(activityRequestDTO.getDepartment());
            newEntry.setBowp(activityRequestDTO.getBowpNumber());
            newEntry.setFteCostTransferFrom(activityRequestDTO.getFteCostTransferFrom());
            newEntry.setFteCostTransferTo(activityRequestDTO.getFteCostTransferTo());
            newEntry.setEntryDate(dateOfEntry);
            newEntry.setProjectActivity(activityRequestDTO.getProjectActivity());
            newEntry.setStatus(activityRequestDTO.getStatus());
            sheetEntryRepo.save(newEntry);
        }
        return "Activity saved";
    }

    public String updatePassword(String userId, String password) {
        String encryptedPass = bCryptPasswordEncoder.encode(password);
        this.timeSheetRepo.updatePasswordForUserId(userId, encryptedPass);
        return "Password updated successfully for user: " + userId;
    }

//    public List<reportGenerationDTO> generateExcel(int year, int month, String userId) {
//        String formattedDate = String.format("%d-%02d", year, month);
//        YearMonth yearMonth = YearMonth.of(year, month);
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM-yyyy");
//        String formatted = yearMonth.format(formatter);
//        List<User> listOfUser = timeSheetRepo.findAll();
//        List<TimeSheetEntry> timeSheet = sheetEntryRepo.findByUserIdAndMonthYear(year,month,userId);
//        List<reportGenerationDTO> listOfReportDetails = new ArrayList<>();
//
//        for(User userObj : listOfUser){
//            for(TimeSheetEntry timeSheetEntry : timeSheet){
//                if(timeSheetEntry.getUserId().equals(userObj.getUserId())){
//                    reportGenerationDTO reportGenObj = new reportGenerationDTO();
//
//                    reportGenObj.setMonth(formatted);
//                    reportGenObj.setRnId(userObj.getRnNumber());
//                    reportGenObj.setNameOfEmployee(userObj.getFirstName()+" "+userObj.getLastName());
//                    reportGenObj.setDesignation(userObj.getDesignation());
//                    reportGenObj.setVerticalTo("EMO");
//                    reportGenObj.setVerticalFrom("IB1");
//                    reportGenObj.setSubmittedBy("Hariharan Dhanasekaran");
////            reportGenObj.setSubmittedOn();
//                    reportGenObj.setRemarks(" ");
//                    reportGenObj.setFteTransferFromCostCode(timeSheetEntry.getFteCostTransferFrom());
//                    reportGenObj.setFteTransferToCostCode(timeSheetEntry.getFteCostTransferTo());
//                    reportGenObj.setBowp(timeSheetEntry.getBowp());
//                    int workingDays = sheetEntryRepo.getTotalWorkingDays(year,month,userId);
//                    reportGenObj.setNumberOfWorkingDays(String.valueOf(workingDays));
//                    int workingHours = workingDays * 8;
//                    reportGenObj.setNumberOfWorkingHours(String.valueOf(workingHours));
//
//                    if(!listOfReportDetails.contains(reportGenObj)){
//                        listOfReportDetails.add(reportGenObj);
//                    }
//                }
//            }

    public List<reportGenerationDTO> generateExcel(int year, int month, String userId1) {
        YearMonth yearMonth = YearMonth.of(year, month);
        String formatted = yearMonth.format(DateTimeFormatter.ofPattern("MMMM-yyyy"));

        List<User> listOfUser = timeSheetRepo.findAll();
        Set<reportGenerationDTO> reportDetailsSet = new HashSet<>();

        for (User userObj : listOfUser) {
            String userId = userObj.getUserId();
            List<TimeSheetEntry> timeSheet = sheetEntryRepo.findByUserIdAndMonthYear(year, month, userId);

            if (timeSheet.isEmpty()) {
                continue; // Skip users with no entries
            }

//            int workingDays = sheetEntryRepo.getTotalWorkingDays(year, month, userId);
//            int workingHours = workingDays * 8;

            for (TimeSheetEntry timeSheetEntry : timeSheet) {
                reportGenerationDTO reportGenObj = new reportGenerationDTO();

                int workingDays = sheetEntryRepo.getTotalWorkingDays(year, month, userId,timeSheetEntry.getDepartment());
                int workingHours = workingDays * 8;

                reportGenObj.setMonth(formatted);
                reportGenObj.setRnId(userObj.getRnNumber());
                reportGenObj.setNameOfEmployee(userObj.getFirstName() + " " + userObj.getLastName());
                reportGenObj.setDesignation(userObj.getDesignation());
                reportGenObj.setVerticalTo("EMO");
                reportGenObj.setVerticalFrom("IB1");
                reportGenObj.setSubmittedBy("Hariharan Dhanasekaran");
//                reportGenObj.setRemarks(" ");
                reportGenObj.setFteTransferFromCostCode(timeSheetEntry.getFteCostTransferFrom());
                reportGenObj.setFteTransferToCostCode(timeSheetEntry.getFteCostTransferTo());
                reportGenObj.setBowp(timeSheetEntry.getBowp());
                reportGenObj.setNumberOfWorkingDays(String.valueOf(workingDays));
                reportGenObj.setNumberOfWorkingHours(String.valueOf(workingHours));

                reportDetailsSet.add(reportGenObj);


            }
        }

        return  new ArrayList<>(reportDetailsSet);
    }


    public ByteArrayResource generateExcelReport(List<reportGenerationDTO> listOfReportDetails) throws IOException {
        try(Workbook workbook = new XSSFWorkbook();
            ByteArrayOutputStream out = new ByteArrayOutputStream()){
            Sheet sheet = workbook.createSheet("Report");

            String[] headers ={
                    "Month", "RN ID", "Employee Name", "Designation",
                    "FTE transfer From (Cost center code)", "FTE transfer To (Cost center code)", "No of Working Days",
                    "Vertical From", "Vertical To", "Submitted By","Submitted on","Remarks/Justification","Working Hours", "BOWP"
            };

//            int[] columnWidths = {5000, 6000, 7000, 4000}; // Adjust as needed
            for (int i = 0; i < 14; i++) {
                sheet.setColumnWidth(i, 3200);
            }

            // Create styles
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setWrapText(true);
            setBorders(headerStyle);

            CellStyle wrapStyle = workbook.createCellStyle();
            wrapStyle.setWrapText(true);
            setBorders(wrapStyle);

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
//                cell.setCellStyle(wrapStyle);
            }

            int rowIdx =1;
            for(reportGenerationDTO dto : listOfReportDetails){
                Row row = sheet.createRow(rowIdx++);
                String[] values = {
                        dto.getMonth(), dto.getRnId(), dto.getNameOfEmployee(), dto.getDesignation(),
                        dto.getFteTransferFromCostCode(), dto.getFteTransferToCostCode(), dto.getNumberOfWorkingDays(),
                        dto.getVerticalFrom(), dto.getVerticalTo(), dto.getSubmittedBy(),   dto.getSubmittedOn() != null ? dto.getSubmittedOn().toString() : "",
                        dto.getRemarks(), dto.getNumberOfWorkingHours(), dto.getBowp()
                };
                for (int i = 0; i < values.length; i++) {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(values[i] != null ? values[i] : "");
                    cell.setCellStyle(wrapStyle);
                }
            }
            workbook.write(out);
            return new ByteArrayResource(out.toByteArray());
        }
    }

    private void setBorders(CellStyle style) {
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
    }

    public List<String> pendingEntriesMailId() {
        return null;
    }

//    public entryDatesLeaveCountDTO fetchCountOfLeavesAndDates(int year, int month, String userId) {
//      int entryDates = sheetEntryRepo.leaveCount(year, month, userId);
//      List<LocalDate> entries = sheetEntryRepo.datesOfLeave(year,month,userId);
//      entryDatesLeaveCountDTO leaveCountDTO = new entryDatesLeaveCountDTO(entries,entryDates);
//        return  leaveCountDTO;
//    }
}
