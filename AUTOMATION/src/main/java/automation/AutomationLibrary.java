package automation;

import java.util.HashMap;
import java.util.Iterator;
import java.io.InputStream;
import org.jboss.forge.roaster.Roaster;
import java.io.IOException;
import java.util.Properties;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import java.util.Map;

public class AutomationLibrary
{
    private static final Map<String, MethodSource<JavaClassSource>> methodMap;
    private static final Properties properties;
    
    public static void main(final String[] args) throws IOException {
        loadAllResources();
    }
    
    public static void loadAllResources() throws IOException {
        loadResource("CommonBase.java");
        loadResource("JDBCConnectManagement.java");
        loadResource("ManageAccount.java");
        loadResource("TestLogger.java");
        loadProperty("config.properties");
    }
    
    private static void loadResource(final String resourceName) {
        final ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        final InputStream is = classloader.getResourceAsStream(resourceName);
        final JavaClassSource javaClass = (JavaClassSource)Roaster.parse((Class)JavaClassSource.class, is);
        for (final MethodSource<JavaClassSource> javaClassSourceMethodSource : javaClass.getMethods()) {
            AutomationLibrary.methodMap.put(javaClassSourceMethodSource.getName(), javaClassSourceMethodSource);
        }
    }
    
    private static void loadProperty(final String resourceName) throws IOException {
        final ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        final InputStream is = classloader.getResourceAsStream(resourceName);
        AutomationLibrary.properties.load(is);
    }
    
    public static MethodSource<JavaClassSource> getMethod(final String methodName) {
        return AutomationLibrary.methodMap.get(methodName);
    }
    
    public static String getProperty(final String name) {
        return AutomationLibrary.properties.getProperty(name);
    }
    
    static {
        methodMap = new HashMap<String, MethodSource<JavaClassSource>>();
        properties = new Properties();
    }
}