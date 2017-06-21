package RandomNumberGame;

import java.util.*;
import java.util.concurrent.SynchronousQueue;


public class User extends Thread
{
	GameUserNum gu= new GameUserNum();
	Random rd = new Random();

	@Override
	public void run() 
	{	
		try
		{	
			GameUserNum.setUsername(Thread.currentThread().getName());
	
			synchronized(this)
			{
				if(GameUserNum.getUsername().equals("User1"))
				{
					GameUserNum.setUsernum(rd.nextInt(20)+1);
					System.out.printf("[%s]유저 입력값은 : %d%n",GameUserNum.getUsername(),GameUserNum.getUsernum());
					Thread.sleep(3000);
					notify(); wait();
				}
				
				else if(GameUserNum.getUsername().equals("User2"))
				{
					GameUserNum.setUsernum(rd.nextInt(20)+1);
					System.out.printf("[%s]유저 입력값은 : %d%n",GameUserNum.getUsername(),GameUserNum.getUsernum());
					Thread.sleep(3000);
					notify(); wait();
				}
				Thread.sleep(2000);
			}					
		} catch (InterruptedException e) {e.printStackTrace();}
	}

	
	
}//클래스
