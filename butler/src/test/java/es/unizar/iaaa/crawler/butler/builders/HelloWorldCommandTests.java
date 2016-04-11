
package es.unizar.iaaa.crawler.butler.builders;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.shell.core.CommandResult;
import org.springframework.shell.core.JLineShellComponent;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import es.unizar.iaaa.crawler.butler.Application;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@ActiveProfiles("test")
public class HelloWorldCommandTests {

	@Autowired
	private JLineShellComponent shell;
	 
	@Test
	public void testSimple() {
		CommandResult cr = shell.executeCommand("date");
		assertEquals(true, cr.isSuccess());
	}
}