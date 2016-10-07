import java.util.*;
import java.io.*;
class Map{
   
   	public static void main(String []argh) throws Exception 
   	{
   		Scanner sc = new Scanner(new FileReader("map.txt"));
    	
      	Integer m=Integer.parseInt(sc.next()),
      		n=Integer.parseInt(sc.next()),
      		s=Integer.parseInt(sc.next()),
      		p=Integer.parseInt(sc.next());

      	Boolean vstd[] = new Boolean[m];
      	Integer mindist[] = new Integer[m];
     	Integer mtx[][] = new Integer[m][m];
     	for(int i=0; i<m; i++){
     		vstd[i]=false;
     		mtx[i][i]=0;
     		mindist[i]=Integer.MAX_VALUE;
     		for(int j=i+1; j<m; j++){
     			mtx[i][j]=Integer.MAX_VALUE;
     			mtx[j][i]=Integer.MAX_VALUE;
     		}
     	}
      	for(int k=0; k<n; k++){
      	Integer vi=Integer.parseInt(sc.next()),
      		vj=Integer.parseInt(sc.next()),
      		wt=Integer.parseInt(sc.next());
      		mtx[vi][vj]=wt;
      		mtx[vj][vi]=wt;

      	}
      	mindist[s]=0;
      	int c=s;
      	while(c!=p){
      		int nextc=-1; 
      		Integer nextcmindist=Integer.MAX_VALUE;
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
      		c = nextc;
      		nextcmindist = Integer.MAX_VALUE;

      	}
      	if(mindist[p]==Integer.MAX_VALUE) System.out.println("Path does not exist\n");
      	else System.out.println("Length of shortest path = "+mindist[p].toString());

      
   }
}