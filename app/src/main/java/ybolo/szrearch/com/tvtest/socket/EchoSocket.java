package ybolo.szrearch.com.tvtest.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoSocket {

    private final ServerSocket serverSocket;

    //创建一个ServerSocket并监听端口port
    public EchoSocket(int port) throws IOException{
        serverSocket=new ServerSocket(port);
    }

    //开始接受客户端的连接
    public void run() throws IOException{
        Socket socket=serverSocket.accept();
        handleClient(socket);
    }

    //处理通讯
    private void handleClient(Socket socket) throws IOException{
        InputStream in=socket.getInputStream();
        OutputStream out=socket.getOutputStream();
        byte[] buffer=new byte[1024];
        int n;
        while ((n=in.read(buffer))>0){
            out.write(buffer,0,n);
        }
    }
}
