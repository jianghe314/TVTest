package ybolo.szrearch.com.tvtest.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class EchoClient {

    private final Socket socket;

    //创建socket连接服务器端
    public EchoClient(String host,int port) throws IOException{
        socket=new Socket(host,port);
    }

    //处理与服务器间的通讯
    private void run() throws IOException{
        Thread readerThread=new Thread(this::readReponse);
        readerThread.start();

        OutputStream out=socket.getOutputStream();
        byte[] buffer=new byte[1024];
        int n;
        while ((n=System.in.read(buffer))>0){
            out.write(buffer,0,n);
        }
    }


    private void readReponse(){
        try {
            InputStream in=socket.getInputStream();
            byte[] buffer=new byte[1024];
            int n;
            while ((n=in.read(buffer))>0){
                System.out.write(buffer,0,n);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
