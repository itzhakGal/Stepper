package stepper.step.impl;

import stepper.dataDefinition.impl.DataDefinitionRegistry;
import stepper.flow.execution.StepExecutionDataImpl;
import stepper.flow.execution.context.ExecutionContextInterface;
import stepper.step.api.AbstractStepDefinition;
import stepper.step.api.DataDefinitionDeclarationImpl;
import stepper.step.api.DataNecessity;
import stepper.step.api.StepResult;

import java.time.LocalTime;

public class SpendSomeTimeStep extends AbstractStepDefinition
{

    public SpendSomeTimeStep() {
        super("Spend Some Time", true);

        addInput(new DataDefinitionDeclarationImpl("TIME_TO_SPEND", DataNecessity.MANDATORY, "Total sleeping time (sec)",DataDefinitionRegistry.NUMBER, false,""));

        // step outputs
    }

    @Override
    public StepResult invoke(ExecutionContextInterface context) {

        int TIME_TO_SPEND = context.getDataValue("TIME_TO_SPEND", Integer.class, this);
        StepResult result;
        LocalTime localStartTime = LocalTime.now();
        long startTime = System.nanoTime();
        try {
            if (TIME_TO_SPEND <= 0)
            {
                context.updateLogDataAndSummeryLine("A negative or zero number was received and this is an invalid condition for this step");
                result=  StepResult.FAILURE;
            }
            else
            {

                context.storeLogsData("About to sleep for " + TIME_TO_SPEND + " seconds");
                Thread.sleep(1000 * TIME_TO_SPEND);
                StepExecutionDataImpl dataStep = context.convertFromNameStepExecutionData();
                dataStep.setSummaryLine("The sleep operation was performed successfully");
                context.storeLogsData("Done sleeping");
                result= StepResult.SUCCESS;

            }
            LocalTime localEndTime = LocalTime.now();
            context.storeTotalTimeStep(localStartTime, localEndTime, startTime);
        } catch (InterruptedException e) {
            result= StepResult.FAILURE;
        }
        context.updateStatusStep(result);
         return result;
    }
}
