package com.gravitant.tests;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.apache.commons.lang3.time.FastDateFormat;
import com.gravitant.utils.XL_Reader;
import com.gravitant.utils.Util;
import org.apache.log4j.Logger;

public class RunTests{
	static Logger APPLICATION_LOGS = Logger.getLogger(RunTests.class);
	//Logger APPLICATION_LOGS = Logger.getLogger(getClass().getName());
	public int currentTestCaseID;
	public String currentTestSuite;
	public int currentTestStepRow;
	public String currentTestCaseName;
	public XL_Reader currentTestSuiteXLS;
	public String testResultsFolderName;
	public String automatedTestsFolder = "C:\\AutomatedTests\\";
	public String xlFilePath = automatedTestsFolder + "Tests\\Test_Cases.xlsx";
	public XL_Reader suiteXLS = new XL_Reader(xlFilePath);
	public String currentDate = FastDateFormat.getInstance("dd-MMM-yyyy").format(System.currentTimeMillis( ));
	Calendar cal = Calendar.getInstance();
	SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
	String currentTime = dateFormat.format(cal.getTime()).replaceAll(":","-");
	public String path =  getClass().getClassLoader().getResource(".").getPath().toString();
	
	public static void main(String[] args) throws Exception{
		RunTests test = new RunTests();
		test.start();
	}
	public void start() throws Exception{
		Util getUtils = new Util();
		suiteXLS.removeColumn("Test_Steps", 9);
		suiteXLS.addColumn("Test_Steps", "Result");
		int rowCountTestCases = suiteXLS.getRowCount("Test_Cases");
		int rowCountTestSteps = suiteXLS.getRowCount("Test_Steps");
		for(int currentRow=2; currentRow<=rowCountTestCases; currentRow++){
			if(suiteXLS.getCellData("Test_Cases", "Runmode", currentRow).equals("Y")){
				String testCaseId = suiteXLS.getCellData("Test_Cases", "TCID", currentRow).trim().toString();
				String testCaseName = suiteXLS.getCellData("Test_Cases", "Test_Case", currentRow).trim().toString();
				APPLICATION_LOGS.info("-----------------------------------------------------------------------------");
				APPLICATION_LOGS.info("Starting Test Case: " + testCaseName);
				for(int testStepNo=2; testStepNo<=rowCountTestSteps;testStepNo++){
					if(testCaseId.equals(suiteXLS.getCellData("Test_Steps", "TCID", testStepNo).trim())){
						getUtils.currentTestStepRow= testStepNo;
						String keyword = suiteXLS.getCellData("Test_Steps", "Keyword", testStepNo).trim().toLowerCase();
						String testData = suiteXLS.getCellData("Test_Steps", "Data", testStepNo).trim();
						String objectID = suiteXLS.getCellData("Test_Steps", "Object_ID", testStepNo).trim().toLowerCase();
						String objectLocatorType = suiteXLS.getCellData("Test_Steps", "Locator_Type", testStepNo).trim().toString();
						String objectLocator = suiteXLS.getCellData("Test_Steps", "Locator_Value", testStepNo).trim();
						getUtils.executeKeyword(keyword, objectLocatorType, objectLocator, testData);
					}
				}
			}
		}
		String testResultsFolderName = getUtils.createFolder(automatedTestsFolder, "TestResults").toString();//create test results folder
		String currentResultsFolderName = getUtils.createFolder(testResultsFolderName, currentDate).toString();//create folder with todays date within above folder
		getUtils.copyFile(xlFilePath, currentResultsFolderName, "Results_" + currentTime + ".xlsx");//copy test cases XL file to test results folder.
		suiteXLS.removeColumn("Test_Steps", 9);
	}
}

	 

