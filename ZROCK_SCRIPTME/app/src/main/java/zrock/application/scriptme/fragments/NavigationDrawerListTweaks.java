package zrock.application.scriptme.fragments;

import zrock.application.scriptme.R;
import zrock.application.scriptme.adapters.*;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class NavigationDrawerListTweaks extends Fragment
{

    private View rootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView =  inflater.inflate(R.layout.fragment_navigation_drawer_tweaks,container,false);
        ListView summonerList = (ListView)rootView.findViewById(R.id.list_scriptme_runner);
        summonerList.setAdapter(new ListViewAdapter(getActivity()));
        return rootView;
    }


}
