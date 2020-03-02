package zrock.application.scriptme.activities;

import zrock.application.scriptme.R;
import zrock.application.scriptme.activities.*;
import zrock.application.scriptme.fragments.*;

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

public class CreateActivity extends FragmentActivity
{
	public static void start(Context mContext){
		Intent mApplication = new Intent(mContext, CreateActivity.class);
		mContext.startActivity(mApplication);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_application_scriptme_create);
	}
}
