package my.tank.project;

import java.util.HashMap;
import java.util.Random;

import org.apache.log4j.Logger;

import ai.community.tanks.game.api.TankLogic;
import ai.community.tanks.game.logic.TankCommandStack;
import ai.community.tanks.game.logic.TankState;
import ai.community.tanks.game.logic.TankState.Field;
import ai.community.tanks.game.logic.TankState.Radar.RadarState;
import ai.community.tanks.game.logic.TankState.Vicinity;
import ai.community.tanks.game.world.Location;

public class Piekoczolg extends TankLogic 
{
	static TankCommandStack stos;
	static int[][] enem;
	static int[][] wiezyczka_enem;
	static int[][] mapaBHP;
	static int [][] miny;
	static int runda;
	static RadarState radar;
	int idex=0;
	int idey=0;
	int cycki;
	int skrencam;
	int popehp;
	int tam_sie_wybieram_x;
	int tam_sie_wybieram_y;
	int cycki_x,cycki_y;
	int teraehp;
	int cycuszki;
	int ile_w_miejscu;
	int ja_x;
	int ja_y;
	int skanowawszy;
	static int s1x,s2x,s1y,s2y; // punkty startowane
	
	
	private static Logger LOGGER = Logger.getLogger("CombatLog");
	
	@Override
	protected TankColor chooseTankColor() {
		// TODO Auto-generated method stub
		return TankColor.WHITE;
	}

	@Override
	protected String chooseTankName() {
		// TODO Auto-generated method stub
		return "SeksownaZuzia";
	}

	@Override
	protected String getAuthor() {
		// TODO Auto-generated method stub
		return "Piotr Iwaniuk";
	}

	@Override
	public TankBonusInfo getTankBonusInfoForRound(int arg0) {
		// TODO Auto-generated method stub
		TankBonusInfo bonusy =  new TankBonusInfo();
		bonusy.setPassiveBonus(TankPassiveBonus.SCANNER);
		bonusy.setOffensiveBonus(TankOffensiveBonus.AIRSTRIKE);
		bonusy.setDefensiveBonus(TankDefensiveBonus.REPAIR);
		
		return bonusy;
	}
	TankState stan;
	int[][] plansza;
	
	private int zeskanowal;
	
	private Vicinity vincent;
	
	
	
	
	
	private void pokaz_mape()
	{
		String es = new String("");
		for(int i=0;i<18;i++)
		{
			es="";
			for(int j=0;j<18;j++)
			{
				es = es + (char)(plansza[j][i]+'0');
			}
			LOGGER.info(es);
		}
	}
	
	private void pokaz_enem()
	{
		
		String es = new String("");
		for(int i=18;i>=0;i--)
		{
			es="";
			for(int j=0;j<18;j++)
			{
				es = es + (char)(enem[j][i]+'0');
			}
			LOGGER.info(es);
		}
		
	}
	
	private void pokaz_wiezyczka_enem()
{
		
		String es = new String("");
		for(int i=18;i>=0;i--)
		{
			es="";
			for(int j=0;j<18;j++)
			{
				es = es + (char)(wiezyczka_enem[j][i]+'0');
			}
			LOGGER.info(es);
		}
		
	}
	
	void pokaz_mine()
	{
		String es = new String("");
		for(int i=18;i>=0;i--)
		{
			es="";
			for(int j=0;j<18;j++)
			{
				es = es + (char)(miny[j][i]+'0');
			}
			LOGGER.info(es);
		}
	}
	
	private void pokaz_BHP()
	{
		String es = new String("");
		for(int i=18;i>=0;i--)
		{
			es="";
			for(int j=0;j<18;j++)
			{
				es = es + " ";
				if( mapaBHP[j][i]<10)
					es = es + " ";
				if( stan.getLocationX() == j && stan.getLocationY() == i)
					es = es + "--";
				else
				{
					es = es + String.valueOf(mapaBHP[j][i]%100);
					if( es.length() < 3)
					{
						es = "0" + es;
					}
				}
			}
			LOGGER.info(es);
			LOGGER.info(" ");
		}
	}
	
	void szoruj_enem()
	{
		for(int i=0;i<25;i++)
			for(int j=0;j<25;j++)
				enem[i][j]=0;
	}
	
	
	void copjuj_enem()
	{
		for(int i=0;i<25;i++)
			for(int j=0;j<25;j++)
				wiezyczka_enem[i][j] = enem[i][j];
	}
	
	
	
	void szoruj_BHP()
	{
		for(int i=0;i<25;i++)
			for(int j=0;j<25;j++)
				mapaBHP[i][j]=9999;
	}
	
	
	void szoruj_miny()
	{
		for(int i=0;i<25;i++)
			for(int j=0;j<25;j++)
				miny[i][j]=-1;
	}
	
	
	
	
	void postaw_tegesa(int x, int y)
	{
		if( plansza[x][y]==3)
		{
			plansza[17-x][17-y]=1;
		}
		plansza[x][y]=1;
	}
	
	
	
	
	private void plansza_apdejt(int x,int y)
	{
		

			/// disco polo
		 if ( stan.getTankOrientation() == TankState.Orientation.NORTH)
		 {
			 if( vincent.getFront().isBlockingShot() == true )
			 {
				 plansza[x][y+1] = 1;
//				 postaw_tegesa(x,y+1);
			 }
			 
			 if( vincent.getBack().isBlockingShot() == true )
			 {
				 plansza[x][y-1] = 1;
//				 postaw_tegesa(x,y-1);
			 }
			 
			 if( vincent.getLeft().isBlockingShot() == true )
			 {
				 plansza[x-1][y] = 1;
//				 postaw_tegesa(x-1,y);
			 }
			 
			 if( vincent.getRight().isBlockingShot() == true )
			 {
				 plansza[x+1][y] = 1;
//				 postaw_tegesa(x+1,y);
			 }
			 
			 if( vincent.getFrontLeft().isBlockingShot() == true )
			 {
				 plansza[x-1][y+1] = 1;
		//		 postaw_tegesa(x-1,y+1);
			 }
			 
			 if( vincent.getFrontRight().isBlockingShot() == true )
			 {
				 plansza[x+1][y+1] = 1;
		//		 postaw_tegesa(x+1,y+1);
			 }

			 
			 if( vincent.getBackLeft().isBlockingShot() == true )
			 {
				plansza[x-1][y-1] = 1;
		//		 postaw_tegesa(x-1,y-1);
			 }
			 
			 if( vincent.getBackRight().isBlockingShot() == true )
			 {
				 plansza[x+1][y-1] = 1;
			//	 postaw_tegesa(x+1,y-1);
			 }
		 }
		 
		 
		 // alkoholo
		 if ( stan.getTankOrientation() == TankState.Orientation.SOUTH)
		 {
			 if( vincent.getFront().isBlockingShot() == true )
			 {
				 plansza[x][y-1] = 1;
			//	 postaw_tegesa(x,y-1);
			 }
			 
			 if( vincent.getBack().isBlockingShot() == true )
			 {
				 plansza[x][y+1] = 1;
		//		 postaw_tegesa(x,y+1);
			 }
			 
			 if( vincent.getLeft().isBlockingShot() == true )
			 {
				 plansza[x+1][y] = 1;
			//	 postaw_tegesa(x+1,y);
			 }
			 
			 if( vincent.getRight().isBlockingShot() == true )
			 {
				 plansza[x-1][y] = 1;
			//	 postaw_tegesa(x-1,y);
			 }
			 
			 if( vincent.getFrontLeft().isBlockingShot() == true )
			 {
				 plansza[x+1][y-1] = 1;
			//	 postaw_tegesa(x+1,y-1);
			 }
			 
			 if( vincent.getFrontRight().isBlockingShot() == true )
			 {
				 plansza[x-1][y-1] = 1;
			//	 postaw_tegesa(x-1,y-1);
			 }

			 
			 if( vincent.getBackLeft().isBlockingShot() == true )
			 {
				 plansza[x+1][y+1] = 1;
			//	 postaw_tegesa(x+1,y+1);
			 }
			 
			 if( vincent.getBackRight().isBlockingShot() == true )
			 {
				 plansza[x-1][y+1] = 1;
			//	 postaw_tegesa(x-1,y+1);
			 }
		 }
		 
		 
		 // disco chlosta
				 if ( stan.getTankOrientation() == TankState.Orientation.WEST)
				 {
					 if( vincent.getFront().isBlockingShot() == true )
					 {
						 plansza[x-1][y] = 1;
				//		 postaw_tegesa(x-1,y);
					 }
					 
					 if( vincent.getBack().isBlockingShot() == true )
					 {
						 plansza[x+1][y] = 1;
				//		 postaw_tegesa(x+1,y);
					 }
					 
					 if( vincent.getLeft().isBlockingShot() == true )
					 {
						 plansza[x][y-1] = 1;
					//	 postaw_tegesa(x,y-1);
					 }
					 
					 if( vincent.getRight().isBlockingShot() == true )
					 {
						 plansza[x][y+1] = 1;
					//	 postaw_tegesa(x,y+1);
					 }
					 
					 if( vincent.getFrontLeft().isBlockingShot() == true )
					 {
						 plansza[x-1][y-1] = 1;
				//		 postaw_tegesa(x-1,y-1);
					 }
					 
					 if( vincent.getFrontRight().isBlockingShot() == true )
					 {
						 plansza[x-1][y+1] = 1;
					//	 postaw_tegesa(x-1,y+1);
					 }

					 
					 if( vincent.getBackLeft().isBlockingShot() == true )
					 {
						 plansza[x+1][y-1] = 1;
					//	 postaw_tegesa(x+1,y-1);
					 }
					 
					 if( vincent.getBackRight().isBlockingShot() == true )
					 {
						 plansza[x+1][y+1] = 1;
					//	 postaw_tegesa(x+1,y+1);
					 }
				 }
				 
				 
				 // tu jest polska
				 if ( stan.getTankOrientation() == TankState.Orientation.EAST)
				 {
					 if( vincent.getFront().isBlockingShot() == true )
					 {
						 plansza[x+1][y] = 1;
				//		 postaw_tegesa(x+1,y);
					 }
					 
					 if( vincent.getBack().isBlockingShot() == true )
					 {
						 plansza[x-1][y] = 1;
				//		 postaw_tegesa(x-1,y);
					 }
					 
					 if( vincent.getLeft().isBlockingShot() == true )
					 {
						 plansza[x][y+1] = 1;
					//	 postaw_tegesa(x,y+1);
					 }
					 
					 if( vincent.getRight().isBlockingShot() == true )
					 {
						 plansza[x][y-1] = 1;
						// postaw_tegesa(x,y-1);
					 }
					 
					 if( vincent.getFrontLeft().isBlockingShot() == true )
					 {
						 plansza[x+1][y+1] = 1;
					//	 postaw_tegesa(x+1,y+1);
					 }
					 
					 if( vincent.getFrontRight().isBlockingShot() == true )
					 {
						 plansza[x+1][y-1] = 1;
					//	 postaw_tegesa(x+1,y-1);
					 }

					 
					 if( vincent.getBackLeft().isBlockingShot() == true )
					 {
						 plansza[x-1][y+1] = 1;
				//		 postaw_tegesa(x-1,y+1);
					 }
					 
					 if( vincent.getBackRight().isBlockingShot() == true )
					 {
						 plansza[x-1][y-1] = 1;
					//	 postaw_tegesa(x-1,y-1);
					 }
				 }
		 
		 
		 
	}
	
	
	

