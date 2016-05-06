package lai.adat.ui.fragments.appmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.appmonitor.R;
import lai.adat.ui.fragments.BaseFragment;
import lai.adat.ui.fragments.app.AppAdapter;
import lai.adat.ui.fragments.app.AppContent;
import lai.adat.ui.fragments.app.AppDummyItem;
import lai.adat.ui.fragments.app.AppManager;

/**
 * Created by bin on 2/19/16.
 * <p/>
 * 应用管理
 */
public class AppManagerFragment extends BaseFragment {


    SimpleAdapter.ViewBinder viewBinder = new SimpleAdapter.ViewBinder() {
        @Override
        public boolean setViewValue(View view, Object data, String textRepresentation) {
            if (view instanceof ImageView && data instanceof Drawable) {
                ImageView imageView = (ImageView) view;
                imageView.setImageDrawable((Drawable) data);
                return true;
            } else {
                return false;
            }
        }
    };

    ListView.OnItemClickListener onItemClickListener = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TextView viewById = (TextView) view.findViewById(R.id.package_name);
            String pkg = viewById.getText().toString();
            Uri uri = Uri.parse("package:" + pkg);
            Intent intent = new Intent(Intent.ACTION_DELETE, uri);
            startActivity(intent);
        }
    };
    private ListView mListView;
    private SimpleAdapter simpleAdapter;
    private ListView packageList;
    private List<String> hookPackageList;
    private ArrayList<AppDummyItem> appInfoList;
    private boolean[] isCheckedArr;


    public AppManagerFragment() {
        super();
    }

    public static AppManagerFragment newInstance(int sectionNum) {
        AppManagerFragment fragment = new AppManagerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(SECTION_NUM, sectionNum);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context context = this.getActivity();

        appInfoList = new ArrayList<AppDummyItem>();

//        initAppManagerContent(context);
//        initAppInfoList(context);
//        int resource = R.layout.app_list_item;
//        String[] from = new String[]{AppManager.LABEL, AppManager.PACKAGE_NAME, AppManager.ICON};
//        int[] to = new int[]{R.id.label, R.id.package_name, R.id.icon};
//
//        simpleAdapter = new SimpleAdapter(context, AppContent.packageInfos, resource, from, to);
//        simpleAdapter.setViewBinder(viewBinder);

    }

    public void initAppInfoList(Context mContext) {
        PackageManager manager = mContext.getPackageManager();

        List<PackageInfo> packages = AppManager.getInstallApp(mContext);

        for (int i = 0; i < packages.size(); i++) {
            PackageInfo info = packages.get(i);

            if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                AppDummyItem appInfo = new AppDummyItem();

                appInfo.setAppIcon(manager
                        .getApplicationIcon(info.applicationInfo));
                appInfo.setAppLabel(manager.getApplicationLabel(
                        info.applicationInfo).toString());
                appInfo.setPkgName(info.applicationInfo.packageName);

                appInfoList.add(appInfo);

            }
        }

        Collections.sort(appInfoList, new Comparator<AppDummyItem>() {
            @Override
            public int compare(AppDummyItem lhs, AppDummyItem rhs) {
                return lhs.getPkgName().compareTo(rhs.getPkgName());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_app_list, container, false);
//        mListView = (ListView) view.findViewById(R.id.app_list);
//
//        mListView.setAdapter(simpleAdapter);
//        mListView.setOnItemClickListener(onItemClickListener);
//
//        return view;

        initAppInfoList(inflater.getContext());

        View view = inflater.inflate(R.layout.activity_main, container, false);

        this.packageList = (ListView) view.findViewById(R.id.packageList);

        AppAdapter packgaeAdapter = new AppAdapter(inflater.getContext(), appInfoList, isCheckedArr);
        packageList.setAdapter(packgaeAdapter);
//        packageList.setOnItemClickListener(this);
        packageList.setAlwaysDrawnWithCacheEnabled(true);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    private void initAppManagerContent(Context context) {
        if (AppContent.packageInfos.size() > 0) {
            return;
        }

        AppContent.packageInfos = AppManager.getPackageInfoList(context);
    }
}
