package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class ListNeighbourPagerAdapter extends FragmentPagerAdapter {

    public NeighbourFragment mNeighbourgFragment = new NeighbourFragment();
    public FavoriteNeighboursFragment mNeighbourgFavorisFragment = new FavoriteNeighboursFragment();
    public ListNeighbourPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * getItem is called to instantiate the fragment for the given page.
     * @param position
     * @return
     */
    @Override
    //public Fragment getItem(int position) {return NeighbourFragment.newInstance();}

    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return mNeighbourgFragment;
                // Update for use fragment for favorie list
            case 1:
                return mNeighbourgFavorisFragment;
        }
        return null;
    }

    /**
     * get the number of pages
     * @return
     */
    @Override
    // Add one page for favoris list
    public int getCount() {
        return 2;
    }
}