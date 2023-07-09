package stepper.flow.execution.runner;

import stepper.flow.definition.api.StepUsageDeclaration;
import stepper.flow.execution.FlowExecutionResult;
import stepper.flow.execution.StepExecutionDataImpl;
import stepper.flow.execution.context.ExecutionContextImpl;
import stepper.flow.execution.context.ExecutionContextInterface;
import stepper.statistics.StatisticsDataImpl;
import stepper.step.api.StepResult;

import java.time.LocalDateTime;

public class FlowExecutorConsoleUI {

    public void executeFlow(ExecutionContextInterface context, StatisticsDataImpl statisticsData) {

        context.getFlowExecution().setStartTime(System.nanoTime());
        context.getFlowExecution().setActivationDate(LocalDateTime.now());

        // start actual execution
        for (int i = 0; i < context.getFlowExecution().getFlowDefinition().getFlowSteps().size(); i++) {
            StepUsageDeclaration stepUsageDeclaration = context.getFlowExecution().getFlowDefinition().getFlowSteps().get(i);

            ExecutionContextInterface contextStep = new ExecutionContextImpl(context.getFlowExecution());
            contextStep.setStepName(stepUsageDeclaration.getFinalStepName());

            StepResult stepResult = stepUsageDeclaration.getStepDefinition().invoke(contextStep);
            long timeStep = contextStep.convertFromNameStepExecutionData().getTotalTimeStep().toMillis();
            statisticsData.getStatisticsStep().updateStatisticsStep(stepUsageDeclaration.getStepName(), timeStep);

            if (updateFlowResult(context, stepUsageDeclaration, stepResult)) break;
        }

        if(context.getFlowExecution().getFlowExecutionResult() == null)
            context.getFlowExecution().setFlowExecutionResult(FlowExecutionResult.SUCCESS);

        context.getFlowExecution().setEndTime(System.nanoTime());
        context.getFlowExecution().storeTotalTime(context.getFlowExecution().getStartTime());
        long timeFlow = context.getFlowExecution().getTotalTime().toMillis();
        statisticsData.getStatisticsFlow().updateStatisticsFlow(context.getFlowExecution().getFlowDefinition().getName(), timeFlow);
    }

    private static boolean updateFlowResult(ExecutionContextInterface context, StepUsageDeclaration stepUsageDeclaration, StepResult stepResult) {
        if(stepResult == StepResult.FAILURE)
        {
            if(!stepUsageDeclaration.skipIfFail()) {
                context.getFlowExecution().setFlowExecutionResult(FlowExecutionResult.FAILURE);
                return true;
            }else{
                context.getFlowExecution().setFlowExecutionResult(FlowExecutionResult.WARNING);
            }
        }
        if(stepResult == StepResult.WARNING)
            context.getFlowExecution().setFlowExecutionResult(FlowExecutionResult.WARNING);
        return false;
    }

}
