package zrock.application.engine;

import android.app.Activity;
import android.app.ActivityManager;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.os.Build;
import android.net.Uri;
import android.util.Log;
import android.view.*;
import android.widget.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Closeable;
import java.net.Socket;
import java.net.DatagramSocket;

import static android.content.Intent.ACTION_SENDTO;
public class Engine
{
	private static String VERSION_NAME = "1.3";
	private static final String[] BinaryPlaces = { "/data/bin/", "/system/bin/", "/system/xbin/", "/sbin/",
        "/data/local/xbin/", "/data/local/bin/", "/system/sd/xbin/", "/system/bin/failsafe/",
        "/data/local/" };
    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean hasJellyBeanMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }

    public static boolean hasJellyBeanMR2() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean hasLollipopMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1;
    }

    public static boolean hasMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static boolean hasNougat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    public static boolean hasNougatMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1;
    }

    public static boolean hasOreo() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    public static boolean hasOreoMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1;
    }

    //public static boolean hasPie() {
        //return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P;
    //}

    public static boolean hasMoreHeap(){
    	return Runtime.getRuntime().maxMemory() > 20971520;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean isLowRamDevice(Context context) {
    	if(Engine.hasKitKat()){
    		final ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
    		return am.isLowRamDevice();
    	}
    	return !hasMoreHeap();
	}
	
	private Context mContext;

	public Engine(Context context) {
		mContext = context;
	}
	
	private static View rootView;
	public static void addClickListener(int buttonId, View.OnClickListener onClickListener) {
        ((Button) rootView.findViewById(buttonId)).setOnClickListener(onClickListener);
	}
	
	public String runcommand(String command, boolean root) {
		// execute a shell command, returning output in a string
		try {
			Runtime rt = Runtime.getRuntime();
			Process process;
			if (root) process = rt.exec("su");
			else process = rt.exec("sh");
			DataOutputStream os = new DataOutputStream(
				process.getOutputStream());
			os.writeBytes(command + "\n");
			os.flush();
			os.writeBytes("exit\n");
			os.flush();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
														   process.getInputStream()));
			int read;
			char[] buffer = new char[4096];
			StringBuffer output = new StringBuffer();
			while ((read = reader.read(buffer)) > 0) {
				output.append(buffer, 0, read);
			}
			reader.close();

			process.waitFor();

			return output.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public String exec(String command) {
		return runcommand(command, false);
	}

	public String execroot(String command) {
		return runcommand(command, true);
	}

	public void doTheMeat() {

		String filesPath = mContext.getFilesDir().getAbsolutePath();
		String output;

		SharedPreferences prefs = PreferenceManager
			.getDefaultSharedPreferences(mContext);
		boolean debuggable = prefs.getBoolean("debuggable", true);

		try {
			copyFromAssets(mContext, "setpropex", "setpropex");
			copyFromAssets(mContext, "rootadb", "rootadb");
			exec("chmod 700 " + filesPath + "/setpropex");
			exec("chmod 700 " + filesPath + "/rootadb");
		} catch (Exception e) {
			e.printStackTrace();
		}


		output = exec("LD_LIBRARY_PATH=/system/lib getprop ro.secure");
		if (output.equals("0\n")) {
			// run it as shell
			execroot("stop adbd");
			execroot(filesPath + "/setpropex ro.secure 1");
			if (debuggable)
				execroot(filesPath + "/setpropex ro.debuggable 0");
			execroot("mount -o remount,rw /");
			execroot("cp /sbin/adbd.bak /sbin/adbd");
			execroot("rm /shell");
			execroot("mount -o remount,ro /");
		} else {
			// run it as root
			execroot(filesPath + "/setpropex ro.secure 0");
			if (debuggable)
				execroot(filesPath + "/setpropex ro.debuggable 1");
			execroot("mount -o remount,rw /");
			execroot("cp /sbin/adbd /sbin/adbd.bak");
			execroot("mount -o remount,ro /");
			execroot(filesPath + "/rootadb");
		}

		execroot("start adbd");
		exec("rm " + filesPath + "/setpropex");
		exec("rm " + filesPath + "/rootadb");

	}

	public static final void copyFromAssets(Context context, String source,
											String destination) throws IOException {

		// read file from the apk
		InputStream is = context.getAssets().open(source);
		int size = is.available();
		byte[] buffer = new byte[size];
		is.read(buffer);
		is.close();

		// write files in app private storage
		FileOutputStream output = context.openFileOutput(destination,
														 Context.MODE_PRIVATE);
		output.write(buffer);
		output.close();

	}

	/**
	 * Enables Receiver
	 * 
	 * @param ctx
	 * @param cls
	 *            of the Receiver
	 */
	public static void enableReceiver(final Context ctx, final Class<?> cls) {
		ComponentName receiver = new ComponentName(ctx, cls);
		PackageManager pm = ctx.getPackageManager();
		pm.setComponentEnabledSetting(receiver,
									  PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
									  PackageManager.DONT_KILL_APP);
	}

	/**
	 * Disables Receiver
	 * 
	 * @param ctx
	 * @param cls
	 *            of the Receiver
	 */
	public static void disableReceiver(final Context ctx, final Class<?> cls) {
		ComponentName receiver = new ComponentName(ctx, cls);
		PackageManager pm = ctx.getPackageManager();
		pm.setComponentEnabledSetting(receiver,
									  PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
									  PackageManager.DONT_KILL_APP);
	}
	
	/**
     * Recursively delete everything in {@code dir}.
     */
    public static void deleteContents(File dir){
        File[] files = dir.listFiles();
        if (files == null) {
        	return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                deleteContents(file);
            }
            if (!file.delete()) {

            }
        }
    }
	
	public static void deleteContents(String item){
        File dir = new File(item);
		File[] files = dir.listFiles();
        if (files == null) {
        	return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                deleteContents(item);
            }
            if (!file.delete()) {

            }
        }
    }
	
    public static boolean isRooted(){
        for (String p : Engine.BinaryPlaces) {
            File su = new File(p + "su");
            if (su.exists()) {
                return true;
            }
        }
        return false;//RootTools.isRootAvailable();
    }
	
	public static void copyFile_dd(){
		try {           
			Process su;

			su = Runtime.getRuntime().exec("su");

			String cmd = "dd if=/storage/emulated/0/test.dat of=/storage/emulated/0/Download/test1.dat \n" + "exit\n";
			su.getOutputStream().write(cmd.getBytes());

			if ((su.waitFor() != 0)) {
				throw new SecurityException();
			}

		} catch (Exception e) {
			e.printStackTrace();
			//throw new SecurityException();
		}
		
	}
	
	
	public static void sudo(String...strings) {
		try{
			Process su = Runtime.getRuntime().exec("su");
			DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());

			for (String s : strings) {
				outputStream.writeBytes(s + "\n");
				outputStream.flush();
			}

			outputStream.writeBytes("exit\n");
			outputStream.flush();
			try {
				su.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			outputStream.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	
	public static void suMkdirs(String path) {
		if (!new File(path).isDirectory()) {
			sudo("mkdir -p " + path);
		}
	}
	
	
	public static String sudoForResult(String...strings) {
		String res = "";
		DataOutputStream outputStream = null;
		InputStream response = null;
		try{
			Process su = Runtime.getRuntime().exec("su");
			outputStream = new DataOutputStream(su.getOutputStream());
			response = su.getInputStream();

			for (String s : strings) {
				outputStream.writeBytes(s +"\n");
				outputStream.flush();
			}

			outputStream.writeBytes("exit\n");
			outputStream.flush();
			try {
				su.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			res = readFully(response);
		} catch (IOException e){
			e.printStackTrace();
		} finally {
			Closer.closeSilently(outputStream, response);
		}
		return res;
	}
	
	public static String readFully(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length = 0;
		while ((length = is.read(buffer)) != -1) {
			baos.write(buffer, 0, length);
		}
		return baos.toString("UTF-8");
	}
	
	public static class Closer {
        // closeAll()
		public static final void closeSilently(final Object... xs) {
			// Note: on Android API levels prior to 19 Socket does not implement Closeable
			for (Object x : xs) {
				if (x != null) {
					try {
						//Log.d("" + x);
						if (x instanceof Closeable) {
							((Closeable)x).close();
						} else if (x instanceof Socket) {
							((Socket)x).close();
						} else if (x instanceof DatagramSocket) {
							((DatagramSocket)x).close();
						} else {
							//Log.d("cannot close: "+x);
							throw new RuntimeException("cannot close "+x);
						}
					} catch (Throwable e) {
						
					}
				}
			}
		}
	}
	
	public static void openFeedback(Activity activity){
        sendEmail(activity, "Send Feedback", "AnExplorer Feedback");
    }

    public static void sendEmail(Activity activity, String title, String subject){
        final Intent result = new Intent(ACTION_SENDTO);
        result.setData(Uri.parse("mailto:"));
        result.putExtra(Intent.EXTRA_EMAIL, new String[]{"asepmo.story@gmail.com"});
        result.putExtra(Intent.EXTRA_SUBJECT, subject);
        result.putExtra(Intent.EXTRA_TEXT, "AnExplorer Feedback" + " v" + VERSION_NAME);

        activity.startActivity(Intent.createChooser(result, title));
    }
}

