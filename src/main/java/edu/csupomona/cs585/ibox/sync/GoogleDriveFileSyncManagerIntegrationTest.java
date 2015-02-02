package edu.csupomona.cs585.ibox.sync;


import static org.junit.Assert.*;


import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;


import org.junit.*;



import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;


public class GoogleDriveFileSyncManagerIntegrationTest {


Drive drive;
GoogleDriveFileSyncManager GD;

@Before
public void setUp() throws Exception {
initGoogleDriveServices();
GD = new GoogleDriveFileSyncManager(drive);
}


@After
public void tearDown() throws Exception {
	GD = null;
}


@Test
public void initGoogleDriveServices() throws IOException {
        HttpTransport httpTransport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();


        GoogleCredential credential = new  GoogleCredential.Builder()
		  .setTransport(httpTransport)
		  .setJsonFactory(jsonFactory)
		  .setServiceAccountId("893735387924-629qn7a333a6dmvo3aoppfl8nkle66nd@developer.gserviceaccount.com")
				  .build();


		drive = new Drive.Builder(httpTransport, jsonFactory, credential).setApplicationName("ibox").build();


    }

@Test
public void testAddFile() throws IOException {
java.io.File localFile = new java.io.File("/temp/test.txt");
localFile.createNewFile();
GD.addFile(localFile);
assertNotNull(GD.getFileId(localFile.getName()));
}

@Test
public void testUpdateFile() throws IOException {
java.io.File localFile = new java.io.File("/temp/test.txt");
GD.updateFile(localFile);
assertNotNull(GD.getFileId(localFile.getName()));
}

@Test
public void testDeleteFile() throws IOException {
java.io.File localFile = new java.io.File("/temp/test.txt");
GD.deleteFile(localFile);
assertNotNull(GD.getFileId(localFile.getName()));
}
}

