import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.*;
import java.util.List;

public class math {

	public static BigInteger choose(BigInteger n, BigInteger k) {
		BigInteger one = new BigInteger("1");
		if(k.compareTo(new BigInteger("0")) == 0) {
			return one;
		}
		else {
			return n.multiply( choose( n.subtract(one), k.subtract(one) ) ).divide(k);
		}
	}

	public static BigInteger mChoosen(BigInteger m, BigInteger n) {
		BigInteger one = new BigInteger("1");
		BigInteger start = n.subtract(new BigInteger("5"));
		BigInteger val = new BigInteger("0"), term = choose(m,start);

		for(int i=0;i<=10;i++) {
			val = val.add(term);
			term = term.multiply(m.subtract(start)).divide(start.add(one)); 
			start = start.add(one);
		}

		return val;
	} 

	public static void main(String[] args) {
		// TaskA
		try{

		BigInteger m,n,zero,five;
		zero = new BigInteger("0");
		five = new BigInteger("5");
		Path inpPath = Paths.get("./", "input.txt");
		Charset charset = Charset.forName("ISO-8859-1");

		File fout = new File("output.txt");
		FileOutputStream fos = new FileOutputStream(fout);	 
		OutputStreamWriter writer = new OutputStreamWriter(fos);

			List<String> num = Files.readAllLines(inpPath,charset);		
			int testcases = Integer.parseInt(num.get(0));
			for(int ai = 1; ai <= testcases; ai++) {
				try {

					try {
						m = new BigInteger(num.get(ai).split(" ")[0]);
					}
					catch(NumberFormatException e) {
						writer.write("Invalid value: m cannot be decimal\n");
						continue;
					}

					try {
						n = new BigInteger(num.get(ai).split(" ")[1]);
					}
					catch(NumberFormatException e) {
						writer.write("Invalid value: n cannot be decimal\n");
						continue;
					}

					if(m.compareTo(zero) == -1) {
						throw new ArithmeticException("Invalid value: m cannot be negative\n");
					}
					else if(n.compareTo(zero) == -1) {
						throw new ArithmeticException("Invalid value: n cannot be negative\n");
					}
 					else if(m.compareTo(n.add(five))==-1 || n.compareTo(five) == -1 ){	// m < n+5 or n < 5
						throw new ArithmeticException("Invalid value: n should be between 5 and m-5\n");
					}
					
					writer.write(mChoosen(m,n).toString()+"\n");
				}
				catch(NumberFormatException e) {
					writer.write("Invalid value: Numbers cannot be decimal\n");
				}
				catch(ArithmeticException e) {
					writer.write(e.getMessage());
				}
			}

		writer.close();

		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}		

	}
	
}