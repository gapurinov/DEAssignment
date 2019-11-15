package sample;

import javafx.scene.chart.XYChart;

class RungeKutta extends Controller {
    static double globalError(int numberOfSteps) {
        double maxGlError = 0.0, stepLength = X - x0, y = y0, x = x0;
        stepLength /= numberOfSteps;

        while(x + stepLength <= X) {
            double K1 = f(x, y);
            double K2 = f(x + stepLength * 0.5, y + stepLength * K1 * 0.5);
            double K3 = f(x + stepLength * 0.5, y + stepLength * K2 * 0.5);
            double K4 = f(x + stepLength, y + stepLength * K3);

            double temp = (K1 + K2 * 2.0 + K3 * 2.0 + K4);
            y += stepLength / 6.0 * temp;
            x += stepLength;
            maxGlError = Math.max(maxGlError, Math.abs(exactSolution(x) - y));
        }
        return maxGlError;
    }

    static void findApproximation() {
        XYChart.Series<Number, Number> RK = new XYChart.Series<>();
        XYChart.Series<Number, Number> RKLocError = new XYChart.Series<>();
        RK.setName("Runge-Kutta Method");
        RKLocError.setName("Runge-Kutta Method");

        double zero = 0.0;
        RKLocError.getData().add(new XYChart.Data<>(x0, zero));
        RK.getData().add(new XYChart.Data<>(x0, y0));
        for (int i = 1; i <= N; i++) {
            double K1, K2, K3, K4, x, y, prvExcY, curExcY;

            double prvX = RK.getData().get(i - 1).getXValue().doubleValue();
            double prvY = RK.getData().get(i - 1).getYValue().doubleValue();
            K1 = f(prvX, prvY);
            K2 = f(prvX + h * 0.5, prvY + h * K1 * 0.5);
            K3 = f(prvX + h * 0.5, prvY + h * K2 * 0.5);
            K4 = f(prvX + h, prvY + h * K3);
            x = x0 + h * i;
            y = prvY + h / 6.0 * (K1 + K2 * 2.0 + K3 * 2.0 + K4);
            RK.getData().add(new XYChart.Data<>(x, y));
            prvExcY = exactSolution(x - h);
            curExcY = exactSolution(x);

            K1 = f(prvX, prvExcY);
            K2 = f(prvX + h * 0.5, prvExcY + h * K1 * 0.5);
            K3 = f(prvX + h * 0.5, prvExcY + h * K2 * 0.5);
            K4 = f(prvX + h, prvExcY + h * K3);
            RKLocError.getData().add(new XYChart.Data<>(x, Math.abs(curExcY - prvExcY - h * (K1 + K2 * 2.0 + K3 * 2.0 + K4) / 6.0)));
        }
        locErrorsChart.getData().add(RKLocError);
        apprxAndExcSolutionChart.getData().add(RK);
    }
}