package com.dooble.audiotoggle;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.media.AudioManager;
import android.media.AudioDeviceInfo;

import java.util.List;
import android.util.Log;


public class AudioTogglePlugin extends CordovaPlugin {
	public static final String ACTION_SET_AUDIO_MODE = "setAudioMode";
    private static final String TAG = "ALawPlayer";

    private List<AudioDeviceInfo> devices;

	@Override
	public boolean execute(String action, JSONArray args,
			CallbackContext callbackContext) throws JSONException {
		if (action.equals(ACTION_SET_AUDIO_MODE)) {
			if (!setAudioMode(args.getString(0))) {
				callbackContext.error("Invalid audio mode");
				return false;
			}

			return true;
		}

		callbackContext.error("Invalid action");
		return false;
	}

    private void switchOutputDevice(AudioManager audioManager, int targetType) {
        for (AudioDeviceInfo device : devices) {
            if (device.getType() == targetType) {
                boolean result = audioManager.setCommunicationDevice(device);
            }
        }
    }

	public boolean setAudioMode(String mode) {
	    Context context = webView.getContext();
	    AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        devices = audioManager.getAvailableCommunicationDevices();

	    if (mode.equals("earpiece")) {
	    	audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
	    	audioManager.setSpeakerphoneOn(false);
            switchOutputDevice(audioManager, AudioDeviceInfo.TYPE_BUILTIN_EARPIECE);
	        return true;
	    } else if (mode.equals("speaker")) {
	    	audioManager.setMode(AudioManager.STREAM_MUSIC);
	    	audioManager.setSpeakerphoneOn(true);
            switchOutputDevice(audioManager, AudioDeviceInfo.TYPE_BUILTIN_SPEAKER);
	        return true;
	    } else if (mode.equals("ringtone")) {
	    	audioManager.setMode(AudioManager.MODE_RINGTONE);
	    	audioManager.setSpeakerphoneOn(false);
	        return true;
	    } else if (mode.equals("normal")) {
	    	audioManager.setMode(AudioManager.MODE_NORMAL);
	    	audioManager.setSpeakerphoneOn(false);
	        return true;
	    }

	    return false;
	}
}
