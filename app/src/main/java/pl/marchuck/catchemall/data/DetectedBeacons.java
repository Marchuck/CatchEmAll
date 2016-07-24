package pl.marchuck.catchemall.data;

import android.content.Context;
import android.support.v4.util.Pair;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import pl.marchuck.catchemall.adapters.BeaconAdapter;
import uk.co.alt236.bluetoothlelib.device.BluetoothLeDevice;
import uk.co.alt236.bluetoothlelib.device.beacon.BeaconType;
import uk.co.alt236.bluetoothlelib.device.beacon.BeaconUtils;

/**
 * Created by Lukasz Marczak on 2015-08-23.
 */
public class DetectedBeacons {

    public static final String TAG = DetectedBeacons.class.getSimpleName();
    private Context context;

    public DetectedBeacons(Context context) {
        this.context = context;
    }

    private static List<BluetoothLeDevice> detectedBeacons = new ArrayList<>();

    public void addBeacon(BluetoothLeDevice newDevice, BeaconAdapter toUpdateAdapter) {

        Log.d(TAG, "addBeacon()");
        boolean notUpdated = true;
        for (BluetoothLeDevice device : detectedBeacons) {
            if (device.getAddress().equals(newDevice.getAddress())) {
                device = newDevice;
                Log.d(TAG, "Beacon updated!!!");
                toUpdateAdapter.notifyData(this);
                notUpdated = false;
            }
        }
        if (notUpdated)
            detectedBeacons.add(newDevice);
    }

    public List<BluetoothLeDevice> getDetectedBeacons() {
        return detectedBeacons;
    }

    public int size() {
        return detectedBeacons.size();
    }

    public void clear() {
        detectedBeacons.clear();
    }

    public void addAll(DetectedBeacons beacons) {
        for (BluetoothLeDevice device : beacons.getDetectedBeacons()) {
            detectedBeacons.add(device);
        }
    }

    public Pair<BluetoothLeDevice, Boolean> get(int position) {
        if (position >= detectedBeacons.size())
            throw new IndexOutOfBoundsException("Index is greater than dataSet length!!!");
        return new Pair<>(detectedBeacons.get(position), isBeacon(detectedBeacons.get(position)));
    }

    public Boolean isBeacon(BluetoothLeDevice device) {
        return BeaconUtils.getBeaconType(device) == BeaconType.IBEACON;
    }

}