	private void we_lewo()
	{
		if( stan.getTankOrientation() == TankState.Orientation.NORTH || stan.getTankOrientation() == TankState.Orientation.SOUTH )
		{
			stos.push(TURN_TANK_LEFT);
			skrencam = 1;
		}
		else
		{
			tam_sie_wybieram_x = ja_x-1;
			tam_sie_wybieram_y = ja_y;
			if ( stan.getTankOrientation() == TankState.Orientation.WEST  )
			{
				stos.push(MOVE_FORWARD);
			}
			else
			{
				stos.push(MOVE_BACKWARD);
			}
		}
		
		
		
	}
	
	
	
	
	private void we_prawo()
	{
		if( stan.getTankOrientation() == TankState.Orientation.NORTH || stan.getTankOrientation() == TankState.Orientation.SOUTH )
		{
			stos.push(TURN_TANK_RIGHT);
			skrencam = 1;
		}
		else
		{
			tam_sie_wybieram_x = ja_x+1;
			tam_sie_wybieram_y = ja_y;
			if ( stan.getTankOrientation() == TankState.Orientation.WEST  )
			{
				stos.push(MOVE_BACKWARD);
			}
			else
			{
				stos.push(MOVE_FORWARD);
				
			}
		}
	}
	
	private void we_gore()
	{
		if( stan.getTankOrientation() == TankState.Orientation.WEST || stan.getTankOrientation() == TankState.Orientation.EAST )
		{
			stos.push(TURN_TANK_RIGHT);
			skrencam = 1;
		}
		else
		{
			tam_sie_wybieram_x = ja_x;
			tam_sie_wybieram_y = ja_y+1;
			if ( stan.getTankOrientation() == TankState.Orientation.NORTH  )
			{
				stos.push(MOVE_FORWARD);
			}
			else
			{
				stos.push(MOVE_BACKWARD);
				
			}
		}
	}
	
	
	private void we_dol()
	{
		if( stan.getTankOrientation() == TankState.Orientation.WEST || stan.getTankOrientation() == TankState.Orientation.EAST )
		{
			stos.push(TURN_TANK_RIGHT);
			skrencam = 1;
		}
		else
		{
			tam_sie_wybieram_x = ja_x;
			tam_sie_wybieram_y = ja_y-1;
			if ( stan.getTankOrientation() == TankState.Orientation.NORTH  )
			{
				stos.push(MOVE_BACKWARD);
			}
			else
			{
				stos.push(MOVE_FORWARD);	
			}
		}
	}
	
	
	protected class paracentymetr
	{
		public int x,y,dx,dy,bimbaly;
		public paracentymetr(int xx, int yy, int ddx, int ddy, int bbimbaly)
		{
			x=xx;
			y=yy;
			dx=ddx;
			dy=ddy;
			bimbaly=bbimbaly;
		}
		
	}
	
	private class klele
	{
		private int spodek;
		private int s;
		private paracentymetr[] t;
		
		klele()
		{
			t = new paracentymetr[1100];
			s=0;
			spodek=0;
		}
		
		void dodaj(paracentymetr p)
		{
			t[s++] = p;
			s%=1000;
		}
		boolean sajz()
		{
			return s!=spodek;
		}
		
		paracentymetr zdejm()
		{
			int co = spodek;
			spodek++;
			spodek%=1000;
			return t[co];		
		}
	}
	
	
	
	static int wielkosc_cyckow;
	static int MISECZKA_C;
	
	static int temporar[][];
	
	
	private void szoruj_temporara()
	{
		for(int i=0;i<20;i++)
			for(int j=0;j<20;j++)
				temporar[i][j]=0;
	}

	
	
	
	boolean bedzie(int x, int y)
	{
		
		if( plansza[x][y] == 0 || plansza[x][y] == 3)
			return true;
		return false;
		
	}
	
	
	
	
	
	void figo_fagot(int x,int y)
	{
		klele mokolotywka = new klele();
		paracentymetr py;
		
		mokolotywka.dodaj ( new paracentymetr(x,y,0,0,0));
		
		int p=0;
		
		szoruj_temporara();
		
		while( mokolotywka.sajz() )
		{
			py = mokolotywka.zdejm();
			x = py.x;
			y = py.y;
			p = py.bimbaly;
			

			
			p++;
			if( mapaBHP[x][y] > p)
			mapaBHP[x][y] = p;
			
			temporar[x][y]=1;
			
			/*
			if( mapaBHP[x][y] == 0 && ( plansza[x][y] == 0 || plansza[x][y] == 3) )
			{
				idex=x;
				idey=y;
					return;
			}
			*/
			
			if( bedzie(x-1,y)  && temporar[x-1][y] == 0 )
			{
				temporar[x-1][y] = 1;
				mokolotywka.dodaj( new paracentymetr(x-1, y, 0, 0,  p) );
				//beefes(x-1, y, dx, dy, bimbaly+1);
			}
			
	
			//if( plansza[x+1][y] == 0 && temporar[x+1][y] == 0)
			if( bedzie(x+1,y)  && temporar[x+1][y] == 0 )
			{
				temporar[x+1][y] = 1;
				mokolotywka.dodaj( new paracentymetr(x+1, y, 0, 0,  p) );;
				//beefes(x+1, y, dx, dy, bimbaly+1);
			}
			
	
		//	if( plansza[x][y+1] == 0 && temporar[x][y+1] == 0)
			if( bedzie(x,y+1)  && temporar[x][y+1] == 0 )
			{
				temporar[x][y+1] = 1;
				mokolotywka.dodaj( new paracentymetr(x, y+1, 0, 0,  p) );
			//	beefes(x, y+1, dx, dy, bimbaly+1);
			}
			
	
			//if( plansza[x][y-1] == 0 && temporar[x][y-1] == 0)
			if( bedzie(x,y-1)  && temporar[x][y-1] == 0 )
			{
				temporar[x][y-1] = 1;
				mokolotywka.dodaj( new paracentymetr(x, y-1, 0, 0,  p) );
				//beefes(x, y-1, dx, dy,  bimbaly+1);
			}
			
			
			
			
			
			
		}
		
		
	}
	
	
	
	
	
	
	
	void byfysy(int x,int y)
	{
		klele mokolotywka = new klele();
		paracentymetr py;
		
		mokolotywka.dodaj ( new paracentymetr(x,y,0,0,0));
		
		szoruj_temporara();
		
		while( mokolotywka.sajz() )
		{
			py = mokolotywka.zdejm();
			x = py.x;
			y = py.y;
			
			temporar[x][y]=1;
			
			
			if( mapaBHP[x][y] == 0 && ( plansza[x][y] == 0 || plansza[x][y] == 3) )
			{
				idex=x;
				idey=y;
					return;
			}
			
			if( bedzie(x-1,y)  && temporar[x-1][y] == 0 )
			{
				temporar[x-1][y] = 1;
				mokolotywka.dodaj( new paracentymetr(x-1, y, 0, 0,  0) );
				//beefes(x-1, y, dx, dy, bimbaly+1);
			}
			
	
			//if( plansza[x+1][y] == 0 && temporar[x+1][y] == 0)
			if( bedzie(x+1,y)  && temporar[x+1][y] == 0 )
			{
				temporar[x+1][y] = 1;
				mokolotywka.dodaj( new paracentymetr(x+1, y, 0, 0,  0) );;
				//beefes(x+1, y, dx, dy, bimbaly+1);
			}
			
	
		//	if( plansza[x][y+1] == 0 && temporar[x][y+1] == 0)
			if( bedzie(x,y+1)  && temporar[x][y+1] == 0 )
			{
				temporar[x][y+1] = 1;
				mokolotywka.dodaj( new paracentymetr(x, y+1, 0, 0,  0) );
			//	beefes(x, y+1, dx, dy, bimbaly+1);
			}
			
	
			//if( plansza[x][y-1] == 0 && temporar[x][y-1] == 0)
			if( bedzie(x,y-1)  && temporar[x][y-1] == 0 )
			{
				temporar[x][y-1] = 1;
				mokolotywka.dodaj( new paracentymetr(x, y-1, 0, 0,  0) );
				//beefes(x, y-1, dx, dy,  bimbaly+1);
			}
			
			
			
			
			
			
		}
		
		
	}
	
	
	
