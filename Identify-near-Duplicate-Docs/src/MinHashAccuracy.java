import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class MinHashAccuracy implements Runnable
{
	
	static int errorCount=0;
	
	double exactJac = 0.0;
	double approximateJac = 0.0;
	static double epsilon = 0.0;
	static MinHash minH;
	static String[] fileNames;
	
	int fileindex=0;
	
	
	
	public MinHashAccuracy(int fileindex) 
	{
	     this.fileindex = fileindex;
	}
	
	
	@Override
	public void run() 
	{
		for(int j=fileindex+1; j<fileNames.length; j++)
		{
				try 
				{
					exactJac = minH.exactJaccard(fileNames[fileindex], fileNames[j]);
					System.out.println("exactJaccard...File1:" + fileindex + " File2:" +j+"--->"+exactJac);
				} 
				catch (FileNotFoundException e) 
				{
					e.printStackTrace();
				}
				try 
				{
					approximateJac = minH.approximateJaccard(fileNames[fileindex], fileNames[j]);
					System.out.println("approxJaccard...File1:" + fileindex + " File2:" +j+"--->"+approximateJac);	
				} 
				catch (FileNotFoundException e) 
				{
					e.printStackTrace();
				}
				if(Math.abs(exactJac - approximateJac)>= epsilon)
					errorCount++;					
				
		}
	}
		
	
	public static void main(String arg[]) throws FileNotFoundException, InterruptedException
	{
		Scanner in = new Scanner(System.in);
		String folderName="";
		int numPermutations = 0;
		
		
		System.out.print("Name of a folder: ");
		folderName = in.next();
		
		System.out.print("Number of permutations to be used: ");
		numPermutations = in.nextInt();
		
		System.out.print("Error parameter (less than one): ");
		epsilon = in.nextDouble();
		
		minH = new MinHash(folderName,numPermutations);
		
		fileNames = minH.allDocs();
		
		List<MinHashAccuracy> m= new ArrayList<MinHashAccuracy>();
		
		for(int i=0; i<fileNames.length; i++)
			m.add(new MinHashAccuracy(i));
			
	
		Thread forEachFile[] = new Thread[m.size()];
		
		for (int j = 0; j < m.size(); j++) 
		{
			forEachFile[j] = new Thread(m.get(j));
			forEachFile[j].start();
		}
		for (int j = 0; j <  m.size(); j++) 
			forEachFile[j].join();
		
		
		System.out.println("Error Count"+ errorCount);
	}

}
