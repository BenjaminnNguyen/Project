package automation;

public class TestParam
{
    private String name;
    private String value;
    
    public TestParam(final String name, final String value) {
        this.name = name;
        this.value = value;
    }
    
    public String getName() {
        return this.name;
    }
    
    public TestParam setName(final String name) {
        this.name = name;
        return this;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public TestParam setValue(final String value) {
        this.value = value;
        return this;
    }
}