package stepper.flow.execution.runner;

import stepper.flow.definition.api.StepUsageDeclaration;
import stepper.flow.execution.FlowExecutionResult;
import stepper.flow.execution.StepExecutionDataImpl;
import stepper.flow.execution.context.ExecutionContextInterface;
import stepper.flow.execution.context.ExecutionContextImpl;
import stepper.statistics.StatisticsDataImpl;
import stepper.step.api.StepResult;

import java.time.LocalDateTime;

public class FLowExecutor implements Runnable{

    private ExecutionContextInterface context;
    private StatisticsDataImpl statisticsData;

    public FLowExecutor(ExecutionContextInterface context, StatisticsDataImpl statisticsData)
    {
        this.context = context;
        this.statisticsData = statisticsData;
    }

    @Override
    public void run() {
        context.getFlowExecution().setStartTime(System.nanoTime());
        context.getFlowExecution().setActivationDate(LocalDateTime.now());

        // start actual execution
        FlowExecutionResult flowExecutionResult = null; // Track the flow execution result

        for (int i = 0; i < context.getFlowExecution().getFlowDefinition().getFlowSteps().size(); i++) {
            StepUsageDeclaration stepUsageDeclaration = context.getFlowExecution().getFlowDefinition().getFlowSteps().get(i);

            ExecutionContextInterface contextStep = new ExecutionContextImpl(context.getFlowExecution());
            contextStep.setStepName(stepUsageDeclaration.getFinalStepName());


            context.getFlowExecution().getStepsDataJavaFX().add(new StepExecutionDataImpl(stepUsageDeclaration.getFinalStepName()));

            StepResult stepResult = stepUsageDeclaration.getStepDefinition().invoke(contextStep);


            long timeStep = contextStep.convertFromNameStepExecutionData().getTotalTimeStep().toMillis();
            statisticsData.getStatisticsStep().updateStatisticsStep(stepUsageDeclaration.getStepName(), timeStep);

            updateFlowResult(context, stepUsageDeclaration, stepResult, flowExecutionResult);
            if(flowExecutionResult != null)
            {
                if(flowExecutionResult.equals(FlowExecutionResult.FAILURE))
                    break;
            }
        }

        context.getFlowExecution().setEndTime(System.nanoTime());
        context.getFlowExecution().storeTotalTime(context.getFlowExecution().getStartTime());
        long timeFlow = context.getFlowExecution().getTotalTime().toMillis();
        statisticsData.getStatisticsFlow().updateStatisticsFlow(context.getFlowExecution().getFlowDefinition().getName(), timeFlow);

        if (flowExecutionResult == null) {
            context.getFlowExecution().setFlowExecutionResult(FlowExecutionResult.SUCCESS);
        } else {
            context.getFlowExecution().setFlowExecutionResult(flowExecutionResult);
        }
    }

    private static void updateFlowResult(ExecutionContextInterface context, StepUsageDeclaration stepUsageDeclaration, StepResult stepResult, FlowExecutionResult currentResult) {
        if (stepResult == StepResult.FAILURE && currentResult != FlowExecutionResult.FAILURE) {
            if (!stepUsageDeclaration.skipIfFail()) {
                currentResult = FlowExecutionResult.FAILURE;
            } else if (currentResult == null) {
                currentResult = FlowExecutionResult.WARNING;
            }
        } else if (stepResult == StepResult.WARNING && currentResult == null) {
            currentResult = FlowExecutionResult.WARNING;
        }
    }

    public ExecutionContextInterface getContext() {
        return context;
    }

    public StatisticsDataImpl getStatisticsData() {
        return statisticsData;
    }

    public void setContext(ExecutionContextInterface context) {
        this.context = context;
    }

    public void setStatisticsData(StatisticsDataImpl statisticsData) {
        this.statisticsData = statisticsData;
    }
}
