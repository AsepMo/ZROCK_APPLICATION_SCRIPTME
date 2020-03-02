package zrock.application.scriptme.activities;

import zrock.application.scriptme.R;
import zrock.application.scriptme.activities.*;
import zrock.application.scriptme.editor.*;
import zrock.application.scriptme.editor.SelectFileActivity;
import zrock.application.scriptme.editor.PreferenceAbout;
import zrock.application.scriptme.editor.event.NewFileOpened;
import zrock.application.scriptme.editor.event.FileSavedEvent;
import zrock.application.scriptme.editor.event.FileSelectedEvent;
import zrock.application.scriptme.editor.event.ErrorOpeningFileEvent;
import de.greenrobot.event.EventBus;

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
import android.view.inputmethod.InputMethodManager;

public class EditorActivity extends FragmentActivity
{

	public static void start(Context mContext){
		Intent mApplication = new Intent(mContext, EditorActivity.class);
		mContext.startActivity(mApplication);
	}
	
	private String TAG = "A0A";
    public static final int SELECT_FILE_CODE = 121;

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
        setContentView(R.layout.activity_application_scriptme_editor);
        //
        setupDrawerLayout();
        // Replace fragment
        getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.fragment_editor,  new NoFileOpenedFragment())
			.commit();
        /* First Time we open this activity */
        if (savedInstanceState == null) {
            // Open
            mDrawerLayout.openDrawer(Gravity.START);
            // Set the default title
            getActionBar().setTitle(getString(R.string.app_activity_scriptme_editor));
        }
        //
        receiveIntent();
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
        // Register the Event Bus for events
        EventBus.getDefault().register(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPause() {
        super.onPause();
        // Unregister the Event Bus
        EventBus.getDefault().unregister(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onDestroy() {
        try {
            closeKeyBoard();
        } catch (NullPointerException e) {
            Log.e(TAG, e.getMessage(), e);
        }
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
					startActivityForResult(new Intent(EditorActivity.this, SelectFileActivity.class)
										   .putExtra("path", "")
										   .putExtra("action", SelectFileActivity.Actions.SelectFile),
										   SELECT_FILE_CODE);
					return true;
				case R.id.im_info:
					startActivity(new Intent(this, PreferenceAbout.class));
					return true;
				default:
					return super.onOptionsItemSelected(item);
			}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == SELECT_FILE_CODE) {
			String path = data.getStringExtra("path");
			if(!TextUtils.isEmpty(path)){
				EventBus.getDefault().postSticky(new NewFileOpened(path));
            }
        }
    }

    /**
     *
     * @param event
     */
    public void onEvent(FileSelectedEvent event){
        // Close the drawer
        mDrawerLayout.closeDrawer(Gravity.LEFT);
        // Replace fragment
        getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.fragment_editor,  EditorFragment.newInstance(event.getPath()))
			.commit();
    }

    /**
     * When a file is saved
     * Invoked by the EditorFragment
     * @param event The event called
     */
    public void onEvent(FileSavedEvent event){
        try {
            closeKeyBoard();
        } catch (NullPointerException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        // Get intent, action and MIME type
        final Intent intent = getIntent();
        final String action = intent.getAction();
        final String type = intent.getType();

        if (Intent.ACTION_VIEW.equals(action)
			|| Intent.ACTION_EDIT.equals(action)
			|| Intent.ACTION_PICK.equals(action)
			&& type != null) {
            //This Activity was called by startActivityForResult
            final Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
            // finish the activity
            finish();
        } else {
            //This Activity was called by startActivity
            //
            mDrawerLayout.openDrawer(Gravity.LEFT);
            //
            getActionBar().setTitle(getString(R.string.nome_app_turbo_editor));
            // Replace fragment
            getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.fragment_editor,  new NoFileOpenedFragment())
				.commit();
        }
    }

    /**
     *
     */
    public void onEvent(ErrorOpeningFileEvent event){
        //
        mDrawerLayout.openDrawer(Gravity.LEFT);
        //
        getActionBar().setTitle(getString(R.string.nome_app_turbo_editor));
        // Replace fragment
        getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.fragment_editor,  new NoFileOpenedFragment())
			.commit();
    }

    private void closeKeyBoard() throws NullPointerException {
        // Central system API to the overall input method framework (IMF) architecture
        InputMethodManager inputManager =
			(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        // Base interface for a remotable object
        IBinder windowToken = getCurrentFocus().getWindowToken();

        // Hide type
        int hideType = InputMethodManager.HIDE_NOT_ALWAYS;

        // Hide the KeyBoard
        inputManager.hideSoftInputFromWindow(windowToken, hideType);
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
        final String defaultTitle = getString(R.string.nome_app_turbo_editor);
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        final String action = intent.getAction();
        final String type = intent.getType();

        if (Intent.ACTION_VIEW.equals(action)
			|| Intent.ACTION_EDIT.equals(action)
			|| Intent.ACTION_PICK.equals(action)
			&& type != null) {
            // Post the NewFileOpened Event
            EventBus.getDefault().postSticky(new NewFileOpened(intent.getData().getPath()));
        }
    }

    /**
     *
     */
    private void receiveIntent(){
        // Get intent, action and MIME type
        final Intent intent = getIntent();
        final String action = intent.getAction();
        final String type = intent.getType();

        if (Intent.ACTION_VIEW.equals(action)
			|| Intent.ACTION_EDIT.equals(action)
			|| Intent.ACTION_PICK.equals(action)
			&& type != null) {
            // Post the NewFileOpened Event
            EventBus.getDefault().postSticky(new NewFileOpened(intent.getData().getPath()));
        }
    }
}
