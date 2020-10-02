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
