package neeedo.imimaprx.htw.de.neeedo.models;


import neeedo.imimaprx.htw.de.neeedo.entities.Demand;
import neeedo.imimaprx.htw.de.neeedo.entities.Demands;
import neeedo.imimaprx.htw.de.neeedo.entities.SingleDemand;

public class DemandsModel {

    private Demands demands;
    private SingleDemand singleDemand;
    private Demand postDemand;
    private static DemandsModel demandsModel;

    public static DemandsModel getInstance() {
        if (demandsModel == null)
            demandsModel = new DemandsModel();
        return demandsModel;
    }

    private DemandsModel() {
    }


    public neeedo.imimaprx.htw.de.neeedo.entities.SingleDemand createNewSingleDemand(){
        singleDemand = new SingleDemand();
        return singleDemand;
    }

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
