package lai.adat.ui.fragments.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import org.appmonitor.R;

public class AppAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater = null;
    private List<AppDummyItem> mPackageInfo = null;
    private boolean[] isChecked;

    public AppAdapter(Context context, List<AppDummyItem> packageInfo, boolean[] isSelected) {
        super();
        mLayoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPackageInfo = packageInfo;
        isChecked = isSelected;
    }

    @Override
    public int getCount() {
        return mPackageInfo.size();
    }

    @Override
    public Object getItem(int position) {
        return mPackageInfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ListViewItem item;

        if (convertView == null) {
            view = mLayoutInflater.inflate(R.layout.layout_list, null);
            item = new ListViewItem(view);
            view.setTag(item);
        } else {
            view = convertView;
            item = (ListViewItem) view.getTag();
        }

        AppDummyItem appInfo = (AppDummyItem) getItem(position);
        item.appIcon.setImageDrawable(appInfo.getAppIcon());
        item.appPkgName.setText(appInfo.getPkgName());
//        item.checkBox.setChecked(isChecked[position]);

        return view;
    }

    class ListViewItem {

        ImageView appIcon;
        TextView appPkgName;
        CheckBox checkBox;

        public ListViewItem(View view) {
            this.appIcon = (ImageView) view.findViewById(R.id.appIcon);
            this.appPkgName = (TextView) view.findViewById(R.id.packagename);
            this.checkBox = (CheckBox) view.findViewById(R.id.isChecked);
        }
    }
}
