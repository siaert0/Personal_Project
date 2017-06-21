package Chat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Iterator;
import java.util.Set;

public class Service extends Thread {
	
	private Socket soc;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private boolean islogin;
	private String userId;
	
	public Service(Socket soc) {
		super();
		this.soc = soc;
		try {
			InputStream ips = soc.getInputStream();
			OutputStream ops = soc.getOutputStream();
			this.oos = new ObjectOutputStream(ops);
			this.ois = new ObjectInputStream(ips);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

	@Override
	public void run() {
		MsgVO vo = new MsgVO();
		try {
			while(true){
				vo = (MsgVO)ois.readObject();
				if(vo.getCmd().equals("login")){
					boolean pass = login(vo.getId(),vo.getPwd());
					vo.setPass(pass);
					oos.reset();
					oos.writeObject(vo);
					oos.flush();
					this.islogin = true;
					this.userId = vo.getId();
					if(pass){
						Server.userOos.put(userId, oos);
						vo.setMsg("입장했습니다.");
						broadcast(vo);
						continue;
					}
				}
				else if(vo.getCmd().equals("msg")){
					broadcast(vo);
				}
				else if(vo.getCmd().equals("rec")){
					Server.userOos.get(vo.getRec()).reset();
					Server.userOos.get(vo.getRec()).writeObject(vo);
					Server.userOos.get(vo.getRec()).flush();
				}
			}
			
		} catch (Exception e) {
			Set<String> keySet= Server.userOos.keySet();
			Iterator<String> itr = keySet.iterator();
			if(!itr.hasNext())return;
			Server.userOos.remove(userId);
			vo.setId(userId);
			vo.setMsg("퇴장하셨습니다.");
			broadcast(vo);
		}
	}
	
	public void broadcast(MsgVO vo){
		Set<String> keySet= Server.userOos.keySet();
		Iterator<String> itr = keySet.iterator();
		if(!itr.hasNext())System.out.println("접속자가 없습니다.");
		while(itr.hasNext()){
			String key = itr.next();
			try {
				Server.userOos.get(key).reset();
				Server.userOos.get(key).writeObject(vo);
				Server.userOos.get(key).flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public boolean login(String id,String pwd){
		File f = new File("C:/ChatInfo/userinfo.txt");
		try {
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String user=null;
			while((user=br.readLine())!=null){
				String[] arr =user.split("\\|");
				if(id.equals(arr[0])&&pwd.equals(arr[1])){
					return true;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
