/*
 * This Java source file was generated by the Gradle 'init' task.
 */
import org.junit.Test;
import static org.junit.Assert.*;

public class EmailScraperTest {
    @Test public void testAppHasAGreeting() {
        EmailScraper classUnderTest = new EmailScraper("abc.com");
        assertNotNull("app should have a greeting", classUnderTest.getHelp());
    }
}
