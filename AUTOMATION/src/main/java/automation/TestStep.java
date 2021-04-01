package automation;

public class TestStep
{
    private String testCase;
    private String step;
    private String script;
    private String object;
    private String input;
    private String output;
    
    public String getStep() {
        return this.step;
    }
    
    public TestStep setStep(final String step) {
        this.step = step;
        return this;
    }
    
    public String getScript() {
        return this.script;
    }
    
    public TestStep setScript(final String script) {
        this.script = script;
        return this;
    }
    
    public String getObject() {
        return this.object;
    }
    
    public TestStep setObject(final String object) {
        this.object = object;
        return this;
    }
    
    public String getInput() {
        return this.input;
    }
    
    public TestStep setInput(final String input) {
        this.input = input;
        return this;
    }
    
    public String getTestCase() {
        return this.testCase;
    }
    
    public TestStep setTestCase(final String testCase) {
        this.testCase = testCase;
        return this;
    }
    
    public String getOutput() {
        return this.output;
    }
    
    public TestStep setOutput(final String output) {
        this.output = output;
        return this;
    }
    
    @Override
    public String toString() {
        return "automation.TestStep{testCase='" + this.testCase + '\'' + ", step='" + this.step + '\'' + ", script='" + this.script + '\'' + ", object='" + this.object + '\'' + ", input='" + this.input + '\'' + '}';
    }
}