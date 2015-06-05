package neeedo.imimaprx.htw.de.neeedo.rest.util;

import android.util.Log;

import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import neeedo.imimaprx.htw.de.neeedo.entities.BaseEntity;
import neeedo.imimaprx.htw.de.neeedo.entities.Demand;
import neeedo.imimaprx.htw.de.neeedo.entities.Offer;
import neeedo.imimaprx.htw.de.neeedo.entities.User;
import neeedo.imimaprx.htw.de.neeedo.factory.HttpRequestFactoryProviderImpl;
import neeedo.imimaprx.htw.de.neeedo.rest.util.returntype.RestResult;
import neeedo.imimaprx.htw.de.neeedo.utils.ServerConstantsUtils;

public class DeleteAsyncTask extends BaseAsyncTask {

    private enum EntityType {
        DEMAND, OFFER, USER
    }

    private EntityType entityType;
    private BaseEntity baseEntity;

    public DeleteAsyncTask(BaseEntity baseEntity) {
        if (baseEntity == null) {
            throw new IllegalArgumentException("Invalid arguments given.");
        }
        this.baseEntity = baseEntity;
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
            }

            HttpHeaders requestHeaders = new HttpHeaders();
            setAuthorisationHeaders(requestHeaders);
            RestTemplate restTemplate = new RestTemplate(HttpRequestFactoryProviderImpl.getClientHttpRequestFactorySSLSupport(9000));
            restTemplate.delete(url);

            return new RestResult(this.getClass().getSimpleName(), RestResult.ReturnType.SUCCESS);
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
            return new RestResult(this.getClass().getSimpleName(), RestResult.ReturnType.FAILED);
        }
    }
}
