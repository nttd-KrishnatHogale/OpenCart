package API.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReportManager implements ITestListener
{
    public ExtentSparkReporter sparkReporter; // UI of report
    public ExtentReports extent;	// Common information such as OS, model name or some other common details
    public ExtentTest test;	// Creating entries in report.

    String repName;

    public void onStart(ITestContext testContext)
    {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());//time stamp
        repName="Test-Report-"+timeStamp+".html";

        sparkReporter=new ExtentSparkReporter(".\\reports\\"+repName);//specify location of the report

        sparkReporter.config().setDocumentTitle("APIAutomationDemo"); // Title of report
        sparkReporter.config().setReportName("Macaseinou Demo API Environment"); // name of the report
        sparkReporter.config().setTheme(Theme.DARK);

        //printing from where test was executed.
        extent=new ExtentReports();
        extent.attachReporter(sparkReporter);
        extent.setSystemInfo("Application", "Macaseinou Demo Store API Application");
        extent.setSystemInfo("Operating System", System.getProperty("os.name"));
        //extent.setSystemInfo("User Name", System.getProperty("user.name"));
        extent.setSystemInfo("Environemnt","QA");
        extent.setSystemInfo("User Name","Mayur Karote");
    }

    /*
    public void onTestSuccess(ITestResult result)
    {
        test=extent.createTest(result.getName());
        test.assignCategory(result.getMethod().getGroups());
        test.createNode(result.getName());
        test.log(Status.PASS, "Test Passed");
    }
    */
    public void onTestSuccess(ITestResult result)
    {
        test=extent.createTest(result.getName());
        test.assignCategory(result.getMethod().getGroups());
        test.assignCategory(result.getTestClass().getName());
        test.createNode(result.getName());
        test.log(Status.PASS, "Test Passed");
    }

    public void onTestFailure(ITestResult result)
    {
        test=extent.createTest(result.getName());
        test.createNode(result.getName());
        test.assignCategory(result.getMethod().getGroups());
        test.assignCategory(result.getTestClass().getName());
        test.log(Status.FAIL, "Test Failed");
        test.log(Status.FAIL, result.getThrowable().getMessage());
        test.log(Status.FAIL, result.getThrowable().fillInStackTrace());
        //test.log(LogStatus.INFO/ERROR, ExceptionUtils.getStackTrace(NULL))
    } //mayur

    public void onTestSkipped(ITestResult result)
    {
        test=extent.createTest(result.getName());
        test.createNode(result.getName());
        test.assignCategory(result.getMethod().getGroups());
        test.assignCategory(result.getTestClass().getName());
        test.log(Status.SKIP, "Test Skipped");
        test.log(Status.SKIP, result.getThrowable().getMessage());
    }

    // unless flush method is called, report will not be generated.
    public void onFinish(ITestContext testContext)
    {
        extent.flush();
    }

}
