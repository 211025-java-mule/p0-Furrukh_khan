import java.io.*;

public class coronaFileHandler extends FileHandler{

    public coronaFileHandler(String fileName, String path) {
        super(fileName, path);
    }

    @Override
    public void clearFile() {
        File myFile = new File(path + fileName);
        if(myFile.delete()){
            System.out.println("File Deleted");
        }
        else{
        System.out.println("File does not exist!");
        }

    }

    @Override
    public void writeFile() {
        try{
            FileWriter myFileWriter = new FileWriter(path + fileName);
            for(int x = 0;x<clientHandler.coronaObjects.size();x++){
                myFileWriter.write(clientHandler.coronaObjects.get(x).toString() + "\n");
            }
            myFileWriter.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }


    }

    @Override
    public void displayFile() {
        try{
            FileReader myFileReader = new FileReader(path+fileName);
            BufferedReader br = new BufferedReader(myFileReader);
            String line = "";
            while((line = br.readLine()) != null){
                System.out.println(line);
            }

        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }


}

