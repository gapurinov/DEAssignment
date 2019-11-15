package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


public class Controller {

    static double x0, y0, X;
    static int N;
    private static int stepStart, stepFinish;
    static double h;

    @FXML TextField x0Prompt, y0Prompt, XPrompt, NPrompt, stepStartPrompt, stepFinishPrompt;
    @FXML Button draw;

    private static NumberAxis locErrorsAxisX;

    private static NumberAxis locErrorsAxisY;
    private static NumberAxis ErrorsAxisXTotal;
    private static NumberAxis ErrorsAxisYTotal;
    private static NumberAxis apprxAndExcSolutionAxisX;
    private static NumberAxis apprxAndExcSolutionAxisY;

    static { locErrorsAxisX = new NumberAxis(); }
    static { locErrorsAxisY = new NumberAxis(); }
    static { ErrorsAxisXTotal = new NumberAxis(); }
    static { ErrorsAxisYTotal = new NumberAxis(); }
    static { apprxAndExcSolutionAxisX = new NumberAxis(); }
    static { apprxAndExcSolutionAxisY = new NumberAxis(); }

    private static LineChart<Number, Number> ttlErrorsChart;
    static LineChart<Number, Number> apprxAndExcSolutionChart;
    static LineChart<Number, Number> locErrorsChart;
    static { ttlErrorsChart = new LineChart<>(ErrorsAxisXTotal, ErrorsAxisYTotal); }
    static { apprxAndExcSolutionChart = new LineChart<>(apprxAndExcSolutionAxisX, apprxAndExcSolutionAxisY); }
    static { locErrorsChart = new LineChart<>(locErrorsAxisX, locErrorsAxisY); }

    static double exactSolution(double x) {
        double ans = (Math.log(x/2) * Math.log(x)) / Math.log(2);
        return ans;
    }
    static double f(double xCoor, double yCoor){
        double ans = (1.0 / xCoor) + (2.0 * yCoor) / (xCoor * Math.log(xCoor));
        return ans;
    }

    private static void settingLabelsAndTitles(){
        ttlErrorsChart.setTitle("Total Errors");
        locErrorsChart.setTitle("Local Errors");
        apprxAndExcSolutionChart.setTitle("Approximations");
        locErrorsAxisX.setLabel("x");
        locErrorsAxisY.setLabel("y");
        apprxAndExcSolutionAxisX.setLabel("x");
        apprxAndExcSolutionAxisY.setLabel("y");
        ErrorsAxisXTotal.setLabel("number of steps");
        ErrorsAxisYTotal.setLabel("maximum global error");
    }

    private static void clearPrevGraph(){
        apprxAndExcSolutionChart.getData().clear();
        locErrorsChart.getData().clear();
        ttlErrorsChart.getData().clear();
    }

    private static void readPrompts(TextField stepStartPrompt, TextField stepFinishPrompt,
                                    TextField x0Prompt, TextField y0Prompt, TextField NPrompt, TextField XPrompt){
        stepStart = Integer.parseInt(stepStartPrompt.getText());
        stepFinish = Integer.parseInt(stepFinishPrompt.getText());
        x0 = Double.parseDouble(x0Prompt.getText());
        y0 = Double.parseDouble(y0Prompt.getText());
        N = Integer.parseInt(NPrompt.getText());
        X = Double.parseDouble(XPrompt.getText());
    }

    private static void calcJump(){
        double temp = X - x0;
        h = temp / N;
    }


    private static void drawExact(){

        XYChart.Series<Number, Number> exact = new XYChart.Series<>();

        for (int i = 0; i <= N; ++i){

            double x = x0 + h * i;
            double y = exactSolution(x);

            exact.getData().add(new XYChart.Data<>(x, y));
        }
        exact.setName("Exact");
        apprxAndExcSolutionChart.getData().add(exact);
    }

    private static void drawApprxAndLocErrors(){
        Euler.findApproximation();
        ImprovedEuler.findApproximation();
        RungeKutta.findApproximation();
    }

    private static void drawTtlErrors(){
        XYChart.Series<Number, Number> rungeKuttaTotalErrors = new XYChart.Series<>();
        XYChart.Series<Number, Number> improvedEulerTotalErrors = new XYChart.Series<>();
        XYChart.Series<Number, Number> eulerTotalErrors = new XYChart.Series<>();
        rungeKuttaTotalErrors.setName("Runge Kutta Method");
        improvedEulerTotalErrors.setName("Improved Euler Method");
        eulerTotalErrors.setName("Euler Method");

        for (int i = stepStart; i <= stepFinish; i++){
            rungeKuttaTotalErrors.getData().add(new XYChart.Data<>(i, RungeKutta.globalError(i)));
            improvedEulerTotalErrors.getData().add(new XYChart.Data<>(i, ImprovedEuler.globalError(i)));
            eulerTotalErrors.getData().add(new XYChart.Data<>(i, Euler.globalError(i)));
        }
        ttlErrorsChart.getData().add(eulerTotalErrors);
        ttlErrorsChart.getData().add(improvedEulerTotalErrors);
        ttlErrorsChart.getData().add(rungeKuttaTotalErrors);
    }

    private static void deleteDots(){
        ttlErrorsChart.setCreateSymbols(false);
        locErrorsChart.setCreateSymbols(false);
        apprxAndExcSolutionChart.setCreateSymbols(false);
    }

    private void drawAndOrganiseLayout() {
        drawExact();
        drawApprxAndLocErrors();
        drawTtlErrors();

        Stage window;
        window = new Stage();
        HBox hbox;
        hbox = new HBox();

        hbox.getChildren().add(apprxAndExcSolutionChart);
        hbox.getChildren().add(ttlErrorsChart);
        hbox.getChildren().add(locErrorsChart);

        Scene scene;
        scene = new Scene(hbox, 1600, 800);
        window.setScene(scene);
        window.show();
    }
    public void plot(ActionEvent actionEvent) {
        settingLabelsAndTitles();
        clearPrevGraph();
        readPrompts(stepStartPrompt, stepFinishPrompt, x0Prompt, y0Prompt, NPrompt, XPrompt);
        calcJump();
        deleteDots();
        drawAndOrganiseLayout();
    }
}