	void beefes(int xx,int yy,int dxx, int dyy, int bimbalyy)
	{
		
		paracentymetr pe;
		klele ciuchcia = new klele();
		
		ciuchcia.dodaj( new paracentymetr(xx,yy,dxx,dyy,bimbalyy));
		
		int x,y,dx,dy,bimbaly;

		while( ciuchcia.sajz() )
		{
			
		//	LOGGER.info( String.valueOf(ciuchcia.s) );
			
			pe = ciuchcia.zdejm();
			
			x = pe.x;
			y = pe.y;
			dx = pe.dx;
			dy = pe.dy;
			bimbaly = pe.bimbaly;
			temporar[x][y] = 1;
			if( x == dx && y == dy && bimbaly < wielkosc_cyckow ) // deska...
			{
				wielkosc_cyckow = bimbaly;
				return;
			}
			
			if( x == dx && y == dy )
				return;
			
			//if( plansza[x-1][y] == 0  && temporar[x-1][y] == 0 )
			if( bedzie(x-1,y)  && temporar[x-1][y] == 0 )
			{
				temporar[x-1][y] = 1;
				ciuchcia.dodaj( new paracentymetr(x-1, y, dx, dy, bimbaly+1) );
				//beefes(x-1, y, dx, dy, bimbaly+1);
			}
			
	
			//if( plansza[x+1][y] == 0 && temporar[x+1][y] == 0)
			if( bedzie(x+1,y)  && temporar[x+1][y] == 0 )
			{
				temporar[x+1][y] = 1;
				ciuchcia.dodaj( new paracentymetr(x+1, y, dx, dy, bimbaly+1) );
				//beefes(x+1, y, dx, dy, bimbaly+1);
			}
			
	
		//	if( plansza[x][y+1] == 0 && temporar[x][y+1] == 0)
			if( bedzie(x,y+1)  && temporar[x][y+1] == 0 )
			{
				temporar[x][y+1] = 1;
				ciuchcia.dodaj( new paracentymetr(x, y+1, dx, dy, bimbaly+1) );
			//	beefes(x, y+1, dx, dy, bimbaly+1);
			}
			
	
			//if( plansza[x][y-1] == 0 && temporar[x][y-1] == 0)
			if( bedzie(x,y-1)  && temporar[x][y-1] == 0 )
			{
				temporar[x][y-1] = 1;
				ciuchcia.dodaj( new paracentymetr(x, y-1, dx, dy,  bimbaly+1) );
				//beefes(x, y-1, dx, dy,  bimbaly+1);
			}
		
		}
	}
	
	private int ide_albo_nie_ide(int x,int y, int dx, int dy) // skad xy dokad xy
	{
		if( x == dx && y == dy )
			return 1;
		wielkosc_cyckow=MISECZKA_C;
		
		int cycmax = MISECZKA_C;
		
		int gdzie_mnie_trzeba_isc=1; // ide se we lewo
		
		
		
		
		szoruj_temporara();
	//	if( plansza[x-1][y] == 0)
		if ( bedzie(x-1,y) )
		{
			beefes(x-1, y, dx, dy, 1);
		}
		
		cycmax = wielkosc_cyckow;
		
		
		
		szoruj_temporara();
		//if( plansza[x+1][y] == 0)
		if ( bedzie(x+1,y) )
		{
			beefes(x+1, y, dx, dy, 1);
		}
		
		if ( wielkosc_cyckow < cycmax )
		{
			gdzie_mnie_trzeba_isc = 3; // jednak we prawo ide
			cycmax = wielkosc_cyckow;
		}
		
		
		szoruj_temporara();
	//	if( plansza[x][y+1] == 0)
		if ( bedzie(x,y+1) )
		{
			beefes(x, y+1, dx, dy,1);
		}
		
		if ( wielkosc_cyckow < cycmax )
		{
			gdzie_mnie_trzeba_isc = 2; // abo we gore
			cycmax = wielkosc_cyckow;
		}
		
		
		szoruj_temporara();
		//if( plansza[x][y-1] == 0)
		if ( bedzie(x,y-1) )
		{
			beefes(x, y-1, dx, dy, 1);
		}
		
		if ( wielkosc_cyckow < cycmax )
		{
			gdzie_mnie_trzeba_isc = 4; // dupa jasiu karuzela
			cycmax = wielkosc_cyckow;
		}
		
		if ( wielkosc_cyckow == MISECZKA_C )
			return 2;
		
		
	//	LOGGER.info( String.valueOf(wielkosc_cyckow) );
		
	//	pokaz_mape();
		
		if ( gdzie_mnie_trzeba_isc == 1 )
		{
			we_lewo();
		}
		
		if( gdzie_mnie_trzeba_isc == 2)
		{
			we_gore();
		}
		
		if( gdzie_mnie_trzeba_isc == 3)
		{
			we_prawo();
		}
		
		if( gdzie_mnie_trzeba_isc == 4)
		{
			we_dol();
		}
			
		return 0;
		
		
	}
	
	
	int gdzie_wiezyczkie;
	
