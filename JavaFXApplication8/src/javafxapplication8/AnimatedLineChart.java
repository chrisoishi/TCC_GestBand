package javafxapplication8;

import java.io.IOException;
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
import javafx.scene.layout.FlowPane;

public class AnimatedLineChart extends Application {

    private static final int MAX_DATA_POINTS = 500;
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

    private NumberAxis xAxis;
    private NumberAxis xAxis1;

    private void init(Stage primaryStage) {

        xAxis = new NumberAxis(0, MAX_DATA_POINTS, MAX_DATA_POINTS / 10);
        xAxis.setForceZeroInRange(false);
        xAxis.setAutoRanging(false);
        xAxis.setTickLabelsVisible(false);
        xAxis.setTickMarkVisible(false);
        xAxis.setMinorTickVisible(false);

        xAxis1 = new NumberAxis(0, MAX_DATA_POINTS, MAX_DATA_POINTS / 10);
        xAxis1.setForceZeroInRange(false);
        xAxis1.setAutoRanging(false);
        xAxis1.setTickLabelsVisible(false);
        xAxis1.setTickMarkVisible(false);
        xAxis1.setMinorTickVisible(false);

        NumberAxis yAxis = new NumberAxis();
        NumberAxis yAxis1 = new NumberAxis();

        // Create a LineChart
        final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis) {
            // Override to remove symbols on each data point
            @Override
            protected void dataItemAdded(Series<Number, Number> series, int itemIndex, Data<Number, Number> item) {
            }
        };
        final LineChart<Number, Number> lineChart1 = new LineChart<Number, Number>(xAxis1, yAxis1) {
            // Override to remove symbols on each data point
            @Override
            protected void dataItemAdded(Series<Number, Number> series, int itemIndex, Data<Number, Number> item) {
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

        root.getChildren().addAll(lineChart, lineChart1);

        Scene scene = new Scene(root, 1000, 400);
        primaryStage.setScene(scene);

    }

    @Override
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
        startConnection();
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
            series1.getData().add(new XYChart.Data<>(xSeriesData++, dataQ1.remove()));
            series2.getData().add(new XYChart.Data<>(xSeriesData++, dataQ2.remove()));
            series3.getData().add(new XYChart.Data<>(xSeriesData++, dataQ3.remove()));
            series4.getData().add(new XYChart.Data<>(xSeriesData++, dataQ4.remove()));
            series5.getData().add(new XYChart.Data<>(xSeriesData++, dataQ5.remove()));
            series6.getData().add(new XYChart.Data<>(xSeriesData++, dataQ6.remove()));
        }
        // remove points to keep us at no more than MAX_DATA_POINTS
        if (series1.getData().size() > MAX_DATA_POINTS) {
            series1.getData().remove(0, series1.getData().size() - MAX_DATA_POINTS);
        }
        if (series2.getData().size() > MAX_DATA_POINTS) {
            series2.getData().remove(0, series2.getData().size() - MAX_DATA_POINTS);
        }
        if (series3.getData().size() > MAX_DATA_POINTS) {
            series3.getData().remove(0, series3.getData().size() - MAX_DATA_POINTS);
        }
        if (series4.getData().size() > MAX_DATA_POINTS) {
            series4.getData().remove(0, series4.getData().size() - MAX_DATA_POINTS);
        }
        if (series5.getData().size() > MAX_DATA_POINTS) {
            series5.getData().remove(0, series5.getData().size() - MAX_DATA_POINTS);
        }
        if (series6.getData().size() > MAX_DATA_POINTS) {
            series6.getData().remove(0, series6.getData().size() - MAX_DATA_POINTS);
        }
        // update
        xAxis.setLowerBound(xSeriesData - MAX_DATA_POINTS);
        xAxis.setUpperBound(xSeriesData - 1);
        xAxis1.setLowerBound(xSeriesData - MAX_DATA_POINTS);
        xAxis1.setUpperBound(xSeriesData - 1);
    }

    private void startConnection() throws IOException {
        ServerInSocket.Actions actionsReceive = new ServerInSocket.Actions() {
            public void run(char c) {

                if (c == '|') {
                    s2 = s.split(":");
                    if (s2[0].equals("A")) {
                        dataQ1.add(Integer.valueOf(s2[1]));
                        dataQ2.add(Integer.valueOf(s2[2]));
                        dataQ3.add(Integer.valueOf(s2[3]));
                    } else {
                        dataQ4.add(Integer.valueOf(s2[1]));
                        dataQ5.add(Integer.valueOf(s2[2]));
                        dataQ6.add(Integer.valueOf(s2[3]));
                    }
                    s = "";
                } else {

                    s = s + Character.toString(c);
                }
            }
        };
        ServerInSocket.Start(actionsReceive);
    }
}
