package RedBot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class HParser {
    
    private static String source="";

    public HParser(String url) {
        InputStream is = null;
        BufferedReader br;
        String line;
        String source="";
        try {
            URL address = new URL(url);
            is = address.openStream();  // throws an IOException
            br = new BufferedReader(new InputStreamReader(is));

            while ((line = br.readLine()) != null) {
                source+=line;
            }
        } catch (MalformedURLException mue) {
             mue.printStackTrace();
        } catch (IOException ioe) {
             ioe.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException ioe) {}
        }
        this.source = source;
        //System.out.println(this.source);
    }
    
    public boolean noResults() { 
        Document doc = Jsoup.parse(source);
        try{
            return doc.select("h2").last().toString().equals("<h2>No results found</h2>");
        } catch (NullPointerException e){
            return false;
        }
    }
    
    public static Elements getData(String type) {
        Document doc = Jsoup.parse(source);
        Elements data = null;
        switch(type) {
            case "link": //All links on the page
                data = doc.select("a[href]");
                break;
                
            case"numbers": //Links with magic numbers
                data = doc.select("a[href*='/g/']");
                break;
                
            case "title": //Doujinshi titles
                data = doc.select("span");
                break;
                
            case "thumb": //Thumbnails, i.e. cover pages
                data = doc.select("img[class]");
                break;
                
            case "tags": //Self explanatory
                data = doc.select("a[href*='/tag/'] span[class='name']");
                break;
        }
        return data;
    }
}
