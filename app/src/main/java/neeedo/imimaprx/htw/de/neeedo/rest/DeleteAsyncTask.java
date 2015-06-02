package neeedo.imimaprx.htw.de.neeedo.rest;

import android.util.Log;

import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import neeedo.imimaprx.htw.de.neeedo.entities.BaseEntity;
import neeedo.imimaprx.htw.de.neeedo.entities.Demand;
import neeedo.imimaprx.htw.de.neeedo.entities.Offer;
import neeedo.imimaprx.htw.de.neeedo.entities.User;
import neeedo.imimaprx.htw.de.neeedo.factory.HttpRequestFactoryProviderImpl;
import neeedo.imimaprx.htw.de.neeedo.models.ActiveUser;
import neeedo.imimaprx.htw.de.neeedo.utils.ServerConstantsUtils;

public class DeleteAsyncTask extends BaseAsyncTask {

    private EntityTyp entityTyp;
    private BaseEntity baseEntity;


    /**
     * Needs the Entity enum Type defined in {@link BaseAsyncTask} and the Entity to delete.
     *
     * @param baseEntity
     * @param entityTyp
     */
    public DeleteAsyncTask(BaseEntity baseEntity, EntityTyp entityTyp) {

        if (baseEntity == null | entityTyp == null) {
            throw new IllegalArgumentException("Invalid arguments given.");
        }

        this.entityTyp = entityTyp;
        this.baseEntity = baseEntity;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {

            String url = ServerConstantsUtils.getActiveServer();

            switch (entityTyp) {

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

            final ActiveUser activeUser = ActiveUser.getInstance();
            HttpBasicAuthentication authentication = new HttpBasicAuthentication(activeUser.getUsername(), activeUser.getUserPassword());
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setAuthorization(authentication);
            RestTemplate restTemplate = new RestTemplate(HttpRequestFactoryProviderImpl.getClientHttpRequestFactorySSLSupport(9000));
            restTemplate.delete(url);

            return ReturnTyp.SUCCESS;
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
            return ReturnTyp.FAILED;
        }
    }
}
