package neeedo.imimaprx.htw.de.neeedo;

import android.app.Application;
import android.test.ApplicationTestCase;

import neeedo.imimaprx.htw.de.neeedo.rest.HttpPostAsyncTask;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testExample() throws Exception {
        assertTrue("miau", true);
    }
}