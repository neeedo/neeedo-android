package neeedo.imimaprx.htw.de.neeedo.rest.demand;

import android.util.Log;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import neeedo.imimaprx.htw.de.neeedo.entities.Demand;
import neeedo.imimaprx.htw.de.neeedo.entities.SingleDemand;
import neeedo.imimaprx.htw.de.neeedo.factory.HttpRequestFactoryProviderImpl;
import neeedo.imimaprx.htw.de.neeedo.models.DemandsModel;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.util.returntype.RestResult;
import neeedo.imimaprx.htw.de.neeedo.utils.ServerConstantsUtils;

public class PostCreateUpdateDemandAsyncTask extends BaseAsyncTask {

    private SendMode sendMode;

    public PostCreateUpdateDemandAsyncTask(SendMode sendMode) {
        if (sendMode == null) {
            throw new IllegalArgumentException("No mode is given.");
        }
        this.sendMode = sendMode;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            String url = ServerConstantsUtils.getActiveServer();
            HttpMethod httpMethod = HttpMethod.POST;
            DemandsModel demandsModel = DemandsModel.getInstance();
            Demand postDemand = demandsModel.getPostDemand();
            switch (sendMode) {
                case CREATE: {
                    url += "demands";
                }
                break;
                case UPDATE: {
                    url += "demands/" + postDemand.getId() + "/" + postDemand.getVersion();
                    //to avoid to include these in the json
                    postDemand.setId(null);
                    postDemand.setVersion(0);
                    httpMethod = HttpMethod.PUT;
                }
                break;
                case DELETE: {
                    url += "demands/" + postDemand.getId() + "/" + postDemand.getVersion();
                    httpMethod = HttpMethod.DELETE; // TODO fix "400 Bad Request"
                }
            }

            HttpHeaders requestHeaders = new HttpHeaders();
            setAuthorisationHeaders(requestHeaders);
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Demand> requestEntity = new HttpEntity<Demand>(postDemand, requestHeaders);
            RestTemplate restTemplate = new RestTemplate(HttpRequestFactoryProviderImpl.getClientHttpRequestFactorySSLSupport(9000));
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<SingleDemand> response = restTemplate.exchange(url, httpMethod, requestEntity, SingleDemand.class);
            SingleDemand singleDemand = response.getBody();
            demandsModel.setSingleDemand(singleDemand);
            demandsModel.getDemands().getDemands().add(singleDemand.getDemand());
            demandsModel.setPostDemand(null);
            return new RestResult(this.getClass().getSimpleName(), RestResult.ReturnType.SUCCESS);
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
            return new RestResult(this.getClass().getSimpleName(), RestResult.ReturnType.FAILED);
        }
    }
}
