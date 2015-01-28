package neeedo.imimaprx.htw.de.neeedo;

import neeedo.imimaprx.htw.de.neeedo.Entities.Demand;
import neeedo.imimaprx.htw.de.neeedo.Entities.Demands;
import neeedo.imimaprx.htw.de.neeedo.Entities.SingleDemand;


public class LocalDemands {

    private Demands demands;
    private SingleDemand singleDemand;
    private Demand postDemand;

    public static LocalDemands getInstance() {
        if (localDemands == null)
            localDemands = new LocalDemands();
        return localDemands;
    }

    private LocalDemands() {
    }

    private static LocalDemands localDemands;

    public Demands getDemands() {
        return demands;
    }

    public void setDemands(Demands demands) {
        this.demands = demands;
    }

    public SingleDemand getSingleDemand() {
        return singleDemand;
    }

    public void setSingleDemand(SingleDemand singleDemand) {
        this.singleDemand = singleDemand;
    }

    public Demand getPostDemand() {
        return postDemand;
    }

    public void setPostDemand(Demand postDemand) {
        this.postDemand = postDemand;
    }
}
