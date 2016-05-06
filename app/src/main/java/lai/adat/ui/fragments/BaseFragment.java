package lai.adat.ui.fragments;

import android.app.Fragment;
import android.net.Uri;

/**
 * Created by bin on 2/19/16.
 *
 * 基础 Fragment
 */
public class BaseFragment extends Fragment {

    public static final String SECTION_NUM = "SECTION_NUM";

    public BaseFragment() {}

    public  interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
        public void onFragmentInteraction(String id);
        public void onFragmentInteraction(int actionId);
    }
}
