package neeedo.imimaprx.htw.de.neeedo.entities;

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

    public boolean isEmpty() {
        if (demands == null || demands.size() == 0) {
            return true;
        }
        return false;
    }

    public void addSingleDemand(Demand demand) {
        if (demands == null) {
            demands = new ArrayList<>();
        }
        demands.add(demand);

    }

    @Override
    public String toString() {
        return "Demands{" +
                "demands=" + demands +
                '}';
    }
}
