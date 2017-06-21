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
			System.out.println("���� ���ӿϷ�");
			OutputStream ops = soc.getOutputStream();
			InputStream ips = soc.getInputStream();
			ObjectOutputStream oos = new ObjectOutputStream(ops);
			ObjectInputStream ois = new ObjectInputStream(ips);
			MsgVO vo = new MsgVO();
			boolean islogin =false;
			while(!islogin){
				String login = JOptionPane.showInputDialog("���̵�� ����� �������� �����Ͽ� �Է����ּ���");
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
					System.out.printf("[%s]�� �α��μ���!%n",arr[0]);
				}
				else{
					System.out.println("�α��� ����!!!");
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
							System.err.println("������ ����Ǿ����ϴ�.");
						}
					}
				}.start();

				while(true){
					String msg = JOptionPane.showInputDialog("�޼����� �Է��ϼ���(�ӼӸ��� �� ��� @���̵�|�޼����α���)");
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
