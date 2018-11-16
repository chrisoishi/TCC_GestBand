package APP;

import TCP.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;

public class Graph {

    private static final int MAX_DATA_POINTS = 100;
    private int xSeriesData = 0;
    private XYChart.Series<Number, Number> series1 = new XYChart.Series<>();
    private XYChart.Series<Number, Number> series2 = new XYChart.Series<>();
    private XYChart.Series<Number, Number> series3 = new XYChart.Series<>();
    private XYChart.Series<Number, Number> series4 = new XYChart.Series<>();
    private XYChart.Series<Number, Number> series5 = new XYChart.Series<>();
    private XYChart.Series<Number, Number> series6 = new XYChart.Series<>();
    private ExecutorService executor;
    private ConcurrentLinkedQueue<Number> dataQ1 = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<Number> dataQ2 = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<Number> dataQ3 = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<Number> dataQ4 = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<Number> dataQ5 = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<Number> dataQ6 = new ConcurrentLinkedQueue<>();
    private FlowPane root = new FlowPane();
    private String s = "";
    private String[] s2;
    private float[] s_saved = new float[100];
    private float[] s_test = new float[100];

    private NumberAxis xAxis;
    private NumberAxis xAxis1;

    Button btnStop = new Button();
    Button btnSave = new Button();
    Button btnClear = new Button();
    Button btnTest = new Button();
    Button btnRead = new Button();
    boolean stop = false;

    private DTW lDTW = new DTW();
    private boolean test_dtw = false;

    TextField textField = new TextField();
    TextField textField2 = new TextField();

