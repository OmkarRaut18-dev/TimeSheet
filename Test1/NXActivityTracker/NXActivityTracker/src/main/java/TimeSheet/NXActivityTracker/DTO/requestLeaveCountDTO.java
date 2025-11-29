package TimeSheet.NXActivityTracker.DTO;

import java.util.List;

public class requestLeaveCountDTO
{
    private List<entryDatesLeaveCountDTO> dtoList;
    private int countOfLeaves;

    public requestLeaveCountDTO() {
    }

    public requestLeaveCountDTO(List<entryDatesLeaveCountDTO> dtoList, int countOfLeaves) {
        this.dtoList = dtoList;
        this.countOfLeaves = countOfLeaves;
    }

    public List<entryDatesLeaveCountDTO> getDtoList() {
        return dtoList;
    }

    public void setDtoList(List<entryDatesLeaveCountDTO> dtoList) {
        this.dtoList = dtoList;
    }

    public int getCountOfLeaves() {
        return countOfLeaves;
    }

    public void setCountOfLeaves(int countOfLeaves) {
        this.countOfLeaves = countOfLeaves;
    }
}
