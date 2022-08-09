package it.webcrawler.command;

public class FIFOExecutor implements Executor  {

    private Operation operationToExecute;

    public void execute() {
        operationToExecute.performTask();
    }

    public void storeOperation(Operation operation) {
        operationToExecute = operation;
    }

}



