package application.Model.Types;
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
