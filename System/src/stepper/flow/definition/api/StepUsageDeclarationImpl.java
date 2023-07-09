package stepper.flow.definition.api;

import stepper.step.StepDefinitionRegistry;
import stepper.step.api.DataDefinitionDeclaration;
import stepper.step.api.StepDefinition;
import xmlReader.schema.generated.STStepInFlow;

import java.util.ArrayList;
import java.util.List;

public class StepUsageDeclarationImpl implements StepUsageDeclaration {
    private final StepDefinition stepDefinition;
    private final boolean skipIfFail;
    private final String stepName;
    private final String stepNameFinal;
    private List<DataUsageDeclaration> dataUsageDeclaration;

    public StepUsageDeclarationImpl(STStepInFlow stepInFlow) {

        this.stepDefinition = createDataDefinition(stepInFlow.getName());
        this.stepName = stepInFlow.getName();
        this.stepNameFinal = stepInFlow.getAlias() != null ? stepInFlow.getAlias() : stepInFlow.getName();
        this.skipIfFail = stepInFlow.isContinueIfFailing() != null ? stepInFlow.isContinueIfFailing() : false;
        this.dataUsageDeclaration = new ArrayList<>();
        initialListDataUsageDeclaration();
    }

    public StepUsageDeclarationImpl(xmlReaderJavaFX.schema.generated.STStepInFlow stepInFlow) {
        this.stepDefinition = createDataDefinition(stepInFlow.getName());
        this.stepName = stepInFlow.getName();
        this.stepNameFinal = stepInFlow.getAlias() != null ? stepInFlow.getAlias() : stepInFlow.getName();
        this.skipIfFail = stepInFlow.isContinueIfFailing() != null ? stepInFlow.isContinueIfFailing() : false;
        this.dataUsageDeclaration = new ArrayList<>();
        initialListDataUsageDeclaration();
    }

    public void initialListDataUsageDeclaration()
    {
        for(DataDefinitionDeclaration data : stepDefinition.getInputs().values())
            dataUsageDeclaration.add(new DataUsageDeclarationImpl(data.getName(), data, DataKind.INPUT ));
        for(DataDefinitionDeclaration data : stepDefinition.getOutputs().values())
            dataUsageDeclaration.add(new DataUsageDeclarationImpl(data.getName(), data, DataKind.OUTPUT));
    }

    @Override
    public DataUsageDeclaration getDataUsageDeclarationByOriginalName(String originalName)
    {
        for(DataUsageDeclaration data: dataUsageDeclaration)
        {
            if(data.getDataDefinition().getName().equals(originalName))
                return data;
        }
        return null;
    }
    @Override
    public DataUsageDeclaration getDataUsageDeclarationByFinalName(String finalName)
    {
        for(DataUsageDeclaration data: dataUsageDeclaration)
        {
            if(data.getFinalName().equals(finalName))
                return data;
        }
        return null;
    }
    public StepDefinition createDataDefinition(String name) {

        switch (name) {
            case "Spend Some Time":
               return StepDefinitionRegistry.SPEND_SOME_TIME.getStepDefinition();
            case "Collect Files In Folder":
                return StepDefinitionRegistry.COLLECT_FILES_IN_FOLDER.getStepDefinition();
            case "Files Deleter":
                return StepDefinitionRegistry.FILES_DELETER.getStepDefinition();
            case "Files Renamer":
                return StepDefinitionRegistry.FILES_RENAMER.getStepDefinition();
            case "Files Content Extractor":
                return StepDefinitionRegistry.FILES_CONTENT_EXTRACTOR.getStepDefinition();
            case "CSV Exporter":
                return StepDefinitionRegistry.CSV_EXPORTER.getStepDefinition();
            case "Properties Exporter":
                return StepDefinitionRegistry.PROPERTIES_EXPORTER.getStepDefinition();
            case "File Dumper":
                return StepDefinitionRegistry.FILE_DUMPER.getStepDefinition();
            case "Zipper":
                return StepDefinitionRegistry.ZIPPER.getStepDefinition();
            case "Command Line":
                return StepDefinitionRegistry.COMMAND_LINE.getStepDefinition();
            case "HTTP Call":
                return StepDefinitionRegistry.HTTP_CALL.getStepDefinition();
            case "To Json":
                return StepDefinitionRegistry.TO_JSON.getStepDefinition();
            case "Json Data Extractor":
                return StepDefinitionRegistry.JSON_DATA_EXTRACTOR.getStepDefinition();
            default:
                // לזרוק אקספשיין
        }
        return null;
    }

    @Override
    public String getFinalStepName() {
        return stepNameFinal;
    }
    @Override
    public String getStepName() {
        return stepName;
    }

    @Override
    public StepDefinition getStepDefinition() {
        return stepDefinition;
    }

    @Override
    public boolean skipIfFail() {
        return skipIfFail;
    }
}
