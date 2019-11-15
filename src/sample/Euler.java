package sample;

import javafx.scene.chart.XYChart;

class Euler extends Controller {

    static double globalError(int cntSteps){

        double stepSize = (X - x0) / cntSteps;
        double mxGlobalError = 0.0, y = y0, x = x0;

        while(x + stepSize <= X){
            y += stepSize * f(x, y);
            x += stepSize;
            mxGlobalError = Math.max(Math.abs(exactSolution(x) - y), mxGlobalError);
        }
        return mxGlobalError;
    }

    static void findApproximation(){

        XYChart.Series<Number, Number> euler = new XYChart.Series<>();
        XYChart.Series<Number, Number> eulerLocalError = new XYChart.Series<>();

        euler.getData().add(new XYChart.Data<>(x0, y0));
        eulerLocalError.getData().add(new XYChart.Data<>(x0, 0.0));

        for (int i = 1; i <= N; ++i){
            double prvX, prvY, x, y, prvExcY, curExcY;
            prvX = euler.getData().get(i - 1).getXValue().doubleValue();
            prvY = euler.getData().get(i - 1).getYValue().doubleValue();
            x = x0 + h * i;
            y = prvY + h * f(prvX, prvY);
            euler.getData().add(new XYChart.Data<>(x, y));
            prvExcY = exactSolution(x - h);
            curExcY = exactSolution(x);
            eulerLocalError.getData().add(new XYChart.Data<>(x, Math.abs(curExcY - prvExcY - h * f(x - h, prvExcY))));
        }
        euler.setName("Euler Method");
        eulerLocalError.setName("Euler Method");

        locErrorsChart.getData().add(eulerLocalError);
        apprxAndExcSolutionChart.getData().add(euler);
    }

}