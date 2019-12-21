package data;

import components.Component;
import components.Line;
import components.TableComponent;
import components.flipflops.FlipFlop;
import components.gates.Gate;
import components.switches.Switch;
import controllers.MainWindowController;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import main.Main;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FileOperator {
    private MainWindowController mwc;
    private Main main;
    private ArrayList<Component> arrayListCreatedComponents;
    private ArrayList<Gate> arrayListCreatedGates;
    private ArrayList<Switch> arrayListCreatedSwitches;
    private ArrayList<FlipFlop> arrayListCreatedFlipFlops;
    private ArrayList<Line> arrayListCreatedLines;
    private ArrayList<XYChart.Series<Long, String>> arrayListSeries;
    private LineChart lineChartStates;
    private File saveFile = null;

    public FileOperator(MainWindowController mwc) {
        this.mwc = mwc;
        this.main = mwc.getMain();
        this.arrayListCreatedComponents = mwc.getArrayListCreatedComponents();
        this.arrayListCreatedGates = mwc.getArrayListCreatedGates();
        this.arrayListCreatedSwitches = mwc.getArrayListCreatedSwitches();
        this.arrayListCreatedFlipFlops = mwc.getArrayListCreatedFlipFlops();
        this.arrayListCreatedLines = mwc.getArrayListCreatedLines();
        this.arrayListSeries = mwc.getArrayListSeries();
        this.lineChartStates = mwc.getLineChartStates();
    }

    public void actionMenuItemLoad(){
        try{
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Wybierz plik do załadowania");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Pliki symulatora układów cyfrowych", "*.kksuc"));
            File file = fileChooser.showOpenDialog(main.getPrimaryStage());
            loadCircuit(file);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void actionMenuItemSave(){
        if(saveFile != null){
            saveCircuit(saveFile);
        }
        else{
            actionMenuItemSaveAs();
        }
    }

    public void actionMenuItemSaveAs(){
        try{
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Wybierz miejsce do zapisu");
            fileChooser.setInitialFileName("Schemat1 " + new SimpleDateFormat("hh.mm dd-MM-yyyy").format(new Date()));
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Pliki symulatora układów cyfrowych", "*.kksuc"));

            saveFile = fileChooser.showSaveDialog(main.getPrimaryStage());
            if(saveFile != null) {
                if (!saveFile.exists()) {
                    saveFile.createNewFile();
                }
                saveCircuit(saveFile);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void loadCircuit(File file){
        int i = 0;
        try{
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);

            while(true){
                Component c = (Component)ois.readObject();
                if(c == null){
                    break;
                }
                else if(c.getName().equals(Names.lineName)){
                    arrayListCreatedLines.add((Line)c);
                }
                else if(c.getName().contains(Names.gateSearchName)){
                    arrayListCreatedGates.add((Gate)c);
                }
                else if(c.getName().contains(Names.switchSearchName)){
                    arrayListCreatedSwitches.add((Switch)c);
                }
                else if(c.getName().contains(Names.flipFlopSearchName)){
                    arrayListCreatedFlipFlops.add((FlipFlop)c);
                }
                c.setLife();
                if(!c.getName().equals(Names.lineName)) {
                    c.setPictures();
                    XYChart.Series<Long, String> newSeries = new XYChart.Series<>();
                    newSeries.getData().add(new XYChart.Data<Long, String>(0L, c.getName() + " " + c.getId() + ": 0"));
                    newSeries.getData().add(new XYChart.Data<Long, String>(0L, c.getName() + " " + c.getId() + ": 1"));
                    newSeries.getData().add(new XYChart.Data<Long, String>(0L, c.getName() + " " + c.getId() + ": 0"));
                    c.setSeries(newSeries);
                    arrayListSeries.add(newSeries);
                    lineChartStates.getData().add(newSeries);
                    arrayListCreatedComponents.add(c);
                }

                i++;
            }

            ois.close();
            fis.close();
        }
        catch (Exception e){
            System.out.println("Koniec pliku, zaladowano " + i + " obiektow");
            //e.printStackTrace();
        }
    }

    public void saveCircuit(File file){
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            for(Component c : arrayListCreatedComponents){
                oos.writeObject(c);
            }
            for(Line l : arrayListCreatedLines){
                oos.writeObject(l);
            }

            oos.close();
            fos.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
