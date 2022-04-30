package it.javaboss.command;

import java.util.LinkedList;

public class FIFOExecutor implements Executor  {

    private Operation operationToExecute;

    public void execute() {
        operationToExecute.performTask();
    }

    public void storeOperation(Operation operation) {
        operationToExecute = operation;
    }

}
