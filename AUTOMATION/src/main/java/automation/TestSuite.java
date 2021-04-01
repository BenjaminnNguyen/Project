package automation;

import java.util.Iterator;
import java.util.function.Function;
import java.util.Collection;
import one.util.streamex.StreamEx;
import java.util.Map;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.commons.lang3.StringUtils;
import java.util.HashMap;
import java.util.ArrayList;
import org.apache.poi.ss.usermodel.Workbook;
import java.util.List;
import org.apache.poi.ss.usermodel.DataFormatter;

public class TestSuite
{
    private static final DataFormatter df;
    private String name;
    private List<TestCase> cases;
    private List<TestObject> objects;
    private List<TestParam> params;
    private List<String> outputs;
    
    public TestSuite(final String testSuiteName, final Workbook workbook) {
        this.name = testSuiteName;
        this.outputs = new ArrayList<String>();
        
        //////////////
        
     //   this.cases = this.loadTestCases(workbook);
        this.objects = this.loadObjects(workbook);
        (this.params = this.loadTestParams(workbook)).sort((x1, x2) -> {
            if (x2.getName().length() > x1.getName().length()) {
                return 1;
            }
            else if (x2.getName().length() == x1.getName().length()) {
                return 0;
            }
            else {
                return -1;
            }
        });
    }
    
    private List<TestObject> loadObjects(final Workbook workbook) {
        final Sheet objectSheet = workbook.getSheet(AutomationLibrary.getProperty("sheet.name.object"));
        final int nTestObject = objectSheet.getLastRowNum();
        final List<TestObject> testObjects = new ArrayList<TestObject>();
        final Row testObjectHeaderRow = objectSheet.getRow(0);
        final int nRow = testObjectHeaderRow.getLastCellNum();
        final Map<Integer, String> objectTypeIndexMap = new HashMap<Integer, String>();
        for (int i = 0; i < nRow; ++i) {
            final String header = this.cellToString(testObjectHeaderRow, i);
            if (StringUtils.isNotBlank((CharSequence)header)) {
                objectTypeIndexMap.put(i, header);
            }
        }
        for (int i = 1; i <= nTestObject; ++i) {
            final Row currRow = objectSheet.getRow(i);
            if (currRow != null) {
                final String name = this.cellToString(currRow, 0);
                if (!StringUtils.isBlank((CharSequence)name)) {
                    for (int j = 1; j < nRow; ++j) {
                        final String value = this.cellToString(currRow, j);
                        if (StringUtils.isNotBlank((CharSequence)value)) {
                            final String type = objectTypeIndexMap.get(j);
                            final TestObject testObject = new TestObject(name, value, type);
                            testObjects.add(testObject);
                            break;
                        }
                    }
                }
            }
        }
        return testObjects;
    }
    
//    private List<TestCase> loadTestCases(final Workbook workbook) {
//        final List<TestCase> trueCases = new ArrayList<TestCase>();
//        final Sheet testCaseSheet = workbook.getSheet(AutomationLibrary.getProperty("sheet.name.list"));
//        final List<String> runTestCases = this.getRunTestCases(testCaseSheet);
//        final List<TestStep> steps = this.getTestSteps(workbook);
//        final Map<String, List<TestStep>> testCaseMap = (Map<String, List<TestStep>>)StreamEx.of(extracted(steps)).groupingBy((Function)TestStep::getTestCase);
//        for (final String runTestCase : runTestCases) {
//            for (final Map.Entry<String, List<TestStep>> testCase : testCaseMap.entrySet()) {
//                final TestCase aTestCase = new TestCase(testCase.getKey(), testCase.getValue());
//                if (runTestCase.equals(aTestCase.getName())) {
//                    trueCases.add(aTestCase);
//                }
//            }
//        }
//        return trueCases;
//    }

	private Collection extracted(final List<TestStep> steps) {
		return (Collection)steps;
	}
    
    private List<String> getRunTestCases(final Sheet testCaseSheet) {
        final List<String> runTestCases = new ArrayList<String>();
        runTestCases.add("init");
        runTestCases.add("after");
        for (int nTestCase = testCaseSheet.getLastRowNum(), i = 1; i <= nTestCase; ++i) {
            final Row currRow = testCaseSheet.getRow(i);
            if (currRow != null) {
                final String testCaseRun = this.cellToString(currRow, 1);
                if ("Yes".equals(testCaseRun)) {
                    runTestCases.add(this.cellToString(currRow, 0));
                }
            }
        }
        return runTestCases;
    }
    
    private List<TestStep> getTestSteps(final Workbook workbook) {
        final Sheet testCaseDetailSheet = workbook.getSheet(AutomationLibrary.getProperty("sheet.name.detail"));
        final int testCaseDetailRowNum = testCaseDetailSheet.getLastRowNum();
        final List<TestStep> steps = new ArrayList<TestStep>();
        String currTestCase = "";
        for (int i = 1; i <= testCaseDetailRowNum; ++i) {
            final Row currRow = testCaseDetailSheet.getRow(i);
            if (currRow != null) {
                final String testCaseName = this.cellToString(currRow, 0);
                if (StringUtils.isNotBlank((CharSequence)testCaseName)) {
                    currTestCase = testCaseName;
                }
                final TestStep step = new TestStep();
                step.setTestCase(currTestCase);
                step.setStep(this.cellToString(currRow, 1));
                step.setScript(this.cellToString(currRow, 2));
                step.setObject(this.cellToString(currRow, 3));
                step.setInput(this.cellToString(currRow, 4));
                step.setOutput(this.cellToString(currRow, 5));
                if (StringUtils.isNotBlank((CharSequence)step.getScript())) {
                    steps.add(step);
                    if (StringUtils.isNotBlank((CharSequence)step.getOutput())) {
                        this.outputs.add(step.getOutput());
                    }
                }
            }
        }
        return steps;
    }
    
    private List<TestParam> loadTestParams(final Workbook workbook) {
        final Sheet paramSheet = workbook.getSheet(AutomationLibrary.getProperty("sheet.name.param"));
        final int nParam = paramSheet.getLastRowNum();
        final List<TestParam> testParams = new ArrayList<TestParam>();
        for (int i = 1; i <= nParam; ++i) {
            final Row currRow = paramSheet.getRow(i);
            final String name = this.cellToString(currRow, 0);
            final String value = this.cellToString(currRow, 1);
            if (StringUtils.isNotBlank((CharSequence)name)) {
                testParams.add(new TestParam(name, value));
            }
        }
        return testParams;
    }
    
    private String cellToString(final Row row, final int index) {
        return TestSuite.df.formatCellValue(row.getCell(index)).trim();
    }
    
    public String getName() {
        return this.name;
    }
    
    public TestSuite setName(final String name) {
        this.name = name;
        return this;
    }
    
    public List<TestCase> getCases() {
        return this.cases;
    }
    
    public TestSuite setCases(final List<TestCase> cases) {
        this.cases = cases;
        return this;
    }
    
    public List<TestObject> getObjects() {
        return this.objects;
    }
    
    public TestSuite setObjects(final List<TestObject> objects) {
        this.objects = objects;
        return this;
    }
    
    public List<TestParam> getParams() {
        return this.params;
    }
    
    public TestSuite setParams(final List<TestParam> params) {
        this.params = params;
        return this;
    }
    
    public List<String> getOutputs() {
        return this.outputs;
    }
    
    public void setOutputs(final List<String> outputs) {
        this.outputs = outputs;
    }
    
    static {
        df = new DataFormatter();
    }
}