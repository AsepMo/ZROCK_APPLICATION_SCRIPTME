package zrock.application.scriptme.editor.event;

public class FileSavedEvent {
    private String filePath;

    public FileSavedEvent(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }
}
