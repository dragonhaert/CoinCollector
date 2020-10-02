import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class CoinCollector extends PApplet {

PVector p;
PVector v;
PVector a;
PVector playerSize;
int jumps;
boolean standing;
final PVector jump = new PVector(0, -7);
PVector[][] platforms;
PVector platSize = new PVector(100, 25);
Coin[] coins;
int score;

public void setup()
{
  width = 800;
  height = 600;
  
  
  playerSize = new PVector(40, 40);
  p = new PVector(150, height - 3 * playerSize.y);
  v = new PVector(0, 0);
  a = new PVector(0, 0.3f);
  standing = false;

  platforms = new PVector[2][6];
  for(int i = 0; i < platforms[1].length; i++)
  {
    platforms[1][i] = platSize;
  }
  platforms[0][0] = new PVector(450, height - 75);
  platforms[0][1] = new PVector(600, height - 150);
  platforms[0][2] = new PVector(350, height - 250);
  platforms[0][3] = new PVector(100, height - 375);
  platforms[0][4] = new PVector(500, height - 500);
  platforms[0][5] = new PVector(100, height - 200);
  
  coins = new Coin[3];
  coins[0] = new Coin(550, height - 530);
  coins[1] = new Coin(150, height - 230);
  coins[2] = new Coin(650, height - 180);
  score = 0;
  
}

public void draw()
{
  background(200, 200, 200);
  
  /***** Collisions with Motion *****/
  a.y = 0.3f; //naturally falls
  standing = false;

  //if above platform, stop falling at platform
  for (PVector plat : platforms[0])
  {
    boolean xBound = (p.x + playerSize.x > plat.x && p.x < plat.x + platforms[1][0].x);
    boolean yBound = (p.y + playerSize.y + v.y > plat.y && p.y + 0.9f*playerSize.y < plat.y); 
    if (xBound && yBound)
    {
      stand();
    }
  }
  
  //Bound to Window
  if ((p.y + playerSize.y + v.y) > height)
  {
    stand();
  }
  
  if (p.x + v.x + playerSize.x > width || p.x + v.x < 0)
  {
    v.x = 0;
    a.x = 0;
  }
  
  //Collect Coins
  for (Coin coin: coins)
  {
    PVector centerP = new PVector(p.x + 0.5f * playerSize.x, p.y + 0.5f * playerSize.y);
    if(!coin.isClaimed() && (centerP).dist(coin.getPos()) < coin.coinSize)
    {
      //coin.claim();
      score++;
      System.out.println("Coins Collected: " + score);
      coin.newPos();
    }
  }
  

  /****** Horizontal Decceleration *****/
  if (v.x > -0.2f && v.x < 0.2f)
  {
    v.x = 0;
    a.x = 0;
  }

  /***** Final Motion Updates *****/
  v.add(a);
  p.add(v);


  /******* Drawings ******/
  //Player
  fill(255, 0, 0);
  stroke(0, 0, 0);
  rect(p.x, p.y, playerSize.x, playerSize.y);

  //Platforms
  for (PVector plat : platforms[0])
  {
    fill(0);
    rect(plat.x, plat.y, platSize.x, platSize.y);
  }
  
  //Coins
  for (Coin coin: coins)
  {
    if (!coin.isClaimed())
    {
      noStroke();
      fill(255,175,0);
      ellipse(coin.getPos().x, coin.getPos().y, coin.coinSize, coin.coinSize);
      fill(255,225,0);
      ellipse(coin.getPos().x, coin.getPos().y, 0.85f * coin.coinSize, 0.85f * coin.coinSize);
    }
  }
  
}

public void keyPressed()
{
  /****** Jumping and Double Jumping *****/
  if (key == 'w' || key == 'W')
  {
    if (jumps == 1)
    {
      jump();
    }
    if (v.y == 0 || jumps == 0)
    {
      jump();
    }
  }

  /**** Horizontal Motion *****/
  if (key == 'a' || key == 'A')
  {
    v.x = -4;
    a.x = 0;
  }
  if (key == 'd' || key =='D')
  {
    v.x = 4;
    a.x = 0;
  }
}

public void keyReleased()
{
  if (key == 'a' || key == 'A')
  {
    if (v.x <= -0.2f)
    {
      a.x = 0.2f;
    }
  }
  
  if (key == 'd' || key == 'D')
  {
    if (v.x >= 0.2f)
    {
      a.x = -0.2f;
    }
  }
}

public void jump()
{
  v.y = jump.y;
  jumps++;
}

public void stand()
{
  a.y = 0;
  v.y = 0;
  jumps = 0;
  standing = true;
}
public class Coin
{
  public final float coinSize = 40;
  private PVector pos;
  private boolean claimed;
  
  public Coin(float x, float y)
  {
    pos = new PVector(x,y);
    claimed = false;
  }
  
  public boolean isClaimed()
  {
    return claimed;
  }
  
  public PVector getPos()
  {
    return pos;
  }
  
  public void claim()
  {
    claimed = true;
  }
  
  public void newPos()
  {
    pos.x = random(coinSize, width - coinSize);
    pos.y = random(coinSize, height - coinSize);
  }
  
}
  public void settings() {  size(800, 600); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "CoinCollector" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
