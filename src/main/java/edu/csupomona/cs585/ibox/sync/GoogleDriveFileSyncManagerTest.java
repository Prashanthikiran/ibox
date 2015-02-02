package edu.csupomona.cs585.ibox.sync;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.Drive.Files.Delete;
import com.google.api.services.drive.Drive.Files.Insert;
import com.google.api.services.drive.Drive.Files.List;
import com.google.api.services.drive.Drive.Files.Update;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import edu.csupomona.cs585.ibox.sync.GoogleDriveFileSyncManager;
import static org.junit.Assert.*;


public class GoogleDriveFileSyncManagerTest {
	
	GoogleDriveFileSyncManager GD;
	Drive drive;
	
	

	Insert insert;
	Update update;
	Delete delete;
	java.io.File localFile;
	File body;
	Files getFiles;
	List getList;
	FileList listOfFiles;
	java.util.List<File> filesList;
	
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);	
		
		drive = mock(Drive.class);
		GD = new GoogleDriveFileSyncManager(drive);
		
		localFile = mock(java.io.File.class);
		body = new File();
		listOfFiles = new FileList();
		getFiles = mock(Files.class);
		getList = mock(List.class);
		filesList = new ArrayList<File>();
		
		insert = mock(Insert.class);
		update = mock(Update.class);
		delete = mock(Delete.class);
		
	}
	
	@After
	public void after() throws Exception {
		insert = null;
		update = null;
		delete = null;
		drive = null;GD = null;localFile = null;body = null;listOfFiles = null;getFiles = null;getList = null;filesList = null;
		
	}
	

	
	@Test
	public void testAddFile() throws IOException {
	when(drive.files()).thenReturn(getFiles);
		when(getFiles.insert(isA(File.class), isA(AbstractInputStreamContent.class))).thenReturn(insert);
		when(insert.execute()).thenReturn(body);
		GD.addFile(localFile);		
		Mockito.verify(drive, atLeast(0)).files();
	    Mockito.verify(getFiles, atLeast(0)).insert(isA(File.class));
	    Mockito.verify(insert, atLeast(0)).execute();		
	}
	
	@Test(expected = Exception.class)  
	public void testExceptionUpdateFile() throws Exception{
		setUp();
		File localFile = Mockito.mock(File.class);
		when(localFile.getTitle()).thenReturn(null);
		GD.updateFile(null);
	}
	
	@Test
	public void testUpdateFile() throws IOException{
		File testFile = new File();
		testFile.setId("TestFile");
		testFile.setTitle("test.txt");
		filesList.add(testFile);
		listOfFiles.setItems(filesList);
		when(localFile.getName()).thenReturn("test.txt");
		when(drive.files()).thenReturn(getFiles);
		when(getFiles.list()).thenReturn(getList);
		when(getList.execute()).thenReturn(listOfFiles);
		String fileId = GD.getFileId("/temp/test.txt");
		when(getFiles.update(eq(fileId),isA(File.class), isA(AbstractInputStreamContent.class))).thenReturn(update);
		when(update.execute()).thenReturn(body);
		GD.updateFile(localFile);
		verify(drive, atLeast(0)).files();
	    verify(getFiles, atLeast(0)).update(Mockito.eq(fileId), Mockito.isA(File.class));
	    verify(update, atLeast(0)).execute();
	
	}
		
	@Test
	public void testDeleteFile() throws IOException {
		File testFile = new File();
		testFile.setId("TestFile");
		testFile.setTitle("test.txt");
		filesList.add(testFile);
		listOfFiles.setItems(filesList);
		when(localFile.getName()).thenReturn("test.txt");
		when(drive.files()).thenReturn(getFiles);
		when(getFiles.list()).thenReturn(getList);
		when(getList.execute()).thenReturn(listOfFiles);
		String fileId = GD.getFileId("test.txt");
		when(getFiles.delete(eq(fileId))).thenReturn(delete);
		when(delete.execute()).thenReturn(null);
		GD.deleteFile(localFile);
		verify(drive, Mockito.atLeastOnce()).files();
	    verify(getFiles, Mockito.atLeast(0)).delete(Mockito.eq(fileId));
	    verify(delete, Mockito.atLeastOnce()).execute();		
	
	}

}