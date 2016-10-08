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

      	Boolean vstd[] = new Boolean[m];
      	Integer mindist[] = new Integer[m];
     	Integer mtx[][] = new Integer[m][m];
     	for(int i=0; i<m; i++){
     		vstd[i]=false;
     		mtx[i][i]=0;
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
      	mindist[s]=0;
      	int c=s;
      	while(c!=p){
      		int nextc=-1;
      		Integer nextcmindist=2147483647;
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
      		nextcmindist = 2147483647;

      	}
      	if(mindist[p] == 2147483647) System.out.println("Path does not exist\n");
      	else System.out.println("Length of shortest path = "+((Integer)(int)mindist[p]).toString());

      	sc.close();
   }
}