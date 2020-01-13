package data;

import components.*;
import components.flipflops.FlipFlop;
import components.gates.Gate;
import components.switches.Switch;
import controllers.MainWindowController;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
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
    private ArrayList<Component> arrayListCreatedEndComponents;
    private ArrayList<Component> arrayListAllCreatedComponents;
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
        this.arrayListCreatedEndComponents = mwc.getArrayListCreatedEndComponents();
        this.arrayListAllCreatedComponents = mwc.getArrayListAllCreatedComponents();
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
            File file  = fileChooser.showOpenDialog(main.getPrimaryStage());
            if(file != null) {
                saveFile = file;
                loadCircuit(saveFile);
            }
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
            File file = fileChooser.showSaveDialog(main.getPrimaryStage());
            if(file != null) {
                saveFile = file;
                saveFile.createNewFile();
                saveCircuit(saveFile);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void loadCircuit(File file){
        int i = 0;
        try{
            arrayListCreatedEndComponents.clear();
            arrayListAllCreatedComponents.clear();
            arrayListCreatedGates.clear();
            arrayListCreatedSwitches.clear();
            arrayListCreatedFlipFlops.clear();
            arrayListCreatedBulbs.clear();
            arrayListCreatedLines.clear();
            arrayListCreatedConnector.clear();
            arrayListSeries.clear();
            componentCreator.setGateCounter(0);
            componentCreator.setSwitchCounter(0);
            componentCreator.setFlipFlopCounter(0);
            componentCreator.setBulbCounter(0);
            componentCreator.setConnectorCounter(0);
            componentCreator.setLineCounter(0);

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
                if(!c.getName().equals(Names.lineName) && !c.getName().equals(Names.connectorName)) {
                    c.setAddingDataToSeriesEnabled(true);
                    c.setPictures();
                    XYChart.Series<Long, String> newSeries = new XYChart.Series<>();
                    newSeries.getData().add(new XYChart.Data<Long, String>(0L, c.getName() + "   " + c.getId() + ":   0"));
                    newSeries.getData().add(new XYChart.Data<Long, String>(0L, c.getName() + "   " + c.getId() + ":   1"));
                    if (c.isSignalOutput()) {
                        newSeries.getData().add(new XYChart.Data<Long, String>(0L, c.getName() + "   " + c.getId() + ":   1"));
                    } else {
                        newSeries.getData().add(new XYChart.Data<Long, String>(0L, c.getName() + "   " + c.getId() + ":   0"));
                    }
                    c.setSeriesWithTime(newSeries, componentCreator.getTimeStart());
                    arrayListSeries.add(newSeries);
                    arrayListCreatedEndComponents.add(c);
                    lineChartStates.getData().add(newSeries);
                }
                if(!c.getName().equals(Names.lineName)){
                    arrayListAllCreatedComponents.add(c);
                }
                mwc.getStackUndoChanges().push(new Change(1, c));
                i++;
            }
            for(Line l : arrayListCreatedLines){
                l.getArrayListVisitedLines().clear();
                l.getArrayListDependentComponents().clear();
                l.getArrayListDependentFlipFlopOutput().clear();
            }
            for(Line l : arrayListCreatedLines){
                l.checkForSignals(l, l.getArrayListDependentComponents(), l.getArrayListVisitedLines());
            }
            for(Component c : arrayListAllCreatedComponents){
                c.revive();
            }
            for(Line l : arrayListCreatedLines){
                l.revive();
            }

            ois.close();
            fis.close();
        }
        catch (Exception e){
//            System.out.println("Koniec pliku, zaladowano " + i + " obiektow");
//            e.printStackTrace();
        }
        mwc.repaintScreen();
    }

    public void saveCircuit(File file){
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            for(Component c : arrayListAllCreatedComponents){
                oos.writeObject(c);
            }
            for(Line l : arrayListCreatedLines){
                oos.writeObject(l);
            }

            oos.close();
            fos.close();

            mwc.getMain().setUnsavedChanges(false);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
