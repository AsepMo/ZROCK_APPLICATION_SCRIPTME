package zrock.application.scriptme.editor.event;

public class NewFileOpened {
    private String filePath;

    public NewFileOpened(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }
}
