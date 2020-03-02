package zrock.application.scriptme.activities;

import zrock.application.scriptme.R;
import zrock.application.scriptme.activities.*;
import zrock.application.scriptme.fragments.*;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class TweaksActivity extends FragmentActivity
{

	public static void start(Context mContext){
		Intent mApplication = new Intent(mContext, TweaksActivity.class);
		mContext.startActivity(mApplication);
	}
	/*
	 * This class provides a handy way to tie together the functionality of
	 * {@link DrawerLayout} and the framework <code>ActionBar</code> to implement the recommended
	 * design for navigation drawers.
	 */
    protected ActionBarDrawerToggle mDrawerToggle;
    /*
	 * The Drawer Layout
	 */
    protected DrawerLayout mDrawerLayout;

    /**
     * {@inheritDoc}
     */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		checkTaskRoot();
        //
        setContentView(R.layout.activity_application_scriptme_tweaks);
        //
        setupDrawerLayout();
        // Replace fragment
        getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.fragment_editor,  new ScriptMeFragment())
			.commit();
        /* First Time we open this activity */
        if (savedInstanceState == null) {
            // Open
            mDrawerLayout.openDrawer(Gravity.START);
            // Set the default title
            getActionBar().setTitle(getString(R.string.app_activity_scriptme_tweaks));
        }
        //
	}

	/**
     * {@inheritDoc}
     */
    @Override
    protected final void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

	/**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* If we clicked on the Navigation Drawer Menu item */
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else switch (item.getItemId()){
				case R.id.im_open:

					return true;
				case R.id.im_info:

					return true;
				default:
					return super.onOptionsItemSelected(item);
			}
    }

    /**
     *
     */
    private void checkTaskRoot(){
    }

    /**
     *
     */
    private void setupDrawerLayout(){
        final String defaultTitle = getString(R.string.app_activity_scriptme_tweaks);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        /* Action Bar */
        final ActionBar ab = getActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
        /* Navigation drawer */
        mDrawerToggle =
			new ActionBarDrawerToggle(
			this,
			mDrawerLayout,
			R.drawable.ic_drawer,
			R.string.nome_app_turbo_editor,
			R.string.nome_app_turbo_editor) {

			@Override
			public void onDrawerClosed(View view) {
				invalidateOptionsMenu();
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				invalidateOptionsMenu();
			}
		};
        /* link the mDrawerToggle to the Drawer Layout */
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

}
