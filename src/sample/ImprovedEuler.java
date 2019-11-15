package sample;

import javafx.scene.chart.XYChart;

class ImprovedEuler extends Controller {


    static double globalError(int cntSteps){

        double stepLength = X - x0, maxGlError = 0.0, y = y0, x = x0;
        stepLength /= cntSteps;
        while(x + stepLength <= X){
            double K1 = f(x, y);
            double K2 = f(x + stepLength, y + stepLength * K1);
            y += stepLength * 0.5 * (K1 + K2);
            x += stepLength;
            maxGlError = Math.max(maxGlError, Math.abs(exactSolution(x) - y));
        }
        return maxGlError;
    }

    static void findApproximation(){
        XYChart.Series<Number, Number> impEuler;
        impEuler = new XYChart.Series<>();
        XYChart.Series<Number, Number> impEulerLocError;
        impEulerLocError = new XYChart.Series<>();

        impEuler.getData().add(new XYChart.Data<>(x0, y0));
        impEulerLocError.getData().add(new XYChart.Data<>(x0, 0.0));

        for (int i = 1; i <= N; ++i){
            double K1, K2, prvX, prvY, x, y, prvExcY, curExcY;

            prvX = impEuler.getData().get(i - 1).getXValue().doubleValue();
            prvY = impEuler.getData().get(i - 1).getYValue().doubleValue();
            K1 = f(prvX, prvY);
            K2 = f(prvX + h, prvY + h * K1);
            x = x0 + h * i;
            y = prvY + h * 0.5 * (K1 + K2);
            impEuler.getData().add(new XYChart.Data<>(x, y));
            prvExcY = exactSolution(x - h);
            curExcY = exactSolution(x);
            K1 = f(prvX, prvExcY);
            K2 = f(prvX + h, prvExcY + h * K1);
            impEulerLocError.getData().add(new XYChart.Data<>(x, Math.abs(curExcY - prvExcY - h * 0.5 * (K1 + K2))));
        }
        impEuler.setName("Improved Euler Method");
        impEulerLocError.setName("Improved Euler Method");

        locErrorsChart.getData().add(impEulerLocError);
        apprxAndExcSolutionChart.getData().add(impEuler);
    }
}