package subComponents.mainScreen.header;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import stepper.flow.execution.FlowExecutionResult;
import stepper.systemEngine.SystemEngineInterface;
import subComponents.mainScreen.app.AppController;
import subComponents.mainScreen.header.headerBody.HeaderBodyController;

public class HeaderController {
    private SystemEngineInterface systemEngine;
    private AppController mainController;
    @FXML
    private HeaderBodyController headerBodyComponentController;
    @FXML
    private GridPane headerBodyComponent;
    @FXML
    private Label titleLabel;

    @FXML
    private CheckBox animationCheckbox;

    private SimpleBooleanProperty checkboxValueProperty;

    @FXML
    public void initialize() {
        if (headerBodyComponentController != null) {
            headerBodyComponentController.setMainController(this);
        }
        setupCheckboxBinding();
        animationCheckbox.setSelected(true);
    }


    private void setupCheckboxBinding() {
        checkboxValueProperty = new SimpleBooleanProperty();
        checkboxValueProperty.bind(animationCheckbox.selectedProperty());
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void setSystemEngine(SystemEngineInterface engineManager) {
        this.systemEngine = engineManager;
        this.headerBodyComponentController.setSystemEngine(engineManager);
    }

    public void updatePushTabButtons() {
        mainController.updatePushTabButtons();
    }

    public void openTabFlowDefinition() {
        mainController.openTabFlowDefinition();
    }

    public HeaderBodyController getHeaderBodyComponentController() {
        return headerBodyComponentController;
    }

    public SimpleBooleanProperty getCheckboxValueProperty() {
        return checkboxValueProperty;
    }
}
