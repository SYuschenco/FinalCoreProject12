package ua.goit.gauswithhgui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller <N extends Number, T extends Gauss<N, T>> implements Initializable {
    static int columnInGrid = 0;
    static int rowInGrid = 0;
    static int defaultEquationsNumber;
    static int equationInInputtedList = 0;
    static Integer systemSize;
    // "Random" or "Console" or "GUI" way of running program
    static String waysToEnterInformationModifier = "GUI";
    private Stage mainStage;
    private Stage showResultStage;
    private Scene showResultScene;
    private Pane showResultPane;
    private List<TextField> matrix;
    private Float[] x;
    public static List<Float> tempArrayCoefficients;
    public static LinearSystem<Float, MyEquation> list;

    @FXML
    public Button btnShowInputMatrixOnGui;
    @FXML
    public Button btnGetResult;
    @FXML
    public AnchorPane matrixPaneInInputMethod;
    @FXML
    public AnchorPane inputMatrix;
    @FXML
    private TextField fieldForInputSystemSizeOnGui;
    @FXML
    private Label lblInputMatrix;
    @FXML
    private Label lblCheckInput;
    @FXML
    private void initialize() {}

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public void btnGetResultAction(ActionEvent event) throws IOException, NumberFormatException {
        Controller.equationInInputtedList = 0;
        //chekInputCoefficientsInGUI();
        for (TextField cells : matrix) {
            if (cells.getText().isEmpty()) {
                cells.setText("0");
                //System.out.println("empty cell id - " + cells.getId());
            } else {
                char[] chars = cells.getText().toCharArray();
                for (int i = 0; i < chars.length; i++) {
                    if ((chars[i]==' ')) {
                        lblCheckInput.setVisible(true);
                        chars[i]=0;
                        cells.setText("0");
                        cells.requestFocus();
                        cells.setText(cells.getText().replaceAll("[^0-9.-]", ""));
                        cells.setText(cells.getText().replaceAll(" +", ""));

                    //System.out.println("char - " + chars[i]);
                    }
                }
            }
        }
        for (TextField cells : matrix) {
                //System.out.println("2empty cell id - " + cells.getId());
            if (cells.getText().isEmpty()) {
                System.out.println("cell text - " + cells.getText());
                lblCheckInput.setVisible(true);
                //JOptionPane.showMessageDialog(null, "Input all data!");
                cells.requestFocus();
                return;
            }else {
                inputCoefficientsInGui();
                general();
                showResultsOnGui();
                mainStage.show();
            }
            return;
        }

//        chekInputCoefficientsInGUI();
    }

    public void general() {
        defaultEquationsNumber = (Integer.parseInt(fieldForInputSystemSizeOnGui.getText()));
        //System.out.println("defaultEquationsNumber= " + defaultEquationsNumber);
        generateSystem(Integer.parseInt(fieldForInputSystemSizeOnGui.getText()), waysToEnterInformationModifier);
        printSystem(list);
        int i, j;
        Algorithm<Float, MyEquation> alg = new Algorithm<>(list);
        try {
            alg.calculate();
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        } catch (ArithmeticException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
        x = new Float[defaultEquationsNumber];
        for (i = list.size() - 1; i >= 0; i--) {
            Float sum = 0.0f;
            for (j = list.size() - 1; j > i; j--) {
                sum += list.itemAt(i, j) * x[j];
            }
            x[i] = (list.itemAt(i, list.size()) - sum) / list.itemAt(i, j);
        }
        printSystem(list);
        printVector(x);
    }

    public static LinearSystem<Float, MyEquation> generateSystem(int defaultEquationsNumber, String methodForGeneratingSystemCoefficients) {
        list = new LinearSystem<>();
        for (int i = 0; i < defaultEquationsNumber; i++) {
            MyEquation eq = new MyEquation();
            eq.generate(defaultEquationsNumber + 1, methodForGeneratingSystemCoefficients);
            list.push(eq);
        }
        return list;
    }

    public static void printSystem(LinearSystem<Float, MyEquation> system) {
        for (int i = 0; i < system.size(); i++) {
            MyEquation temp = system.get(i);
            String s = "";
            for (int j = 0; j < temp.size(); j++) {
                s += String.format("%f; %s", system.itemAt(i, j), "\t");
            }
            System.out.println(s);
        }
        System.out.println("");
    }

    public static void printVector(Float[] x) {
        String s = "";
        for (int i = 0; i < x.length; i++) {
            s += String.format("x%d = %f; ", i, x[i]);
        }
        System.out.println(s);
    }

    private boolean chekInputCoefficientsInGUI() {
        for (TextField cells : matrix) {
            if (cells.getText().isEmpty()) {
                lblCheckInput.setVisible(true);
                JOptionPane.showMessageDialog(null, "Input all data!");
                return true;
            }
        }
        return false;
    }

    private void isDigit(String s) throws NumberFormatException {
        try {
            Float.parseFloat(s);

            lblCheckInput.setVisible(false);
        } catch (NumberFormatException e) {
            lblCheckInput.setVisible(true);
        }
    }

    private void inputCoefficientsInGui() {
        systemSize = Integer.valueOf(fieldForInputSystemSizeOnGui.getText());
        int systemSizeInNumber = systemSize;
        int equationSize = systemSizeInNumber + 1;
        tempArrayCoefficients = new ArrayList<>();
        tempArrayCoefficients.clear();
        List<Float> equation = new ArrayList<>();
        List<T> list = new ArrayList<>();

        for (TextField textField : matrix) {
            tempArrayCoefficients.add(Float.parseFloat(textField.getText()));
        }
        //System.out.println("tempArrayCoefficients.size()= " + tempArrayCoefficients.size());

    }


    public void btnShowInputMatrixOnGuiAction(ActionEvent event) throws IOException {
        if (fieldForInputSystemSizeOnGui.getText().isEmpty() == false) {
            lblInputMatrix.setVisible(true);
            //fieldForInputSystemSizeOnGui.setText("3");
            matrixPaneInInputMethod.getChildren().clear();
            mainStage.show();
            matrixPaneInInputMethod.requestFocus();
            systemSize = Integer.valueOf(fieldForInputSystemSizeOnGui.getText());
            int systemSizeInNumber = systemSize;
            matrix = new ArrayList<>();
            for (int i = 0; i < systemSizeInNumber; i++)
                for (int j = 0; j < systemSizeInNumber + 1; j++) {
                    String index = "" + (i + 1) + "," + (j + 1);
                    TextField cells = new TextField();
                    cells.focusedProperty().addListener(new ChangeListener<Boolean>() {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                            if (newPropertyValue) {
                                //System.out.println("Textfield on focus");
                            } else {
                                if ((cells.getText() != null && !cells.getText().isEmpty())) {
                                    String cellsNumber = cells.getText();
                                    cellsNumber.toCharArray();
                                    for (char c : cellsNumber.toCharArray()) {
                                        if ((cells.getText() != null && !cells.getText().isEmpty())) {
                                            if (Character.isAlphabetic(c)) {
                                                cells.requestFocus();
                                                lblCheckInput.setVisible(true);
                                                cells.setText(cellsNumber.replaceAll("[^0-9.-]", ""));
                                                cells.setText(cellsNumber.replaceAll(" +", ""));
                                            } else {
                                                lblCheckInput.setVisible(false);
                                            }
                                        }
                                    }
                                    //lblCheckInput.setVisible(true);
                                    //System.out.println("tab--" + cells.getId() + "=" + cellsNumber);
                                }
                            }
                        }
                    });
                cells.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        if ((cells.getText() != null && !cells.getText().isEmpty())) {
                            String cellsNumber = cells.getText();
                            cellsNumber.toCharArray();
                            for (char c : cellsNumber.toCharArray()) {
                                if ((cells.getText() != null && !cells.getText().isEmpty())) {
                                    if (Character.isAlphabetic(c)) {
                                        cells.requestFocus();
                                        lblCheckInput.setVisible(true);
                                        cells.setText(cellsNumber.replaceAll("[^0-9.-]", ""));
                                        cells.setText(cellsNumber.replaceAll(" +", ""));
                                    } else {
                                        lblCheckInput.setVisible(false);
                                    }
                                }
                            }
                            //System.out.println("ENT--" + cells.getId() + "=" + cellsNumber);
                        }
                    }
                });
//                    cells.setOnKeyPressed(new EventHandler<KeyEvent>() {
////                        @Override
////                        public void handle(KeyEvent ke) {
////                            if (ke.getCode().equals(KeyCode.ENTER)) {
////                                    if ((cells.getText() != null && !cells.getText().isEmpty())) {
////                                        String cellsNumber = cells.getText();
////                                        cellsNumber.toCharArray();
////                                        for (char c : cellsNumber.toCharArray()) {
////                                            if ((cells.getText() != null && !cells.getText().isEmpty())) {
////                                                if (Character.isAlphabetic(c)) {
////                                                    cells.requestFocus();
////                                                    lblCheckInput.setVisible(true);
////                                                    cells.setText(cellsNumber.replaceAll("[^0-9.-]", ""));
////                                                    cells.setText(cellsNumber.replaceAll(" +", ""));
////                                                } else {
////                                                    lblCheckInput.setVisible(false);
////                                                }
////                                            }
////                                        }
////                                        //lblCheckInput.setVisible(true);
////                                        System.out.println("ENT--" + cells.getId() + "=" + cellsNumber);
////                                    }
////                            }
////                        }
////                    });
                    cells.setPromptText(index);
                    cells.setId(index);
                    //cells.setText(""+i+j);
                    matrix.add(cells);
                }
            GridPane grid = new GridPane();
            grid.setVgap(0);
            grid.setHgap(0);
            grid.setMaxWidth(700);
            grid.setMaxHeight(200);

            int k = 0;
            for (int i = 1; i < systemSizeInNumber + 1; i++) {
                for (int j = 1; j < (systemSizeInNumber + 2); j++) {
                    k = k + 1;
                    GridPane.setConstraints(matrix.get(k - 1), j, i);
                    grid.getChildren().add(matrix.get(k - 1));
                }
            }
