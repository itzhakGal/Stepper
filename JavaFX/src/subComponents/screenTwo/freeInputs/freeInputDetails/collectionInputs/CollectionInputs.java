package subComponents.screenTwo.freeInputs.freeInputDetails.collectionInputs;


import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Label;
import stepper.step.api.DataNecessity;
import stepper.systemEngine.SystemEngineInterface;
import subComponents.screenTwo.freeInputs.FreeInputsController;
import utils.DTOInputExecution;

public interface CollectionInputs {
    void init(String name, DataNecessity necessity);
    Label getFieldLabel();
    String getName();
    DataNecessity getNecessity();
    SimpleBooleanProperty getIsInputFieldEmptyProperty();
    String getInputData();
    void setMainController(FreeInputsController freeInputsController);
    void setSystemEngine(SystemEngineInterface systemEngine);
   void updateDetails(DTOInputExecution inputExecution);

    void setFreeInput(Object item);

}
