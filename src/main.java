import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.sound.sampled.Line;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        FileChooser fc =new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier .dat","*.dat"));
        LineChart lineChart= null;


        Menu imp = new Menu("Importer");
        MenuItem lignes = new MenuItem("Lignes");
        MenuItem régions = new MenuItem("Régions");
        MenuItem barres = new MenuItem("Barres");
        imp.getItems().addAll(lignes,régions,barres);









        Menu exp = new Menu("Exporter");
        MenuItem png = new MenuItem("PNG");
        MenuItem gif = new MenuItem("GIF");
        exp.getItems().addAll(png,gif);

        MenuBar topBar = new MenuBar();
        topBar.getMenus().addAll(imp,exp);
        topBar.prefWidthProperty().bind(primaryStage.widthProperty());

        BorderPane bp = new BorderPane();
        bp.setTop(topBar);
        bp.setCenter(lineChart);

        //MENU IMPORTER EVENTS

        lignes.setOnAction(event -> {
            File currFile = getFile(fc,primaryStage);
            bp.setCenter(getLineChart(primaryStage,currFile));
        });

        Group root = new Group(bp);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setWidth(600);
        primaryStage.setHeight(600);
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(600);
    }
    public File getFile(FileChooser fc,Stage primaryStage){
        fc.setTitle("Séléctionner un fichier .dat");
        return fc.showOpenDialog(primaryStage);
    }
    public LineChart<String,Number> getLineChart(Stage primaryStage, File fichier){
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Mois");
        yAxis.setLabel("Température");
        LineChart lineChart = new LineChart(xAxis, yAxis);
        lineChart.setTitle("Températures Moyennes");
        String[] lol = new String[0];
        String[] lol2 = new String[0];
        try {
            List<String> allTemps = new ArrayList<>();
            List<String> all=Files.readAllLines(Paths.get(fichier.getPath()));
            for(String line : all){
                int i = 0;
                if(i==0) {
                    lol = line.split(", ");
                }i++;
                if(i==1){
                    lol2 = line.split(", ");
                }

            }
            allTemps.addAll(Arrays.asList(lol));
            Boolean canContinue = true;
            for (String i : lol){
                if(i==null||i.equals("")||i.equals(" ")){
                    canContinue=false;
                }
            }
            for (String i : lol2){
                    try {
                        Integer.parseInt(i);
                    }catch (NumberFormatException e){
                        e.printStackTrace();
                        canContinue=false;
                        continue;
                    }
            }

            if(lol.length!=lol2.length||!canContinue){
                Alert alerte = new Alert(Alert.AlertType.INFORMATION);
                alerte.setTitle("Information Importante");
                alerte.setHeaderText("Le fichier est problématique");
                alerte.setContentText("Le nombre de ligne, les informations sont peut-être non conforme. \nAssurez-vous que le fichier a le même nombre de ligne pour les mois et température ainsi que les nombres soit bien des nombres");
                alerte.showAndWait();
            }else {
                ArrayList<Integer> allNumbers = new ArrayList<>();
                for (int i = 0; i < lol2.length; i++) {
                    allNumbers.add(Integer.parseInt(lol2[i]));
                }
                XYChart.Series series = new XYChart.Series();
                series.setName("Données");
                for (int i = 0; i < allTemps.size(); i++) {
                    series.getData().add(new XYChart.Data(allTemps.get(i), allNumbers.get(i)));
                }
                lineChart.getData().addAll(series);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return lineChart;
    }

}
