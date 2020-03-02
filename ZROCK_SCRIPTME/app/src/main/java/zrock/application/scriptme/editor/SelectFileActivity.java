package zrock.application.scriptme.editor;

import zrock.application.scriptme.R;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import zrock.application.scriptme.editor.adapter.*;
import zrock.application.scriptme.editor.util.*;

/**
 * Created by Vlad on 9/24/13.
 */
public class SelectFileActivity extends Activity implements AdapterView.OnItemClickListener {
    private String currentFolder;
    private ListView listView;
    private boolean wantAFile, wantAFolder;

    // The android SD card root path
    public static final String SD_CARD_ROOT =
            Environment.getExternalStorageDirectory()
                    .getAbsolutePath();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_file);
        final Actions action = (Actions) getIntent().getExtras().getSerializable("action");
        wantAFile = action == Actions.SelectFile;
        wantAFolder = action == Actions.SelectFolder;

        this.listView = (ListView) findViewById(android.R.id.list);
        this.listView.setOnItemClickListener(this);

        String path = getIntent().getExtras().getString("path");
        if (TextUtils.isEmpty(path)) {
            new UpdateList().execute(SD_CARD_ROOT);
        } else {
            new UpdateList().execute(path);
        }
    }

    void returnData(String path) {
        final Intent returnIntent = new Intent();
        returnIntent.putExtra("path", path);
        setResult(RESULT_OK, returnIntent);
        // finish the activity
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent,
                            View view, int position, long id) {
        final String name = ((TextView) view.findViewById(android.R.id.title)).getText().toString();
        if (name.equals("..")) {
            vaiIndietro();
            return;
        } else if (name.equals(getString(R.string.home))) {
            new UpdateList().execute(SD_CARD_ROOT);
            return;
        }

        final File selectedFile = new File(currentFolder, name);

        if (selectedFile.isFile() && wantAFile) {
            returnData(selectedFile.getAbsolutePath());
        } else if (selectedFile.isDirectory()) {
            new UpdateList().execute(selectedFile.getAbsolutePath());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_select_file, menu);
        menu.findItem(R.id.im_button).setTitle(getString(wantAFolder ? R.string.seleziona
                : android.R.string.cancel));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.im_button) {
            if (wantAFolder) {
                returnData(currentFolder);
            } else if (wantAFile) {
                returnData("");
            }
        }
        return super.onOptionsItemSelected(item);
    }

    void vaiIndietro() {
        if (currentFolder.equals("/")) {
            new UpdateList().execute(SD_CARD_ROOT);
        } else {
            File tempFile = new File(currentFolder);
            if (tempFile.isFile()) {
                tempFile = tempFile.getParentFile()
                        .getParentFile();
            } else {
                tempFile = tempFile.getParentFile();
            }
            new UpdateList().execute(tempFile.getAbsolutePath());
        }
    }

    private class UpdateList extends
            AsyncTask<String, Void, LinkedList<AdapterDetailedList.FileDetail>> {

        @Override
        protected LinkedList<AdapterDetailedList.FileDetail> doInBackground(final String... params) {
            try {

                final String path = params[0];
                if (TextUtils.isEmpty(path)) {
                    return null;
                }

                File tempFile = new File(path);
                if (tempFile.isFile()) {
                    tempFile = tempFile.getParentFile();
                }

                final File[] files = tempFile.listFiles();
                Arrays.sort(files,
                        getFileNameComparator());

                final LinkedList<AdapterDetailedList.FileDetail> fileDetails = new LinkedList<AdapterDetailedList.FileDetail>();
                final LinkedList<AdapterDetailedList.FileDetail>
                        folderDetails = new LinkedList<AdapterDetailedList.FileDetail>();
                final AbstractMap<String, File> tempList = new HashMap<String, File>();
                currentFolder = tempFile.getAbsolutePath();

                if (files != null) {
                    for (final File f : files) {
                        if (f.isHidden()) {
                            continue;
                        } else if (f.isDirectory()
                                && f.canRead()) {
                            folderDetails.add(new AdapterDetailedList.FileDetail(f.getName(),
                                    getString(R.string.folder),
                                    ""));
                        } else if (f.isFile()) {
                            final long fileSize = f.length();
                            SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy  hh:mm a");
                            String date = format.format(f.lastModified());
                            fileDetails.add(new AdapterDetailedList.FileDetail(f.getName(),
                                    FileUtils.byteCountToDisplaySize(fileSize), date));
                        }
                        tempList.put(f.getName(), f);
                    }
                }

                folderDetails.addAll(fileDetails);
                return folderDetails;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(final LinkedList<AdapterDetailedList.FileDetail> names) {
            boolean isRoot = currentFolder.equals("/");
            if (names != null) {
                listView.setAdapter(new AdapterDetailedList(getBaseContext(), names, isRoot));
            }
            super.onPostExecute(names);
        }

        public final Comparator<File> getFileNameComparator() {
            return new AlphanumComparator() {
                @Override
                public String getTheString(Object obj) {
                    return ((File) obj).getName()
                            .toLowerCase();
                }
            };
        }
    }

    public enum Actions {
        SelectFile, SelectFolder
    }
}
