public abstract class FileHandler {

    protected String fileName;
    protected String path;

    public FileHandler(){
        this.fileName = "newFile";
        this.path = "/";
    }

    public FileHandler(String fileName, String path) {
        this.fileName = fileName;
        this.path = path;
    }

    abstract public void clearFile();

    abstract public void writeFile();

    abstract public void displayFile();

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


}
