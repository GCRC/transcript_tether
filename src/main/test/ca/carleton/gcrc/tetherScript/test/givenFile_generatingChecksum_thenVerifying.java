package ca.carleton.gcrc.tetherScript.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

import org.junit.Test;

public class givenFile_generatingChecksum_thenVerifying {

	@Test
	public void test() throws NoSuchAlgorithmException, IOException, URISyntaxException {
	    
	   
	         
	    MessageDigest md = MessageDigest.getInstance("MD5");
	    md.update(Files.readAllBytes(Paths.get( getClass().getResource("/testhash.mp4").toURI()) ));
	    byte[] digest = md.digest();
	    String myChecksum = DatatypeConverter
	      .printHexBinary(digest).toUpperCase();
	    System.out.println(myChecksum);
	    assert(!(myChecksum).isEmpty());
	}

}
