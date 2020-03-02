package zrock.application.scriptme.editor;

import zrock.application.scriptme.R;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Vlad on 9/23/13.
 */
public class NoFileOpenedFragment extends Fragment {

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Out custom layout
        View rootView = inflater.inflate(R.layout.fragment_no_file_open, container, false);
        return rootView;
    }
}

