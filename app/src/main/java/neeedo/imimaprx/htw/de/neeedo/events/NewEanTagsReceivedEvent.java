package neeedo.imimaprx.htw.de.neeedo.events;

import neeedo.imimaprx.htw.de.neeedo.rest.util.returntype.OutpanResult;

public class NewEanTagsReceivedEvent {
    private final OutpanResult outpanResult;

    public NewEanTagsReceivedEvent(OutpanResult outpanResult) {
        this.outpanResult = outpanResult;
    }

    public OutpanResult getOutpanResult() {
        return outpanResult;
    }
}
