package data;

import components.*;
import components.flipflops.FlipFlop;
import components.gates.Gate;
import components.switches.Switch;
import controllers.MainWindowController;
import javafx.collections.FXCollections;
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
    private ComponentCreator componentCreator;
    private ArrayList<Component> arrayListCreatedComponents;
    private ArrayList<Gate> arrayListCreatedGates;
    private ArrayList<Switch> arrayListCreatedSwitches;
    private ArrayList<FlipFlop> arrayListCreatedFlipFlops;
    private ArrayList<Bulb> arrayListCreatedBulbs;
    private ArrayList<Line> arrayListCreatedLines;
    private ArrayList<Connector> arrayListCreatedConnector;
    private ArrayList<XYChart.Series<Long, String>> arrayListSeries;
    private LineChart lineChartStates;
    private File saveFile = null;

    public FileOperator(MainWindowController mwc) {
        this.mwc = mwc;
        this.main = mwc.getMain();
        this.componentCreator = mwc.getComponentCreator();
        this.arrayListCreatedComponents = mwc.getArrayListCreatedComponents();
        this.arrayListCreatedGates = mwc.getArrayListCreatedGates();
        this.arrayListCreatedSwitches = mwc.getArrayListCreatedSwitches();
        this.arrayListCreatedFlipFlops = mwc.getArrayListCreatedFlipFlops();
        this.arrayListCreatedBulbs = mwc.getArrayListCreatedBulbs();
        this.arrayListCreatedLines = mwc.getArrayListCreatedLines();
        this.arrayListCreatedConnector = mwc.getArrayListCreatedConnectors();
        this.arrayListSeries = mwc.getArrayListSeries();
        this.lineChartStates = mwc.getLineChartStates();
    }

    public void actionMenuItemLoad(){
        try{
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Wybierz plik do załadowania");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Pliki symulatora układów cyfrowych", "*.kksuc"));
            saveFile = fileChooser.showOpenDialog(main.getPrimaryStage());
            loadCircuit(saveFile);
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
            saveFile.createNewFile();
            saveCircuit(saveFile);
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
                    componentCreator.setLineCounter(componentCreator.getLineCounter() + 1);
                    c.setId(componentCreator.getLineCounter());
                }
                else if(c.getName().contains(Names.gateSearchName)){
                    arrayListCreatedGates.add((Gate)c);
                    componentCreator.setGateCounter(componentCreator.getGateCounter() + 1);
                    c.setId(componentCreator.getGateCounter());
                }
                else if(c.getName().contains(Names.switchSearchName)){
                    arrayListCreatedSwitches.add((Switch)c);
                    componentCreator.setSwitchCounter(componentCreator.getSwitchCounter() + 1);
                    c.setId(componentCreator.getSwitchCounter());
                }
                else if(c.getName().contains(Names.flipFlopSearchName)){
                    arrayListCreatedFlipFlops.add((FlipFlop)c);
                    componentCreator.setFlipFlopCounter(componentCreator.getFlipFlopCounter() + 1);
                    c.setId(componentCreator.getFlipFlopCounter());
                }
                else if(c.getName().contains(Names.bulbName)){
                    arrayListCreatedBulbs.add((Bulb)c);
                    componentCreator.setBulbCounter(componentCreator.getBulbCounter() + 1);
                    c.setId(componentCreator.getBulbCounter());
                }
                else if(c.getName().contains(Names.connectorName)){
                    arrayListCreatedConnector.add((Connector)c);
                    componentCreator.setConnectorCounter(componentCreator.getConnectorCounter() + 1);
                    c.setId(componentCreator.getConnectorCounter());
                }
                c.setLife();
                if(!c.getName().equals(Names.lineName) || !c.getName().equals(Names.connectorName)) {
                    c.setAddingDataToSeriesEnabled(true);
                    c.setPictures();
                    System.out.println(c.getName());
                    XYChart.Series<Long, String> newSeries = new XYChart.Series<>();
                    newSeries.getData().add(new XYChart.Data<Long, String>(0L, c.getName() + " " + c.getId() + ": 0"));
                    newSeries.getData().add(new XYChart.Data<Long, String>(0L, c.getName() + " " + c.getId() + ": 1"));
                    if (c.isSignalOutput()) {
                        newSeries.getData().add(new XYChart.Data<Long, String>(0L, c.getName() + " " + c.getId() + ": 1"));
                    } else {
                        newSeries.getData().add(new XYChart.Data<Long, String>(0L, c.getName() + " " + c.getId() + ": 0"));
                    }
                    c.setSeriesWithTime(newSeries, componentCreator.getTimeStart());
                    arrayListSeries.add(newSeries);
                    arrayListCreatedComponents.add(c);
                    lineChartStates.getData().add(newSeries);
                }
                i++;
            }

            ois.close();
            fis.close();
        }
        catch (Exception e){
            System.out.println("Koniec pliku, zaladowano " + i + " obiektow");
            e.printStackTrace();
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
