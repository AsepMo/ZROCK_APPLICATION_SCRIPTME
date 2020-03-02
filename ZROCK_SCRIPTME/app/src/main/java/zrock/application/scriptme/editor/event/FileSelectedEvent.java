package zrock.application.scriptme.editor.event;

public class FileSelectedEvent {

    private String path;

    public FileSelectedEvent(String path){
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
