package bluetoothconnection;

import java.io.IOException;
import java.util.Vector;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.microedition.io.Connection;

public class BluetoothConnection {
 
    LocalDevice local;
    RemoteDevice remote;
    DiscoveryAgent agent;
    Connection c;
    
    public int devices, accessCode;
    public static final Vector/*<RemoteDevice>*/ devicesDiscovered = new Vector();

    private BluetoothConnection(){
        
        try {
            // Retrieve the local Bluetooth device object
            local = LocalDevice.getLocalDevice();
            local.setDiscoverable(DiscoveryAgent.GIAC);
            
            // Retrieve the Bluetooth address of the local device
            String address = local.getBluetoothAddress();
            
            // Retrieve the name of the local Bluetooth device
            String name = local.getFriendlyName();
            System.out.println("Local device "+address +"\n "+"name"+name);
 
            deviceDiscovery();
            
        } catch(Exception e) {
            e.printStackTrace();
            
    }
    }
    
    public void deviceDiscovery() throws IOException, InterruptedException {
        
            //retrieve the discovery agent
            DiscoveryAgent agent = local.getDiscoveryAgent();
            
            final Object inquiryCompletedEvent = new Object();

            devicesDiscovered.clear();
            DiscoveryListener listener = new DiscoveryListener() {

            public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
                System.out.println("Device " + btDevice.getBluetoothAddress() + " found");
                devicesDiscovered.addElement(btDevice);
                try {
                    System.out.println("     name " + btDevice.getFriendlyName(false));
                } catch (IOException cantGetDeviceName) {
                }
            }
            
            public void inquiryCompleted(int discType) {
                System.out.println("Device Inquiry completed!");
                synchronized(inquiryCompletedEvent){
                    inquiryCompletedEvent.notifyAll();
                }
            }
            public void serviceSearchCompleted(int transID, int respCode) { 
            }

            public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
            }
            };
            
            synchronized(inquiryCompletedEvent) {
            boolean started = LocalDevice.getLocalDevice().getDiscoveryAgent().startInquiry(DiscoveryAgent.GIAC, listener);
            if (started) {
                System.out.println("wait for device inquiry to complete...");
                inquiryCompletedEvent.wait();
                System.out.println(devicesDiscovered.size() +  " device(s) found");
            }
        }   
}
   
    public static void main(String[] args) {
        
        BluetoothConnection newconnection = new BluetoothConnection();
        
    }
    
}
