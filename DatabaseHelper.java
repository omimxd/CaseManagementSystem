import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import java.sql.SQLException;


public class DatabaseHelper {
        private static final String DatabaseURL = "jdbc:sqlite:case.db"; //URL of the database to be used to establish connection

        // ===== Initialising Database ===== 
        public static void createDatabaseAndTables() {
                String casesTable = "CREATE TABLE IF NOT EXISTS cases (" +
                        "case_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "case_title TEXT NOT NULL," +
                        "defendant_name TEXT," +
                        "defendant_age TEXT," +
                        "defendant_dob TEXT," +
                        "status TEXT NOT NULL," +
                        "case_type TEXT," +
                        "hearing_date TEXT," +
                        "date_created TEXT DEFAULT (datetime('now'))" +
                        ");";

                String notesTable = "CREATE TABLE IF NOT EXISTS notes (" +
                        "note_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "case_id INTEGER NOT NULL," +
                        "note_text TEXT," +
                        "date_created TEXT DEFAULT (datetime('now'))," +
                        "FOREIGN KEY(case_id) REFERENCES cases(case_id) ON DELETE CASCADE" +
                        ");";

                String summariesTable = "CREATE TABLE IF NOT EXISTS summaries (" +
                        "summary_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "case_id INTEGER NOT NULL," +
                        "summary_text TEXT," +
                        "date_created TEXT DEFAULT (datetime('now'))," +
                        "FOREIGN KEY(case_id) REFERENCES cases(case_id) ON DELETE CASCADE" +
                        ");";

                String archiveTable = "CREATE TABLE IF NOT EXISTS archives (" +
                        "case_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "case_title TEXT NOT NULL," +
                        "defendant_name TEXT," +
                        "defendant_age TEXT," +
                        "defendant_dob TEXT," +
                        "status TEXT NOT NULL," +
                        "case_type TEXT," +
                        "hearing_date TEXT," +
                        "date_created TEXT," +
                        "archived_date TEXT DEFAULT (datetime('now'))" +
                        ");";

                String archiveNotesTable = "CREATE TABLE IF NOT EXISTS archived_notes (" +
                        "note_id INTEGER PRIMARY KEY," +
                        "case_id INTEGER NOT NULL," +
                        "note_text TEXT," +
                        "date_created TEXT," +
                        "archived_date TEXT DEFAULT (datetime('now'))," +
                        "FOREIGN KEY(case_id) REFERENCES archived_cases(case_id) ON DELETE CASCADE" +
                        ");";

                String archiveSummariesTable = "CREATE TABLE IF NOT EXISTS archived_summaries (" +
                        "summary_id INTEGER PRIMARY KEY," +
                        "case_id INTEGER NOT NULL," +
                        "summary_text TEXT," +
                        "date_created TEXT," +
                        "archived_date TEXT DEFAULT (datetime('now'))," +
                        "FOREIGN KEY(case_id) REFERENCES archived_cases(case_id) ON DELETE CASCADE" +
                        ");";

                File databaseFile = new File("case.db");
                if (!databaseFile.exists()) {  
                        System.out.println("No Database Found");
                        System.out.println("Creating a New Database...");
                        try (Connection conn = DriverManager.getConnection(DatabaseURL);
                                Statement stmt = conn.createStatement()) {   
                        
                                stmt.execute(casesTable);
                                stmt.execute(notesTable);
                                stmt.execute(summariesTable);

                                stmt.execute(archiveTable);
                                stmt.execute(archiveNotesTable);
                                stmt.execute(archiveSummariesTable);

                                // System.out.println("Database created successfully.");
                        
                        } catch (SQLException e) {
                                System.err.println(e.getMessage());
                        }

                } 
                else {
                        System.out.println("Database Already Exists");
                }

                

        }

