package stepper.step;

import stepper.step.api.StepDefinition;
import stepper.step.impl.*;

public enum StepDefinitionRegistry
{
    SPEND_SOME_TIME(new SpendSomeTimeStep()),
    PROPERTIES_EXPORTER(new PropertiesExporterStep()),
    FILES_RENAMER(new FilesRenamerStep()),
    FILES_DELETER(new FilesDeleterStep()),
    FILES_CONTENT_EXTRACTOR(new FilesContentExtractorStep()),
    FILE_DUMPER(new FileDumperStep()),
    CSV_EXPORTER(new CSVExporterStep()),
    COLLECT_FILES_IN_FOLDER(new CollectFilesInFolderStep()),
    ZIPPER(new ZipperStep()),
    COMMAND_LINE(new CommandLineStep()),
    HTTP_CALL(new HTTPCall()),
    TO_JSON(new ToJson()),
    JSON_DATA_EXTRACTOR(new JsonDataExtractor());

    private final StepDefinition stepDefinition;

    StepDefinitionRegistry(StepDefinition stepDefinition) {
        this.stepDefinition = stepDefinition;
    }

    public StepDefinition getStepDefinition() {
        return stepDefinition;
    }
}
