package neeedo.imimaprx.htw.de.neeedo.Entities;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.ArrayList;

@Root(name = "demands")
public class Demands implements Serializable {

    @Element
    private ArrayList<Demand> demands;

    public Demands() {
    }

    public ArrayList<Demand> getDemands() {
        return demands;
    }

    public void setDemands(ArrayList<Demand> demands) {
        this.demands = demands;
    }

    @Override
    public String toString() {
        return "Demands{" +
                "demands=" + demands +
                '}';
    }
}