        // 1. ===== Insert Cases Method =====
        public static void insertCase(Case c) {

                String sql = "INSERT INTO cases (case_title, defendant_name, defendant_age, defendant_dob, status, case_type, hearing_date) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?)";

                try (Connection conn = DriverManager.getConnection(DatabaseURL);
                        PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                        // SQL parameters (?,?,?,?,?,?,?)
                        pstmt.setString(1, c.getCaseTitle());
                        pstmt.setString(2, c.getDefendantName());
                        pstmt.setString(3, c.getDefendantAge());
                        pstmt.setString(4, c.getDefendantDOB());
                        pstmt.setString(5, c.getStatus());
                        pstmt.setString(6, c.getCaseType());
                        pstmt.setString(7, c.getHearingDate());

                        // Execute INSERT
                        pstmt.executeUpdate();
                        System.out.println("Case Added!");
                        

                } catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error saving case: " + e.getMessage());
                }

        }


        // 2. ===== Get All Cases Method =====     
        public static ArrayList<Case> getAllCases() {
                ArrayList<Case> casesList = new ArrayList<>();

                String queryString = "SELECT * FROM cases WHERE status=? ORDER BY case_id ASC";
                String status = "Ongoing";

                try (Connection conn = DriverManager.getConnection(DatabaseURL);
                        PreparedStatement stmt = conn.prepareStatement(queryString)) {

                        stmt.setString(1, status);
                        ResultSet rs = stmt.executeQuery();

                        while (rs.next()) {
                                Case c = new Case(
                                        rs.getInt("case_id"),
                                        rs.getString("case_title"),
                                        rs.getString("defendant_name"),
                                        rs.getString("defendant_age"),
                                        rs.getString("defendant_dob"),
                                        rs.getString("status"),
                                        rs.getString("case_type"),
                                        rs.getString("hearing_date"),
                                        rs.getString("date_created")
                                );
                        casesList.add(c);
                        }

                } catch (Exception e) {
                        e.printStackTrace();
                }

                // try (Connection conn = DriverManager.getConnection(DatabaseURL);
                //         Statement stmt = conn.createStatement();
                //         ResultSet rs = stmt.executeQuery(queryString)) {

                //         while (rs.next()) {
                //                 Case c = new Case(
                //                         rs.getInt("case_id"),
                //                         rs.getString("case_title"),
                //                         rs.getString("defendant_name"),
                //                         rs.getString("defendant_age"),
                //                         rs.getString("defendant_dob"),
                //                         rs.getString("status"),
                //                         rs.getString("case_type"),
                //                         rs.getString("hearing_date"),
                //                         rs.getString("date_created")
                //                 );
                //                 casesList.add(c);
                //         }

                // } catch (Exception e) {
                //         e.printStackTrace();
                // }

                return casesList;
        }


        // 3. ===== Search Cases Method =====
        public static ArrayList<Case> searchCases(String defendant, String caseType, String dob, String hearingDate) {
                ArrayList<Case> cases = new ArrayList<>();

                String sql = "SELECT * FROM cases WHERE " +
                                "(? = '' OR defendant_name LIKE ?) AND " +
                                "(? = 'All' OR case_type = ?) AND " +
                                "(? = '' OR defendant_dob = ?) AND " +
                                "(? = '' OR hearing_date = ?)";

                try (Connection conn = DriverManager.getConnection(DatabaseURL);
                        PreparedStatement stmt = conn.prepareStatement(sql)) {

                        // SQL parameters
                        stmt.setString(1, defendant);                   // for when feild is empty
                        stmt.setString(2, "%" + defendant + "%");      // partial match for person eg. finding all names with ti, mo, abb as the first set if letters
                        stmt.setString(3, caseType);                   // for "All"
                        stmt.setString(4, caseType);                // if not "All", For "Criminal", "Civil", "Family", "Juvenile"
                        stmt.setString(5, dob);                        // for when feild is empty
                        stmt.setString(6, dob);                     // exact match if not empty
                        stmt.setString(7, hearingDate);                 // for when feild is empty
                        stmt.setString(8, hearingDate);            // exact match if not empty

                        ResultSet rs = stmt.executeQuery();

                        while (rs.next()) {
                        Case c = new Case(
                                rs.getInt("case_id"),
                                rs.getString("case_title"),
                                rs.getString("defendant_name"),
                                rs.getString("defendant_age"),
                                rs.getString("defendant_dob"),
                                rs.getString("status"),
                                rs.getString("case_type"),
                                rs.getString("hearing_date"),
                                rs.getString("date_created")
                        );
                        cases.add(c);
                        }

                } catch (Exception e) {
                        e.printStackTrace();
                }

                return cases;
        }


        // 4. ===== Get Case By ID =====
        public static Case getCaseById(int caseId) {
                // Getting and creating a case object 
                Case c = null;
                String sql = "SELECT * FROM cases WHERE case_id = ?";

                try (Connection conn = DriverManager.getConnection(DatabaseURL);
                        PreparedStatement stmt = conn.prepareStatement(sql)) {

                        stmt.setInt(1, caseId);
                        ResultSet rs = stmt.executeQuery();

                        if (rs.next()) {
                        c = new Case(
                                rs.getInt("case_id"),
                                rs.getString("case_title"),
                                rs.getString("defendant_name"),
                                rs.getString("defendant_age"),
                                rs.getString("defendant_dob"),
                                rs.getString("status"),
                                rs.getString("case_type"),
                                rs.getString("hearing_date"),
                                rs.getString("date_created")
                        );
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }

                return c;
        }


        // 5. =====  =====
        public static void updateCase(Case c) {
                String sql = "UPDATE cases SET case_title=?, defendant_name=?, defendant_age=?, defendant_dob=?, status=?, " +
                                "case_type=?, hearing_date=? WHERE case_id=?";

                try (Connection conn = DriverManager.getConnection(DatabaseURL);
                        PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {


                        // SQL parameters (?,?,?,?,?,?,?,?)
                        pstmt.setString(1, c.getCaseTitle());
                        pstmt.setString(2, c.getDefendantName());
                        pstmt.setString(3, c.getDefendantAge());
                        pstmt.setString(4, c.getDefendantDOB());
                        pstmt.setString(5, c.getStatus());
                        pstmt.setString(6, c.getCaseType());
                        pstmt.setString(7, c.getHearingDate());
                        pstmt.setInt(8, c.getCaseId());

                        pstmt.executeUpdate();
                        System.out.println("Case Added!");

                }  catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error Updating case: " + e.getMessage());
                }
        }
         
        public static ArrayList<Case> getArchives() {
                ArrayList<Case> casesList = new ArrayList<>();

                String queryString = "SELECT * FROM archives ORDER BY case_id ASC";

                try (Connection conn = DriverManager.getConnection(DatabaseURL);
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery(queryString)) {

                        while (rs.next()) {
                                Case c = new Case(
                                        rs.getInt("case_id"),
                                        rs.getString("case_title"),
                                        rs.getString("defendant_name"),
                                        rs.getString("defendant_age"),
                                        rs.getString("defendant_dob"),
                                        rs.getString("status"),
                                        rs.getString("case_type"),
                                        rs.getString("hearing_date"),
                                        rs.getString("date_created")
                                );
                                casesList.add(c);
                        }

                } catch (Exception e) {
                        e.printStackTrace();
                }

                return casesList;
        }

        public static void insertCaseIntoArchives(Case c) {

                String sql = "INSERT INTO archives (case_id, case_title, defendant_name, defendant_age, defendant_dob, status, case_type, hearing_date) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

                try (Connection conn = DriverManager.getConnection(DatabaseURL);
                        PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                        
                        

                        // SQL parameters (?,?,?,?,?,?,?,?)
                        pstmt.setInt(1,c.getCaseId());
                        pstmt.setString(2, c.getCaseTitle());
                        pstmt.setString(3, c.getDefendantName());
                        pstmt.setString(4, c.getDefendantAge());
                        pstmt.setString(5, c.getDefendantDOB());
                        pstmt.setString(6, c.getStatus());
                        pstmt.setString(7, c.getCaseType());
                        pstmt.setString(8, c.getHearingDate());

                        // Execute INSERT
                        pstmt.executeUpdate();
                        System.out.println("Case Removed (Check Archive)!");
                        

                } catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error saving case: " + e.getMessage());
                }

        }
        
        public static void removeFromCases(Case c) {

                String sql = "DELETE FROM cases WHERE case_id=?";

                try (Connection conn = DriverManager.getConnection(DatabaseURL);
                        PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                        // SQL parameters (?,?,?,?,?,?,?,?)
                        pstmt.setInt(1,c.getCaseId());
                        
                        // Execute INSERT
                        pstmt.executeUpdate();
                        System.out.println("Case Added!");
                        

                } catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error saving case: " + e.getMessage());
                }

        }

        public static void removeFromArchives(Case c) {

                String sql = "DELETE FROM archives WHERE case_id=?";

                try (Connection conn = DriverManager.getConnection(DatabaseURL);
                        PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                        // SQL parameters (?,?,?,?,?,?,?,?)
                        pstmt.setInt(1,c.getCaseId());
                        
                        // Execute INSERT
                        pstmt.executeUpdate();
                        System.out.println("Case Added!");
                        

                } catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error saving case: " + e.getMessage());
                }

        }
        

        public static ArrayList<Summary> getSummariesForCase(int caseId) {
                ArrayList<Summary> list = new ArrayList<>();

                String sql = "SELECT * FROM summaries WHERE case_id = ? ORDER BY date_created ASC";

                try (Connection conn = DriverManager.getConnection(DatabaseURL);
                        PreparedStatement pstmt = conn.prepareStatement(sql)) {

                        pstmt.setInt(1, caseId);

                        ResultSet rs = pstmt.executeQuery();

                        while (rs.next()) {
                        list.add(new Summary(
                                rs.getInt("summary_id"),
                                rs.getInt("case_id"),
                                rs.getString("summary_text"),
                                rs.getString("date_created")
                        ));
                        }

                } catch (Exception e) {
                        e.printStackTrace();
                }
                return list;
        }

        public static void insertSummary(int caseId, String text) {
                if (text == null || text.trim().isEmpty()) {
                        System.out.println("Attempted to insert empty summary — ignored.");
                        return;
                }

                String sql = "INSERT INTO summaries (case_id, summary_text) VALUES (?, ?)";

                try (Connection conn = DriverManager.getConnection(DatabaseURL);
                        PreparedStatement pstmt = conn.prepareStatement(sql)) {

                        pstmt.setInt(1, caseId);
                        pstmt.setString(2, text);

                        pstmt.executeUpdate();

                } catch (Exception e) {
                        e.printStackTrace();
                }
        }


        public static void resolveCase(int caseId, String text) {

                String status = "Completed";

                if (text == null || text.trim().isEmpty()) {
                        System.out.println("Attempted to insert empty summary — ignored.");
                        return;
                }

                String sql = "INSERT INTO summaries (case_id, summary_text) VALUES (?, ?)";

                try (Connection conn = DriverManager.getConnection(DatabaseURL);
                        PreparedStatement pstmt = conn.prepareStatement(sql)) {

                        pstmt.setInt(1, caseId);
                        pstmt.setString(2, text);


                        pstmt.executeUpdate();

                } catch (Exception e) {
                        e.printStackTrace();
                }

                String status_sql = "UPDATE cases SET status=? WHERE case_id=?";

                try (Connection conn = DriverManager.getConnection(DatabaseURL);
                        PreparedStatement pstmt = conn.prepareStatement(status_sql)) {

                        pstmt.setString(1, status);
                        pstmt.setInt(2, caseId);

                        pstmt.executeUpdate();

                } catch (Exception e) {
                        e.printStackTrace();
                }

        }

        


        
}