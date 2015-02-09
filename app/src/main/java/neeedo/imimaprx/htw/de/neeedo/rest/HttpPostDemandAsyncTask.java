package neeedo.imimaprx.htw.de.neeedo.rest;

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
import neeedo.imimaprx.htw.de.neeedo.factory.ClientHttpRequestFactoryProvider;
import neeedo.imimaprx.htw.de.neeedo.models.DemandsModel;

public class HttpPostDemandAsyncTask extends SuperHttpAsyncTask {
    @Override
    protected Object doInBackground(Object[] params) {
        try {


            final String url = ServerConstants.ACTIVE_SERVER + "demands";

            DemandsModel demandsModel = DemandsModel.getInstance();
            HttpHeaders requestHeaders;
            requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Demand> requestEntity = new HttpEntity<Demand>(demandsModel.getPostDemand(), requestHeaders);


            RestTemplate restTemplate = new RestTemplate(ClientHttpRequestFactoryProvider.getClientHttpRequestFactory(9000));


            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            ResponseEntity<SingleDemand> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, SingleDemand.class);

            SingleDemand singleDemand = response.getBody();
            demandsModel.setSingleDemand(singleDemand);

            //Is needed to merge {@link SingleDemand} object into demands list.
            demandsModel.getDemands().getDemands().add(singleDemand.getDemand());

            return "Success";
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
            return "Failed";
        }
    }
}
