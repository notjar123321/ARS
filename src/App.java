import javafx.application.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.*;
import javafx.event.*;
import javafx.scene.text.*;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;


public class App extends Application{
    
    private String currentUserPath = System.getProperty("user.home");
    private String fullPath = currentUserPath + "\\AppData\\Local\\ARS Loan Tracker";
    private String ending = ".arsloantracker";

    @FXML
    private TextField nameField, incomeField, loanAmountField, interestField, monthlyExpensesField, paymentField, fileOpener;
    private Scene mainScene;
    @FXML
    private Text remainingDebt, loanName;
    private File currentLoan;
    @FXML
    private BorderPane fileOpenPane;

    public void makePayment(){
        if (currentLoan == null){return;}
        try{
            Scanner s = new Scanner(currentLoan);
            String w = s.nextLine() + "\n";
            w += Math.max(Double.parseDouble(s.nextLine()) - Double.parseDouble(paymentField.getText()), 0);
            w += "\n" + s.nextLine() + "\n" + s.nextLine();
            s.close();
            writeToFile(currentLoan, w);
            updateRemainingDebt(currentLoan);
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    public void newFile(ActionEvent e){
        File f = new File(fullPath + "\\" + nameField.getText() + ending);
        createFile(f);
        try{
            writeToFile(f, incomeField.getText() + "\n" + loanAmountField.getText() + "\n" + 
            interestField.getText() + "\n" + monthlyExpensesField.getText() + "\n");
            open(f);
        }
        catch(Exception ex){
            System.out.println(ex);
            System.out.println("GO FUCK YOURSELF.");
        }
    }
    public void openFile(ActionEvent e){
        fileOpenPane.setVisible(true);
    }
    public void updateRemainingDebt(File f){
        try{
            Scanner s = new Scanner(f);
            s.nextLine();
            remainingDebt.setText("Remaining Payment: $" + s.nextLine());
            s.close();
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    public void actuallyOpenFile(ActionEvent e){
        File f = new File(fullPath + "\\" + fileOpener.getText() + ending);
        System.out.println("open");
        if (!f.exists()){return;}
        open(f);
        closeFileOpener(e);
    }
    public void closeFileOpener(ActionEvent e){
        fileOpenPane.setVisible(false);
    }
    public void open(File f){
        currentLoan = f;
        loanName.setText("Loan \"" + f.getName().substring(0, f.getName().indexOf(".arsloantracker")) + "\"");
        updateRemainingDebt(f);
    }

    public void writeToFile(File f, String t){
        try{
            FileWriter writer = new FileWriter(f);
            writer.write(t);
            writer.close();
        }
        catch(Exception e){
            System.out.println("something's gone wrong again.");
            System.out.println(e);
        }
    }

    public void start(Stage mainStage){
        //create the filefolder if it's not there
        File folder = new File(fullPath);
        if (!folder.exists()){
            folder.mkdir();
        }
        mainStage.setTitle("Loan Tracker");
        mainScene = getScene("MainApp.fxml");
        if (mainScene != null)
            mainStage.setScene(mainScene);
        mainStage.setResizable(false);
        mainStage.show();
    }

    public double parse(String s){
        return s.equals("") ? 0 : Double.parseDouble(s);
    }
    public boolean createFile(File f){
        try{
            return f.createNewFile();
        }
        catch(Exception e){
            System.out.println("couldn't fucking create the file for some reason. great, we're cooked.");
            System.out.println(e);
            return false;
        }
    }
    public Scene getScene(String fileName){
        try{
            return new Scene(FXMLLoader.load(getClass().getResource(fileName)));
        }
        catch(Exception e){
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    
    
}
