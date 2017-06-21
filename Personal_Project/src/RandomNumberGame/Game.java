package RandomNumberGame;

import java.util.Random;


public class Game 
{
	static boolean tf;
	static String winner;
	
	public static void main(String[] args) 
	{
		int ran = random();
	
		while(!tf)
		{
			Thread t1 = new Thread(new User(), "User1");
			Thread t2 = new Thread(new User(), "User2");
			
			try 
			{
				t1.start();
				Thread.sleep(1000);
				Check(GameUserNum.getUsernum(),ran);
				
				t2.start();
				Thread.sleep(1000);
				Check(GameUserNum.getUsernum(),ran);
				
				
			} catch (Exception e) {e.printStackTrace();}
		}
		
		
	}//메인
	
	
	
	public static int random()
	{
		Random rd = new Random();
		int rannum = 0;
		
		rannum = rd.nextInt(20)+1;
		
		return rannum;
	}
	
	
	static synchronized boolean Check(int userNum, int ran)
	{
		winner = GameUserNum.getUsername();
		if(ran < userNum){System.out.println("Down!");}
		else if(ran > userNum){System.out.println("up!");}
		else if(ran == userNum)
		{
			System.out.println("정답!!");
			System.out.println("승자는 : " + winner);
			tf = true;
			
			System.exit(0);			
		}
		
		return tf;
	}
}//클래스
