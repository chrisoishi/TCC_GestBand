package APP;

import Controllers.DTWController;
import Controllers.GestureController;
import Controllers.MainController;
import java.io.IOException;
import static java.lang.Thread.sleep;

import javafx.animation.AnimationTimer;
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
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;

public class Graph {

    private static final int MAX_DATA_POINTS = 200;
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
    private GridPane root = new GridPane();
    private String s = "";
    private String[] s2;
    private float[] s_saved = new float[100];
    private float[] s_test = new float[100];

    private NumberAxis xAxis;
    private NumberAxis xAxis1;

    Button btnStart = new Button("Iniciar");
    Label instruction = new Label();
    Button btnSave = new Button();
    Button btnClear = new Button();
    Button btnTest = new Button();
    Button btnRead = new Button();
    boolean stop = false;
    boolean test = false;
    boolean clear = false;

    private Gestures gestureValidate;
    private int mediaValidation;

    TextField txtName = new TextField();
    TextField textField2 = new TextField();

    TextField txtBegin = new TextField();
    TextField txtEnd = new TextField();

    private void init(Stage primaryStage, boolean isView) {

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

        btnStart.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                mediaValidation = 100;
                clear();
                stop = true;
                instruction.setText("Posicione seu braço na posição inicial...");
                Task t = new Task() {
                    @Override
                    protected Object call() throws Exception, InterruptedException {
                        while (!DTWController.GB_INITIAL) {
                            sleep(100);
                        }
                        return true;
                    }

                    @Override
                    protected void succeeded() {
                        stop = false;
                        instruction.setText("Agora faça seu gesto...");
                        Task t = new Task() {
                            @Override
                            protected Object call() throws Exception, InterruptedException {
                                while (!DTWController.GB_MOVE) {
                                    sleep(100);
                                }
                                while (DTWController.GB_MOVE) {
                                    sleep(100);
                                }
                                return true;
                            }

                            @Override
                            protected void succeeded() {
                                stop = true;

                                txtBegin.setText("0");
                                txtEnd.setText(Integer.toString(series1.getData().size() - 1));
                                while (mediaValidation > 70) {
                                    instruction.setText("Posicione seu braço novamente na posição inicial");
                                    gestureValidate = getGesture();
                                    Task t = new Task() {
                                        @Override
                                        protected Object call() throws Exception, InterruptedException {
                                            while (!DTWController.GB_INITIAL) {
                                                sleep(100);
                                            }
                                            return true;
                                        }

                                        @Override
                                        protected void succeeded() {
                                            stop = false;
                                            instruction.setText("Repita seu gesto");
                                            Task t = new Task() {
                                                @Override
                                                protected Object call() throws Exception, InterruptedException {
                                                    while (!DTWController.GB_MOVE) {
                                                        sleep(100);
                                                    }
                                                    while (DTWController.GB_MOVE) {
                                                        sleep(100);
                                                    }
                                                    return true;
                                                }

                                                @Override
                                                protected void succeeded() {
                                                    stop = true;

                                                    mediaValidation = DTWController.compare(gestureValidate, getGesture());
                                                    if (mediaValidation > 70) {
                                                        instruction.setText("Erro: " + Integer.toString(mediaValidation) + " - Máximo: 70");
                                                    }
                                                    try {
                                                        sleep(3000);
                                                    } catch (InterruptedException ex) {
                                                        Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
                                                    }
                                                }
                                            };
                                            new Thread(t).start();
                                        }
                                    };
                                    new Thread(t).start();
                                }
                            }
                        };
                        new Thread(t).start();
                    }
                };
                new Thread(t).start();
            }
        });

        btnSave.setText("Salvar gesto");
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

                    clear();

                }
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
        lineChart.setTitle("Acelerometro");
        lineChart.setHorizontalGridLinesVisible(true);

        lineChart1.setAnimated(false);
        lineChart1.setTitle("Giroscópio");
        lineChart1.setHorizontalGridLinesVisible(true);

        // Set Name for Series
        series1.setName("A-X");
        series2.setName("A-Y");
        series3.setName("A-Z");
        series4.setName("G-X");
        series5.setName("G-Y");
        series6.setName("G-Z");

        // Add Chart Series
        lineChart.getData().addAll(series1, series2, series3);
        lineChart1.getData().addAll(series4, series5, series6);
        root.getStylesheets().add("lib/style.css");

        AnchorPane.setTopAnchor(root, (double) 0);
        AnchorPane.setLeftAnchor(root, (double) 0);
        AnchorPane.setRightAnchor(root, (double) 0);

        root.add(lineChart, 0, 0);
        root.add(lineChart1, 1, 0);
        GridPane new_gesture_pane = new GridPane();
        new_gesture_pane.getStyleClass().add("grid-pane-content");
        new_gesture_pane.setStyle("-fx-padding:10");
        Scene scene;
        if (!isView) {
            root.add(new_gesture_pane, 0, 2, 2, 1);
            instruction.setAlignment(Pos.CENTER);
            instruction.setStyle("-fx-font-size: 20pt;");
            instruction.setText("...");
            instruction.setPrefWidth(1000);
            root.add(instruction, 0, 3, 2, 1);
            new_gesture_pane.add(lHeader("Gesto"), 0, 0, 2, 1);

            new_gesture_pane.add(new Label("Nome do Gesto"), 0, 1);
            new_gesture_pane.add(txtName, 1, 1);

            new_gesture_pane.add(lHeader("Gravar"), 3, 0, 2, 1);
            btnStart.setPrefWidth(250);
            new_gesture_pane.add(btnStart, 3, 1);

            new_gesture_pane.add(lHeader("Intervalo de dados"), 5, 0, 2, 1);

            new_gesture_pane.add(new Label("Inicio"), 5, 1);
            new_gesture_pane.add(txtBegin, 6, 1);

            new_gesture_pane.add(new Label("Fim"), 5, 2);
            new_gesture_pane.add(txtEnd, 6, 2);
            btnSave.setPrefWidth(300);
            new_gesture_pane.add(btnSave, 5, 3, 2, 1);
            scene = new Scene(root, 1000, 700);
        } else {
            scene = new Scene(root, 1000, 300);
        }
        //root.getChildren().addAll(btnStop, btnSave, btnClear, btnTest, btnRead, textField, txtBegin, txtEnd);

        primaryStage.setScene(scene);

    }

    public void start(Stage stage, String title, boolean isView) throws IOException {

        stage.setTitle(title);
        init(stage, isView);
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
                try {
                    addDataToSeries();
                } catch (Exception ex) {
                    Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }.start();
    }

    private void addDataToSeries() throws Exception {
        if (clear) {
            series1.getData().clear();
            series2.getData().clear();
            series3.getData().clear();
            series4.getData().clear();
            series5.getData().clear();
            series6.getData().clear();
            xSeriesData = 0;
            clear = false;
        }
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

    public void print(int type, String[] s) {
        if (DTWController.GB_MOVE & !stop) {
            if (type == 0) {
                dataQ1.add(Integer.valueOf(s[1]));
                dataQ2.add(Integer.valueOf(s[2]));
                dataQ3.add(Integer.valueOf(s[3]));
            } else {
                dataQ4.add(Integer.valueOf(s[1]));
                dataQ5.add(Integer.valueOf(s[2]));
                dataQ6.add(Integer.valueOf(s[3]));
            }

        }
    }

    void start(Scene scene2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void saveGesture() throws IOException {
        if (!txtName.getText().equals("")) {
            Gestures g = getGesture();
            g.name = txtName.getText();
            GestureController.gestos.add(g);
            GestureController.saveGesture();
            instruction.setText("Seu gesto foi salvo!!!");
            clear();
            txtName.setText("");
        } else {
            instruction.setText("Digite um nome para seu novo gesto!");
        }
    }

    private Gestures getGesture() {
        int end = Integer.parseInt(txtEnd.getText());
        int begin = Integer.parseInt(txtBegin.getText());
        int size = end - begin;
        float[][] s = new float[6][size];
        for (int i = begin; i < end; i++) {
            s[0][i - begin] = series1.getData().get(i).getYValue().floatValue();
            s[1][i - begin] = series2.getData().get(i).getYValue().floatValue();
            s[2][i - begin] = series3.getData().get(i).getYValue().floatValue();
            s[3][i - begin] = series4.getData().get(i).getYValue().floatValue();
            s[4][i - begin] = series5.getData().get(i).getYValue().floatValue();
            s[5][i - begin] = series6.getData().get(i).getYValue().floatValue();
        }
        return new Gestures("", size, s[0], s[1], s[2], s[3], s[4], s[5]);
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

        try {
            addDataToSeries();
        } catch (Exception ex) {
            Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private float[][] getData(int size) {
        float[][] s = new float[6][size];
        int size_serie = series1.getData().size() - size;
        if (size_serie < 0) {
            size_serie = 0;
        }
        for (int i = 0; i < size - 2; i++) {
            if ((size_serie + i) < MAX_DATA_POINTS) {
                s[0][i] = series1.getData().get(size_serie + i).getYValue().floatValue();
                s[1][i] = series2.getData().get(size_serie + i).getYValue().floatValue();
                s[2][i] = series3.getData().get(size_serie + i).getYValue().floatValue();
                s[3][i] = series4.getData().get(size_serie + i).getYValue().floatValue();
                s[4][i] = series5.getData().get(size_serie + i).getYValue().floatValue();
                s[5][i] = series6.getData().get(size_serie + i).getYValue().floatValue();
            } else {
                //System.out.println("erros");
            }
        }
        return s;
    }

    private void clear() {
        clear = true;

    }

    private Label lHeader(String text) {
        Label l = new Label();
        l.setText(text);
        l.getStyleClass().add("header");
        return l;
    }
}
