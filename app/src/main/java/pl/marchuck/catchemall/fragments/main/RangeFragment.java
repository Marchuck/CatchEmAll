package pl.marchuck.catchemall.fragments.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tt.whorlviewlibrary.WhorlView;

import pl.marchuck.catchemall.R;
import pl.marchuck.catchemall.activities.FightActivity;
import pl.marchuck.catchemall.activities.MainActivity;
import pl.marchuck.catchemall.adapters.BeaconAdapter;
import pl.marchuck.catchemall.bluetooth.BLEScanner;
import pl.marchuck.catchemall.bluetooth.BluetoothUtils;
import pl.marchuck.catchemall.configuration.Config;
import pl.marchuck.catchemall.data.BeaconsInfo;
import pl.marchuck.catchemall.data.DetectedBeacons;
import pl.marchuck.catchemall.game.WeakHandler;
import uk.co.alt236.bluetoothlelib.device.BluetoothLeDevice;
import uk.co.alt236.bluetoothlelib.device.beacon.ibeacon.IBeaconDevice;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * <p>
 * to handle interaction events.
 * Use the {@link RangeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RangeFragment extends Fragment {
    private static final String TAG = RangeFragment.class.getSimpleName();
    private WhorlView whorlView;
    private BLEScanner beaconScanner;
    private BluetoothUtils beaconUtils;
    private TextView timeLeft;
    private TextView timeLeftDescription;
    private DetectedBeacons detectedBeacons = new DetectedBeacons(getActivity());
    private BeaconAdapter beaconAdapter;
    private Loop aLoop;

    private Loop getLoop() {
        if (aLoop == null) aLoop = new Loop();
        return aLoop;
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            Log.d(TAG, "onLeScan()");
            final BluetoothLeDevice deviceLe = new BluetoothLeDevice(device, rssi, scanRecord,
                    System.currentTimeMillis());

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "Detected beacon!");
                    Log.d(TAG, "MAC address: " + deviceLe.getAddress());
                    Log.d(TAG, "name: " + deviceLe.getName());
                    Log.d(TAG, "bond state: " + deviceLe.getBluetoothDeviceBondState());
                    Log.d(TAG, "bt device class name: " + deviceLe.getBluetoothDeviceClassName());

                    if (BeaconsInfo.isBeacon(deviceLe)) {
                        final IBeaconDevice iBeacon = new IBeaconDevice(deviceLe);
                        Log.d(TAG, "UUID: " + iBeacon.getUUID());
                        Log.d(TAG, "accuracy: " + iBeacon.getAccuracy());
                        Log.d(TAG, "calibrated power: " + iBeacon.getCalibratedTxPower());
                        Log.d(TAG, "company identifier: " + iBeacon.getCompanyIdentifier());
                        Log.d(TAG, "distance descriptor: " + iBeacon.getDistanceDescriptor());
                        Log.d(TAG, "IBeaconData: " + iBeacon.getIBeaconData().getIBeaconAdvertisement());
                        Log.d(TAG, "Minor: " + iBeacon.getMinor());
                        Log.d(TAG, "Major: " + iBeacon.getMajor());

                        if (iBeacon.getDistanceDescriptor().toString().equals("IMMEDIATE")) {
                            startWildPokemonFight(deviceLe, iBeacon);
                        }
                    }


//                    detectedBeacons.addBeacon(deviceLe,beaconAdapter);
//                    if (beaconAdapter != null)
//                        beaconAdapter.notifyData(detectedBeacons);
                }
            });
        }
    };

    private void startWildPokemonFight(BluetoothLeDevice deviceLe, IBeaconDevice iBeacon) {
        if (BeaconsInfo.NEW_FIGHT)
            return;
        BeaconsInfo.NEW_FIGHT = true;
        whorlView.stop();
        whorlView.setVisibility(View.GONE);

        BeaconsInfo.FORCE_STOP_SCAN = true;
        Intent fightIntent = new Intent(getActivity(), FightActivity.class);
        fightIntent.putExtra(BeaconsInfo.Bundler.MAC, deviceLe.getAddress());
        fightIntent.putExtra(BeaconsInfo.Bundler.MINOR, iBeacon.getMinor());
        fightIntent.putExtra(BeaconsInfo.Bundler.MAJOR, iBeacon.getMajor());
        fightIntent.putExtra(BeaconsInfo.Bundler.CALIBRATED_POWER, iBeacon.getCalibratedTxPower());
        fightIntent.putExtra(BeaconsInfo.Bundler.ACCURACY, iBeacon.getAccuracy());
        startActivityForResult(fightIntent, Config.IntentCode.START_WILD_FIGHT);
    }


//    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RangeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RangeFragment newInstance() {
        RangeFragment fragment = new RangeFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    public RangeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate()");

        beaconUtils = new BluetoothUtils(getActivity());
//        beaconUtils.askUserToEnableBluetoothIfNeeded();

    }

    public void restartScan() {
        if (BeaconsInfo.FORCE_STOP_SCAN)
            return;
        Log.d(TAG, "resetScan()");
        timeLeftDescription.setOnClickListener(null);

        int value = Integer.valueOf(timeLeft.getText().toString());
        timeLeft.setText(String.valueOf(value - 1));
        if (beaconScanner != null && beaconScanner.isScanning()) {
            beaconScanner.forceStopScan();
        }

        beaconScanner = new BLEScanner(mLeScanCallback, beaconUtils);

        if (beaconUtils.isBluetoothOn() && beaconUtils.isBluetoothLeSupported()) {
            beaconScanner.scanLeDevice(-1, true);
        }
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart()");
        super.onStart();
        setupScanOnStart();
    }

    public void setupScanOnStart() {

        timeLeft.setText(String.valueOf(BeaconsInfo.SCANTIME));
        timeLeft.setVisibility(View.GONE);
        timeLeftDescription.setText("Click to start scan");
        timeLeftDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick()");
                if (beaconUtils.isBluetoothOn() && beaconUtils.isBluetoothLeSupported()) {
                    getLoop().start(RangeFragment.this);
                    timeLeftDescription.setText("Scanning...");
                    timeLeft.setVisibility(View.VISIBLE);
                    whorlView.setVisibility(View.VISIBLE);
                    if (!whorlView.isCircling())
                        whorlView.start();

                } else {
                    beaconUtils.askUserToEnableBluetoothIfNeeded();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.template_recyclerview, container, false);
        timeLeftDescription = (TextView) view.findViewById(R.id.timeLeftDescription);
        timeLeft = (TextView) view.findViewById(R.id.timeLeft);
        whorlView = (WhorlView) view.findViewById(R.id.whorl2);
        whorlView.setVisibility(View.GONE);
        beaconAdapter = new BeaconAdapter(detectedBeacons);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated");
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        Log.d(TAG, "onAttach()");
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach()");
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        super.onDestroy();

    }

    public synchronized void buildNoPokemonsHereDialog() {

        AlertDialog.Builder okno = new AlertDialog.Builder(this.getActivity());
        okno.setMessage("No pokemons in range. Retry?");
        okno.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                setupScanOnStart();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ((MainActivity) getActivity()).selectItem(Config.FRAGMENT.TRIP);
            }
        });
        okno.show();
    }
}