package neeedo.imimaprx.htw.de.neeedo.entities;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(name = "price")
public class Price implements Serializable {

    @Element
    private double min;

    @Element
    private double max;

    public Price() {

    }

    public Price(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    @Override
    public String toString() {
        return "Price{" +
                "min=" + min +
                ", max=" + max +
                '}';
    }
}
