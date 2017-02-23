package com.mdg.androble;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.io.IOException;

/**
 * Created by this pc on 02-08-2016.
 */
public class BluetoothManager {

    public enum ConnectionType {
        CLIENT, SERVER
    }

    public ConnectionType connectionType;
    public static SocketManager serverSocket;
    ClientSocket clientSocket;
    public static Object recieve_msg;

    private static BluetoothManager bluetoothManager;
    private Context context;

    private BluetoothManager(Context context) {
        serverSocket = new SocketManager(context);
        clientSocket = new ClientSocket();
        this.context = context;
    }

    public static BluetoothManager getInstance(Context context) {
        if (bluetoothManager == null){
            bluetoothManager = new BluetoothManager(context);
        }
        return bluetoothManager;
    }

    /**
     *
     * @param msgObject takes msg obj
     * @param connectionType sdfd
     */

    public void init(Object msgObject, ConnectionType connectionType){
        setType(connectionType);
        setMessageObject(msgObject);
    }

    public void setType(ConnectionType connectionType) {
        this.connectionType = connectionType;
    }

    public void setMessageObject(Object myObject){
        recieve_msg = myObject;
    }

    void scanClients() {
        serverSocket.startConnection(BluetoothActivity.bluetoothAdapter);
    }

    public void connectTo(String s) {
        clientSocket.startConnection(BluetoothActivity.bluetoothAdapter, s);
    }

    public void sendText(String s) {
        if (clientSocket.check.equals(("connected")) && connectionType.equals(ConnectionType.CLIENT)) {
            clientSocket.write(ServerSocket.my_id + ":" + s);
        }
    }

    public String getId() {
        return ServerSocket.my_id;
    }

    public void sendText(String s1, int id) {
        if (id <= (serverSocket.getSocketCounter() + 1)) {
            serverSocket.write(s1, id);
        }
    }

    public void clientToClient(String s1, int id) {
        if (id <= (serverSocket.getSocketCounter() + 1)) {
            clientSocket.write("<" + id + ">" + s1);
        }
    }

    public String getAllConnectedDevices() {
        if (connectionType.equals(ConnectionType.CLIENT)) {
            clientSocket.write("(" + ServerSocket.my_id + ")");
            return null;
        } else {
            return ServerSocket.sb.substring(0);
        }
    }

    public String disconnect() throws IOException {
        if (connectionType.equals(ConnectionType.CLIENT)) {
            clientSocket.disconnectClient();
            return "DISCONNECTED";
        } else if (connectionType.equals(ConnectionType.SERVER)) {
            serverSocket.disconnectServer();
            return "DISCONNECTED";
        }
        return null;
    }

}
