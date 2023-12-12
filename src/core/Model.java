package core;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private ArrayList<Element> elements;
    private double tCurr = 0;
    private double tNext = Double.MAX_VALUE;

    public Model() {
        elements = new ArrayList<>();
    }

    public Model(List<Element> elements) {
        this.elements = new ArrayList<>(elements);
    }

    public void simulate(double time) {
        while (tCurr < time) {
            tNext = Double.MAX_VALUE;
            for (Element element : elements) {
                if (element.getTNext() < tNext) {
                    tNext = element.getTNext();
                }
            }
            for (Element element : elements) {
                element.doStatistics(tNext - tCurr);
            }
            tCurr = tNext;
            for (var element : elements) {
                element.setTCurr(tCurr);
            }

            for (Element element : elements) {
                if (element.getTNext() == tCurr) {
                    element.outAct();
                }
            }
        }
    }

    public void printResults() {
        for (Element element : elements) {
            element.printResult();
        }
    }
}