	int apdejt_enem(int x, int y, int enem[][])
	{
		int[][] new_enem = new int[20][20];
		
		int ile=0;
		int mykmyk=0;
		int ii=0;
		int jj=0;
		int teges=0;
		
		for(int i=0;i<20;i++)
			for(int j=0;j<20;j++)
			{
				new_enem[i][j]=0;
			}
		
		if(
				(radar == RadarState.FRONT_RIGHT && stan.getTankOrientation() == TankState.Orientation.NORTH )
				||
				(radar == RadarState.BACK_LEFT && stan.getTankOrientation() == TankState.Orientation.SOUTH )
				||
				(radar == RadarState.BACK_RIGHT && stan.getTankOrientation() == TankState.Orientation.WEST )
				||
				(radar == RadarState.FRONT_LEFT && stan.getTankOrientation() == TankState.Orientation.EAST )
		  )
		{
			LOGGER.info("Front-Rajt");
			gdzie_wiezyczkie = 2;
			teges = y - x;
			for(int i = x + 1 ; i < 17 ; i++)
			{
				for(int j = y + 1 ; j< 17 ; j++)
				{
					if( (plansza[i][j]==0 || plansza[i][j]==3) && ( enem[i-1][j] == 1 || enem[i+1][j] == 1 || enem[i][j+1] == 1 || enem[i][j-1] == 1 || enem[i][j] == 1 ) )
					{
						new_enem[i][j]=1;
						ile++;
						if( j-i > teges )
							mykmyk++;
						if( j-i < teges)
							mykmyk--;
					}
					jj++;
				}
				ii++;
			}
			
			if( mykmyk >= 0 )
				gdzie_wiezyczkie = 2;
			else
				gdzie_wiezyczkie = 3;
			
			szoruj_enem();
			if ( ile > 0)
				for(int i = x + 1 ; i < 17 ; i++)
					for(int j = y + 1 ; j< 17 ; j++)
					{
						if( new_enem[i][j] == 1)
						{
							enem[i][j]=1;
							
						}
						
					}
			else
				for(int i = x + 1 ; i < 17 ; i++)
					for(int j = y + 1 ; j< 17 ; j++)
						enem[i][j]=1;
		} // koniec przod prawo
		
		
		
		
		if(
				(radar == RadarState.FRONT_LEFT && stan.getTankOrientation() == TankState.Orientation.NORTH )
				||
				(radar == RadarState.BACK_RIGHT && stan.getTankOrientation() == TankState.Orientation.SOUTH )
				||
				(radar == RadarState.FRONT_RIGHT && stan.getTankOrientation() == TankState.Orientation.WEST )
				||
				(radar == RadarState.BACK_LEFT && stan.getTankOrientation() == TankState.Orientation.EAST )
		  )
		{
			LOGGER.info("Front-Left");
			gdzie_wiezyczkie = 2;
			teges = x + y;
			for(int i = 1  ; i < x ; i++)
			{
				for(int j = y+1 ; j < 17 ; j++)
				{
					if( (plansza[i][j]==0 || plansza[i][j]==3) && ( enem[i-1][j] == 1 || enem[i+1][j] == 1 || enem[i][j+1] == 1 || enem[i][j-1] == 1 || enem[i][j] == 1 ) )
					{
						new_enem[i][j]=1;
						ile++;
						
						
						if( i + j > teges )
							mykmyk++;
						if( i + j < teges )
							mykmyk--;
						
					}
					jj++;
				}
				ii++;
			}
			
			
			if( mykmyk >= 0 )
				gdzie_wiezyczkie = 2;
			else
				gdzie_wiezyczkie = 1;
			
			
			szoruj_enem();
			if( ile > 0)
				for(int i = 1  ; i < x ; i++)
					for(int j = y+1 ; j < 17 ; j++)
				{
					if( new_enem[i][j] == 1)
					{
						enem[i][j]=1;
					}
				}
			else
				for(int i = 1  ; i < x ; i++)
					for(int j = y+1 ; j < 17 ; j++)
						enem[i][j]=1;
		} // koniec przod lewo
		
		
		
		if(
				(radar == RadarState.BACK_RIGHT && stan.getTankOrientation() == TankState.Orientation.NORTH )
				||
				(radar == RadarState.FRONT_LEFT && stan.getTankOrientation() == TankState.Orientation.SOUTH )
				||
				(radar == RadarState.BACK_LEFT && stan.getTankOrientation() == TankState.Orientation.WEST )
				||
				(radar == RadarState.FRONT_RIGHT && stan.getTankOrientation() == TankState.Orientation.EAST )
		  )
		{
			LOGGER.info("Bak-Rajt");
			gdzie_wiezyczkie = 4;
			teges = x + y;
			for(int i = x+1  ; i < 17  ; i++)
			{
				for(int j = 1 ; j<y ; j++)
				{
					if( (plansza[i][j]==0 || plansza[i][j]==3) && ( enem[i-1][j] == 1 || enem[i+1][j] == 1 || enem[i][j+1] == 1 || enem[i][j-1] == 1 || enem[i][j] == 1 ) )
					{
						new_enem[i][j]=1;
						ile++;
						
						
						if( i+j > teges )
							mykmyk++;
						if( j+i < teges )
							mykmyk--;
						
						
					}
					jj++;
				}
				ii++;
			}
			
			
			if( mykmyk >= 0 )
				gdzie_wiezyczkie = 3;
			else
				gdzie_wiezyczkie = 4;
			
			
			
			szoruj_enem();
			if( ile > 0)
				for(int i = x+1  ; i < 17  ; i++)
					for(int j = 1 ; j<y ; j++)
				{
					if( new_enem[i][j] == 1)
					{
						enem[i][j]=1;
					}
				}
			else
				for(int i = x+1  ; i < 17  ; i++)
					for(int j = 1 ; j<y ; j++)
						enem[i][j]=1;
		} // koniec tyl prawo
		
		
		if(
				(radar == RadarState.BACK_LEFT && stan.getTankOrientation() == TankState.Orientation.NORTH )
				||
				(radar == RadarState.FRONT_RIGHT && stan.getTankOrientation() == TankState.Orientation.SOUTH )
				||
				(radar == RadarState.FRONT_LEFT && stan.getTankOrientation() == TankState.Orientation.WEST )
				||
				(radar == RadarState.BACK_RIGHT && stan.getTankOrientation() == TankState.Orientation.EAST )
		  )
		{
			LOGGER.info("Back-Left");
			gdzie_wiezyczkie = 4;
			teges = y - x;
			for(int i = 1 ; i < x   ; i++)
			{
				for(int j = 1 ; j<y ; j++)
				{
					if( (plansza[i][j]==0 || plansza[i][j]==3) && ( enem[i-1][j] == 1 || enem[i+1][j] == 1 || enem[i][j+1] == 1 || enem[i][j-1] == 1 || enem[i][j] == 1 ) )
					{
						new_enem[i][j]=1;
						ile++;
						
						
						if( j - i > teges )
							mykmyk++;
						if( j - i < teges )
							mykmyk--;
						
						
					}
					jj++;
				}
				ii++;
			}
			
			if( mykmyk >= 0 )
				gdzie_wiezyczkie = 1;
			else
				gdzie_wiezyczkie = 4;
			
			
			szoruj_enem();
			if( ile > 0)
				for(int i = 1 ; i < x   ; i++)
					for(int j = 1 ; j<y ; j++)
				{
					if( new_enem[i][j] == 1)
					{
						enem[i][j]=1;
					}
				}
			else
				for(int i = 1 ; i < x   ; i++)
					for(int j = 1 ; j<y ; j++)
						enem[i][j]=1;
		} // koniec tyl lewo
		
		
		
		
		
		
		
		
		int wiac = 0 ;
		int juz = 0;
		
		
		
		
		if( stan.isEnemyVisible() && stan.getTurretOrientation() == TankState.Orientation.NORTH );
		else
			juz=1;
		
		
		
		if(
				(radar == RadarState.FRONT && stan.getTankOrientation() == TankState.Orientation.NORTH )
				||
				(radar == RadarState.BACK && stan.getTankOrientation() == TankState.Orientation.SOUTH )
				||
				(radar == RadarState.RIGHT && stan.getTankOrientation() == TankState.Orientation.WEST )
				||
				(radar == RadarState.LEFT && stan.getTankOrientation() == TankState.Orientation.EAST )
		  )
		{
			LOGGER.info("Front");
			gdzie_wiezyczkie = 2;
			int  i = x;
				for(int j = y+1 ; j<17  ; j++)
				{
					if( (plansza[i][j]==0 || plansza[i][j]==3) && ( enem[i-1][j] == 1 || enem[i+1][j] == 1 || enem[i][j+1] == 1 || enem[i][j-1] == 1 || enem[i][j] == 1 ) )
					{
						
						if( wiac == 1 && juz == 0)
						{
							new_enem[i][j]=1;
							ile++;
							break;
						}
						
						if( juz == 1)
						{
							new_enem[i][j]=1;
							ile++;
						}
					}
					if( plansza[i][j] != 0 && plansza[i][j]!=3)
						juz=1;
				}
			
			szoruj_enem();
			if( ile > 0)

				for(int j = y+1 ; j<17  ; j++)
				{
					if( new_enem[i][j] == 1)
					{
						enem[i][j]=1;
					}
				}
			else
					for(int j = y+1 ; j<17  ; j++)
						enem[i][j]=1;
		} // koniec Front
		
		
		if( stan.isEnemyVisible() && stan.getTurretOrientation() == TankState.Orientation.SOUTH );
			else
				juz=1;
		
		
		if(
				(radar == RadarState.BACK && stan.getTankOrientation() == TankState.Orientation.NORTH )
				||
				(radar == RadarState.FRONT && stan.getTankOrientation() == TankState.Orientation.SOUTH )
				||
				(radar == RadarState.LEFT && stan.getTankOrientation() == TankState.Orientation.WEST )
				||
				(radar == RadarState.RIGHT && stan.getTankOrientation() == TankState.Orientation.EAST )
		  )
		{
			LOGGER.info("Back");
			gdzie_wiezyczkie = 4;
			int  i = x;
				for(int j = y-1 ; j > 0 ; j--)
				{
					if( (plansza[i][j]==0 || plansza[i][j]==3) && ( enem[i-1][j] == 1 || enem[i+1][j] == 1 || enem[i][j+1] == 1 || enem[i][j-1] == 1 || enem[i][j] == 1 ) )
					{
						if( wiac == 1 && juz == 0)
						{
							new_enem[i][j]=1;
							ile++;
							break;
						}
						
						if( juz == 1)
						{
							new_enem[i][j]=1;
							ile++;
						}
						
						
					}
					if( plansza[i][j] != 0 && plansza[i][j]!=3)
						juz=1;
				}
			
			szoruj_enem();
			if( ile > 0)

				for(int j = 1 ; j< y ; j++)
				{
					if( new_enem[i][j] == 1)
					{
						enem[i][j]=1;
					}
				}
			else
					for(int j = 1 ; j< y ; j++)
						enem[i][j]=1;
		} // koniec Front
		
		
		
		
		if( stan.isEnemyVisible() && stan.getTurretOrientation() == TankState.Orientation.EAST );
			else
				juz=1;
		
		
		
		if(
				(radar == RadarState.RIGHT && stan.getTankOrientation() == TankState.Orientation.NORTH )
				||
				(radar == RadarState.LEFT && stan.getTankOrientation() == TankState.Orientation.SOUTH )
				||
				(radar == RadarState.BACK && stan.getTankOrientation() == TankState.Orientation.WEST )
				||
				(radar == RadarState.FRONT && stan.getTankOrientation() == TankState.Orientation.EAST )
		  )
		{
			LOGGER.info("Right");
			gdzie_wiezyczkie = 3;
			int  j = y;
				for(int i = x+1 ; i< 17 ; i++)
				{
					if( (plansza[i][j]==0 || plansza[i][j]==3) && ( enem[i-1][j] == 1 || enem[i+1][j] == 1 || enem[i][j+1] == 1 || enem[i][j-1] == 1 || enem[i][j] == 1 ) )
					{
						if( wiac == 1 && juz == 0)
						{
							new_enem[i][j]=1;
							ile++;
							break;
						}
						
						if( juz == 1)
						{
							new_enem[i][j]=1;
							ile++;
						}
						
					}
					if( plansza[i][j] != 0 && plansza[i][j]!=3)
						juz=1;
				}
			
			szoruj_enem();
			if( ile > 0)

				for(int i = x+1 ; i< 17 ; i++)
				{
					if( new_enem[i][j] == 1)
					{
						enem[i][j]=1;
					}
				}
			else
				for(int i = x+1 ; i< 17 ; i++)
						enem[i][j]=1;
		} // koniec RIGHT
		
		
		if( stan.isEnemyVisible() && stan.getTurretOrientation() == TankState.Orientation.WEST );
		else
			juz=1;
		
		if(
				(radar == RadarState.LEFT && stan.getTankOrientation() == TankState.Orientation.NORTH )
				||
				(radar == RadarState.RIGHT && stan.getTankOrientation() == TankState.Orientation.SOUTH )
				||
				(radar == RadarState.FRONT && stan.getTankOrientation() == TankState.Orientation.WEST )
				||
				(radar == RadarState.BACK && stan.getTankOrientation() == TankState.Orientation.EAST )
		  )
		{
			LOGGER.info("Left");
			gdzie_wiezyczkie = 1;
			int  j = y;
				for(int i = x-1 ; i>0 ; i--)
				{
					if( (plansza[i][j]==0 || plansza[i][j]==3) && ( enem[i-1][j] == 1 || enem[i+1][j] == 1 || enem[i][j+1] == 1 || enem[i][j-1] == 1 || enem[i][j] == 1 ) )
					{
						if( wiac == 1 && juz == 0)
						{
							new_enem[i][j]=1;
							ile++;
							break;
						}
						
						if( juz == 1)
						{
							new_enem[i][j]=1;
							ile++;
						}
					}
					if( plansza[i][j] != 0 && plansza[i][j]!=3)
						juz=1;
				}
			
			szoruj_enem();
			if( ile > 0)

				for(int i = 1 ; i< x ; i++)
				{
					if( new_enem[i][j] == 1)
					{
						enem[i][j]=1;
					}
				}
			else
				for(int i = 1 ; i< x ; i++)
						enem[i][j]=1;
		} // koniec Left
		
		
		
		
		
		
		
		
		
		return ile;
		
		
		
		
		
		
		
		
	}
	
	
	
	
	void cycki_w_wachocku()
	{
		int [][] new_enem = new int[25][25];
		for(int i = 0 ; i < 20 ; i++)
			for(int j = 0 ; j < 20 ; j++ )
				new_enem[i][j]=0;
		
		for(int i = 1 ; i < 17 ; i++)
			for(int j = 1 ; j < 17 ; j++)
			{
				if( (plansza[i][j]==0 || plansza[i][j]==3) && ( wiezyczka_enem[i-1][j] == 1 || wiezyczka_enem[i+1][j] == 1 || wiezyczka_enem[i][j+1] == 1 || wiezyczka_enem[i][j-1] == 1 || wiezyczka_enem[i][j] == 1 ) )
				{
					new_enem[i][j] = 1;
				}
			}
		
		
		
		for(int i = 1 ; i < 17 ; i++)
			for(int j = 1 ; j < 17 ; j++)
			{
				wiezyczka_enem[i][j] = new_enem[i][j];
			}
		
		
	}
	
	
	
	
	int ile_patafianow(int x, int y, int k)
	{
		int ile=0;
		
		if( k == 1 )
		{
			for(int i = x - 1 ; i > 0 && plansza[i][y] != 1   ; i--)
				if( wiezyczka_enem[i][y] == 1 )
					ile++;
		}
		
		if( k == 2 )
		{
			for(int i = y + 1 ;  i < 17  && plansza[x][i] != 1 ; i++)
				if( wiezyczka_enem[x][i] == 1 )
					ile++;
		}
		
		if( k == 3 )
		{
			for(int i = x + 1 ;  i < 17  && plansza[i][y] != 1 ; i++)
				if( wiezyczka_enem[i][y] == 1 )
					ile++;
		}
		
		if( k == 4 )
		{
			for(int i = y - 1 ;  i > 0  && plansza[x][i] != 1 ; i--)
				if( wiezyczka_enem[x][i] == 1 )
					ile++;
		}
		
		
		
		return ile;
	}
	
	
	
	
	
