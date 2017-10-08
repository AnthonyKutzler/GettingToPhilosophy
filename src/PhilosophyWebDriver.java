import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.util.LinkedList;
import java.util.List;

class PhilosophyWebDriver {

    private String currentPage;
    private int linkHops;
    private List<String> pageList;
    private String message;
    private final String wikiHome = "http://en.wikipedia.org";

    /**
     * Starting webDriver
     * @param url full URL to first page
     */
    PhilosophyWebDriver(String url){
        if(!url.startsWith("http"))
            currentPage = wikiHome + url;
        else
            currentPage = url;
        linkHops = 0;
        pageList = new LinkedList<>();
    }

    /**
     * Run web driver
     */
    void runDriver() {
        if(!currentPage.contains("en.wikipedia")){
            pageList.add("Not a wiki URL");
            return;
        }
        String nextPage = "nothing";
        while(!currentPage.equalsIgnoreCase("http://en.wikipedia.org/wiki/philosophy")){
            if(linkHops > 200){
                message = "Max Links Reached(200)";
            }else if (pageList.contains(wikiHome + nextPage)) {
                message = "Found a loop";
                break;
            }else{
                nextPage = getLink(currentPage);
                //nextLink = getLink(currentUrl);
                if(nextPage == null) {
                    message = "No Links Found on Page";
                    break;
                }
                else {
                    linkHops++;
                    currentPage = wikiHome + nextPage;
                }
            }
        }
        if(currentPage.equalsIgnoreCase("http://en.wikipedia.org/wiki/philosophy")) {
            message = "Reached Philosophy after " + linkHops + " hops";
            pageList.add("http://en.wikipedia.org/wiki/Philosophy");
        }
        pageList.add(message);
    }

    private String getLink(String url){
        try {
            if(!url.contains("wiki") && !url.startsWith("http://em.wikipedia.org/wiki/")){
                return null;
            }
            Document document = Jsoup.connect(url).userAgent("Mozilla").get();
            pageList.add(url);
            Elements bodies = document.select("p");
            for(Element body : bodies) {
                boolean parenthesis = false;
                //just in case another parenthesis bock is within the current parenthesis block
                boolean nestedParenthesis = false;
                List<Node> nodes = body.childNodes();
                for (Node node : nodes) {
                    //check for parenthesis block with in text
                    if (node.nodeName().equals("#text")) {
                        //if find both open and closed parenthesis do nothing
                        if (node.toString().contains("(") && node.toString().contains(")")) {
                        } else if(node.toString().contains("(")){
                            if (parenthesis) {
                                nestedParenthesis = true;
                            } else
                                parenthesis = true;
                        } else if (node.toString().contains(")")) {
                            if (nestedParenthesis)
                                nestedParenthesis = false;
                            else
                                parenthesis = false;
                        }
                        //if node is 'a' or link
                    } else if (node.nodeName().equals("a") && !parenthesis && !nestedParenthesis) {
                        Element link = (Element) node;
                        if (link.attr("href").contains("wiki")) {
                            if (!link.text().contains("(")){
                                if (link.attr("href") != null)
                                    //return link path
                                    return link.attr("href");
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Add each item in String List to WikiPage List
     * @return WikiPage List
     */
    List<WikiPage> getPageList() {
        List<WikiPage> wikiPageList = new LinkedList<>();
        for(String page : pageList){
            wikiPageList.add(new WikiPage(page, page.substring(page.lastIndexOf("/") + 1, page.length())));
        }
        return wikiPageList;
    }
}

