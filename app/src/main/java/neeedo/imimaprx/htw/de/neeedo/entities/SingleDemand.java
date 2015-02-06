package neeedo.imimaprx.htw.de.neeedo.entities;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(name = "demand")
public class SingleDemand implements Serializable {

    @Element
    private Demand demand;

    public Demand getDemand() {
        return demand;
    }

    public void setDemand(Demand demand) {
        this.demand = demand;
    }

    @Override
    public String toString() {
        return "SingleDemand{" +
                "demand=" + demand +
                '}';
    }
}
