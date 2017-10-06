import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

@Named("philoPage")
@ViewScoped
public class PhilosophyPage implements Serializable {

    private List<WikiPage> pages;
    private int hops = 0;
    private String linkHops;
    private String url = "";
    private PhilosophyWebDriver driver;

    public PhilosophyPage(){

    }

    public void findPhilosophy(){
        if(!url.equals("")){
            try {
                pages = new DatabaseController().getPreviousPath(url);
            }catch (SQLException e){
                System.out.println(e.getMessage());
            }
            if(pages.size() < 1) {
                driver = new PhilosophyWebDriver(url);
                driver.runDriver();
                pages = driver.getPageList();
            }
            //If URL isnt a wiki link
            if(!pages.get(0).getUrl().startsWith("Not")) {
                try {
                    new DatabaseController().insertPath(url, pages);
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public List<WikiPage> getPages() {
        return pages;
    }

    public void setPages(List<WikiPage> pages) {
        this.pages = pages;
    }

    public int getHops() {
        return hops;
    }

    public void setHops(int hops) {
        this.hops = hops;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLinkHops() {
        return linkHops;
    }

    public void setLinkHops(String linkHops) {
        this.linkHops = linkHops;
    }
}

