package zrock.application.scriptme.fragments;

import zrock.application.scriptme.R;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class ScriptMeFragment extends Fragment
{

    private View parentView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
        parentView = inflater.inflate(R.layout.fragment_application_scriptme, container, false);
       

        return parentView;
    }


}
