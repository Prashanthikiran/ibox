package edu.csupomona.cs585.ibox;

import static org.junit.Assert.*;

import java.awt.Toolkit;
import java.util.Timer;
import java.util.TimerTask;

import java.nio.file.Path;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.api.services.drive.Drive;

import edu.csupomona.cs585.ibox.sync.FileSyncManager;
import edu.csupomona.cs585.ibox.sync.GoogleDriveFileSyncManager;

public class WatchDirTest {

	WatchDir watchdir;
	Path path;
	FileSyncManager manager;
	Drive drive;
	Timer time;
	Toolkit tool;
	
	
	@Before
	public void setUp() throws Exception {
		drive = Mockito.mock(Drive.class);
		manager = new GoogleDriveFileSyncManager(drive);
		path = Mockito.mock(Path.class);
		watchdir = new WatchDir(path, manager);
		time = new Timer();
		tool = Toolkit.getDefaultToolkit();
		}

	@After
	public void tearDown() throws Exception {
		drive = null;
		manager = null;
		watchdir = null;
		path = null;
	}

	@Test
	public void ProcessEventstest() {
		time.schedule(new ProcessEvents(), 50000);
	}
	
	class ProcessEvents extends TimerTask {
		public void run() {
			watchdir.processEvents();
			System.exit(0);
		}	
	}

}
