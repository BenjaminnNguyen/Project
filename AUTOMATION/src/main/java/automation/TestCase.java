package automation;

import java.util.List;

public class TestCase
{
    private String name;
    private List<TestStep> steps;
    
    public TestCase() {
    }
    
    public TestCase(final String name, final List<TestStep> steps) {
        this.name = name;
        this.steps = steps;
    }
    
    public String getName() {
        return this.name;
    }
    
    public TestCase setName(final String name) {
        this.name = name;
        return this;
    }
    
    public List<TestStep> getSteps() {
        return this.steps;
    }
    
    public TestCase setSteps(final List<TestStep> steps) {
        this.steps = steps;
        return this;
    }
    
    @Override
    public String toString() {
        return "automation.TestCase{name='" + this.name + '\'' + ", steps=" + this.steps + '}';
    }
}