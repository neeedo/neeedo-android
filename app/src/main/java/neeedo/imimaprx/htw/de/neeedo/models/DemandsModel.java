package neeedo.imimaprx.htw.de.neeedo.models;


import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.entities.demand.Demand;
import neeedo.imimaprx.htw.de.neeedo.entities.demand.Demands;
import neeedo.imimaprx.htw.de.neeedo.entities.offer.Offer;
import neeedo.imimaprx.htw.de.neeedo.entities.offer.Offers;

public class DemandsModel {

    private Demands demands = new Demands();
    private boolean useLocalList = false;
    private String lastDeletedEntityId = "";

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
        ArrayList<Offer> offerList = new ArrayList<>();
        if (demand.getMatchingOfferList() != null) {
            offerList = demand.getMatchingOfferList();
        }
        return offerList;
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
        demands.addDemand(demand);
    }

    public void replaceDemand(Demand demand) {
        demands.replaceDemand(demand);
    }

    public void removeDemandByID(String id) {
        demands.removeById(id);
    }

    public void appendDemands(ArrayList<Demand> newDemands) {
        for (Demand newDemand : newDemands) {
            if (!demands.checkIfOfferWithIdExists(newDemand.getId())) {
                if (lastDeletedEntityId.equals(newDemand.getId()))
                    continue;
                demands.addDemand(newDemand);
            } else {
                replaceDemand(newDemand);
            }
        }
    }

    public void clearDemands() {
        demands.setDemands(new ArrayList<Demand>());
    }

    public String getLastDeletedEntityId() {
        return lastDeletedEntityId;
    }

    public void setLastDeletedEntityId(String lastDeletedEntityId) {
        this.lastDeletedEntityId = lastDeletedEntityId;
    }
}
