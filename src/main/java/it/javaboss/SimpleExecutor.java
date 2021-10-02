package it.javaboss;

import java.util.LinkedList;


// Da migliorare
public class SimpleExecutor implements Executor  {

    private LinkedList<Operation> operationsToBeExecuted = new LinkedList<>();

    public void execute() {
        Operation extractDocumentDataOperation = operationsToBeExecuted.removeFirst();
        extractDocumentDataOperation.performTask();
    }

    public void storeOperation(Operation operation) {
        operationsToBeExecuted.add(operation);
        execute();
    }




}
