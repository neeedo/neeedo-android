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
    private ArrayList<Demand> demands = new ArrayList<>();

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

    public void addDemand(Demand demand) {
        demands.add(0, demand);
    }

    public void replaceDemand(Demand demand) {

        boolean notfound = true;
        String id = demand.getId();
        for (int i = 0; i < demands.size(); i++) {
            if (demands.get(i).getId().equals(id)) {
                demands.set(i, demand);
                notfound = false;
            }
        }
        if (notfound) {
            demands.add(demand);
        }
    }

    public void removeById(String id) {
        Demand toRemove = null;
        for (Demand d : demands) {
            if (d.getId().equals(id)) {
                toRemove = d;
            }
        }
        if (toRemove != null) {
            demands.remove(toRemove);
        }
    }

    public boolean checkIfOfferWithIdExists(String id) {
        boolean temp = false;

        for (Demand demand : demands) {
            if (demand.getId().equals(id)) {
                temp = true;
                break;
            }
        }
        return temp;
    }


    @Override
    public String toString() {
        return "Demands{" +
                "demands=" + demands +
                '}';
    }
}
