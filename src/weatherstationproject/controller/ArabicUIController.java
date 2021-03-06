package weatherstationproject.controller;

// this program designed and implemented by Muhammed Azzab 159.
import weatherstationproject.model.ArduinoLine;
import com.fazecast.jSerialComm.SerialPort;
import com.gluonhq.charm.glisten.control.ProgressIndicator;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jssc.SerialPortList;

public class ArabicUIController implements Initializable {

    //UI
    @FXML
    private Pane header;
    @FXML
    private JFXButton connectBtn;
    @FXML
    private JFXComboBox<String> portComboBox;
    @FXML
    private JFXTextField tempretureField;
    @FXML
    private JFXTextField humField;
    @FXML
    private JFXTextField presField;
    @FXML
    private JFXTextField windSpeedField;
    @FXML
    private JFXTextField windDirectionField;
    @FXML
    private Label dateLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private ProgressIndicator tempretureIndicator;
    @FXML
    private ProgressIndicator humidityIndicator;
    @FXML
    private ProgressIndicator pressureIndicator;
    @FXML
    private ProgressIndicator wsIndicator;
    @FXML
    private ProgressIndicator wdIndicator;

    //settings panel
    @FXML
    private AnchorPane settingsPanel;
    @FXML
    private JFXComboBox tempretureUnits;
    @FXML
    private JFXComboBox windSpeedUnits;
    @FXML
    private JFXComboBox windDirectionUnits;
    @FXML
    private JFXComboBox pressureUnits;
    @FXML
    private JFXButton languageBtn;

    //for serial com
    static SerialPort chosenPort;
    ObservableList<String> portList;

    //reading data
    static String tempText;
    static String humText;
    static String presText;
    static String airSpeed;
    static String airDirection;

    //units - languages lists
    ObservableList<String> temretureList;
    ObservableList<String> pressureList;
    ObservableList<String> windDirectionList;
    ObservableList<String> windSpeedList;

    //units selection
    public int tempretureUnitSelection = 0;
    public int pressureUnitSelection = 0;
    public int windDirectionSelection = 0;
    public int windSpeedSelection = 0;

    //graph
    @FXML
    private LineChart<?, ?> lineChart;
    @FXML
    private CategoryAxis x;
    @FXML
    private NumberAxis y;

    static int n = 0;

    XYChart.Series tempSeries = new XYChart.Series();
    XYChart.Series humSeries = new XYChart.Series();
    XYChart.Series wsSeries = new XYChart.Series();
    XYChart.Series wdSeries = new XYChart.Series();
    @FXML
    private AnchorPane parent;

    private void detectPort() {
        portList = FXCollections.observableArrayList();
        String[] serialPortNames = SerialPortList.getPortNames();
        for (String name : serialPortNames) {
            portList.add(name);
            portComboBox.getItems().addAll(portList);
        }
    }

    public void updateConnectButton() {
        String connect = "??????????";
        String disconnect = "?????????? ??????????????";
        if (connectBtn.getText().equals(connect)) {
            if (portComboBox.getSelectionModel().getSelectedItem() == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("?????????? ???????????? ????????");
                alert.setHeaderText("???? ?????? ???????????? ???? ????????!");
                alert.setContentText("???? ???????? ???? ?????????????? ???????????? ?????????? ?????????? ?????????????? ???????????? ??????????????????.");
                alert.show();
            } else {
                connectBtn.setText(disconnect);
                languageBtn.setVisible(false);
            }
        } else {
            connectBtn.setText(connect);
        }
    }

