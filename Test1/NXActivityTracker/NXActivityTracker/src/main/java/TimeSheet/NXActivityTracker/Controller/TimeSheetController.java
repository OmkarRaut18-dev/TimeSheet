package TimeSheet.NXActivityTracker.Controller;

import TimeSheet.NXActivityTracker.DTO.*;
import TimeSheet.NXActivityTracker.Model.User;
import TimeSheet.NXActivityTracker.Service.TimeSheetSerivce;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
//import io.swagger.v3.oas.annotations.Operation;

import java.io.IOException;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
//@RequestMapping("/api")
public class TimeSheetController
{
    @Autowired
    TimeSheetSerivce timeSheetSerivce;

    private static final Logger logger = LoggerFactory.getLogger(TimeSheetController.class);
    @Operation(summary = "Get all products", description = "Fetch all products from the database")
    @GetMapping("/greeting")
    public String greeting(){
        return "Hello application working";
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUsers(@RequestBody User user){
        User savedUser = timeSheetSerivce.storeUserCredentials(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PostMapping("/userlogin")
    public ResponseEntity<LoginResponseDTO> loginVerify(@RequestBody LoginRequestDTO loginRequest){
        logger.info("Login attempt for userId: {}", loginRequest.getUserId());
        try {
            User user = timeSheetSerivce.VerifyUser(loginRequest.getUserId(), loginRequest.getPassword());
            UUID projectId = UUID.randomUUID();
            logger.info("Login successful for userId: {}", loginRequest.getUserId());
            LoginResponseDTO responseDTO = new LoginResponseDTO(user.getId(),user.getUserId(),user.getRole(),user.getRnNumber(),projectId,user.getFirstName(),user.getLastName(),user.getEmail(),user.getDesignation(),user.getCreatedAt());
            return ResponseEntity.ok(responseDTO);
        } catch (IllegalArgumentException e) {
            logger.warn("Login failed for userId: {}", loginRequest.getUserId());
            return ResponseEntity.status(401).body(null);
        }
    }

    @GetMapping("/fetchDevelopersDetails")
    public ResponseEntity<List<UserListForAdminDTO>> getListOfDevelopers(){
        logger.info("Attempt to fetch list of developer: {}");
        try{
            List<UserListForAdminDTO> userListResponseDTO = timeSheetSerivce.fetchDetails();
            logger.info("Successfully fetched the list of developer: {}",userListResponseDTO);

            if (userListResponseDTO.isEmpty()) {
                logger.info("Empty developer list, no developer present: {}");
                return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
            }
            return new ResponseEntity<>(userListResponseDTO, HttpStatus.OK);
        }
        catch(IllegalArgumentException e){
            logger.warn("Failed to fetch the list of developers from database table User");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/createNewUser")
    public ResponseEntity<CreateUserDTO> createUser(@RequestBody CreateUserDTO createUserDTO){
        logger.info("Attempt to create and add new user to the application");
        try{
           CreateUserDTO savedUser = timeSheetSerivce.addUser(createUserDTO);
            logger.info("New user added to the application :",savedUser);
           return ResponseEntity.ok(savedUser);
        }
        catch(IllegalArgumentException e){
            logger.warn("Failed to add new user to the application", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/removeUser")
    public ResponseEntity<String> removeUser(@RequestBody String userId){
        logger.info("Attempt to remove the user with userId", userId);
        try{
            String messageResponse = timeSheetSerivce.removeUserService(userId);
            if ("User ID not present in database".equals(messageResponse)) {
                logger.info("No user present with userId : "+userId+" to remove from database");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageResponse);
            }
            logger.info("User with userId : "+userId+" successfully removed");
            return ResponseEntity.ok(messageResponse);
        }
        catch(IllegalArgumentException e){
            logger.warn("Failed to remove user with userId : "+userId);
            return ResponseEntity.badRequest().body("Invalid User ID");
        }
    }

    @PostMapping("/saveActivity")
    public ResponseEntity<Map<String, String>> saveProjectActivity(@RequestBody TimeSheetActivityRequestDTO activityRequestDTO){
        logger.info("Attempt to save project activity entries to the database", activityRequestDTO);
        try{
            String response = timeSheetSerivce.uploadProjectActivity(activityRequestDTO);
            if("Activity saved".equals(response)){
                logger.info("Project activity successfully saved");
                return ResponseEntity.ok(Map.of("message",response));
            }
            else{
                logger.info("Failed to save project activity to the database");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message",response));
            }
        }
        catch(IllegalArgumentException e) {
            logger.warn("Failed to save project activity to the database ", e);
            return ResponseEntity.badRequest().body(Map.of("message","Activity not saved"));
        }
    }

    @GetMapping("/getUsedDates")
    public ResponseEntity<List<getEntryDatesWithStatusDTO>> getUsedDates(@RequestParam String userId) {
        logger.info("Attempt to get the used dates (Dates for which already entries have been filled) for userId : {}", userId);
        try{
            List<getEntryDatesWithStatusDTO> usedDates = timeSheetSerivce.findUsedDates(userId);
            logger.info("Used dates", usedDates);
            return ResponseEntity.ok(usedDates);
        }
        catch(IllegalArgumentException e) {
            logger.warn("Failed to fetch the used dates for userId : {}", userId);
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/getStatus")
    public ResponseEntity<StatusCountDTO> fetchStatus(@RequestBody StatusRequestDTO statusRequestDTO){
        logger.info("Attempt to fetch the status count of working days, leaves and holidays  for requested userId : {} Year : {} Month : {}", statusRequestDTO.getUserId(), statusRequestDTO.getYear(), statusRequestDTO
                .getMonth());
        try{
         StatusCountDTO statusCountDTO =  timeSheetSerivce.getStatus(statusRequestDTO.getYear(),statusRequestDTO.getMonth(),statusRequestDTO.getUserId());
            logger.info("Successfully fetched the status count of working days :{}  leaves : {} and holidays : {} ", statusCountDTO.getWorkingDays(),statusCountDTO.getLeaveDays(),statusCountDTO.getHolidays());
         return ResponseEntity.ok(statusCountDTO);
        }
        catch(IllegalArgumentException e){
            logger.warn("Failed to fetch the status count for for requested userId : {} Year : {} Month : {}", statusRequestDTO.getUserId(), statusRequestDTO.getYear(), statusRequestDTO.getMonth());
         return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/reviewdata")
    public ResponseEntity<List<ReviewFilledDataResponseDTO>> reviewDetails(@RequestParam int year,@RequestParam int month, @RequestParam String userId){
        logger.info("Review data request received for year: {}, month: {}, userId: {}", year, month, userId);
        try{
           List<ReviewFilledDataResponseDTO> responseDTOList = timeSheetSerivce.getProjectActivityInfo(year,month,userId);
           if(responseDTOList.isEmpty()){
               logger.warn("No review data found for year: {}, month: {}, userId: {}", year, month, userId);
               return ResponseEntity.notFound().build();
           }
           else{
               logger.info("Review data retrieved successfully for year: {}, month: {}, userId: {}", year, month, userId);
               return ResponseEntity.ok(responseDTOList);
           }
        }
        catch(IllegalArgumentException e){
            logger.error("Invalid request parameters for review data - year: {}, month: {}, userId: {}", year, month, userId, e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/editActivities")
    public ResponseEntity<Map<String, String>> updateProjectActivity(@RequestBody TimeSheetActivityRequestDTO activityRequestDTO){
        logger.info("Received request to edit project activity for user: {}", activityRequestDTO.getUserId());
        try{
            String response = timeSheetSerivce.editProjectActivity(activityRequestDTO);
            if("Activity saved".equals(response)){
                logger.info("Activity successfully saved for user: {}", activityRequestDTO.getUserId());
                return ResponseEntity.ok(Map.of("message",response));
            }
            else{
                logger.warn("Activity not found or not saved. Reason: {} for user: {}", response, activityRequestDTO.getUserId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message",response));
            }
        }
        catch(IllegalArgumentException e) {
            logger.error("Bad request while editing activity for user: {}", activityRequestDTO.getUserId(), e);
            return ResponseEntity.badRequest().body(Map.of("message","Activity not saved"));
        }
    }

    @GetMapping("/updatePassword")
    public ResponseEntity<String> updateUserPassword(String userId, String password){
        logger.info("Received password update request for userId: {}", userId);
        try{
            if(!userId.isEmpty() && !password.isEmpty()){
                logger.debug("Attempting to update password for userId: {}", userId);
                String response = this.timeSheetSerivce.updatePassword(userId, password);
                if(!response.isEmpty()){
                    logger.info("Password update successful for userId: {}", userId);
                    return ResponseEntity.ok(response);
                }
                else {
                    logger.warn("Password update failed for userId: {}", userId);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Password Not Updated");
                }
            }
            else{
                logger.warn("Empty userId or password provided in update request");
                return ResponseEntity.badRequest().body("UserId and Password are empty");
            }
        }
        catch(IllegalArgumentException e){
            logger.error("IllegalArgumentException during password update for userId: {}", userId, e);
            return ResponseEntity.badRequest().body("Password Not Updated"+ e);
        }
    }

    @GetMapping("/reportGeneration")
    public ResponseEntity<Resource> generateReportForAllUser(@RequestParam int year, @RequestParam int month, @RequestParam String userId){
        logger.info("Report generation request received for userId: {}, year: {}, month: {}", userId, year, month);
        try{
        List<reportGenerationDTO> listOfReportDetails = timeSheetSerivce.generateExcel(year,month,userId);
        logger.debug("Fetched {} report records for userId: {}", listOfReportDetails.size(), userId);
        ByteArrayResource resource = timeSheetSerivce.generateExcelReport(listOfReportDetails);

        YearMonth yearMonth = YearMonth.of(year, month);
        String monthName = yearMonth.format(DateTimeFormatter.ofPattern("MMMM"));  // e.g., April
        String formattedFilename = String.format("NXActivityTracker-FY-%d-%s.xlsx", year, monthName);
            logger.info("Report generated successfully for userId: {}. Filename: {}", userId, formattedFilename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + formattedFilename)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(resource);
    }
    catch (IOException e) {
        logger.error("Error generating report for userId: {}, year: {}, month: {}", userId, year, month, e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/GetEmailId")
    public ResponseEntity<List<String>> getEmialIds (){
        try{
            List<String> mailIDs = timeSheetSerivce.pendingEntriesMailId();

        }
        catch(IllegalArgumentException e){

        }
        return null;
    }

//    @GetMapping("/countOfLeaves")
//    public ResponseEntity<entryDatesLeaveCountDTO> getCountOfLeaves(@RequestParam int year,@RequestParam int month, @RequestParam String userId){
//        logger.info("Received request to get leave count for userId: {}, year: {}, month: {}", userId, year, month);
//
//        if (month < 1 || month > 12 || year < 1900) {
//            logger.warn("Invalid input parameters: year = {}, month = {}", year, month);
//            return ResponseEntity.badRequest().build();
//        }
//
//        try {
//            entryDatesLeaveCountDTO obj = timeSheetSerivce.fetchCountOfLeavesAndDates(year, month, userId);
//            logger.info("Successfully retrieved leave count and entry dates for userId: {}", userId);
//            return ResponseEntity.ok(obj);
//        } catch (IllegalArgumentException e) {
//            logger.error("Error while fetching leave data for userId: {} - {}", userId, e.getMessage());
//            return ResponseEntity.badRequest().build();
//        }
//    }


}
