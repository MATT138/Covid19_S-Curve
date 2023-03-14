public class CountryInfected
{
  private String country;
  private int[] infected;
  public CountryInfected(String s, int[] i)
  {
    country=s;
    infected=i;
  }
  public CountryInfected(int[] i,String s) //Order of variables is reversable
  {
    country=s;
    infected=i;
  }
  public String getName() //Return country name
  {
    return country;
  }
  public void setName(String s)//Rename country name
  {
    country=s;
  }
  public int[] getInfected()//Return number of infected in array of int format
  {
    return infected;
  }
  public void setInfected(int[] i)
  {
    infected=i;
  }
  public static void main(String[] args)
  {
    //Nothing important just testing&debuging
    int[] n = {1,2,3,6,9,13,18,25};
    CountryInfected test = new CountryInfected("TestCountry",n);
    System.out.println(test.getName());
    System.out.println(test.getInfected()[0]);
    System.out.println(test.getInfected()[1]);
    test.setName("NewTest");
    int[] n2= {30,37};
    test.setInfected(n2);
    System.out.println(test.getName());
    System.out.println(test.getInfected()[0]);
    System.out.println(test.getInfected()[1]);
  }
}
