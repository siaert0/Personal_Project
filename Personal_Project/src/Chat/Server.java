package Chat;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

public class Server {

	public static Hashtable<String, ObjectOutputStream> userOos = new Hashtable<>();
	
	public static void main(String[] args) {
		
		try {
			ServerSocket ss = new ServerSocket(9876);
			System.out.println("서버 준비완료");
			while(true){
				Socket soc = ss.accept();
				Service svc = new Service(soc);
				svc.start();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
