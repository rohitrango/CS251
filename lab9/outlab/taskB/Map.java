import java.util.*;
import java.io.*;
class Map{

   	public static void main(String[] args) throws Exception
   	{
   		Scanner sc = new Scanner(new FileReader("input.txt"));

      	Integer m=Integer.parseInt(sc.next()),
      		n=Integer.parseInt(sc.next()),
      		s=Integer.parseInt(sc.next())-1,
      		p=Integer.parseInt(sc.next())-1;

      	Boolean vstd[] = new Boolean[m];
      	Double mindist[] = new Double[m];
     	Double mtx[][] = new Double[m][m];
     	for(int i=0; i<m; i++){
     		vstd[i]=false;
     		mtx[i][i]=0.0;
     		mindist[i]=Double.POSITIVE_INFINITY;
     		for(int j=i+1; j<m; j++){
     			mtx[i][j]=Double.POSITIVE_INFINITY;
     			mtx[j][i]=Double.POSITIVE_INFINITY;
     		}
     	}
      	for(int k=0; k<n; k++){
      	Integer vi=Integer.parseInt(sc.next())-1,
      		vj=Integer.parseInt(sc.next())-1;
      	Double wt= (double) Integer.parseInt(sc.next());
      		mtx[vi][vj]=wt;
      		mtx[vj][vi]=wt;

      	}
      	mindist[s]=0.0;
      	int c=s;
      	while(c!=p){
      		int nextc=-1;
      		Double nextcmindist=Double.POSITIVE_INFINITY;
      		for(int i=0; i<m; i++){
      			if(!vstd[i]){
      				if(mindist[c]+mtx[c][i]<mindist[i]) mindist[i] =mindist[c]+mtx[c][i];
      				if(nextcmindist>mindist[i]){
      					nextc = i;
      					nextcmindist = mindist[i];
      				}
      			}
      		}
      		vstd[c] = true;
      		if(nextc>=0) c = nextc;
          else{
            c=0;
            while(c!=p && vstd[c]) c++;
          }
      		nextcmindist = Double.POSITIVE_INFINITY;

      	}
      	if(mindist[p].isInfinite()) System.out.println("Path does not exist\n");
      	else System.out.println("Length of shortest path = "+((Integer)(int)(double)mindist[p]).toString());


   }
}