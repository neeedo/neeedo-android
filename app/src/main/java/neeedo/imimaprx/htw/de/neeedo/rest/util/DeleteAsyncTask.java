package neeedo.imimaprx.htw.de.neeedo.rest.util;

import android.util.Log;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import neeedo.imimaprx.htw.de.neeedo.entities.demand.Demand;
import neeedo.imimaprx.htw.de.neeedo.entities.favorites.Favorite;
import neeedo.imimaprx.htw.de.neeedo.entities.offer.Offer;
import neeedo.imimaprx.htw.de.neeedo.entities.user.User;
import neeedo.imimaprx.htw.de.neeedo.entities.util.BaseEntity;
import neeedo.imimaprx.htw.de.neeedo.events.DeleteFinishedEvent;
import neeedo.imimaprx.htw.de.neeedo.factory.HttpRequestFactoryProviderImpl;
import neeedo.imimaprx.htw.de.neeedo.models.DemandsModel;
import neeedo.imimaprx.htw.de.neeedo.models.OffersModel;
import neeedo.imimaprx.htw.de.neeedo.rest.util.returntype.RestResult;
import neeedo.imimaprx.htw.de.neeedo.utils.ServerConstantsUtils;

public class DeleteAsyncTask extends BaseAsyncTask {

    private EntityType entityType;
    private BaseEntity baseEntity;
    private BaseEntity backupEntity;

    public DeleteAsyncTask(BaseEntity baseEntity) {
        if (baseEntity == null) {
            throw new IllegalArgumentException("Invalid arguments given.");
        }
        this.baseEntity = baseEntity;
    }

    @Override
    protected void onPostExecute(Object result) {
        if (result instanceof RestResult)

            if (((RestResult) result).getResultBoolean()) {
                eventService.post(new DeleteFinishedEvent(true));
            } else {
                eventService.post(new DeleteFinishedEvent(false));
            }
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {

            if (baseEntity instanceof Demand) {
                entityType = EntityType.DEMAND;
            } else if (baseEntity instanceof User) {
                entityType = EntityType.USER;
            } else if (baseEntity instanceof Offer) {
                entityType = EntityType.OFFER;
            } else if (baseEntity instanceof Favorite) {
                entityType = EntityType.FAVORITE;
            }

            if (entityType == EntityType.DEMAND) {
                DemandsModel model = DemandsModel.getInstance();
                String id = ((Demand) baseEntity).getId();
                baseEntity = model.getDemandById(id);
                model.removeDemandByID(id);
                model.setUseLocalList(true);
                model.setLastDeletedEntityId(id);
            }
            if (entityType == EntityType.OFFER) {
                OffersModel model = OffersModel.getInstance();
                String id = ((Offer) baseEntity).getId();
                baseEntity = model.getOfferByID(id);
                model.removeOfferByID(id);
                model.setUseLocalList(true);
                model.setLastDeletedEntityId(id);
            }

            String url = ServerConstantsUtils.getActiveServer();
            switch (entityType) {

                case DEMAND: {
                    Demand demand = (Demand) baseEntity;
                    url += "demands/" + demand.getId() + "/" + demand.getVersion();
                }
                break;

                case OFFER: {
                    Offer offer = (Offer) baseEntity;
                    url += "offers/" + offer.getId() + "/" + offer.getVersion();
                }
                break;

                case USER: {
                    User user = (User) baseEntity;
                    url += "usersd/" + user.getId() + "/" + user.getVersion();
                }
                break;

                case FAVORITE: {
                    Favorite favorite = (Favorite) baseEntity;
                    url += "favorites/" + favorite.getUserId() + "/" + favorite.getOfferId();
                }
            }

            HttpHeaders requestHeaders = new HttpHeaders();
            setAuthorisationHeaders(requestHeaders);
            HttpEntity requestEntity = new HttpEntity(requestHeaders);
            RestTemplate restTemplate = new RestTemplate(HttpRequestFactoryProviderImpl.getClientHttpRequestFactorySSLSupport(9000));

            restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, Void.class);


            return new RestResult(RestResult.ReturnType.SUCCESS);
        } catch (Exception e) {

            if (entityType == EntityType.DEMAND) {
                DemandsModel model = DemandsModel.getInstance();
                Demand demand = ((Demand) backupEntity);
                model.addDemand(demand);
                model.setLastDeletedEntityId("");
            }
            if (entityType == EntityType.OFFER) {
                OffersModel model = OffersModel.getInstance();
                Offer offer = ((Offer) backupEntity);
                model.addOffer(offer);
                model.setLastDeletedEntityId("");
            }

            Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
            String message = getErrorMessage(e.getMessage());
            showToast(message);
            return new RestResult(RestResult.ReturnType.FAILED);
        }
    }
}
