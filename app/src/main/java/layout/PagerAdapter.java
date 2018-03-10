package layout;

/**
 * Created by Aparna on 03-03-2018.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    Bundle b;
    public PagerAdapter(FragmentManager fm, int NumOfTabs,Bundle b) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.b=b;
    }

    @Override
    public Fragment getItem(int position) {
        HashMap<String, float[]> route1= (HashMap<String, float[]>) b.getSerializable("route1");
        HashMap<String, float[]> route2= (HashMap<String, float[]>) b.getSerializable("route2");
        HashMap<String, float[]> route3= (HashMap<String, float[]>) b.getSerializable("route3");
        String email=b.getString("email");

        Set routeset1 = route3.keySet();
        for (Iterator in = routeset1.iterator(); in.hasNext(); ) {
            String key = (String) in.next();
            float[] value = route3.get(key);
            Log.d("Route3inpa:", key + "lat " + value[0]+"long "+value[1]);
        }
        Bundle bundle=new Bundle();

        switch (position) {
            case 0:

                tab_fragment_1 tab1 = new tab_fragment_1();
                bundle.putSerializable("route1",route1);
                bundle.putString("email",email);
                tab1.setArguments(bundle);
                return tab1;
            case 1:
                bundle.putSerializable("route2",route2);
                bundle.putString("email",email);
                tab_fragment_2 tab2 = new  tab_fragment_2();
                tab2.setArguments(bundle);
                return tab2;
            case 2:
                bundle.putSerializable("route3",route3);
                bundle.putString("email",email);
                tab_fragment_3 tab3 = new tab_fragment_3();
                tab3.setArguments(bundle);
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}