//        System.out.println("\nPrinted cells--" + k);
            matrixPaneInInputMethod.getChildren().add(grid);
            mainStage.show();
        }else {
            JOptionPane.showMessageDialog(null, "Input system size!");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void onEnterActionInFieldForInputSystemSizeOnGui(ActionEvent event) {
        String cellsNumber = fieldForInputSystemSizeOnGui.getText();
        fieldForInputSystemSizeOnGui.focusedProperty().addListener(new ChangeListener<Boolean>(){
        @Override
        public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue){
            if (newPropertyValue)
            {
                //System.out.println("Textfield on focus");
            }
            else {
                if ((fieldForInputSystemSizeOnGui.getText() != null && !fieldForInputSystemSizeOnGui.getText().isEmpty())) {
                    String cellsNumber = fieldForInputSystemSizeOnGui.getText();
                    cellsNumber.toCharArray();
                    for (char c : cellsNumber.toCharArray()) {
                            if (Character.isAlphabetic(c)) {
                                //fieldForInputSystemSizeOnGui.requestFocus();
                                lblCheckInput.setVisible(true);
                                fieldForInputSystemSizeOnGui.setText(cellsNumber.replaceAll("[^0-9]", ""));
                                fieldForInputSystemSizeOnGui.setText(cellsNumber.replaceAll(" +", ""));
                                //System.out.println("INVALID");
                                //break;
                            } else {
                                lblCheckInput.setVisible(false);
                                btnShowInputMatrixOnGui.requestFocus();
                            }
                    }
                    //lblCheckInput.setVisible(true);
                    //System.out.println(fieldForInputSystemSizeOnGui.getId() + "=" + cellsNumber);
                }
            }
        }
    });

        if ((fieldForInputSystemSizeOnGui.getText() != null && !fieldForInputSystemSizeOnGui.getText().isEmpty())) {
            fieldForInputSystemSizeOnGui.requestFocus();
            fieldForInputSystemSizeOnGui.setText(cellsNumber.replaceAll("[^0-9]",""));
            btnShowInputMatrixOnGui.requestFocus();
        }else {

            lblCheckInput.setVisible(true);

        }

    }

    public void printResultMatrixOnGui(LinearSystem<Float, MyEquation> system) {
        //System.out.println("columnInGrid="+columnInGrid+ "|rowInGrid=" +rowInGrid);
        showResultStage = new Stage();
        showResultPane = new Pane();
        GridPane grid = new GridPane();
        AnchorPane anchorPane = new AnchorPane();
        grid.setVgap(0);
        grid.setHgap(0);
        grid.setMaxWidth(400);
        grid.setMaxHeight(200);
        for (int i = 0; i < system.size(); i++) {
            MyEquation temp = system.get(i);
            for (int j = 0; j < temp.size(); j++) {
                columnInGrid++;
                TextField cells = new TextField();
                cells.setText(String.valueOf(system.itemAt(i, j)));
                grid.getColumnConstraints().add(new ColumnConstraints(100));
                GridPane.setConstraints(cells, j, i);
                grid.getChildren().add(cells);
            }
        }
        rowInGrid++;


        showResultPane.getChildren().addAll(anchorPane,grid);
        showResultScene = new Scene(showResultPane, 400, 200);
        showResultStage.setScene(showResultScene);
        showResultStage.setTitle("Result of solving a system of linear equations by Gauss. ");
        showResultStage.setMinHeight(250);
        showResultStage.setMinWidth(850);
        showResultStage.setX(550);
        showResultStage.setY(650);
        showResultStage.show();
    }

    public void printResultXOnGui(Float[] x) {
        GridPane grid = new GridPane();
            columnInGrid++;
        Label lblX = new Label("                                                   ");
        GridPane.setConstraints(lblX, columnInGrid, rowInGrid);
        grid.getChildren().add(lblX);
        for (int i = 0; i < x.length; i++) {
            rowInGrid++;
            Label lbl = new Label("x" + i + "= ");
            TextField cells = new TextField();
            cells.setText(String.valueOf(x[i]));
            grid.getColumnConstraints().add(new ColumnConstraints(100));
            GridPane.setConstraints(cells, columnInGrid+2, i);
            GridPane.setConstraints(lbl, columnInGrid+1, i);
            grid.getChildren().addAll(lbl,cells);
        }

        showResultPane.getChildren().add(grid);
        showResultScene.setRoot(showResultPane);
        showResultStage.setScene(showResultScene);
        showResultStage.show();
    }

    public void showResultsOnGui() {
        printResultMatrixOnGui(list);
        printResultXOnGui(x);
    }
}
