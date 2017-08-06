import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


public class TCPDataClient {
    public static void main(String[] args) {
        try {
            TCPDataClient obj = new TCPDataClient();
            Socket obj_client = new Socket(InetAddress.getByName("localhost"), 19000);
            DataInputStream din = new DataInputStream(obj_client.getInputStream());
            DataOutputStream dout = new DataOutputStream(obj_client.getOutputStream());
            byte[] buffer = obj.CreateDataPacket("Connection Established".getBytes("UTF8"));
            dout.write(buffer);
            dout.flush();
        } catch (UnknownHostException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private byte[] CreateDataPacket(byte[] data) {
        byte[] packet = null;
        try {
            byte[] initialize = new byte[1];
            initialize[0] = 2;
            byte[] separator = new byte[1];
            separator[0] = 4;
            byte[] data_length = String.valueOf(data.length).getBytes("UTF8");
            packet = new byte[initialize.length + separator.length + data_length.length + data.length];
            System.arraycopy(initialize, 0, packet, 0, initialize.length);
            System.arraycopy(data_length, 0, packet, initialize.length, data_length.length);
            System.arraycopy(separator, 0, packet, initialize.length + data_length.length, separator.length);
            System.arraycopy(data, 0, packet, initialize.length + data_length.length + separator.length, data.length);
        } catch (UnsupportedEncodingException e) {
            System.out.println(e);
        }
        return packet;
    }
}
