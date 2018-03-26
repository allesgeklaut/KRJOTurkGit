package service;

public class LearningApplication extends LearningInstance {

    private int approvedFlag;
    private int createdBy;

    public LearningApplication(int id, String name, String description, int approvedFlag, int createdBy) {
        super(id, name, description);
        this.approvedFlag = approvedFlag;
        this.createdBy = createdBy;
    }

    public int getApprovedFlag() {
        return approvedFlag;
    }

    public void setApprovedFlag(int approvedFlag) {
        this.approvedFlag = approvedFlag;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
