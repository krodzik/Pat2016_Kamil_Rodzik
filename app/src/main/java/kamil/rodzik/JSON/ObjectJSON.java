package kamil.rodzik.JSON;

/**
 * Created by Kamil on 24.01.2016.
 * JSONStruct for getting and setting JSON object.
 */

public class ObjectJSON {

    private String title;
    private String desc;
    private String image;

    public ObjectJSON(){
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
