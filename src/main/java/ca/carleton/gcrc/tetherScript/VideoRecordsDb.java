package ca.carleton.gcrc.tetherScript;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.bind.DatatypeConverter;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;

public class VideoRecordsDb {
	private static final String videoRecordsDbFolder = System.getProperty("user.home") + "/.youtubeapi3credentials/";
	private static String videoRecordsDbFile ="";
	protected static VideoRecordsDb getDbInstance(){

		createNewDatabase("tetherVideoRecord.db");

		return new VideoRecordsDb();
	}

	private Connection connect() {
		String url = "jdbc:sqlite:" + videoRecordsDbFile;
		Connection conn = null;
		try{
			conn = DriverManager.getConnection(url);
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		return conn;
	}
	protected static void createNewDatabase(String filename) {
		videoRecordsDbFile = videoRecordsDbFolder + filename;
		String url = "jdbc:sqlite:" + videoRecordsDbFile;
		try (Connection conn = DriverManager.getConnection(url)){

			String sql = "CREATE TABLE IF NOT EXISTS "+ System.getProperty("user.name") +" (\n"
					+ "id integer PRIMARY KEY, \n"
					+ "	videoid text NOT NULL,\n"
					+ "	youtubeid text NOT NULL, \n"
					+ "CONSTRAINT uc_unique UNIQUE (videoid)\n"
					+ ");";
			try (Statement stmt = conn.createStatement()){
				stmt.execute(sql);
			}catch (SQLException e) {
				System.out.println(e.getMessage());
			} 


		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} 

	}

	private String calculateHash(String filename) throws NoSuchAlgorithmException, IOException  {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(Files.readAllBytes(Paths.get(filename) ));
		byte[] digest = md.digest();
		String myChecksum = DatatypeConverter
				.printHexBinary(digest).toUpperCase();
		return myChecksum;


	}
	protected boolean addNewVideoRecord (String filename, String youtubeid) {
		String localhashid;
		try {
			localhashid = calculateHash(filename);
		} catch (NoSuchAlgorithmException e1) {
			System.err.println("Adding Record Error: cannot calculate the hash");
			e1.printStackTrace();
			return false;
		} catch (IOException e1) {
			System.err.println("Adding Record Error: cannot calculate the hash");
			e1.printStackTrace();
			return false;
		}
		String sql = "INSERT INTO " + System.getProperty("user.name") +"(videoid,youtubeid) VALUES(?, ?)";
		if(dupliatedVideoExisting(filename).isEmpty() ) {
			try {
				Connection conn = this.connect();
			
				PreparedStatement pstmt  = conn.prepareStatement(sql);

				pstmt.setString(1, localhashid);
				pstmt.setString(2, youtubeid);
				
				pstmt.executeUpdate();
				return true;

			}catch(SQLException e) {
				
				System.out.println(e.getMessage());
				return false;
			}

		} else {
			return false;
		}
	}
	protected String dupliatedVideoExisting (String filename) {
		String localhashid="";
		try {
			localhashid = calculateHash(filename);
		} catch (NoSuchAlgorithmException e1) {

			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String res= "";
		if (localhashid !=null && !localhashid.isEmpty()) {
			String sql = "SELECT videoid, youtubeid "
					+ "FROM " + System.getProperty("user.name") + " WHERE videoid=?";
			try (Connection conn = this.connect();
					PreparedStatement pstmt  = conn.prepareStatement(sql)) {
				pstmt.setString(1,localhashid);

				ResultSet rs  = pstmt.executeQuery();
				if(rs.next() ) {
					res  = rs.getString(2);
				} 
			} catch (SQLException e) {
			
				System.out.println(e.getMessage());
			} 
		}
		return res;
	}
}
