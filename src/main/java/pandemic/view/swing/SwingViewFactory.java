package pandemic.view.swing;

import javax.swing.JScrollPane;

import pandemic.BoardController;
import pandemic.model.PandemicModel;
import pandemic.util.ResourceProvider;
import pandemic.view.BoardView;
import pandemic.view.DiscardPileView;
import pandemic.view.ForecastView;
import pandemic.view.TroubleshooterView;
import pandemic.view.ViewFactory;

public class SwingViewFactory implements ViewFactory {

    private JScrollPane container;
    
    private ResourceProvider resourceProvider;

    public SwingViewFactory(JScrollPane container) {
        this.container = container;
    }

    @Override
    public BoardView createBoardView(BoardController controller, PandemicModel model) {
        SwingBoardView boardView = new SwingBoardView(container, controller, model);
        boardView.setResourceProvider(resourceProvider);
        return boardView;
    }

    @Override
    public DiscardPileView createDiscardPileView(BoardController controller, PandemicModel model) {
        SwingDiscardPileView discardPileView = new SwingDiscardPileView(controller, model);
        discardPileView.setResourceProvider(resourceProvider);
        return discardPileView;
    }

    @Override
    public ForecastView createForecastView(BoardController controller, PandemicModel model) {
        SwingForecastView forecastView = new SwingForecastView(container, controller, model);
        forecastView.setResourceProvider(resourceProvider);
        return forecastView;
    }

    @Override
    public TroubleshooterView createTroubleshooterView(BoardController controller, PandemicModel model) {
        SwingTroubleshooterView troubleshooterView = new SwingTroubleshooterView(controller, model);
        troubleshooterView.setResourceProvider(resourceProvider);
        return troubleshooterView;
    }

    /*@Override
    public NewAssignmentView createNewAssignmentView(BoardController listener, PandemicModel model) {
        SwingNewAssignmentView newAssignmentView = new SwingNewAssignmentView(listener, model);
        return newAssignmentView;
    }*/

    @Override
    public void setResourceProvider(ResourceProvider resourceProvider) {
        this.resourceProvider = resourceProvider;
    }
}
