package core;

import java.util.ArrayList;

public class Model {
    private ArrayList<Element> elements;
    private double tCurr = 0;
    private double tNext = Double.MAX_VALUE;

    public Model() {
        elements = new ArrayList<>();
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
            for (Element element : elements) {
                if (element.getTNext() == tCurr) {
                    element.outAct();
                }
            }
        }
    }
}
