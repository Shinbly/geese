package u_bordeaux.etu.geese;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Class ViewPagerAdapter, manage the display of the two fragments FragmentEdit and FragmentFilters
 */

public class ViewPagerAdapter extends FragmentPagerAdapter{

    /**
     * The list of all the fragments managed by the adapter
     */
    private final List<Fragment> fragmentList = new ArrayList<>();


    /**
     * The list of the fragments's names
     */
    private final List<String> fragmentListTitles = new ArrayList<>();




    /**
     * Constructor
     * Summons the constructor of the superclass
     * @param fm
     */
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }


    @Override
    public int getCount() {
        return fragmentListTitles.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentListTitles.get(position);
    }


    /**
     * Method addFragment
     * Add a fragment to the list of fragments
     * @param fragment the fragment to add to the adapter
     * @param title the title of the fragment
     */
    public void addFragment(Fragment fragment, String title) {
        fragmentList.add(fragment);
        fragmentListTitles.add(title);
    }
}