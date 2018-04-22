import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class NearDuplicates 
{

	public static void main(String[] args) throws FileNotFoundException {
		
		Scanner in = new Scanner(System.in);
		
		String folderName="";
		int numPermutations = 0;
		int bands = 0;
		double thresholdS = 0.0;
		String docName = "";
		
		System.out.print("Name of the folder: ");
		folderName = in.next();
		
		System.out.print("Number of Permutations to be used: ");
		numPermutations = in.nextInt();
		
		System.out.print("Number of Bands to be used: ");
		bands = in.nextInt();
		
		System.out.print("Similarity threshold: ");
		thresholdS = in.nextDouble();
		
		System.out.print("Name of a document from the collection: ");
		docName = in.next();
		
		MinHash minH = new MinHash(folderName,numPermutations);
		
		String[] allDocs = minH.allDocs();
		
		minH.findMinHashMatrix();
	
		int[][] minHashMatrix = minH.getMinHashMatrix();
		
		LSH l = new LSH(minHashMatrix, allDocs, bands);
		
		ArrayList<String> nearDuplicate = l.nearDuplicatesOf(docName);
			
		double sim =0;
		int falsePositiveCount = 0;
		String file1 ="";
			
			
		System.out.println("Documents that are more than s-similar");
		for(int i=0;i<nearDuplicate.size();i++)
		{
			file1 = nearDuplicate.get(i);
				
			sim = minH.approximateJaccard(docName, file1);
			if(sim >  thresholdS) 
				System.out.println(file1);
			else 
				falsePositiveCount++;
		}
			
		System.out.println("Number of false positives: "+ falsePositiveCount);
			
			/*
			String docnames[] = {"space-0.txt","space-100.txt","space-200.txt","space-300.txt",
		             "space-400.txt","space-500.txt","space-600.txt","space-700.txt",
		             "space-800.txt","space-900.txt"};
			
			for(int j=0;j<docnames.length;j++)
			{
				ArrayList<String> nearDuplicate = l.nearDuplicatesOf(docnames[j]);
			
				double sim =0;
				int falsePositiveCount = 0;
				String file1 ="";
			
			
				System.out.println("Documents that are more than s-similar to " + docnames[j]);
				for(int i=0;i<nearDuplicate.size();i++)
				{
					file1 = nearDuplicate.get(i);
				
					sim = minH.approximateJaccard(docnames[j], file1);
					if(sim >  thresholdS) 
						System.out.println(file1);
					else 
						falsePositiveCount++;
				}
			
				System.out.println("Number of false positives to "+ docnames[j]+": "+falsePositiveCount);
			
			}
			*/
				
		
		
	}

}
