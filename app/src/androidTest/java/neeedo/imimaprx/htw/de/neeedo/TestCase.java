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
        solo.waitForActivity(MainActivity.class, shortDelay);
        solo.clickOnActionBarHomeButton();
        solo.waitForView(R.layout.activity_main, 0, shortDelay, true);

        clickOnScreen(3, 6);

        solo.waitForDialogToOpen(longDelay);
    }

    @MediumTest
    public void testButtonNewDemand() {
        solo.waitForActivity(MainActivity.class, shortDelay);
        solo.waitForView(R.layout.activity_main, 0, shortDelay, true);
        solo.clickOnView(solo.getView(R.id.btnNewDemand));
        solo.waitForDialogToOpen(longDelay);
    }

    @MediumTest
    public void testButtonNewOffer() {
        solo.waitForActivity(MainActivity.class, shortDelay);
        solo.waitForView(R.layout.activity_main, 0, shortDelay, true);
        solo.clickOnView(solo.getView(R.id.btnNewOffer));
        solo.waitForDialogToOpen(longDelay);
    }

    @MediumTest
    public void testListDemands() {
        solo.waitForActivity(MainActivity.class, shortDelay);
        solo.clickOnActionBarHomeButton();
        solo.waitForView(R.layout.activity_main, 0, shortDelay, true);

        clickOnScreen(3, 10);

        solo.waitForDialogToOpen(longDelay);
    }

    @MediumTest
    public void testHome() {
        solo.waitForActivity(MainActivity.class, shortDelay);
        solo.clickOnActionBarHomeButton();
        solo.waitForView(R.layout.activity_main, 0, shortDelay, true);

        clickOnScreen(3, 3);

        solo.waitForDialogToOpen(longDelay);
    }

    @MediumTest
    public void testNewOffer() {
        solo.waitForActivity(MainActivity.class, shortDelay);
        solo.clickOnActionBarHomeButton();
        solo.waitForView(R.layout.activity_main, 0, shortDelay, true);

        clickOnScreen(3, 5);

        solo.waitForDialogToOpen(longDelay);
    }

    @MediumTest
    public void testListOffers() {
        solo.waitForActivity(MainActivity.class, shortDelay);
        solo.clickOnActionBarHomeButton();
        solo.waitForView(R.layout.activity_main, 0, shortDelay, true);

        clickOnScreen(3, 8);

        solo.waitForDialogToOpen(longDelay);
    }

    @MediumTest
    public void testSwiper() {
        solo.waitForActivity(MainActivity.class, shortDelay);
        solo.clickOnActionBarHomeButton();
        solo.waitForView(R.layout.activity_main, 0, shortDelay, true);

        clickOnScreen(3, 12);

        solo.waitForDialogToOpen(longDelay);
        swipe();
        solo.waitForDialogToOpen(shortDelay);
        swipe();
        solo.waitForDialogToOpen(shortDelay);
        swipe();
        solo.waitForDialogToOpen(shortDelay);
    }

    public void swipe() {
        Point deviceSize = new Point();
        solo.getCurrentActivity().getWindowManager().getDefaultDisplay().getSize(deviceSize);

        int screenWidth = deviceSize.x;
        int screenHeight = deviceSize.y;
        int fromX = deviceSize.x / 4;
        int toX = screenWidth;
        int fromY = deviceSize.y / 2;
        int toY = fromY;

        solo.drag(fromX, toX, fromY, toY, 50);
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
