package neeedo.imimaprx.htw.de.neeedo.models;


import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.entities.demand.Demand;
import neeedo.imimaprx.htw.de.neeedo.entities.demand.Demands;
import neeedo.imimaprx.htw.de.neeedo.entities.demand.SingleDemand;
import neeedo.imimaprx.htw.de.neeedo.entities.offer.Offer;
import neeedo.imimaprx.htw.de.neeedo.entities.offer.Offers;

public class DemandsModel {

    private Demands demands;
    private SingleDemand singleDemand;
    private Demand postDemand; // TODO rename to draft

    public static DemandsModel getInstance() {
        if (demandsModel == null)
            demandsModel = new DemandsModel();
        return demandsModel;
    }

    private DemandsModel() {
    }

    private static DemandsModel demandsModel;

    public ArrayList<Demand> getDemands() {
        if (demands == null) {
            demands = new Demands();
            demands.setDemands(new ArrayList<Demand>());
        }
        return demands.getDemands();
    }

    public void setDemands(Demands demands) {
        this.demands = demands;
    }

    public Demand getSingleDemand() {
        if (singleDemand == null) {
            return null;
        }
        return singleDemand.getDemand();
    }

    public void addOffersToCurrentDemand(Demand demand, Offers offers) {

        ArrayList<Offer> list = offers.getOffers();
        for (Demand d : demands.getDemands()) {
            if (d.getId().equals(demand.getId())) {
                d.setMatchingOfferList(list);
                break;
            }
        }
    }

    public ArrayList<Offer> getOfferlistToDemandById(String demandId) {
        Demand demand = getDemandById(demandId);
        return demand.getMatchingOfferList();
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

    public Demand getDemandById(String demandId) {
        Demand foundDemand = null;
        for (Demand demand : demands.getDemands()) {
            if (demand.getId().equals(demandId)) {
                foundDemand = demand;
                break;
            }
        }
        return foundDemand;
    }
}
