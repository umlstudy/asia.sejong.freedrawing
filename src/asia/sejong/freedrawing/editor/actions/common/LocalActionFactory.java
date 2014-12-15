package asia.sejong.freedrawing.editor.actions.common;

public abstract class LocalActionFactory {

    private final String actionId;
    
    protected LocalActionFactory(String actionId) {
    	this.actionId = actionId;
    }

    public String getId() {
        return actionId;
    }
}