    private void init(Stage primaryStage) {

        Label label1 = new Label("Name:");

        xAxis = new NumberAxis(0, MAX_DATA_POINTS, MAX_DATA_POINTS / 10);
        xAxis.setForceZeroInRange(false);
        xAxis.setAutoRanging(false);
        xAxis.setTickLabelsVisible(true);
        xAxis.setTickMarkVisible(true);
        xAxis.setMinorTickVisible(true);

        xAxis1 = new NumberAxis(0, MAX_DATA_POINTS, MAX_DATA_POINTS / 10);
        xAxis1.setForceZeroInRange(false);
        xAxis1.setAutoRanging(false);
        xAxis1.setTickLabelsVisible(true);
        xAxis1.setTickMarkVisible(true);
        xAxis1.setMinorTickVisible(true);

        btnStop.setText("Start/Stop");
        btnStop.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                if (stop) {
                    stop = false;
                } else {
                    stop = true;
                }
            }
        });

        btnSave.setText("Save");
        btnSave.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    saveGesture();
                } catch (IOException ex) {
                    Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        btnClear.setText("Clear");
        btnClear.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (stop) {
                    series1.getData().clear();
                    series2.getData().clear();
                    series3.getData().clear();
                    series4.getData().clear();
                    series5.getData().clear();
                    series6.getData().clear();
                    xSeriesData = 0;
                    addDataToSeries();
                }
            }
        });

        btnTest.setText("Test");
        btnTest.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                for (int i = 0; i < series1.getData().size(); i++) {
                    s_test[i] = series1.getData().get(i).getYValue().floatValue();
                }

                System.out.println(lDTW.compute(s_test, s_saved).getDistance());
            }
        });

        NumberAxis yAxis = new NumberAxis();
        NumberAxis yAxis1 = new NumberAxis();

        // Create a LineChart
        final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis) {
            // Override to remove symbols on each data point
            @Override
            protected void dataItemAdded(XYChart.Series<Number, Number> series, int itemIndex, XYChart.Data<Number, Number> item) {
            }
        };
        final LineChart<Number, Number> lineChart1 = new LineChart<Number, Number>(xAxis1, yAxis1) {
            // Override to remove symbols on each data point
            @Override
            protected void dataItemAdded(XYChart.Series<Number, Number> series, int itemIndex, XYChart.Data<Number, Number> item) {
            }
        };

        lineChart.setAnimated(false);
        lineChart.setTitle("Acc Time Series");
        lineChart.setHorizontalGridLinesVisible(true);

        lineChart1.setAnimated(false);
        lineChart1.setTitle("Gyro Time Series");
        lineChart1.setHorizontalGridLinesVisible(true);

        // Set Name for Series
        series1.setName("Acc X");
        series2.setName("Acc Y");
        series3.setName("Acc Z");
        series4.setName("Gyro X");
        series5.setName("Gyro Y");
        series6.setName("Gyro Z");

        // Add Chart Series
        lineChart.getData().addAll(series1, series2, series3);
        lineChart1.getData().addAll(series4, series5, series6);

        root.getChildren().addAll(lineChart, lineChart1, btnStop, btnSave, btnClear, btnTest, btnRead, textField, textField2);
        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setScene(scene);

    }

    public void start(Stage stage) throws IOException {

        stage.setTitle("GestBand");
        init(stage);
        stage.show();

        executor = Executors.newCachedThreadPool(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                return thread;
            }
        });

        prepareTimeline();
    }

    //-- Timeline gets called in the JavaFX Main thread
    private void prepareTimeline() {
        // Every frame to take any data from queue and add to chart
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                addDataToSeries();
            }
        }.start();
    }

    private void addDataToSeries() {
        for (int i = 0; i < 20; i++) { //-- add 20 numbers to the plot+
            if (dataQ1.isEmpty()) {
                break;
            }
            series1.getData().add(new XYChart.Data<>(xSeriesData, dataQ1.remove()));
            series2.getData().add(new XYChart.Data<>(xSeriesData, dataQ2.remove()));
            series3.getData().add(new XYChart.Data<>(xSeriesData, dataQ3.remove()));
            series4.getData().add(new XYChart.Data<>(xSeriesData, dataQ4.remove()));
            series5.getData().add(new XYChart.Data<>(xSeriesData, dataQ5.remove()));
            series6.getData().add(new XYChart.Data<>(xSeriesData, dataQ6.remove()));
            if (xSeriesData < MAX_DATA_POINTS) {
                xSeriesData++;
            }
        }

        if (series1.getData().size() > MAX_DATA_POINTS) {
            series1.getData().remove(0, series1.getData().size() - MAX_DATA_POINTS);
            series2.getData().remove(0, series2.getData().size() - MAX_DATA_POINTS);
            series3.getData().remove(0, series3.getData().size() - MAX_DATA_POINTS);
            series4.getData().remove(0, series4.getData().size() - MAX_DATA_POINTS);
            series5.getData().remove(0, series5.getData().size() - MAX_DATA_POINTS);
            series6.getData().remove(0, series6.getData().size() - MAX_DATA_POINTS);
            for (int j = 0; j < series1.getData().size(); j++) {
                series1.getData().get(j).setXValue(j - 1);
                series2.getData().get(j).setXValue(j - 1);
                series3.getData().get(j).setXValue(j - 1);
                series4.getData().get(j).setXValue(j - 1);
                series5.getData().get(j).setXValue(j - 1);
                series6.getData().get(j).setXValue(j - 1);
            }
        }
        // update
        xAxis.setLowerBound(xSeriesData - MAX_DATA_POINTS);
        xAxis.setUpperBound(xSeriesData - 1);
        xAxis1.setLowerBound(xSeriesData - MAX_DATA_POINTS);
        xAxis1.setUpperBound(xSeriesData - 1);
    }

    public void print(String s) {
        s2 = s.split(":");
        if (!stop) {
            if (s2[0].equals("A")) {
                dataQ1.add(Integer.valueOf(s2[1]));
                dataQ2.add(Integer.valueOf(s2[2]));
                dataQ3.add(Integer.valueOf(s2[3]));
            } else {
                dataQ4.add(Integer.valueOf(s2[1]));
                dataQ5.add(Integer.valueOf(s2[2]));
                dataQ6.add(Integer.valueOf(s2[3]));
                testDTW();
            }
        }
    }

    void start(Scene scene2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void saveGesture() throws IOException {
        float[][] s = new float[6][100];
        for (int i = 0; i < series1.getData().size(); i++) {
            s[0][i] = series1.getData().get(i).getYValue().floatValue();
        }
        for (int i = 0; i < series2.getData().size(); i++) {
            s[1][i] = series2.getData().get(i).getYValue().floatValue();
        }
        for (int i = 0; i < series3.getData().size(); i++) {
            s[2][i] = series3.getData().get(i).getYValue().floatValue();
        }
        for (int i = 0; i < series4.getData().size(); i++) {
            s[3][i] = series4.getData().get(i).getYValue().floatValue();
        }
        for (int i = 0; i < series5.getData().size(); i++) {
            s[4][i] = series5.getData().get(i).getYValue().floatValue();
        }
        for (int i = 0; i < series6.getData().size(); i++) {
            s[5][i] = series6.getData().get(i).getYValue().floatValue();
        }
        Gestures g = new Gestures(textField.getText(), s[0],s[1],s[2],s[3],s[4],s[5]);
        Main.gestos.add(g);
        Main.saveGesture();
    }

    public void setGesture(Gestures g) {
        for (int i = 0; i < g.acX.length; i++) {
            dataQ1.add(g.acX[i]);
            dataQ2.add(g.acY[i]);
            dataQ3.add(g.acZ[i]);
            dataQ4.add(g.gX[i]);
            dataQ5.add(g.gY[i]);
            dataQ6.add(g.gZ[i]);

        }

        addDataToSeries();
    }
    
    
        private void testDTW() {
        float[][] s = new float[6][100];
        for (int i = 0; i < series1.getData().size(); i++) {
            s[0][i] = series1.getData().get(i).getYValue().floatValue();
        }
        for (int i = 0; i < series2.getData().size(); i++) {
            s[1][i] = series2.getData().get(i).getYValue().floatValue();
        }
        for (int i = 0; i < series3.getData().size(); i++) {
            s[2][i] = series3.getData().get(i).getYValue().floatValue();
        }
        for (int i = 0; i < series4.getData().size(); i++) {
            s[3][i] = series4.getData().get(i).getYValue().floatValue();
        }
        for (int i = 0; i < series5.getData().size(); i++) {
            s[4][i] = series5.getData().get(i).getYValue().floatValue();
        }
        for (int i = 0; i < series6.getData().size(); i++) {
            s[5][i] = series6.getData().get(i).getYValue().floatValue();
        }
        float m = 0;
        for(int i = 0;i<Main.gestos.size();i++){
            m += lDTW.compute(Main.gestos.get(i).acX, s[0]).getDistance();
            m += lDTW.compute(Main.gestos.get(i).acY, s[1]).getDistance();
            m += lDTW.compute(Main.gestos.get(i).acZ, s[2]).getDistance();
            //m += lDTW.compute(Main.gestos.get(i).gX, s[3]).getDistance();
            //m += lDTW.compute(Main.gestos.get(i).gY, s[4]).getDistance();
            //m += lDTW.compute(Main.gestos.get(i).gZ, s[5]).getDistance();
        }
        m = m/3;
            System.out.println(m);
        
    }
        
        
}
