import org.junit.Before;
import org.junit.Test;

import javax.ejb.BeforeCompletion;

import static org.junit.Assert.*;

public class UnitTests {

    String[] goodLinks;
    String[] badLinks;

    @Before
    public void setUp(){
        goodLinks = new String[] {"https://en.wikipedia.org/wiki/Underwater_diving",
                "https://en.wikipedia.org/wiki/Kazuo_Ishiguro",
                "https://en.wikipedia.org/wiki/Catalan_independence_referendum,_2017",
                "https://en.wikipedia.org/wiki/Spanish_Constitution_of_1978",
                "https://en.wikipedia.org/wiki/Surface-supplied_diving",
                "https://en.wikipedia.org/wiki/Submersible",
                "Civil_Rights_Movement",
                "Alison_Van_Eenennaam"};
        badLinks = new String[] {"Math",
                "https://en.wikipedia.org/wiki/January_1",
                "https://en.wikipedia.org/wiki/New_Year%27s_Day",
                "https://en.wikipedia.org/wiki/Violet_%26_Daisy",
                "https://en.wikipedia.org/wiki/2017_Las_Vegas_Strip_shooting"};
    }

    @Test
    public void testGoodLinks(){
        PhilosophyWebDriver driver;
        for(String link : goodLinks){
            driver = new PhilosophyWebDriver(link);
            driver.runDriver();
            assertEquals("http://en.wikipedia.org/wiki/Philosophy", driver.getPageList().get(driver.getPageList().size() - 2).getUrl());
        }
    }
    @Test
    public void testBadLinks(){
        PhilosophyWebDriver driver;
        for(String link : badLinks){
            driver = new PhilosophyWebDriver(link);
            driver.runDriver();
            assertNotEquals("http://en.wikipedia.org/wiki/Philosophy", driver.getPageList().get(driver.getPageList().size() - 2).getUrl());
        }
    }

}
