package zrock.application.scriptme;

import zrock.application.scriptme.R;
import zrock.application.scriptme.activities.*;
import zrock.application.scriptme.fragments.*;
import zrock.application.scriptme.utils.Root;
import zrock.application.engine.Engine;
import zrock.application.engine.view.ResideMenu;
import zrock.application.engine.view.ResideMenuItem;
import zrock.application.engine.view.TouchDisableView;
import zrock.application.engine.view.menu.v1.*;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.graphics.Color;
import android.content.Intent;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.List;
import java.util.ArrayList;

public class ScriptMeActivity extends FragmentActivity implements View.OnClickListener
{

	/*This ZROCK APPLICATION SCRIPTME*/
    private ResideMenu resideMenu;
    private ScriptMeActivity mContext;
    private ResideMenuItem itemHome;
    private ResideMenuItem itemScriptMe;
    private ResideMenuItem itemEditor;
    private ResideMenuItem itemRunner;
	private RadialMenuWidget pieMenu;

	public RadialMenuItem menuItem, menuCloseItem, menuExpandItem;
	public RadialMenuItem Home, Right, Left;
	private List<RadialMenuItem> children = new ArrayList<RadialMenuItem>();


    /**
     * Called when the activity is first created.
     */
	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_scriptme);
		Root.requestRoot();
		mContext = this;
        setUpMenu();
        if (savedInstanceState == null)
            changeFragment(new ScriptMeFragment());
    }

    private void setUpMenu()
	{

        // attach to current activity;
        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.activity_background);
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip. 
        resideMenu.setScaleValue(0.6f);

        // create menu items;
        itemHome     = new ResideMenuItem(this, android.R.drawable.ic_input_add,     "Home");
        itemScriptMe  = new ResideMenuItem(this, android.R.drawable.ic_input_add,  "Tweaks");
        itemEditor = new ResideMenuItem(this, android.R.drawable.ic_input_add,    "Editor");
        itemRunner = new ResideMenuItem(this, android.R.drawable.ic_input_add,  "Run Script");

        itemHome.setOnClickListener(this);
        itemScriptMe.setOnClickListener(this);
        itemEditor.setOnClickListener(this);
        itemRunner.setOnClickListener(this);

        resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemScriptMe, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemEditor, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(itemRunner, ResideMenu.DIRECTION_RIGHT);

        // You can disable a direction by setting ->
        // resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
		pieMenu = new RadialMenuWidget(this);
		menuCloseItem = new RadialMenuItem(getString(R.string.close), null);
		menuCloseItem.setDisplayIcon(android.R.drawable.ic_menu_close_clear_cancel);
		menuItem = new RadialMenuItem(getString(R.string.zrock_radial_create), null);
		menuItem.setDisplayIcon(R.drawable.ic_edit_script);
		menuItem.setOnMenuItemPressed(new RadialMenuItem.RadialMenuItemClickListener() {
				@Override
				public void execute()
				{
					CreateActivity.start(ScriptMeActivity.this);
					pieMenu.dismiss();
				}
			});

		Home = new RadialMenuItem(getString(R.string.zrock_radial_home), null);
		Home.setDisplayIcon(R.drawable.ic_action_github);
		Home.setOnMenuItemPressed(new RadialMenuItem.RadialMenuItemClickListener() {
				@Override
				public void execute()
				{
					// Can edit based on preference. Also can add animations
					// here.		
					//pieMenu.dismiss();
				}
			});

		Right = new RadialMenuItem(getString(R.string.zrock_radial_right), null);
		Right.setDisplayIcon(android.R.drawable.ic_media_next);
		Right.setOnMenuItemPressed(new RadialMenuItem.RadialMenuItemClickListener() {
				@Override
				public void execute()
				{
					// Can edit based on preference. Also can add animations
					// here.
					resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
					pieMenu.dismiss();
				}
			});

		Left = new RadialMenuItem(getString(R.string.zrock_radial_left), null);
		Left.setDisplayIcon(android.R.drawable.ic_media_previous);
		Left.setOnMenuItemPressed(new RadialMenuItem.RadialMenuItemClickListener() {
				@Override
				public void execute()
				{
					// Can edit based on preference. Also can add animations
					// here.
					resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
					pieMenu.dismiss();
				}
			});

		menuExpandItem = new RadialMenuItem(getString(R.string.zrock_radial_expandable), getString(R.string.zrock_radial_expandable));
		children.add(Home);
		children.add(Right);
		children.add(Left);
		menuExpandItem.setMenuChildren(children);
		menuCloseItem.setOnMenuItemPressed(new RadialMenuItem.RadialMenuItemClickListener() {
				@Override
				public void execute()
				{
					// menuLayout.removeAllViews();
					pieMenu.dismiss();
				}
			});

		// pieMenu.setDismissOnOutsideClick(true, menuLayout);
		pieMenu.setAnimationSpeed(0L);
		pieMenu.setSourceLocation(200, 200);
		pieMenu.setIconSize(15, 30);
		pieMenu.setTextSize(13);
		pieMenu.setOutlineColor(Color.BLACK, 225);
		pieMenu.setInnerRingColor(Color.GRAY, 180);
		pieMenu.setOuterRingColor(Color.DKGRAY, 180);
		pieMenu.setSelectedColor(Color.GREEN,180);
		//pieMenu.setHeader("Test Menu", 20);
		pieMenu.setCenterCircle(menuCloseItem);

		pieMenu.addMenuEntry(new ArrayList<RadialMenuItem>() {
				{
					add(menuItem);
					add(menuExpandItem);
				}
			});

		findViewById(R.id.main_fragment).setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View view)
				{
					pieMenu.show(view);
				}
			});
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
	{
        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View view)
	{

        if (view == itemHome)
		{
            new android.os.Handler().postDelayed(new Runnable(){
					@Override
					public void run(){
						android.os.Process.killProcess(android.os.Process.myPid());
					}
				},1200);
        }
		else if (view == itemScriptMe)
		{
			TweaksActivity.start(ScriptMeActivity.this);
        }
		else if (view == itemEditor)
		{
			EditorActivity.start(ScriptMeActivity.this);
	    }
		else if (view == itemRunner)
		{
            RunnerActivity.start(ScriptMeActivity.this);
        }

        resideMenu.closeMenu();
    }

    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu()
		{
            Toast.makeText(mContext, "Menu is opened!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void closeMenu()
		{
            Toast.makeText(mContext, "Menu is closed!", Toast.LENGTH_SHORT).show();
        }
    };

    private void changeFragment(Fragment targetFragment)
	{
        resideMenu.clearIgnoredViewList();
        getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.main_fragment, targetFragment, "fragment")
			.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
			.commit();
    }

    // What good method is to access resideMenu？
    public ResideMenu getResideMenu()
	{
        return resideMenu;
    }

	public RadialMenuWidget getRadialMenu()
	{
		return pieMenu;
	}

	@Override
	protected void onResume()
	{
		// TODO: Implement this method
		super.onResume();
		//findViewById(R.id.main_fragment).post(new Runnable() {
		//public void run()
		//{
		//pieMenu.show(findViewById(R.id.main_fragment));
		//}
		//});
	}

	@Override
	protected void onDestroy()
	{
		Log.v("MainActivity", "onDestroy");
		super.onDestroy();
	}
}

