package it.javaboss.command;

import java.util.LinkedList;


// TODO : vedere se ci sono problemi di concorrenza
// Onestamente è inutile
public class FIFOExecutor implements Executor  {

    private LinkedList<Operation> operationsToBeExecuted = new LinkedList<>();

    public void execute() {
        Operation extractDocumentDataOperation = operationsToBeExecuted.removeLast();
        extractDocumentDataOperation.performTask();
    }

    public void storeOperation(Operation operation) {
        operationsToBeExecuted.add(operation);
    }

}
