package neeedo.imimaprx.htw.de.neeedo;

import android.graphics.Point;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.WindowManager;

import com.robotium.solo.Solo;

public class TestCase extends ActivityInstrumentationTestCase2<MainActivity> {
    private Solo solo;
    private int shortDelay = 1000;
    private int longDelay = 5000;

    public TestCase() {
        super(MainActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
        solo.setActivityOrientation(Solo.PORTRAIT);

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
            }
        });
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    @MediumTest
    public void testNewDemand() {
        solo.goBack();
        solo.waitForActivity(MainActivity.class, shortDelay);
        solo.clickOnActionBarHomeButton();
        solo.waitForView(R.layout.activity_main, 0, shortDelay, true);

        //clickOnScreen(3, 9);
        solo.clickLongOnScreen(300, 994);

        solo.waitForDialogToOpen(longDelay);
    }

    @MediumTest
    public void testButtonNewDemand() {
        solo.goBack();
        solo.waitForActivity(MainActivity.class, shortDelay);
        solo.waitForView(R.layout.activity_main, 0, shortDelay, true);
        solo.clickOnView(solo.getView(R.id.btnNewDemand));
        solo.waitForDialogToOpen(longDelay);
    }

    @MediumTest
    public void testButtonNewOffer() {
        solo.goBack();
        solo.waitForActivity(MainActivity.class, shortDelay);
        solo.waitForView(R.layout.activity_main, 0, shortDelay, true);
        solo.clickOnView(solo.getView(R.id.btnNewOffer));
        solo.waitForDialogToOpen(longDelay);
    }

    @MediumTest
    public void testListDemands() {
        solo.goBack();
        solo.waitForActivity(MainActivity.class, shortDelay);
        solo.clickOnActionBarHomeButton();
        solo.waitForView(R.layout.activity_main, 0, shortDelay, true);

        //clickOnScreen(3, 10);
        solo.clickLongOnScreen(300, 1079);

        solo.waitForDialogToOpen(longDelay);
    }

    @MediumTest
    public void testHome() {
        solo.goBack();
        solo.waitForActivity(MainActivity.class, shortDelay);
        solo.clickOnActionBarHomeButton();
        solo.waitForView(R.layout.activity_main, 0, shortDelay, true);

        //clickOnScreen(3, 3);
        solo.clickLongOnScreen(300, 300);

        solo.waitForDialogToOpen(longDelay);
    }

    @MediumTest
    public void testNewOffer() {
        solo.goBack();
        solo.waitForActivity(MainActivity.class, shortDelay);
        solo.clickOnActionBarHomeButton();
        solo.waitForView(R.layout.activity_main, 0, shortDelay, true);

        //clickOnScreen(3, 5);
        solo.clickLongOnScreen(300, 677);

        solo.waitForDialogToOpen(longDelay);
    }

    @MediumTest
    public void testListOffers() {
        solo.goBack();
        solo.waitForActivity(MainActivity.class, shortDelay);
        solo.clickOnActionBarHomeButton();
        solo.waitForView(R.layout.activity_main, 0, shortDelay, true);

        //clickOnScreen(3, 8);
        solo.clickLongOnScreen(300, 772);

        solo.waitForDialogToOpen(longDelay);
    }

    private void clickOnScreen(int xFac, int yFac) {
        Point deviceSize = new Point();
        solo.getCurrentActivity().getWindowManager().getDefaultDisplay().getSize(deviceSize);
        int screenWidth = deviceSize.x;
        int screenHeight = deviceSize.y;

        int y = screenHeight / 20;
        int x = screenWidth / 20;

        solo.clickLongOnScreen(x * xFac, y * yFac);
    }
}
