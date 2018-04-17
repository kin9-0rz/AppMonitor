package me.mikusjelly.amon.ui.fragments.phoneinfo;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import java.util.ArrayList;

import me.mikusjelly.amon.R;
import me.mikusjelly.amon.ui.Home;
import me.mikusjelly.amon.ui.fragments.BaseFragment;


public class PhoneInfoFragment extends BaseFragment {

    private AbsListView mListView;
    private ListAdapter mAdapter;

    public PhoneInfoFragment() {
        super();
    }

    public static PhoneInfoFragment newInstance(int sectionNum) {
        PhoneInfoFragment fragment = new PhoneInfoFragment();
        Bundle args = new Bundle();
        args.putInt(SECTION_NUM, sectionNum);
        fragment.setArguments(args);
        return fragment;
    }

    private static ArrayList<String> getPhoneInfo(Context ctx) {
        ArrayList<String> strings = new ArrayList<String>();

        TelephonyManager manager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        String line1Number = manager.getLine1Number() + "";

        strings.add("===== HARDWARE INFO =====");
        strings.add("MANUFACTURER : " + Build.MANUFACTURER);
        strings.add("BRAND : " + Build.BRAND);
        strings.add("MODEL : " + Build.MODEL);
        strings.add("HARDWARE : " + Build.HARDWARE);
        strings.add("PRODUCT : " + Build.PRODUCT);
        strings.add("DEVICE : " + Build.DEVICE);
        strings.add("SERIAL : " + Build.SERIAL);
        strings.add("BOARD : " + Build.BOARD);
        strings.add("RadioVersion : " + Build.getRadioVersion());
        strings.add("IMEI : " + manager.getDeviceId());
        strings.add("line1Number : " + line1Number);

        strings.add("====== OS INFO ======");
        strings.add("FINGERPRINT : " + Build.FINGERPRINT);
        strings.add("HOST : " + Build.HOST);
        strings.add("VERSION.RELEASE : " + Build.VERSION.RELEASE);
        strings.add("VERSION.SDK_INT : " + Build.VERSION.SDK_INT);
        strings.add("DISPLAY : " + Build.DISPLAY);
        strings.add("TYPE : " + Build.TYPE);
        strings.add("TAGS : " + Build.TAGS);

        return strings;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 注意：Adapter 必须在这里初始化，如果在onCreateView里面初始化，则会变成白色的文字
        mAdapter = new ArrayAdapter<PhoneInfoContent.DummyItem>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, PhoneInfoContent.ITEMS);

        initPhoneInfoContent(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_phoneinfo_list, container, false);

        mListView = (AbsListView) view.findViewById(android.R.id.list);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PhoneInfoContent.DummyItem item = PhoneInfoContent.ITEMS.get(position);
                ClipboardManager cm = (ClipboardManager) Home.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setPrimaryClip(ClipData.newPlainText("Copy Item", item.getContent()));

            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    void initPhoneInfoContent(Context context) {
        if (PhoneInfoContent.ITEMS.size() > 0) {
            return;
        }

        ArrayList<String> strings = getPhoneInfo(context);
        int id = 1;
        for (String str : strings) {
            PhoneInfoContent.addItem(new PhoneInfoContent.DummyItem(String.valueOf(id++), str));
        }
    }
}
