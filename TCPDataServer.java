import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPDataServer {
    public static void main(String args[]) {
        try {
            ServerSocket server_socket = new ServerSocket(19000);
            while (true) {
                new Thread(new ClientWorker(server_socket.accept())).start();
            }
        } catch (IOException e) {
            Logger.getLogger(TCPDataServer.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    static class ClientWorker implements Runnable {
        private Socket target_socket;
        private DataInputStream din;
        private DataOutputStream dout;

        public ClientWorker(Socket recv_socket) {
            try {
                target_socket = recv_socket;
                din = new DataInputStream(target_socket.getInputStream());
                dout = new DataOutputStream(target_socket.getOutputStream());
            } catch (IOException e) {
                Logger.getLogger(TCPDataServer.class.getName()).log(Level.SEVERE, null, e);
            }
        }

        @Override
        public void run() {
            while (true) {
                byte[] initialize = new byte[1];
                try {
                    din.read(initialize, 0, initialize.length);
                    if (initialize[0] == 2) {
                        System.out.println(new String(ReadStream()));
                    }
                } catch (IOException e) {
                    Logger.getLogger(TCPDataServer.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }

        public byte[] ReadStream() {
            byte[] data_buffer = null;
            try {
                int b = 0;
                String buffer_length = "";
                while ((b = din.read()) != 4) {
                    buffer_length += (char) b;
                }
                int data_length = Integer.parseInt(buffer_length);
                data_buffer = new byte[Integer.parseInt(buffer_length)];
                int byte_read = 0;
                int byte_offset = 0;
                while (byte_offset < data_length) {
                    byte_read = din.read(data_buffer, byte_offset, data_length - byte_offset);
                    byte_offset += byte_read;
                }
            } catch (IOException e) {
                Logger.getLogger(TCPDataServer.class.getName()).log(Level.SEVERE, null, e);
            }
            return data_buffer;
        }
    }
}
