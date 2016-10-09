import java.util.*;
import java.io.*;
public class Map{

   	public static void main(String[] args) throws Exception
   	{
   		Scanner sc = new Scanner(new FileReader("input.txt"));

      	Integer m=Integer.parseInt(sc.next()),
      		n=Integer.parseInt(sc.next()),
      		s=Integer.parseInt(sc.next())-1,
      		p=Integer.parseInt(sc.next())-1;

      	Boolean vstd[] = new Boolean[m]; //Visited or not
      	Integer mindist[] = new Integer[m]; // Minimum Distance
     	Integer mtx[][] = new Integer[m][m]; // Matrix to store neighbors
     	Integer path[] = new Integer[m];
     	
     	for(int i=0; i<m; i++){
     		vstd[i]=false;
     		mtx[i][i]=0;
     		path[i] = -1;
     		mindist[i]=2147483647; // Maximum value for Integer
     		for(int j=i+1; j<m; j++){
     			mtx[i][j]=2147483647;
     			mtx[j][i]=2147483647;
     		}
     	}
      	for(int k=0; k<n; k++){
      	Integer vi=Integer.parseInt(sc.next())-1,
      		vj=Integer.parseInt(sc.next())-1;
      	Integer wt= (Integer)Integer.parseInt(sc.next());
      		mtx[vi][vj]=wt;
      		mtx[vj][vi]=wt;

      	}
      	
//      	vstd[s] = true;
      	mindist[s]=0;
      	
      	int c=s;
      	while(c!=p){
      		int nextc=-1;
      		Integer nextcmindist=2147483647;
      		for(int i=0; i<m; i++){
      			if(!vstd[i] && mtx[c][i] != 2147483647){
      				if(mindist[c]+mtx[c][i] < mindist[i])
      				{
      					mindist[i] = mindist[c]+mtx[c][i];
      					path[i] = c;
      				}
      				
      			}
      			if(!vstd[i] && nextcmindist > mindist[i] && i != c){
  					nextc = i;
  					nextcmindist = mindist[i];
  				}
      		}
      		vstd[c] = true;
      		if(nextc >= 0)
  			{
  				c = nextc;
  			}
            else{
            	break;
//	            c=0;
//	            while(c!=p && vstd[c]) c++;
            }
      		nextcmindist = 2147483647;

      	}
      	if(mindist[p] == 2147483647) System.out.println("Path does not exist\n");
      	else
  		{
      		System.out.println("Total Time: "+((Integer)(int)mindist[p]).toString());
      		System.out.print("Path: ");
      		Stack pathStack = new Stack();
      		int k = p;
      		while(k != s)
      		{
      			pathStack.push(k);
      			k = path[k];
      		}
      		System.out.print((s+1) + " ");
      		while(!pathStack.isEmpty())
      		{
      			System.out.print(Integer.toString(((int)pathStack.pop() + 1)) + " ");
      		}
  		}

      	sc.close();
   }
}