package it.javaboss.command;

import java.util.LinkedList;

// Onestamente è inutile
public class FIFOExecutor implements Executor  {

    private final LinkedList<Operation> operationsToBeExecuted = new LinkedList<>();

    public void execute() {
        Operation extractDocumentDataOperation = operationsToBeExecuted.removeLast();
        extractDocumentDataOperation.performTask();
    }

    public void storeOperation(Operation operation) {
        operationsToBeExecuted.add(operation);
    }

}
