package neeedo.imimaprx.htw.de.neeedo.models;


import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.entities.demand.Demand;
import neeedo.imimaprx.htw.de.neeedo.entities.demand.Demands;
import neeedo.imimaprx.htw.de.neeedo.entities.demand.SingleDemand;
import neeedo.imimaprx.htw.de.neeedo.entities.offer.Offer;
import neeedo.imimaprx.htw.de.neeedo.entities.offer.Offers;

public class DemandsModel {

    private Demands demands;
    private boolean useLocalList = false;

    private Demand draft;

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

    public Demand getDraft() {
        return draft;
    }

    public boolean isUseLocalList() {
        return useLocalList;
    }

    public void setUseLocalList(boolean useLocalList) {
        this.useLocalList = useLocalList;
    }

    public void setDraft(Demand draft) {
        this.draft = draft;
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

    public void addDemand(Demand demand) {
        demands.addSingleDemandOnFirstPostion(demand);
    }

    public void replaceDemand(Demand demand) {
        demands.replaceDemand(demand);
    }

    public void removeDemandByID(String id) {
        demands.removeById(id);
    }
}
