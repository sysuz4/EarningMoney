package com.example.asus.earingmoney.menuActionProvider;

import android.content.Context;
import android.support.v4.view.ActionProvider;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

public class meFragementActionProvider extends ActionProvider {
    /**
     * Creates a new instance.
     *
     * @param context Context for accessing resources.
     */
    private Context context;

    public meFragementActionProvider(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View onCreateActionView() {
        return null;
    }

    @Override
    public void onPrepareSubMenu(SubMenu subMenu) {
        subMenu.clear();
    }

    @Override
    public boolean hasSubMenu() {
        return true;
    }
}
