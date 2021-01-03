package corp.fredkiss.smart_commerce.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class JournalAdapter extends FragmentPagerAdapter {

        /**
         * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
         * one of the sections/tabs/pages.
         */
        private  final  List<Fragment> mFragmentList = new ArrayList<>();

        public JournalAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return mFragmentList.size();
        }

        public void addFragment(Fragment f) {
            mFragmentList.add(f);
        }

}
