package pl.marchuck.catchemall.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import pl.marchuck.catchemall.R;
import pl.marchuck.catchemall.data.DetectedBeacons;
import uk.co.alt236.bluetoothlelib.device.BluetoothLeDevice;
import uk.co.alt236.bluetoothlelib.device.beacon.ibeacon.IBeaconDevice;

/**
 * Created by Lukasz Marczak on 2015-08-23.
 */
public class BeaconAdapter extends RecyclerView.Adapter<BeaconAdapter.ViewHolder> {
    public static final String TAG = BeaconAdapter.class.getSimpleName();
    private DetectedBeacons dataset;


    public void notifyData(DetectedBeacons beacons) {
        dataset.clear();
//        dataSet = beacons;
        dataset.addAll(beacons);
        Log.d(TAG, "notifyDataSetChanged");
        notifyDataSetChanged();
        Log.d(TAG, "notifyItemRangeChanged");
        notifyItemRangeChanged(0, dataset.size());
        //    instance = this;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View v;
        public TextView name, mac, rssi,near;
        public RelativeLayout dataParent;
        public ImageView image;

        public ViewHolder(View v) {
            super(v);
            this.v = v;

            dataParent = (RelativeLayout) v.findViewById(R.id.parent);
            name = (TextView) v.findViewById(R.id.name);
            rssi = (TextView) v.findViewById(R.id.rssi);
            near = (TextView) v.findViewById(R.id.near);
            mac = (TextView) v.findViewById(R.id.mac_address);
            image = (ImageView) v.findViewById(R.id.image);
        }
    }

    public BeaconAdapter( DetectedBeacons beacons) {
        this.dataset = beacons;
    }

    @Override
    public BeaconAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        View v = android.view.LayoutInflater.from(parent.getContext())
                .inflate(R.layout.beacon_item, parent, false);
        ViewHolder vh = new ViewHolder(v);


        setupClickListeners(vh);
        return vh;
    }

    private void setupClickListeners(final ViewHolder holder) {

    }

    private  boolean bindingRunning = false;

    @Override
    public void onBindViewHolder(final ViewHolder vh, final int position) {
        Log.d(TAG, "onBindViewHolder");
        if (bindingRunning || dataset == null || dataset.size() <= position || dataset.get(position) == null)
            return;
        bindingRunning = true;

        BluetoothLeDevice device = dataset.get(position).first;
        boolean isBeacon = dataset.get(position).second;
        vh.image.setVisibility(isBeacon ? View.VISIBLE : View.GONE);

        vh.mac.setText(device.getAddress());
        Log.d(TAG, "mac: " + device.getAddress());

        vh.name.setText(device.getName());
        Log.d(TAG, "name: " + device.getName());

        vh.rssi.setText(String.valueOf(device.getRssi()));
        Log.d(TAG, "rssi: " + device.getRssi());

        if (isBeacon) {
            final IBeaconDevice iBeacon = new IBeaconDevice(device);
            Log.d(TAG, "UUID: " + iBeacon.getUUID());
            Log.d(TAG, "accuracy: " + iBeacon.getAccuracy());
            Log.d(TAG, "calibrated power: " + iBeacon.getCalibratedTxPower());
            Log.d(TAG, "company identifier: " + iBeacon.getCompanyIdentifier());
            Log.d(TAG, "distance descriptor: " + iBeacon.getDistanceDescriptor());
            vh.rssi.setText(String.valueOf(iBeacon.getCalibratedTxPower()));
            vh.near.setText(String.valueOf(iBeacon.getDistanceDescriptor()));

            Log.d(TAG, "IBeaconData: " + iBeacon.getIBeaconData().getIBeaconAdvertisement());
            Log.d(TAG, "Minor: " + iBeacon.getMinor());
            Log.d(TAG, "Major: " + iBeacon.getMajor());
        }

        bindingRunning = false;
    }

    @Override
    public int getItemCount() {
        if (dataset == null) {
            Log.e(TAG, "getItemCount: mDataset is null!");
            return 0;
        }
        return dataset.size();
    }

}