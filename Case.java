public class Case {

    private int caseId; 
    private String caseTitle;
    private String defendantName;
    private String defendantAge;
    private String defendantDOB;
    private String status;
    private String caseType;
    private String hearingDate;
    private String dateCreated;

    
    
    
    // Constructor 1
    public Case(String caseTitle, String defendantName, String defendantAge, 
                String defendantDOB, String status, String caseType, String hearingDate) {
        this.caseTitle = caseTitle;
        this.defendantName = defendantName;
        this.defendantAge = defendantAge;
        this.defendantDOB = defendantDOB;
        this.status = status;
        this.caseType = caseType;
        this.hearingDate = hearingDate;
    }

    // Constructor 2 (For Searched cases)
    public Case(int caseId, String caseTitle, String defendantName, String defendantAge, 
                String defendantDOB, String status, String caseType, String hearingDate, String dateCreated) {
        this.caseId = caseId;
        this.caseTitle = caseTitle;
        this.defendantName = defendantName;
        this.defendantAge = defendantAge;
        this.defendantDOB = defendantDOB;
        this.status = status;
        this.caseType = caseType;
        this.hearingDate = hearingDate;
        this.dateCreated = dateCreated;
    }

    // Getters
    public int getCaseId() { 
        return caseId; 
    }

    public String getCaseTitle() { 
        return caseTitle; 
    }
    
    public String getStatus() { 
        return status; 
    }
    
    public String getDefendantName() { 
        return defendantName; 
    }
    
    public String getDefendantDOB() { 
        return defendantDOB; 
    }

    public String getDefendantAge() {
        return defendantAge; 
        }

    public String getCaseType() { 
        return caseType; 
    }
    
    public String getHearingDate() { 
        return hearingDate; 
    }

    public String getDateCreated(){
        return dateCreated;
    }

    // Optionally, setters if you want to modify a case after creation
    public void setCaseTitle(String caseTitle) { this.caseTitle = caseTitle; }
    public void setStatus(String status) { this.status = status; }
    public void setDefendantName(String defendantName) { this.defendantName = defendantName; }
    public void setDefendantAge(String defendantAge) { this.defendantAge = defendantAge; }
    public void setDefendantDOB(String defendantDOB) { this.defendantDOB = defendantDOB; }
    public void setCaseType(String caseType) { this.caseType = caseType; }
    public void setHearingDate(String hearingDate) { this.hearingDate = hearingDate; }
}
