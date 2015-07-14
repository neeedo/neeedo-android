package neeedo.imimaprx.htw.de.neeedo.events;


public class DeleteFinishedEvent {

    private boolean finished;

    public DeleteFinishedEvent(boolean finished) {

        this.finished = finished;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
