package application.Model.Types;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/*
* Represents result from web search
 */
public class OnlineResource {
    private String hyperlink;
    private String title;
    private String description;

    public OnlineResource(String hyperlink, String title, String description) {
        this.hyperlink = hyperlink;
        this.title = title;
        this.description = description;
    }
    public void save(File txtFile) throws IOException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(this);
        FileOutputStream os = new FileOutputStream(txtFile);
        os.write(json.getBytes(StandardCharsets.UTF_8));
    }
    public String getHyperlink() {
        return hyperlink;
    }

    public void setHyperlink(String hyperlink) {
        this.hyperlink = hyperlink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "OnlineResource{" +
                "hyperlink='" + hyperlink + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
