package application.Model.Types;

import java.util.ArrayList;

public class Result {
    private String keyword;
    private ArrayList<OnlineResource> results;

    public Result(String keyword, ArrayList<OnlineResource> results) {
        this.keyword = keyword;
        this.results = results;
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
