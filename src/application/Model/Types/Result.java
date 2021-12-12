package application.Model.Types;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
/*
* wrapper for results of respective keyword when searched
 */
public class Result {
    private String keyword;
    private ArrayList<OnlineResource> results;

    public Result(String keyword, ArrayList<OnlineResource> results) {
        this.keyword = keyword;
        this.results = results;
    }
    public void save(File txtFile) throws IOException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(this);
        FileOutputStream os = new FileOutputStream(txtFile);
        os.write(json.getBytes(StandardCharsets.UTF_8));
    }
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public ArrayList<OnlineResource> getResults() {
        return results;
    }

    public void setResults(ArrayList<OnlineResource> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "Result{" +
                "keyword='" + keyword + '\'' +
                ", results=" + results.toString() +
                '}';
    }
}
