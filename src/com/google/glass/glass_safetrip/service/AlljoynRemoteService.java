package com.google.glass.glass_safetrip.service;

import android.app.Service;
import android.content.Intent;
import android.os.*;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;
import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.TimelineManager;
import com.google.glass.glass_safetrip.R;
import com.google.glass.glass_safetrip.bus.SimpleInterface;
import com.google.glass.glass_safetrip.renderer.RemoteViewRenderer;
import org.alljoyn.bus.*;

/**
 * glass
 * com.google.glass.glass_safetrip.service
 *
 * @autor manolo
 */
public class AlljoynRemoteService extends Service {

    static {
        System.loadLibrary("alljoyn_java");
    }

    private static final String TAG = AlljoynRemoteService.class.getName();

    // private LiveCardRenderer renderer;
    private TimelineManager tm;
    private LiveCard liveCard;



    private static final int MESSAGE_PING = 1;
    private static final int MESSAGE_PING_REPLY = 2;
    private static final int MESSAGE_POST_TOAST = 3;

    private Handler mHandler;
    private SimpleService mSimpleService;
    private Handler mBusHandler;

    private RemoteViewRenderer renderer;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        tm = TimelineManager.from(this);
        renderer = new RemoteViewRenderer(this);
    }

    // @TODO: move to onStart method
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "on start command alljoyn");

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MESSAGE_PING:
                        String ping = (String) msg.obj;
                        Log.d(TAG, "MESSAGE PING " + ping);

                        renderer.publishSpeed(ping);

                        break;
                    case MESSAGE_PING_REPLY:
                        String reply = (String) msg.obj;
                        Log.d(TAG, "MESSAGE REPLY");
                        break;
                    case MESSAGE_POST_TOAST:
                        Toast.makeText(getApplicationContext(), (String) msg.obj, Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
            }
        };

        HandlerThread busThread = new HandlerThread("BusHandler");
        busThread.start();
        mBusHandler = new BusHandler(busThread.getLooper());

        /* Start our service. */
        mSimpleService = new SimpleService();
        mBusHandler.sendEmptyMessage(BusHandler.CONNECT);

        return Service.START_STICKY;
    }

    class SimpleService implements SimpleInterface, BusObject {

        public String Ping(String inStr) {
            sendUiMessage(MESSAGE_PING, inStr);

            /* Simply echo the ping message. */
            sendUiMessage(MESSAGE_PING_REPLY, inStr);
            return inStr;
        }

        /* Helper function to send a message to the UI thread. */
        private void sendUiMessage(int what, Object obj) {
            mHandler.sendMessage(mHandler.obtainMessage(what, obj));
        }
    }


    class BusHandler extends Handler {

        private static final String SERVICE_NAME = "org.alljoyn.bus.samples.simple";
        private static final short CONTACT_PORT = 42;

        private BusAttachment mBus;

        public static final int CONNECT = 1;
        public static final int DISCONNECT = 2;

        public BusHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case CONNECT: {
                    org.alljoyn.bus.alljoyn.DaemonInit.PrepareDaemon(getApplicationContext());

                    mBus = new BusAttachment(getPackageName(), BusAttachment.RemoteMessage.Receive);
                    mBus.registerBusListener(new BusListener());

                    Status status = mBus.registerBusObject(mSimpleService, "/SimpleService");
                    Log.d(TAG, "BusAttachment.registerBusObject() - " + status.toString());

                    status = mBus.connect();
                    Log.d(TAG, "BusAttachment.connect() - " + status.toString());

                    Mutable.ShortValue contactPort = new Mutable.ShortValue(CONTACT_PORT);

                    SessionOpts sessionOpts = new SessionOpts();
                    sessionOpts.traffic = SessionOpts.TRAFFIC_MESSAGES;
                    sessionOpts.isMultipoint = false;
                    sessionOpts.proximity = SessionOpts.PROXIMITY_ANY;
                    sessionOpts.transports = SessionOpts.TRANSPORT_ANY + SessionOpts.TRANSPORT_WFD;

                    status = mBus.bindSessionPort(contactPort, sessionOpts, new SessionPortListener() {
                        @Override
                        public boolean acceptSessionJoiner(short sessionPort, String joiner, SessionOpts sessionOpts) {
                            if (sessionPort == CONTACT_PORT) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
//                    logStatus(String.format("BusAttachment.bindSessionPort(%d, %s)", contactPort.value, sessionOpts.toString()), status);

                    int flag = BusAttachment.ALLJOYN_REQUESTNAME_FLAG_REPLACE_EXISTING | BusAttachment.ALLJOYN_REQUESTNAME_FLAG_DO_NOT_QUEUE;

                    status = mBus.requestName(SERVICE_NAME, flag);
//                    logStatus(String.format("BusAttachment.requestName(%s, 0x%08x)", SERVICE_NAME, flag), status);
                    if (status == Status.OK) {
                        status = mBus.advertiseName(SERVICE_NAME, sessionOpts.transports);
//                        logStatus(String.format("BusAttachement.advertiseName(%s)", SERVICE_NAME), status);
                        if (status != Status.OK) {
                            status = mBus.releaseName(SERVICE_NAME);
//                            logStatus(String.format("BusAttachment.releaseName(%s)", SERVICE_NAME), status);
                            return;
                        }
                    }

                    break;
                }

                case DISCONNECT: {
                    mBus.unregisterBusObject(mSimpleService);
                    mBus.disconnect();
                    mBusHandler.getLooper().quit();
                    break;
                }

                default:
                    break;
            }
        }
    }

}
