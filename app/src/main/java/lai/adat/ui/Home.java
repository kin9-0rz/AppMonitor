package lai.adat.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;

import org.appmonitor.R;
import lai.adat.ui.fragments.BlankFragment;
import lai.adat.ui.fragments.appmanager.AppManagerFragment;
import lai.adat.ui.fragments.monitor.MonitorFragment;
import lai.adat.ui.fragments.phoneinfo.PhoneInfoFragment;

public class Home extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment Identifiers
     */

    public final static String[] TITLE_ARRAYS = new String[]{
            "手机信息",
            "应用管理",
            "监控列表",
    };
    public static final int PHONE_INFO_FRAGMENT = 0;
    public static final String PHONE_INFO_FRAGMENT_TITLE = "手机信息";
    public static final int APP_MANAGER_FRAGMENT = 1;
    public static final String APP_MANAGER_FRAGMENT_TITLE = "应用管理";
    public static final int MONITOR_FRAGMENT = 2;
    public static final String MONITOR_FRAGMENT_TITLE = "监控列表";
    public static final int PLACEHOLDER_FRAGMENT = 3;
    public static final String PLACEHOLDER_FRAGMENT_TITLE = "EXAMPLE";
    private static Application application;
    private static Context context;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    public static Context getContext() {
        return context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        application = this.getApplication();
        context = this.getApplicationContext();

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        Log.d("LDroid", "Home.onNavigationDrawerItemSelected " + position);
        FragmentManager fragmentManager = getFragmentManager();

        Fragment fragment = null;

        switch (position) {
            case PHONE_INFO_FRAGMENT:
                fragment = PhoneInfoFragment.newInstance(position + 1);
                break;
            case APP_MANAGER_FRAGMENT:
                fragment = AppManagerFragment.newInstance(position + 1);
                break;
            case MONITOR_FRAGMENT:
                fragment = MonitorFragment.newInstance(position + 1);
                break;
            default:
                fragment = BlankFragment.newInstance(position + 1);
        }

        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();

    }

    /**
     * 选择后，侧边拦面板的标题
     * 需要去 PhoneInfoFragment.onCreateView 修改标题
     * <p/>
     * TODO 也许放到一个数组里面去比较好。
     *
     * @param number
     */
    public void onSectionAttached(int number) {
        Log.d("LDROID", "Home.onSectionAttached -> " + String.valueOf(number));

        switch (number) {
            case PHONE_INFO_FRAGMENT:
                mTitle = PHONE_INFO_FRAGMENT_TITLE;
                break;
            case APP_MANAGER_FRAGMENT:
                mTitle = APP_MANAGER_FRAGMENT_TITLE;
                break;
            case MONITOR_FRAGMENT:
                mTitle = MONITOR_FRAGMENT_TITLE;
                break;
            default:
                mTitle = PLACEHOLDER_FRAGMENT_TITLE;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        restoreActionBar();
        super.onCreateContextMenu(menu, v, menuInfo);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        if (!mNavigationDrawerFragment.isDrawerOpen()) {
//            // Only show items in the action bar relevant to this screen
//            // if the drawer is not showing. Otherwise, let the drawer
//            // decide what to show in the action bar.
//            getMenuInflater().inflate(R.menu.main, menu);
//            restoreActionBar();
//            return true;
//        }
//        return super.onCreateOptionsMenu(menu);
//    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


}