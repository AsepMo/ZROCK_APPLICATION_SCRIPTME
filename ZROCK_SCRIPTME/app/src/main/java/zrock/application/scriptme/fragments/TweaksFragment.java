package zrock.application.scriptme.fragments;

import zrock.application.scriptme.R;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class TweaksFragment extends Fragment
{

    private View parentView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
        parentView = inflater.inflate(R.layout.fragment_application_scriptme_tweaks, container, false);
        return parentView;
    }
}
