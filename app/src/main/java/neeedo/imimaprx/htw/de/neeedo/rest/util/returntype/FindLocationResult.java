package neeedo.imimaprx.htw.de.neeedo.rest.util.returntype;

import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.FoundLocation;

public class FindLocationResult extends RestResult {

    private ArrayList<FoundLocation> foundLocations;

    public FindLocationResult(String creatorClass, ReturnType result, ArrayList<FoundLocation> foundLocations) {
        super( result);
        this.foundLocations = foundLocations;
    }

    public ArrayList<FoundLocation> getFoundLocations() {
        return foundLocations;
    }
}
