package Chat;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

public class Client {
	
	public static void main(String[] args) {
		try {
			Socket soc = new Socket("localhost", 9876);
			System.out.println("서버 접속완료");
			OutputStream ops = soc.getOutputStream();
			InputStream ips = soc.getInputStream();
			ObjectOutputStream oos = new ObjectOutputStream(ops);
			ObjectInputStream ois = new ObjectInputStream(ips);
			MsgVO vo = new MsgVO();
			boolean islogin =false;
			while(!islogin){
				String login = JOptionPane.showInputDialog("아이디와 비번을 공백으로 구분하여 입력해주세요");
				if(login==null||login.equals(""))continue;
				String[] arr = login.split(" ");
				vo.setId(arr[0]);
				vo.setPwd(arr[1]);
				vo.setCmd("login");
				oos.reset();
				oos.writeObject(vo);
				oos.flush();
				vo = (MsgVO)ois.readObject();
				if(vo.isPass()){
					islogin = true;
					System.out.printf("[%s]님 로그인성공!%n",arr[0]);
				}
				else{
					System.out.println("로그인 실패!!!");
				}
			}
			
			if(islogin){
				new Thread(){

					@Override
					public void run() {
						try {
							MsgVO vo = new MsgVO();
							while(true){
								vo = (MsgVO)ois.readObject();
								System.out.printf("[%s] : %s%n",vo.getId(),vo.getMsg());
							}
						} catch (Exception e) {
							System.err.println("서버가 종료되었습니다.");
						}
					}
				}.start();

				while(true){
					String msg = JOptionPane.showInputDialog("메세지를 입력하세요(귓속말을 할 경우 @아이디|메세지로구분)");
					if(msg==null||msg.equals("")) continue;
					if(msg.startsWith("@")){
						String[] arr = msg.split("\\|");
						vo.setCmd("rec");
						vo.setRec(arr[0].substring(1));
						vo.setMsg(arr[1]);
					}
					else {
						vo.setMsg(msg);
						vo.setCmd("msg");						
					}
					oos.reset();
					oos.writeObject(vo);
					oos.flush();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
