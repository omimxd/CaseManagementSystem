public class Summary {
    private int summaryID;
    private int caseId;
    private String text;
    private String dateCreated;

    public Summary(int summaryID, int caseId, String text, String dateCreated) {
        this.summaryID = summaryID;
        this.caseId = caseId;
        this.text = text;
        this.dateCreated = dateCreated;
    }

    public int getId() { 
        return summaryID;
    }
    
    public int getCaseId() { 
        return caseId; 
    }
    
    public String getText() { 
        return text; 
    }
    
    public String getDateCreated() { 
        return dateCreated; 
    }
}

