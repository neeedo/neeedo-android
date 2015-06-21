package neeedo.imimaprx.htw.de.neeedo.entities.demand;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

import neeedo.imimaprx.htw.de.neeedo.entities.util.BaseEntity;

@Root(name = "demand")
public class SingleDemand implements Serializable, BaseEntity {

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
