package neeedo.imimaprx.htw.de.neeedo.entities.demand;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import neeedo.imimaprx.htw.de.neeedo.entities.util.BaseEntity;

@Root(name = "demands")
public class Demands implements Serializable, BaseEntity {

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

    public void addSingleDemandOnFirstPostion(Demand demand) {
        if (demands == null) {
            demands = new ArrayList<>();
        }
        demands.add(0, demand);
    }

    public void replaceDemand(Demand demand) {

        String id = demand.getId();
        for (int i = 0; i < demands.size(); i++) {
            if (demands.get(i).getId().equals(id)) {
                demands.set(i, demand);
            }
        }
    }

    @Override
    public String toString() {
        return "Demands{" +
                "demands=" + demands +
                '}';
    }
}
