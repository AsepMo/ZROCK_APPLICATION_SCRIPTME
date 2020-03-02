package zrock.application.scriptme.fragments;

import zrock.application.scriptme.R;

import zrock.application.scriptme.helpers.Shell;
import static zrock.application.scriptme.helpers.Shell.Result;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.*;
import android.graphics.*;


public class RunnerFragment extends Fragment
{
	
    private View parentView;
	private TextView script;
    private TextView stdout;
    private TextView stderr;
    private TextView status;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
        parentView = inflater.inflate(R.layout.fragment_application_scriptme_runner, container, false);
		script = (TextView) parentView.findViewById(R.id.script);	
		stdout = (TextView) parentView.findViewById(R.id.stdout);
        stderr = (TextView) parentView.findViewById(R.id.stderr);
        status = (TextView) parentView.findViewById(R.id.status);
		
        return parentView;
    }
	
	public void change(String currentPath){
		script.setText(currentPath);
		script.setTextColor(Color.GREEN);
	}
}
