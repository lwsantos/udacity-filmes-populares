package br.com.lwsantos.filmespopulares.Model;

/**
 * Created by lwsantos on 25/03/17.
 */

public class Video {
    private String key;
    private String name;
    private String type;
    private String site;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }
}