package automation;

public class TestObject
{
    private String name;
    private String value;
    private String type;
    
    public TestObject(final String name, final String value, final String type) {
        this.name = name;
        this.value = value;
        this.type = type;
    }
    
    public String getName() {
        return this.name;
    }
    
    public TestObject setName(final String name) {
        this.name = name;
        return this;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public TestObject setValue(final String value) {
        this.value = value;
        return this;
    }
    
    public String getType() {
        return this.type;
    }
    
    public TestObject setType(final String type) {
        this.type = type;
        return this;
    }
}