    @FXML
    public void connectBtnAction() {
        updateConnectButton();
        String connect = "??????????", disconnect = "?????????? ??????????????";
        if (connectBtn.getText().equals(disconnect)) {
            chosenPort = SerialPort.getCommPort(portComboBox.getValue());
            chosenPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);

            if (chosenPort.openPort()) {
                portComboBox.setDisable(true);
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Scanner scanner = new Scanner(chosenPort.getInputStream());
                    try {
                        while (scanner.hasNextLine()) {
                            //read tempreture, humidity, air speed, air direction, distance
                            String line = scanner.nextLine();
                            System.out.println("line = " + line);
                            String[] SValues = line.split(";");
                            int len = SValues.length;
                            // Encapulate your data
                            ArduinoLine arduinoLine = new ArduinoLine();
                            if (len == 1) {
                                arduinoLine.setTempreture(Integer.parseInt(SValues[0]));
                            } else if (len == 2) {
                                arduinoLine.setTempreture(Integer.parseInt(SValues[0]));
                                arduinoLine.setHumidity(Integer.parseInt(SValues[1]));
                            } else if (len == 3) {
                                arduinoLine.setTempreture(Integer.parseInt(SValues[0]));
                                arduinoLine.setHumidity(Integer.parseInt(SValues[1]));
                                arduinoLine.setPressure(Float.parseFloat(SValues[2]));
                            } else if (len == 4) {
                                arduinoLine.setTempreture(Integer.parseInt(SValues[0]));
                                arduinoLine.setHumidity(Integer.parseInt(SValues[1]));
                                arduinoLine.setPressure(Float.parseFloat(SValues[2]));
                                arduinoLine.setAirSpeed(Double.parseDouble(SValues[3]));
                            } else if (len == 5) {
                                arduinoLine.setTempreture(Integer.parseInt(SValues[0]));
                                arduinoLine.setHumidity(Integer.parseInt(SValues[1]));
                                arduinoLine.setPressure(Float.parseFloat(SValues[2]));
                                arduinoLine.setAirSpeed(Double.parseDouble(SValues[3]));
                                arduinoLine.setAirDirection(Integer.parseInt(SValues[4]));
                            }

                            tempText = "";
                            humText = arduinoLine.getHumidity() + " %";
                            presText = "";
                            airDirection = "";
                            airSpeed = "";

                            n = n + 2;

                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    tempretureIndicator.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
                                    humidityIndicator.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
                                    wsIndicator.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
                                    wdIndicator.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
                                    pressureIndicator.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);

                                    if (tempretureUnitSelection == 0) {
                                        tempText = arduinoLine.getTempretureInCelecuis() + "??C";
                                        tempSeries.getData().add(new XYChart.Data(String.valueOf(n), arduinoLine.getTempretureInCelecuis()));
                                    } else {
                                        tempText = arduinoLine.getTempretureInFernhiet() + "??F";
                                        tempSeries.getData().add(new XYChart.Data(String.valueOf(n), arduinoLine.getTempretureInFernhiet()));
                                    }
                                    
                                    if (pressureUnitSelection == 0) {
                                        presText = arduinoLine.getPressureInBar() + " ??????";
                                    } else {
                                        presText = arduinoLine.getPressureInMB() + " ?????? ??????";
                                    }

                                    if (windSpeedSelection == 0) {
                                        airSpeed = String.format("%.2f", arduinoLine.getAirSpeedInKmPerHour()) + " ????/??";
                                        wsSeries.getData().add(new XYChart.Data(String.valueOf(n), arduinoLine.getAirSpeedInKmPerHour()));
                                    } else {
                                        airSpeed = String.format("%.4f", arduinoLine.getAirSpeedInMeterPerSecond()) + " ??/??";
                                        wsSeries.getData().add(new XYChart.Data(String.valueOf(n), arduinoLine.getAirSpeedInMeterPerSecond()));
                                    }

                                    if (windDirectionSelection == 0) {
                                        airDirection = arduinoLine.getAirDirectionInDegree() + " "
                                                + calculateDirection(arduinoLine.getAirDirectionInDegree());
                                        wdSeries.getData().add(new XYChart.Data(String.valueOf(n), arduinoLine.getAirDirectionInDegree()));
                                    } else {
                                        airDirection = String.format("%.2f", arduinoLine.getAirDirectionInRadian()) + " ????????????";
                                        wdSeries.getData().add(new XYChart.Data(String.valueOf(n), arduinoLine.getAirDirectionInRadian()));
                                    }

                                    humSeries.getData().add(new XYChart.Data(String.valueOf(n), arduinoLine.getHumidity()));

                                    tempretureField.setText(tempText);
                                    humField.setText(humText);
                                    presField.setText(presText);
                                    windDirectionField.setText(airDirection);
                                    windSpeedField.setText(airSpeed);
                                }
                            });
                        }
                        tempretureIndicator.setProgress(0.2);
                        humidityIndicator.setProgress(0.2);
                        wsIndicator.setProgress(0.2);
                        wdIndicator.setProgress(0.2);
                        pressureIndicator.setProgress(0.2);
                    } catch (NumberFormatException ex) {
                        System.out.println("Faild to connect to the station! ," + ex.getMessage());
                    }
                }
            }).start();
        } else {
            try {
                chosenPort.closePort();
            } catch (Exception ex) {
                System.out.println("not connected to any port!");
            }

            n = 0;

            tempSeries.getData().clear();
            humSeries.getData().clear();
            wsSeries.getData().clear();
            wdSeries.getData().clear();

            connectBtn.setText(connect);
            portComboBox.setDisable(false);
        }
    }

    private void setSettingsComboBoxes() {
        // ----- Tempreture ----- //
        temretureList = FXCollections.observableArrayList();
        temretureList.addAll("????????????", "??????????????");
        tempretureUnits.getItems().addAll(temretureList);

        //adding action
        tempretureUnits.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue units, Object oldValue, Object newValue) {
                if (newValue == "????????????") {
                    tempretureUnitSelection = 0;

                } else {
                    tempretureUnitSelection = 1;
                }
            }
        });

        //setting text
        if (tempretureUnitSelection == 0) {
            tempretureUnits.getSelectionModel().select("????????????");
        } else {
            tempretureUnits.getSelectionModel().select("??????????????");
        }

        // ----- Wind Speed ----- //
        windSpeedList = FXCollections.observableArrayList();
        windSpeedList.addAll("????/??", "??/??");
        windSpeedUnits.getItems().addAll(windSpeedList);

        //adding action
        windSpeedUnits.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue units, Object oldValue, Object newValue) {
                if (newValue == "????/??") {
                    windSpeedSelection = 0;
                } else {
                    windSpeedSelection = 1;
                }
            }
        });

        //setting text
        if (windSpeedSelection == 0) {
            windSpeedUnits.getSelectionModel().select("????/??");
        } else {
            windSpeedUnits.getSelectionModel().select("??/??");
        }

        // ----- Wind Direction ----- //
        windDirectionList = FXCollections.observableArrayList();
        windDirectionList.addAll("????????", "????????????");
        windDirectionUnits.getItems().addAll(windDirectionList);

        //adding action
        windDirectionUnits.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue units, Object oldValue, Object newValue) {
                if (newValue == "????????") {
                    windDirectionSelection = 0;
                } else {
                    windDirectionSelection = 1;
                }
            }
        });

        //setting text
        if (windDirectionSelection == 0) {
            windDirectionUnits.getSelectionModel().select("????????");
        } else {
            windDirectionUnits.getSelectionModel().select("????????????");
        }

        // ----- Distance ----- //
        pressureList = FXCollections.observableArrayList();
        pressureList.addAll("??????", "?????? ??????");
        pressureUnits.getItems().addAll(pressureList);

        //adding action
        pressureUnits.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue units, Object oldValue, Object newValue) {
                if (newValue == "??????") {
                    pressureUnitSelection = 0;
                } else {
                    pressureUnitSelection = 1;
                }
            }
        });

        //setting text
        if (pressureUnitSelection == 0) {
            pressureUnits.getSelectionModel().select("??????");
        } else {
            pressureUnits.getSelectionModel().select("?????? ??????");
        }
    }

    @FXML
    private void languageBtnAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/weatherstationproject/view/UI.fxml"));
            Scene scene = new Scene(root);
            Stage englishStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            englishStage.setScene(scene);
            englishStage.show();
        } catch (IOException ex) {
            System.out.println("Error" + ex.getMessage());
        }
    }

    private void initGraph() {
        x = new CategoryAxis();
        y = new NumberAxis();

        tempSeries.setName("???????? ??????????????");
        humSeries.setName("???????? ??????????????");
        wsSeries.setName("???????? ????????????");
        wdSeries.setName("?????????? ????????????");

        lineChart.getData().addAll(tempSeries, humSeries, wsSeries, wdSeries);
    }

    private void getDateAndTime() {
        final Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat day = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        SimpleDateFormat dayNum = new SimpleDateFormat("dd");
        SimpleDateFormat month = new SimpleDateFormat("MMM");
        SimpleDateFormat time = new SimpleDateFormat("HH a");
        dateLabel.setText(day.format(date) + ", " + month.format(date) + " " + dayNum.format(date));
        timeLabel.setText(time.format(date));
    }

    private String calculateDirection(int degree) {
        degree %= 360;
        int[] mainDegrees = {0, 90, 180, 270, 360};
        String[] mainDirections = {"????????", "??????", "????????", "??????", "????????"};
        String[] subDirections = {"???????? ??????", "???????? ??????", "???????? ??????", "???????? ??????"};
        for (int i = 0; i < mainDegrees.length; i++) {
            if (degree == mainDegrees[i]) {
                return mainDirections[i];
            }
        }
        for (int i = 1; i < mainDegrees.length; i++) {
            if (degree > mainDegrees[i - 1] && degree < mainDegrees[i]) {
                return subDirections[i - 1];
            }
        }
        return "Invalid Direction";
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        detectPort();
        getDateAndTime();
        setSettingsComboBoxes();
        initGraph();
    }

}
