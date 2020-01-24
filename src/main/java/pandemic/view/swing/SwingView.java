package pandemic.view.swing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pandemic.BoardController;
import pandemic.model.PandemicModel;
import pandemic.util.ResourceProvider;

/**
 * Superclass of all views.
 *
 * @author Barrie Treloar
 */
public abstract class SwingView {
    protected static final Logger logger = LoggerFactory.getLogger(SwingView.class);

    private BoardController controller;
    private PandemicModel model;
    
    private ResourceProvider resourceProvider;

    public SwingView(BoardController controller, PandemicModel model) {
        this.controller = controller;
        this.model = model;
    }

    /**
     * @return the controller
     */
    protected BoardController getController() {
        return controller;
    }

    /**
     * @return the model
     */
    protected PandemicModel getModel() {
        return model;
    }

    public void setResourceProvider(ResourceProvider resourceProvider) {
        this.resourceProvider = resourceProvider;
    }

    public ResourceProvider getResourceProvider() {
        return resourceProvider;
    }
}