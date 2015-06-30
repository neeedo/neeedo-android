package neeedo.imimaprx.htw.de.neeedo.rest.util.returntype;

import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.FoundLocation;

public class FindLocationResult extends RestResult {

    private int requestNumberThisRequest;
    private ArrayList<FoundLocation> foundLocations;

    public FindLocationResult(ReturnType result, int requestNumberThisRequest, ArrayList<FoundLocation> foundLocations) {
        super(result);
        this.requestNumberThisRequest = requestNumberThisRequest;
        this.foundLocations = foundLocations;
    }

    public ArrayList<FoundLocation> getFoundLocations() {
        return foundLocations;
    }

    public int getRequestNumberThisRequest() {
        return requestNumberThisRequest;
    }
}
