import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.sound.sampled.Line;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
        régions.setOnAction(event -> {
            File currFile = getFile(fc,primaryStage);
            bp.setCenter(getAreaChart(primaryStage,currFile));
        });
        barres.setOnAction(event -> {
            File currFile = getFile(fc,primaryStage);
            bp.setCenter(getBarChart(primaryStage,currFile));
        });
        png.setOnAction(event -> {
            saveAsPng(primaryStage,fc);
        });

        gif.setOnAction(event -> {
            saveAsGif(primaryStage,fc);
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
        fc.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("Fichier .dat","*.dat"));
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
            int y = 0;
            for(String line : all){
                if(y==0) {
                    lol = line.split(", ");
                }
                if(y==1){
                    lol2 = line.split(", ");
                }
                y++;

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
    public AreaChart<String,Number> getAreaChart(Stage primaryStage, File fichier){
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Mois");
        yAxis.setLabel("Température");
        AreaChart chart = new AreaChart(xAxis, yAxis);
        chart.setTitle("Températures Moyennes");
        String[] lol = new String[0];
        String[] lol2 = new String[0];
        try {
            List<String> allTemps = new ArrayList<>();
            List<String> all=Files.readAllLines(Paths.get(fichier.getPath()));
            int y = 0;
            for(String line : all){
                if(y==0) {
                    lol = line.split(", ");
                }
                if(y==1){
                    lol2 = line.split(", ");
                }
                y++;

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
                alerte.setContentText("Le nombre de lignes ou les informations sont peut-être non conforme. \nAssurez-vous que le fichier a le même nombre de ligne pour les mois et température ainsi que les nombres soit bien des nombres");
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
                chart.getData().addAll(series);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return chart;
    }
    public BarChart<String,Number> getBarChart(Stage primaryStage, File fichier){
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Mois");
        yAxis.setLabel("Température");
        BarChart chart = new BarChart(xAxis, yAxis);
        chart.setTitle("Températures Moyennes");
        String[] lol = new String[0];
        String[] lol2 = new String[0];
        try {
            List<String> allTemps = new ArrayList<>();
            List<String> all=Files.readAllLines(Paths.get(fichier.getPath()));
            int y = 0;
            for(String line : all){
                if(y==0) {
                    lol = line.split(", ");
                }
                if(y==1){
                    lol2 = line.split(", ");
                }
                y++;

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
                chart.getData().addAll(series);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return chart;
    }
    public void saveAsPng(Stage primaryStage,FileChooser fileChooser){
        fileChooser.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("Fichier .png","*.png"));
        fileChooser.setTitle("Enregistrez un fichier .png");
        File file = fileChooser.showSaveDialog(primaryStage);
        WritableImage image = primaryStage.getScene().snapshot(null);

        try{
            ImageIO.write(SwingFXUtils.fromFXImage(image,null),"png",file);
        }catch (IOException e){
            e.printStackTrace();
        }

    }
    public void saveAsGif(Stage primaryStage,FileChooser fileChooser){
        fileChooser.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("Fichier .gif","*.gif"));
        fileChooser.setTitle("Enregistrez un fichier .gif");
        File file = fileChooser.showSaveDialog(primaryStage);
        WritableImage image = primaryStage.getScene().snapshot(null);

        try{
                ImageIO.write(SwingFXUtils.fromFXImage(image,null),"gif",file);
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
