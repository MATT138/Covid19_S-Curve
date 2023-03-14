import java.util.*;
import java.io.*;
import java.util.ArrayList;
import java.lang.*;
public class Proj2190101_1_1
{
  public static CountryInfected [] getInfected(String fileName)
  {
    try
    {
      File f = new File(fileName);
      Scanner readFile = new Scanner(f);
      int lines=0;
      int numDate=0;
      String firstLine = readFile.nextLine();
      Scanner readLine = new Scanner(firstLine).useDelimiter(",");
      readLine.next();
      readLine.next();
      readLine.next();
      readLine.next();
      while(readLine.hasNext()) //Count number of days
      {
        readLine.next();
        numDate++;
      }
      String prevs2="";
      int[] sum = new int[numDate];
      CountryInfected[] ret = new CountryInfected[183]; //ret is an array of class CountryInfected that will be returned from the method
      while(readFile.hasNextLine())
      {
        String Line=readFile.nextLine();
        readLine = new Scanner(Line).useDelimiter(",");
        String next="";
        String s1,s2;
        s1=readLine.next();
        s2=readLine.next();
        if(s1.equals("\"Korea")) //',' can cause bug
        {
          s2="South Korea";
          s1=s2;
          readLine.next();
        }
        else if(s1.equals("\"Bonaire"))
        {
          s1+=","+s2;
          s2=readLine.next();
        }
        else if(s1.equals("\"Saint Helena"))
        {
          s1+=","+s2;
          s2=readLine.next();
        }
        if(s2.charAt(0)<'A'||s2.charAt(0)>'Z')
        {
          s2=s1;
        }
        else
          readLine.next();
        if(s2.equals("Taiwan*"))
        {
          s2="Taiwan";
        }
        if(s2.equals("Holy See"))
        {
          s2="Vatican City";
        }
        readLine.next(); //Longtitude
        int[] i = new int[numDate];
        int currentDay=0;
        while(readLine.hasNext())
        {
          i[currentDay]=Integer.parseInt(readLine.next());
          currentDay++;
        }
        if(!prevs2.equals("")&&!s2.equals(prevs2))
        {
          if(sum[currentDay-1]>100)
          {
            ret[lines]=new CountryInfected(prevs2,sum);
            lines++;
          }
          sum = new int[numDate];
        }
        prevs2=s2;
        for(int j=0;j<i.length;j++)
        {
          sum[j]+=i[j];
        }
      }
      ret[lines]=new CountryInfected(prevs2,sum);
      return ret;
    }
    catch(Exception E)
    {
      System.out.println("Error");
      return new CountryInfected[1];
    }
  }
  public static double[] getDoNothingCurve(int[] pastData, int numFutureDays) {
      ArrayList<Double> n = new ArrayList<>();
      double nothing[] = new double[numFutureDays];
      int d = pastData.length;
      for (int i = 0; i < d; i++) {
          n.add((double)pastData[i]);
      }
      for (int j = 0; j < numFutureDays; j++) {
          nothing[j] = n.get(d - 1 + j) *
                  ((((n.get(d - 1 + j)) / n.get(d - 2 + j)) + ((n.get(d - 2 + j)) / n.get(d - 3 + j)) + ((n.get(d - 3 + j)) / n.get(d - 4 + j))) / 3);

          n.add(nothing[j]);
      }
      return nothing;
  }
  public static SCurve getSCurve(int[] pastData,double[] paramLowerBounds,double[] paramUpperBounds)
  {
    SCurve minMSE = new SCurve();
    double LDay,RDay;
    int startDate=(int)paramLowerBounds[0]+1;
    RDay=pastData.length-startDate+1;
    double S=pastData[(int)paramLowerBounds[0]]*0.9;
    for(double D=paramLowerBounds[1];D<=paramUpperBounds[1];D+=2)
    {
      for(double L=paramLowerBounds[2];L<=paramUpperBounds[2];L+=0.001)
      {
        for(double M=paramLowerBounds[3];M<=paramUpperBounds[3];M+=1000)
        {
          double sum=0;
          for(LDay=1;LDay<=RDay;LDay++)
          {
            double e=Math.exp(-L*(LDay-D));
            double y2=S+(M/(1.0+e));
            sum+=Math.pow(y2-pastData[(int)LDay+startDate-2],2);
          }
          double MSE=sum/(pastData.length-startDate+1);
          if (MSE<minMSE.MSE)
            minMSE.setValue(S,D,L,M,MSE,startDate-1);
        }
      }
    }
    return minMSE;
  }
  public static void main(String[] args) throws IOException{
    CountryInfected[] countries=getInfected("time_series_covid19_confirmed_global.csv");
    //France 62, Germany 66, Netherlands 119
    BufferedWriter br = new BufferedWriter(new FileWriter("1_1.csv"));
    int[] France = countries[62].getInfected();
    int[] Germany = countries[66].getInfected();
    int[] Netherlands = countries[119].getInfected();
    double[] FranceDN = getDoNothingCurve(France,90);
    double[] lb={297,120,0.019,5400000};
    double[] ub={297,140,0.023,5600000};
    SCurve FranceScurve = getSCurve(France,lb,ub);
    double[] FranceSPredicted= new double[90];
    double[] GermanySPredicted= new double[90];
    double[] NetherlandsSPredicted= new double[90];
    for(int k=0;k<90;k++)
    {
      double e=Math.exp(-FranceScurve.L*(1+France.length-FranceScurve.startDate+k-FranceScurve.D));
      double y2=FranceScurve.S+(FranceScurve.M/(1.0+e));
      FranceSPredicted[k]=y2;
    }
    double[] GermanyDN = getDoNothingCurve(Germany,90);
    double[] lb2={398,40,0.038,1500000};
    double[] ub2={398,70,0.043,2000000};
    SCurve GermanyScurve = getSCurve(Germany,lb2,ub2);
    for(int k=0;k<90;k++)
    {
      double e=Math.exp(-GermanyScurve.L*(1+Germany.length-GermanyScurve.startDate+k-GermanyScurve.D));
      double y2=GermanyScurve.S+(GermanyScurve.M/(1.0+e));
      GermanySPredicted[k]=y2;
    }
    double[] NetherlandsDN = getDoNothingCurve(Netherlands,90);
    double[] lb3={331,100,0.015,1500000};
    double[] ub3={331,140,0.019,1800000};
    SCurve NetherlandsScurve = getSCurve(Netherlands,lb3,ub3);
    for(int k=0;k<90;k++)
    {
      double e=Math.exp(-NetherlandsScurve.L*(1+Netherlands.length-NetherlandsScurve.startDate+k-NetherlandsScurve.D));
      double y2=NetherlandsScurve.S+(NetherlandsScurve.M/(1.0+e));
      NetherlandsSPredicted[k]=y2;
    }
    String printFile="";
    for(int k=0;k<90;k++)
    {
      printFile+=((int)FranceSPredicted[k])+",";
    }
    br.write(printFile.substring(0,printFile.length()-1));
    br.newLine();
    printFile="";
    for(int k=0;k<90;k++)
    {
      printFile+=((int)FranceDN[k])+",";
    }
    br.write(printFile.substring(0,printFile.length()-1));
    br.newLine();
    printFile="";
    for(int k=0;k<90;k++)
    {
      printFile+=((int)GermanySPredicted[k])+",";
    }
    br.write(printFile.substring(0,printFile.length()-1));
    br.newLine();
    printFile="";
    for(int k=0;k<90;k++)
    {
      printFile+=((int)GermanyDN[k])+",";
    }
    br.write(printFile.substring(0,printFile.length()-1));
    br.newLine();
    printFile="";
    for(int k=0;k<90;k++)
    {
      printFile+=((int)NetherlandsSPredicted[k])+",";
    }
    br.write(printFile.substring(0,printFile.length()-1));
    br.newLine();
    printFile="";
    for(int k=0;k<90;k++)
    {
      printFile+=((int)NetherlandsDN[k])+",";
    }
    br.write(printFile.substring(0,printFile.length()-1));
    br.newLine();
    br.close();
  }
}
