package application.Model;

import application.Model.Types.OnlineResource;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class WebScrap {
    // Does internet search for keyword by scraping google scholar
    public static ArrayList<OnlineResource> searchEntry(String keyword, HttpClient client) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://scholar.google.com/scholar?q=" + keyword))
                .header("Content-Type", "text/html; charset=UTF-8")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.93 Safari/537.36")
                .GET()
                .build();
        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());
        org.jsoup.nodes.Document webPage = Jsoup.parse(response.body());
        Elements sources = webPage.getElementsByClass("gs_r gs_or gs_scl");
        ArrayList<OnlineResource> ret = new ArrayList<OnlineResource>();
        int sourceSize = sources.size();
        if (sources.size() > 5) {
            sourceSize = 5;
        }
        for (int i = 0; i < sourceSize; i++) {
            org.jsoup.nodes.Element source = sources.get(i).getElementsByClass("gs_ri")
                    .get(0);
            try {
                OnlineResource r = new OnlineResource(
                        source.getElementsByClass("gs_rt")
                                .get(0)
                                .getElementsByTag("a")
                                .get(0)
                                .attr("href"),
                        source.getElementsByClass("gs_a")
                                .get(0)
                                .text(),
                        source.getElementsByClass("gs_rs")
                                .get(0)
                                .text()
                );
                ret.add(r);
            } catch (IndexOutOfBoundsException e) {
                OnlineResource r = new OnlineResource("Not Found", "", "");
                ret.add(r);
            }
        }
        return ret;
    }
}
