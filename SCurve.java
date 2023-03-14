public class SCurve
{
  public double S;
  public double L;
  public double M;
  public double D;
  public double MSE;
  public int startDate;
  public SCurve()
  {
    S=0;
    L=0;
    M=0;
    D=0;
    startDate=0;
    MSE=Double.MAX_VALUE;
  }
  public void setValue(double w,double x,double y, double z, double a,int b)
  {
    S=w;
    D=x;
    L=y;
    M=z;
    MSE=a;
    startDate=b;
  }
}
