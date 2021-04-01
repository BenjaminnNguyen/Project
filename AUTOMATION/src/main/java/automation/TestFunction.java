package automation;

import java.util.ArrayList;
import java.util.List;

public class TestFunction
{
    private String name;
    private List<String> functions;
    
    public TestFunction(final String name, final List<String> functions) {
        this.functions = new ArrayList<String>();
        this.name = name;
        this.functions = functions;
    }
    
    public boolean contains(final String function) {
        return this.functions.contains(function);
    }
    
    public String getName() {
        return this.name;
    }
    
    public TestFunction setName(final String name) {
        this.name = name;
        return this;
    }
    
    public List<String> getFunctions() {
        return this.functions;
    }
    
    public TestFunction setFunctions(final List<String> functions) {
        this.functions = functions;
        return this;
    }
}