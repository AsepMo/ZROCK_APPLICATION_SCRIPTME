package zrock.application.scriptme.fragments;

import zrock.application.scriptme.R;
import zrock.application.scriptme.adapters.ListViewAdapter;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.BaseAdapter;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.io.File;

public class NavigationDrawerListRunner extends Fragment
{

	private View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
        rootView = inflater.inflate(R.layout.fragment_navigation_drawer_runner, container, false);
        ListView summonerList = (ListView)rootView.findViewById(R.id.list_scriptme_runner);
        summonerList.setAdapter(new ListViewAdapter(getActivity()));
		return rootView;
    }
}
