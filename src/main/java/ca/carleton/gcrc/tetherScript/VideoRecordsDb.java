package ca.carleton.gcrc.tetherScript;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
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
	private static final String videoRecordsDbFile = videoRecordsDbFolder + "tether";
	protected static VideoRecordsDb initialDb(){
		if(!ApiExample.isSAME_USER()) {
			createNewDatabase("tether");
		}
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
	private static void createNewDatabase(String filename) {
		String url = "jdbc:sqlite:" + videoRecordsDbFolder + filename;
		try (Connection conn = DriverManager.getConnection(url)){
			if(conn != null) {

				String sql = "CREATE TABLE IF NOT EXISTS videorecords (\n"
						+ "id integer PRIMARY KEY, \n"
						+ "	videoid text NOT NULL,\n"
						+ "	youtubeid text NOT NULL\n"
						+ ");";
				try (Statement stmt = conn.createStatement()){
					stmt.execute(sql);
				}catch (SQLException e) {
					System.out.println(e.getMessage());
				} finally {
					conn.close();
				}
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
		String sql = "INSERT INTO videorecords(videoid,youtubeid) VALUES(?,?)";
		if(!dupliatedVideoExisting(localhashid).isEmpty() ) {
			try (Connection conn = this.connect();
					PreparedStatement pstmt  = conn.prepareStatement(sql)){

				pstmt.setString(1, localhashid);
				pstmt.setString(2, youtubeid);

				return true;

			}catch(Exception e) {

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
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String res= "";
		if (localhashid !=null && !localhashid.isEmpty()) {
			String sql = "SELECT videoid, youtubeid "
					+ "FROM videorecords WHERE videoid=?";

			try (Connection conn = this.connect();
					PreparedStatement pstmt  = conn.prepareStatement(sql)) {
				pstmt.setString(1,localhashid);

				ResultSet rs  = pstmt.executeQuery();
				if(rs.last()) {
					res  = rs.getString(3);

				} 

			} catch (SQLException e) {
				System.out.println(e.getMessage());
			} 
		}
		return res;

	}

}
