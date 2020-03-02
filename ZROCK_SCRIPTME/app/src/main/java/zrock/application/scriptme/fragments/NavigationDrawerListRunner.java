package zrock.application.scriptme.fragments;

import zrock.application.scriptme.R;

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

public class NavigationDrawerListRunner extends Fragment implements AdapterView.OnItemClickListener
{

    private View parentView;
	private ArrayList<PluginItem> mPluginItems = new ArrayList<PluginItem>();
    private PluginAdapter mPluginAdapter;
    private ListView mListView;
    private TextView mNoPluginTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
        parentView = inflater.inflate(R.layout.fragment_navigation_drawer_runner, container, false);
        mPluginAdapter = new PluginAdapter();
        mListView = (ListView)parentView.findViewById(R.id.list_scriptme_runner);
		mNoPluginTextView = (TextView)parentView.findViewById(R.id.no_plugin);
		String pluginFolder = Environment.getExternalStorageDirectory() + "/Download";
        File file = new File(pluginFolder);
        File[] plugins = file.listFiles();
        if (plugins == null || plugins.length == 0)
		{
            mNoPluginTextView.setVisibility(View.VISIBLE);
            return parentView;
        }

        for (File plugin : plugins)
		{
            PluginItem item = new PluginItem();
            item.pluginPath = plugin.getAbsolutePath();
            mPluginItems.add(item);
		}
		mListView.setAdapter(mPluginAdapter);
        mListView.setOnItemClickListener(this);
        mPluginAdapter.notifyDataSetChanged();
		registerForContextMenu(mListView);
		return parentView;
    }

	private class PluginAdapter extends BaseAdapter
	{

        private LayoutInflater mInflater;

        public PluginAdapter()
		{
            mInflater = getActivity().getLayoutInflater();
        }

        @Override
        public int getCount()
		{
            return mPluginItems.size();
        }

        @Override
        public Object getItem(int position)
		{
            return mPluginItems.get(position);
        }

        @Override
        public long getItemId(int position)
		{
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
		{
            ViewHolder holder;
            if (convertView == null)
			{
                convertView = mInflater.inflate(R.layout.item_scriptme, parent, false);
                holder = new ViewHolder();
                holder.apkName = (TextView) convertView.findViewById(R.id.title_scriptme);
                convertView.setTag(holder);
            }
			else
			{
                holder = (ViewHolder) convertView.getTag();
            }
            PluginItem item = mPluginItems.get(position);
            holder.apkName.setText(item.pluginPath.substring(item.pluginPath.lastIndexOf(File.separatorChar) + 1));
            return convertView;
        }
    }


    private static class ViewHolder
	{
        public TextView apkName;
    }

    public static class PluginItem
	{
        public String pluginPath;

        public PluginItem()
		{
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		PluginItem items = mPluginItems.get(position);
		String path = items.pluginPath;
		RunnerFragment txtfrag = (RunnerFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_editor);
		txtfrag.change(path);

		Toast.makeText(getActivity(), items.pluginPath, Toast.LENGTH_SHORT).show();
		
	}


	
}