	int apdejt_wiezyczkie(int x, int y, int enem[][])
	{
	
		int ile=0;
		int mykmyk=0;
		int patafiany = 0;
		int teges=0;
		

		
		if(
				(radar == RadarState.FRONT_RIGHT && stan.getTankOrientation() == TankState.Orientation.NORTH )
				||
				(radar == RadarState.BACK_LEFT && stan.getTankOrientation() == TankState.Orientation.SOUTH )
				||
				(radar == RadarState.BACK_RIGHT && stan.getTankOrientation() == TankState.Orientation.WEST )
				||
				(radar == RadarState.FRONT_LEFT && stan.getTankOrientation() == TankState.Orientation.EAST )
		  )
		{
			LOGGER.info("Front-Rajt");
			gdzie_wiezyczkie = 2;
			
			
			patafiany = ile_patafianow(x,y, 2);
			if( patafiany > 0 )
				gdzie_wiezyczkie = 2;
			if( ile_patafianow(x,y,3) > patafiany )
			{
				gdzie_wiezyczkie = 3;
				patafiany=1;
			}
				
			
			if( patafiany == 0)
			{
				teges = y - x;
				for(int i = x + 1 ; i < 17 ; i++)
				{
					for(int j = y + 1 ; j< 17 ; j++)
					{
						if( (plansza[i][j]==0 || plansza[i][j]==3) && ( enem[i-1][j] == 1 || enem[i+1][j] == 1 || enem[i][j+1] == 1 || enem[i][j-1] == 1 || enem[i][j] == 1 ) )
						{
						//	new_enem[i][j]=1;
							ile++;
							if( j-i > teges )
								mykmyk++;
							if( j-i < teges)
								mykmyk--;
						}
		
					}
	
				}
				
				if( mykmyk >= 0 )
					gdzie_wiezyczkie = 2;
				else
					gdzie_wiezyczkie = 3;
			}
			
			
			if( plansza[x][y+1] == 1 ) //(x,y+1) )
				gdzie_wiezyczkie = 3;
			if( plansza[x+1][y] == 1 ) // !bedzie(x+1, y) )
				gdzie_wiezyczkie = 2;
			

		} // koniec przod prawo
		
		
		
		
		if(
				(radar == RadarState.FRONT_LEFT && stan.getTankOrientation() == TankState.Orientation.NORTH )
				||
				(radar == RadarState.BACK_RIGHT && stan.getTankOrientation() == TankState.Orientation.SOUTH )
				||
				(radar == RadarState.FRONT_RIGHT && stan.getTankOrientation() == TankState.Orientation.WEST )
				||
				(radar == RadarState.BACK_LEFT && stan.getTankOrientation() == TankState.Orientation.EAST )
		  )
		{
			LOGGER.info("Front-Left");
			gdzie_wiezyczkie = 2;
			
			
			patafiany = ile_patafianow(x,y, 2);
			if( patafiany > 0 )
				gdzie_wiezyczkie = 2;
			if( ile_patafianow(x,y,1) > patafiany )
			{
				gdzie_wiezyczkie = 1;
				patafiany=1;
			}
			
			if( patafiany == 0)
			{
				teges = x + y;
				for(int i = 1  ; i < x ; i++)
				{
					for(int j = y+1 ; j < 17 ; j++)
					{
						if( (plansza[i][j]==0 || plansza[i][j]==3) && ( enem[i-1][j] == 1 || enem[i+1][j] == 1 || enem[i][j+1] == 1 || enem[i][j-1] == 1 || enem[i][j] == 1 ) )
						{
						//	new_enem[i][j]=1;
							ile++;
							
							
							if( i + j > teges )
								mykmyk++;
							if( i + j < teges )
								mykmyk--;
							
						}
	
					}
	
				}
				
				
				if( mykmyk >= 0 )
					gdzie_wiezyczkie = 2;
				else
					gdzie_wiezyczkie = 1;
			}
			
			
			if( plansza[x][y+1] == 1 ) //!bedzie(x,y+1) )
				gdzie_wiezyczkie = 1;
			if( plansza[x-1][y] == 1) //!bedzie(x-1, y) )
				gdzie_wiezyczkie = 2;
			

		} // koniec przod lewo
		
		
		
		if(
				(radar == RadarState.BACK_RIGHT && stan.getTankOrientation() == TankState.Orientation.NORTH )
				||
				(radar == RadarState.FRONT_LEFT && stan.getTankOrientation() == TankState.Orientation.SOUTH )
				||
				(radar == RadarState.BACK_LEFT && stan.getTankOrientation() == TankState.Orientation.WEST )
				||
				(radar == RadarState.FRONT_RIGHT && stan.getTankOrientation() == TankState.Orientation.EAST )
		  )
		{
			LOGGER.info("Bak-Rajt");
			gdzie_wiezyczkie = 4;
			
			
			patafiany = ile_patafianow(x,y, 4);
			if( patafiany > 0 )
				gdzie_wiezyczkie = 4;
			if( ile_patafianow(x,y,3) > patafiany )
			{
				gdzie_wiezyczkie = 3;
				patafiany=1;
			}
			
			if( patafiany == 0)
			{
				teges = x + y;
				for(int i = x+1  ; i < 17  ; i++)
				{
					for(int j = 1 ; j<y ; j++)
					{
						if( (plansza[i][j]==0 || plansza[i][j]==3) && ( enem[i-1][j] == 1 || enem[i+1][j] == 1 || enem[i][j+1] == 1 || enem[i][j-1] == 1 || enem[i][j] == 1 ) )
						{
				//			new_enem[i][j]=1;
							ile++;
							
							
							if( i+j > teges )
								mykmyk++;
							if( j+i < teges )
								mykmyk--;
							
							
						}
	
					}
	
				}
				
				
				if( mykmyk >= 0 )
					gdzie_wiezyczkie = 3;
				else
					gdzie_wiezyczkie = 4;
			}
			
			if( plansza[x+1][y] == 1 ) // !bedzie(x+1,y) )
				gdzie_wiezyczkie = 4;
			if( plansza[x][y-1] == 1 ) //!bedzie(x, y-1) )
				gdzie_wiezyczkie = 3;
			
			

		} // koniec tyl prawo
		
		
		if(
				(radar == RadarState.BACK_LEFT && stan.getTankOrientation() == TankState.Orientation.NORTH )
				||
				(radar == RadarState.FRONT_RIGHT && stan.getTankOrientation() == TankState.Orientation.SOUTH )
				||
				(radar == RadarState.FRONT_LEFT && stan.getTankOrientation() == TankState.Orientation.WEST )
				||
				(radar == RadarState.BACK_RIGHT && stan.getTankOrientation() == TankState.Orientation.EAST )
		  )
		{
			LOGGER.info("Back-Left");
			gdzie_wiezyczkie = 4;
			
			
			patafiany = ile_patafianow(x,y, 4);
			if( patafiany > 0 )
				gdzie_wiezyczkie = 4;
			if( ile_patafianow(x,y,1) > patafiany )
			{
				gdzie_wiezyczkie = 1;
				patafiany=1;
			}
			
			if( patafiany == 0)
			{
				teges = y - x;
				for(int i = 1 ; i < x   ; i++)
				{
					for(int j = 1 ; j<y ; j++)
					{
						if( (plansza[i][j]==0 || plansza[i][j]==3) && ( enem[i-1][j] == 1 || enem[i+1][j] == 1 || enem[i][j+1] == 1 || enem[i][j-1] == 1 || enem[i][j] == 1 ) )
						{
				//			new_enem[i][j]=1;
							ile++;
							
							
							if( j - i > teges )
								mykmyk++;
							if( j - i < teges )
								mykmyk--;
							
							
						}
	
					}
	
				}
				
				if( mykmyk >= 0 )
					gdzie_wiezyczkie = 1;
				else
					gdzie_wiezyczkie = 4;
			}
			
			if( plansza[x-1][y] == 1 ) // !bedzie(x-1,y) )
				gdzie_wiezyczkie = 4;
			if( plansza[x][y-1] == 1 ) //!bedzie(x, y-1) )
				gdzie_wiezyczkie = 1;
			

		} // koniec tyl lewo
		
		
		
		
		
		
		
		
		int wiac = 0 ;
		int juz = 0;
		
		
		
		
		if( stan.isEnemyVisible() && stan.getTurretOrientation() == TankState.Orientation.NORTH );
		else
			juz=1;
		
		
		
		if(
				(radar == RadarState.FRONT && stan.getTankOrientation() == TankState.Orientation.NORTH )
				||
				(radar == RadarState.BACK && stan.getTankOrientation() == TankState.Orientation.SOUTH )
				||
				(radar == RadarState.RIGHT && stan.getTankOrientation() == TankState.Orientation.WEST )
				||
				(radar == RadarState.LEFT && stan.getTankOrientation() == TankState.Orientation.EAST )
		  )
		{
			LOGGER.info("Front");
			gdzie_wiezyczkie = 2;
			int  i = x;
				for(int j = y+1 ; j<17  ; j++)
				{
					if( (plansza[i][j]==0 || plansza[i][j]==3) && ( enem[i-1][j] == 1 || enem[i+1][j] == 1 || enem[i][j+1] == 1 || enem[i][j-1] == 1 || enem[i][j] == 1 ) )
					{
						
						if( wiac == 1 && juz == 0)
						{
				//			new_enem[i][j]=1;
							ile++;
							break;
						}
						
						if( juz == 1)
						{
				//			new_enem[i][j]=1;
							ile++;
						}
					}
					if( plansza[i][j] != 0 && plansza[i][j]!=3)
						juz=1;
				}

		} // koniec Front
		
		
		if( stan.isEnemyVisible() && stan.getTurretOrientation() == TankState.Orientation.SOUTH );
			else
				juz=1;
		
		
		if(
				(radar == RadarState.BACK && stan.getTankOrientation() == TankState.Orientation.NORTH )
				||
				(radar == RadarState.FRONT && stan.getTankOrientation() == TankState.Orientation.SOUTH )
				||
				(radar == RadarState.LEFT && stan.getTankOrientation() == TankState.Orientation.WEST )
				||
				(radar == RadarState.RIGHT && stan.getTankOrientation() == TankState.Orientation.EAST )
		  )
		{
			LOGGER.info("Back");
			gdzie_wiezyczkie = 4;
			int  i = x;
				for(int j = y-1 ; j > 0 ; j--)
				{
					if( (plansza[i][j]==0 || plansza[i][j]==3) && ( enem[i-1][j] == 1 || enem[i+1][j] == 1 || enem[i][j+1] == 1 || enem[i][j-1] == 1 || enem[i][j] == 1 ) )
					{
						if( wiac == 1 && juz == 0)
						{
			//				new_enem[i][j]=1;
							ile++;
							break;
						}
						
						if( juz == 1)
						{
				//			new_enem[i][j]=1;
							ile++;
						}
						
						
					}
					if( plansza[i][j] != 0 && plansza[i][j]!=3)
						juz=1;
				}

		} // koniec Front
		
		
		
		
		if( stan.isEnemyVisible() && stan.getTurretOrientation() == TankState.Orientation.EAST );
			else
				juz=1;
		
		
		
		if(
				(radar == RadarState.RIGHT && stan.getTankOrientation() == TankState.Orientation.NORTH )
				||
				(radar == RadarState.LEFT && stan.getTankOrientation() == TankState.Orientation.SOUTH )
				||
				(radar == RadarState.BACK && stan.getTankOrientation() == TankState.Orientation.WEST )
				||
				(radar == RadarState.FRONT && stan.getTankOrientation() == TankState.Orientation.EAST )
		  )
		{
			LOGGER.info("Right");
			gdzie_wiezyczkie = 3;
			int  j = y;
				for(int i = x+1 ; i< 17 ; i++)
				{
					if( (plansza[i][j]==0 || plansza[i][j]==3) && ( enem[i-1][j] == 1 || enem[i+1][j] == 1 || enem[i][j+1] == 1 || enem[i][j-1] == 1 || enem[i][j] == 1 ) )
					{
						if( wiac == 1 && juz == 0)
						{
				//			new_enem[i][j]=1;
							ile++;
							break;
						}
						
						if( juz == 1)
						{
				//			new_enem[i][j]=1;
							ile++;
						}
						
					}
					if( plansza[i][j] != 0 && plansza[i][j]!=3)
						juz=1;
				}

		} // koniec RIGHT
		
		
		if( stan.isEnemyVisible() && stan.getTurretOrientation() == TankState.Orientation.WEST );
		else
			juz=1;
		
		if(
				(radar == RadarState.LEFT && stan.getTankOrientation() == TankState.Orientation.NORTH )
				||
				(radar == RadarState.RIGHT && stan.getTankOrientation() == TankState.Orientation.SOUTH )
				||
				(radar == RadarState.FRONT && stan.getTankOrientation() == TankState.Orientation.WEST )
				||
				(radar == RadarState.BACK && stan.getTankOrientation() == TankState.Orientation.EAST )
		  )
		{
			LOGGER.info("Left");
			gdzie_wiezyczkie = 1;
			int  j = y;
				for(int i = x-1 ; i>0 ; i--)
				{
					if( (plansza[i][j]==0 || plansza[i][j]==3) && ( enem[i-1][j] == 1 || enem[i+1][j] == 1 || enem[i][j+1] == 1 || enem[i][j-1] == 1 || enem[i][j] == 1 ) )
					{
						if( wiac == 1 && juz == 0)
						{
			//				new_enem[i][j]=1;
							ile++;
							break;
						}
						
						if( juz == 1)
						{
			//				new_enem[i][j]=1;
							ile++;
						}
					}
					if( plansza[i][j] != 0 && plansza[i][j]!=3)
						juz=1;
				}
			
		} // koniec Left
		
		
		
		LOGGER.info("Patafiany: " + patafiany);
		
		
		
		
		
		return ile;
		
		
		
		
		
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	void szczelaj_z_ajrstrajka(int asdasda)
	{
		Random cyk = new Random();
		int pstryk = cyk.nextInt(asdasda);
		for(int i=1;i<17;i++)
			for(int j=1;j<17;j++)
			{
				if( enem[i][j] == 1)
				{
					pstryk++;
					if( pstryk >= asdasda)
					{
						stos.push(AIRSTRIKE, i,j);
						return;
					}
				}
			}
	}
	
	
	
	
	int wiezyczka_we_lewo()
	{
		if( stan.getTurretOrientation() == TankState.Orientation.NORTH  ||  stan.getTurretOrientation() == TankState.Orientation.EAST)
		{
			stos.push(TURN_TURRET_LEFT);
			return 1;
		}
			
		if( stan.getTurretOrientation() == TankState.Orientation.SOUTH )
		{
			stos.push(TURN_TURRET_RIGHT);
			return 1;
		}
			
			
			return 0;
	}
	
	
	
	int wiezyczka_we_prawo()
	{
		if( stan.getTurretOrientation() == TankState.Orientation.NORTH  ||  stan.getTurretOrientation() == TankState.Orientation.WEST)
		{
			stos.push(TURN_TURRET_RIGHT);
			return 1;
		}
			
		if( stan.getTurretOrientation() == TankState.Orientation.SOUTH )
		{
			stos.push(TURN_TURRET_LEFT);
			return 1;
		}
			
			
			return 0;
	}

	
	
	
	int wiezyczka_we_gore()
	{
		if( stan.getTurretOrientation() == TankState.Orientation.EAST  ||  stan.getTurretOrientation() == TankState.Orientation.SOUTH)
		{
			stos.push(TURN_TURRET_LEFT);
			return 1;
		}
			
		if( stan.getTurretOrientation() == TankState.Orientation.WEST )
		{
			stos.push(TURN_TURRET_RIGHT);
			return 1;
		}
			
			
			return 0;
	}
	
	
	int wiezyczka_we_dol()
	{
		if( stan.getTurretOrientation() == TankState.Orientation.WEST  ||  stan.getTurretOrientation() == TankState.Orientation.NORTH)
		{
			stos.push(TURN_TURRET_LEFT);
			return 1;
		}
			
		if( stan.getTurretOrientation() == TankState.Orientation.EAST )
		{
			stos.push(TURN_TURRET_RIGHT);
			return 1;
		}
			
			
			return 0;
	}
	
	
	
	void przekrenc_wiezyczkie()
	{
		if ( gdzie_wiezyczkie == 1 )
			wiezyczka_we_lewo();
		
		if ( gdzie_wiezyczkie == 2 )
			wiezyczka_we_gore();
		
		if ( gdzie_wiezyczkie == 3 )
			wiezyczka_we_prawo();
		
		if ( gdzie_wiezyczkie == 4 )
			wiezyczka_we_dol();
		
		
		
			
	}
	
	

	
	void BHP(int x, int y, int p, int k)
	{
			
		if( plansza[x][y] == 1 )
			return;
		
		if( x <= 1 || x>=17 || y<=1 || y>=17)
			return;
		
		mapaBHP[x][y]+=p;
		p+=20;
		if ( k == 1)
		{
			if ( bedzie(x-1,y) || plansza[x-1][y] == 2 )
			{
				BHP ( x-1, y , p, k);
			}
		}
		
		if ( k == 2)
		{
			if ( bedzie(x,y+1)  || plansza[x][y+1] == 2 )
			{
				BHP ( x, y+1 , p, k);
			}
		}
		
		if ( k == 3)
		{
			if ( bedzie(x+1,y) || plansza[x+1][y] == 2 )
			{
				BHP ( x+1, y , p, k);
			}
		}
		
		
		if ( k == 4)
		{
			if ( bedzie(x,y-1) || plansza[x][y-1] == 2 )
			{
				BHP ( x, y-1 , p, k);
			}
		}
		
		
		
		
	}
	
	
	void myk_BHP()
	{
		for(int i = 1; i < 17 ; i++)
			for( int j = 1 ; j < 17 ; j ++ )
				if( enem[i][j]==1)
				{
					
					figo_fagot(i,j);
					BHP(i,j,-1000,1);
					BHP(i,j,-1000,2);
					BHP(i,j,-1000,3);
					BHP(i,j,-1000,4);
				}
	
	}
	
	
	
	
	
	boolean gdzie_spierdzielac(int x, int y)
	{
		int dzie=1;
		int min = -9999; // to jednak maks
		
		
		if( mapaBHP[x-1][y] > min && bedzie(x-1,y) )
		{
			dzie = 1;
			min = mapaBHP[x-1][y];
		}
		
		
		if( mapaBHP[x][y+1] > min && bedzie(x,y+1) )
		{
			dzie = 2;
			min = mapaBHP[x][y+1];
		}
		
		if( mapaBHP[x+1][y] > min && bedzie(x+1,y))
		{
			dzie = 3;
			min = mapaBHP[x+1][y];
		}
		
		if( mapaBHP[x][y-1] > min && bedzie(x,y-1) )
		{
			dzie = 4;
			min = mapaBHP[x][y-1];
		}
		
		if( dzie == 1)
		{
			//we_lewo();
			idex = x - 1;
			idey  = y;
			LOGGER.info("Spierdzielam we lewo");
		}
		if ( dzie == 2)
		{
			//we_gore();
			idex = x;
			idey = y + 1;
			LOGGER.info("Spierdzielam we gore");
		}
		if( dzie == 3)
		{
		//	we_prawo();
			idex = x + 1;
			idey = y;
			LOGGER.info("Spierdzielam we prawo");
		}
		if (dzie == 4)
		{
			//we_dol();
			idex = x;
			idey = y - 1;
			LOGGER.info("Spierdzielam we dol");
		}
		
		if ( min <= mapaBHP[x][y])
		{
			return false;
		}
		return true;
		
		
	}
	
	
	
	void szukaj_mina(int x, int y)
	{
		int min = 0;
		for(int i = 1 ; i < 17 ; i ++)
			for( int j = 0 ; j < 17 ; j ++)
			{
				if( i == x && y == j)
					continue;
				if( mapaBHP[i][j] > min && bedzie ( i, j))
				{
					min = mapaBHP[i][j];
					idex=i;
					idey=j;
				}
			}
	}
	
	
	
	boolean tu_mina_dobra_bedzie(int x, int y)
	{
		
		if( bedzie(x-1,y) && bedzie(x+1,y) && !bedzie(x,y+1) && !bedzie(x,y-1)  )
			return true;
		
		if( !bedzie(x-1,y) && !bedzie(x+1,y) && bedzie(x,y+1) && bedzie(x,y-1)  )
			return true;		
	
		return false;
		
		
		
		
		
	}
	
	void apdejt_miny()
	{
		for(int i= 1 ; i < 17 ; i++)
			for(int j = 0 ; j < 17 ; j ++)
			{
				if( enem[i][j] == 1)
				{
					miny[i][j]++;
				}
			}
	}
	
	
	
	void rozdupc_miny(int x, int y)
	{
		miny[x][y] = -1;
		miny[x-1][y] =-1;
		miny[x+1][y] =-1;
		miny[x][y+1]=-1;
		miny[x][y-1]=-1;
		miny[x+1][y+1]=-1;
		miny[x+1][y-1]=-1;
		miny[x-1][y+1]=-1;
		miny[x-1][y-1]=-1;
	}
	
	
	boolean moze_byc(int x, int y)
	{
		for(int i=x-1;i<=x+1;i++)
			for(int j=y-1;j<=y+1;j++)
			{
				if( miny[i][j]>=1)
					return true;
			}
		return false;
	}
	
	
	void losuj(int ja_x, int ja_y)
	{
		Random rrr = new Random();
		idex=rrr.nextInt(17);
		idey=rrr.nextInt(17);

				while( plansza[idex][idey] != 0  && ( ja_x!=idex || ja_y!=idey) )
				{
						idex=rrr.nextInt(17);
						idey=rrr.nextInt(17);
				}
	}
	
	
	@Override
	public TankCommandStack process(TankState arg0) {
		// TODO Auto-generated method stub
		stan = arg0;
		
		skrencam = 0;
		
		
		popehp = teraehp;
		teraehp = stan.getEnemyHealth();
		
		int poppos_x = ja_x;
		int poppos_y = ja_y;
		ja_x = stan.getLocationX();
		ja_y = stan.getLocationY();
		
		
		if( poppos_x == ja_x && poppos_y == ja_y)
		{
			ile_w_miejscu++;
		}
		else
			ile_w_miejscu=0;
		
		
		String stanik = new String("");
		String stanikadd = new String("");
		
		
		
		
		
		stos = stan.getTankCommandStack();
		
		vincent = stan.getVicinity();
		
		plansza_apdejt( ja_x  , ja_y  );
		
		
		int ily=0;
		
		
		
		
		radar = stan.getRadar().getRadarState();
		
		
		
		if( skanowawszy == 1)
		{
			LOGGER.info("Po skanowancji 1");
			if( stan.getLastKnownEnemyLocation().getX() != -1)
			{
				LOGGER.info("Po skanowancji 2");
				szoruj_enem();
				enem[stan.getLastKnownEnemyLocation().getX()][stan.getLastKnownEnemyLocation().getY()]=1;
			}
		}
		
		
		if( runda == 0 )
		{
			if( ja_x == s1x && ja_y == s1y)
			{
				enem[s2x][s2y] = 1;
			}
			else
			{
				enem[s1x][s1y]=1;
			}
		}
		else
			ily = apdejt_enem(ja_x, ja_y, enem);
		
		
		
		
		
		
		
		
		
		if( stan.isEnemyVisible() == true )
		{
			if( stan.getLastKnownEnemyLocation().getX() != -1)
			{
				LOGGER.info("Widze typa");
				szoruj_enem();
				enem[stan.getLastKnownEnemyLocation().getX()][stan.getLastKnownEnemyLocation().getY()]=1;
			}
		}
		
		
		
		
		
		
		
		if ( ily == 0 )
			ily = 99;
		
		
		
		
		
		
		apdejt_miny();
		
		
		
		szoruj_BHP();
		myk_BHP();
		//pokaz_mape();
		pokaz_enem();
		
	//	pokaz_mine();
		LOGGER.info("BHP"); // patalach
		pokaz_BHP();
		//stan.isEnemyVisible();
		
		
		
		if( cycuszki > 0 && stan.getPlacedMines() == 0 && ( teraehp < popehp ) ) 
		{
			ily=1;
			szoruj_enem();
			enem[cycki_x][cycki_y]=1;
			LOGGER.info("Wpierdzielil sie na mine"); // patalach
			cycuszki=0;
		}
		
		
		/////////// porobili sie tegesy, tera mozna nomanie se ten tego
		
		
		stanik = "smigaj";
		stanikadd = "krec";
		
		
		
		
		if( radar == RadarState.FRONT  || radar == RadarState.LEFT || radar == RadarState.RIGHT || radar == RadarState.BACK )
		{
			stanik = "smigaj";
			stanikadd = "krec";
		}
		


		if(  moze_byc (ja_x, ja_y))
		{
			stanik = "scanuj";
			skanowawszy = 0;
		}
		
		if ( skanowawszy == 1 && ( vincent.isMinesAround() || vincent.isTeleportAround() )  )
		{
			stanik = "rozdupcaj";
			skanowawszy = 0;
		}
	
		
		
		if( cycki == 1 && stan.getPlacedMines() == 0 )
		{
			if( tu_mina_dobra_bedzie(ja_x,ja_y) )
				stanik = "minuj";
		}
		
		
		
		if( ily > 0 && ily < 3 && stan.getActionPoints() == 5  )
		{
			stanik = "spierdzielaj";
			stanikadd = "ajrstrajk";
		}
		
		
		
		
		if(  ( stan.getHealth() < 4 || stan.getActionPoints() != 5 ) && stan.isEnemyVisible() == false )
		{
			stanik = "spierdzielaj";
			stanikadd = "krec";
		}
		
		
		if( mapaBHP[ja_x][ja_y] < 5  && ily < 25 )
		{
			stanik = "spierdzielaj";
			stanikadd = "krec";
			
		}
		
		
		if( stan.getHealth() < 4 && stan.getActionPoints() == 5 )
		{
			stanik = "spierdzielaj";
			stanikadd = "repair";
		}
		
		
		if( stan.getHealth() == 4 && stan.getEnemyHealth()>=4 && stan.getActionPoints() == 5 && stanik.equals("spierdzielaj") == false)
			stanikadd = "repair";
		
		
		if( stan.isEnemyVisible() )
		{
			stanik = "spierdzielaj";// = 1;
			stanikadd = "nadupcaj";
			cycki = 1;
			
		}
		
		
		if (   (stan.getHealth() < 4 || stan.isStunned()) && stan.getActionPoints() == 5)
			stanikadd = "repair";
		
		
		
		if( skanowawszy == 1)
			skanowawszy = 0;
		
		
		
		LOGGER.info(ily + " mozliwosci");
		LOGGER.info( mapaBHP[ja_x][ja_y] + " odleglosc od typa");
		LOGGER.info("Stany: " + stanik + " " + stanikadd);
		
		
	//	LOGGER.info( String.valueOf(ja_x) );
	//	LOGGER.info( String.valueOf(ja_y) );
	///	pokaz_mape();
		
		
		
		
		
		/*
		if( stan.getActionPoints() < 5 )
		{
			if( stan.getVicinity().getFront().isBlockingMove() == false)
			stos.push(MOVE_FORWARD);
			else
			stos.push(TURN_TANK_LEFT);
		}
		else
		{
			if( zeskanowal == 0)
			{
				stos.push(RADAR_SCAN);
				zeskanowal=1;
			}
			else
			{
				zeskanowal=0;
				stos.push(AIRSTRIKE, stan.getLastKnownEnemyLocation().getX() , stan.getLastKnownEnemyLocation().getY() );
			}
			
		}
		*/
		
		//we_lewo();
		
		
		int dupa_jasia=0;
		
		if( stanik.equals("smigaj") || stanik.equals("spierdzielaj") )
			{
			
						if(stanik.equals("spierdzielaj")  )
						{
							idex=ja_x;
							idey=ja_y;
							if( gdzie_spierdzielac(ja_x,ja_y) == false && ile_w_miejscu < 8 ) 
							{
								stanik = "scanuj";
								dupa_jasia=1;
							}
							
						}
						else
						{
							LOGGER.info("xy " + idex + " " + idey);
							if(  (ja_x == idex && ja_y == idey) || (idex == 0 && idey == 0))
							{
								losuj(ja_x, ja_y);
							}
							
						}
						if( dupa_jasia == 0)
						{
						LOGGER.info("Ide do " + idex + " " + idey);
							while( ide_albo_nie_ide(ja_x, ja_y, idex,idey) == 2 )
								losuj(ja_x, ja_y);// stos.push(TURN_TANK_LEFT);
						}
	
		
			}
			
				
				
		
		
		/*
		if( stanik.equals("smigaj") || stanik.equals("spierdzielaj") )
			gdzie_spierdzielac(ja_x,ja_y);
		*/
		
		
		
		if( stanikadd.equals("ajrstrajk") )
		{
				szczelaj_z_ajrstrajka(ily);
		}
		
		
		copjuj_enem();
		//apdejt_enem(ja_x, ja_y, wiezyczka_enem);
		cycki_w_wachocku();
		
//		LOGGER.info("wiezyczka enem");
//		pokaz_wiezyczka_enem();
		
		if( skrencam == 0 && stanik.equals("scanuj") == false )
			apdejt_wiezyczkie(tam_sie_wybieram_x, tam_sie_wybieram_y, wiezyczka_enem);
		else
			apdejt_wiezyczkie(ja_x, ja_y, wiezyczka_enem);
		
		if( stanikadd.equals("krec"))
		{
				przekrenc_wiezyczkie();
		}
		
		if( stanikadd.equals("nadupcaj"))
		{
			stos.push(SHOOT_CANNON);
		}
		
		if( stanikadd.equals("repair"))
		{
			stos.push(REPAIR);
		}
		
		
		if ( stanik.equals("minuj"))
		{
			stos.push(PLACE_MINE);
			cycuszki = 1;
			cycki_x = ja_x;
			cycki_y = ja_y;
		}
		
		
		if( stanik.equals("scanuj"))
		{
			stos.push(RADAR_SCAN);
			skanowawszy = 1;
			rozdupc_miny(ja_x, ja_y);
		}
		
		
		if( stanik.equals("rozdupcaj"))
		{
			stos.push(EMP);
		}
		
		

		//	
		
	//	we_prawo();
	
	
	//	we_gore();
		
		 
		 runda++;
		return stos;
	}

	
	private CurrentMapType aktualnaPlansza;
	private HashMap<Location, Character> mapa;
	
	
	@Override
	public void processMatchStart(CurrentMapType arg0) {
		// TODO Auto-generated method stub
		aktualnaPlansza = arg0;
		mapa = aktualnaPlansza.getMap().getTerrain();
		zeskanowal=0;
		
		idex=0;
		idey=0;
		runda = 0;
		cycki=0;
		skanowawszy = 0;
		ile_w_miejscu=0;
		cycuszki = 0;
		MISECZKA_C = 999999;
		temporar = new int[25][25];
		plansza = new int[50][50];
		enem = new int[50][50];
		mapaBHP = new int[50][50];
		wiezyczka_enem = new int[50][50];
		szoruj_enem();
		
		
		miny = new int[50][50];
		szoruj_miny();
		
		
		Field myk = new Field(null, false, false, false);
		
	
		//LOGGER.info(arg0.getName());
		if( aktualnaPlansza.getName().equals("Emerald") )
		{
			s1x = 7;
			s1y = 2;
			s2x = 10;
			s2y = 15;
		}
		
		if( aktualnaPlansza.getName().equals("Centurion") )
		{
			LOGGER.info("Centurion");
			s1x = 2;
			s1y = 5;
			s2x = 15;
			s2y = 12;
		}
		
		if( aktualnaPlansza.getName().equals("Nebula") )
		{
			LOGGER.info("Nebula");
			s1x = 2;
			s1y = 2;
			s2x = 15;
			s2y = 15;
		}
		
		if( aktualnaPlansza.getName().equals("Oblivion") )
		{
			LOGGER.info("Oblivion");
			s1x = 1;
			s1y = 10;
			s2x = 16;
			s2y = 7;
		}
		
		if( aktualnaPlansza.getName().equals("Solaris") )
		{
			LOGGER.info("Solaris");
			s1x = 4;
			s1y = 5;
			s2x = 13;
			s2y = 12;
		}
		
		
		
		
		
		for (int x = 0; x < 18; x++) 
		{
			for (int y = 0; y < 18; y++) 
			{

				myk = getField(x,y,mapa);
				if( myk.isBlockingMove() == true)
					if( myk.isBlockingShot() == false )
						plansza[x][y] = 2; 
					else
						plansza[x][y] = 1;
				else
					if( myk.isBlockerPossible() == true)
						plansza[x][y] = 3;
					else
						plansza[x][y]=0;
					
			}
		}
		
		
		if( aktualnaPlansza.getName().equals("Centurion") )
		{
			//LOGGER.info("Centurion");
			plansza[2][3]=1;
			plansza[17-2][17-3]=1;
		}
		
		
		
		
		
		
	}

	@Override
	public void processRoundFinished(RoundResult arg0) {
		runda = 0;
		LOGGER.info("Koniec rundy");
		szoruj_BHP();
		cycki=0;
		cycki_x=0;
		cycki_y=0;
		ile_w_miejscu=0;
		cycuszki = 0;
		szoruj_miny();
		skanowawszy = 0;
		szoruj_enem();
		// TODO Auto-generated method stub
		
	}

}
