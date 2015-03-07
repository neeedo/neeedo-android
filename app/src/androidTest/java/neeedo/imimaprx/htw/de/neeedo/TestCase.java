package neeedo.imimaprx.htw.de.neeedo;

import android.graphics.Point;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

public class TestCase extends ActivityInstrumentationTestCase2<MainActivity> {
    private Solo solo;

    public TestCase() {
        super(MainActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
        solo.setActivityOrientation(Solo.PORTRAIT);
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    public void testRun() {
        int shortDelay = 1000;
        int longDelay = 10000;

        // Wait for activity:
        // 'course.labs.threadslab.TestFrontEndActivity'
        solo.waitForActivity(MainActivity.class,
                shortDelay);

        solo.clickOnActionBarHomeButton();

        solo.waitForView(R.layout.activity_main, 0, shortDelay, true);


        // solo.waitForFragmentById(R.id.navigation_drawer);

        // solo.waitForActivity(MainActivity.class,
        //         shortDelay);


        //solo.waitForView(R.id.navigation_drawer);

        clickOnScreen(3, 5);




        /*
        solo.waitForCondition(new Condition() {
            @Override
            public boolean isSatisfied() {
                return listView.isEnabled();
            }
        }, longDelay);

        assertTrue(listView.isActivated());

*/
        // Click on taylorswift13
        // solo.clickOnView(solo.getView(android.R.id.text1));

        // Assert that: 'feed_view' is shown
        //     assertTrue("feed_view not shown!", solo.waitForView(solo
//                .getView(R.id.feed_view)));

        // Assert that: 'Taylor Swift' is shown
        //assertTrue("'Taylor Swift' is not shown!",
        //        solo.searchText("Taylor Swift"));

    }

    protected void clickOnHome() {
        solo.clickOnScreen(50, 50); //usually hits Home button
    }

    private void clickOnScreen(int xFac, int yFac) {
        Point deviceSize = new Point();
        solo.getCurrentActivity().getWindowManager().getDefaultDisplay().getSize(deviceSize);
        int screenWidth = deviceSize.x;
        int screenHeight = deviceSize.y;

        int y = screenHeight / 10;
        int x = screenWidth / 10;

        solo.clickLongOnScreen(x * xFac, y * yFac);
    }

}
