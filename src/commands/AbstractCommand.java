package commands;

import java.io.Serializable;

public abstract class AbstractCommand implements Serializable {
    protected CommandNames name;
    protected String specification;
    protected boolean mode;
    private String inputData;

    public CommandNames getName() {
        return name;
    }

    public void setName(CommandNames name) {
        this.name = name;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public void setInputData(String inputData) {
        this.inputData = inputData;
    }

    public String getInputData() {
        return inputData;
    }

    public String getSpecification() {
        return specification;
    }

    public void setMode(boolean mode) {
        this.mode = mode;
    }

    public abstract void use();


    @Override
    public String toString() {
        return this.name + " : " + this.specification;

    }
}
