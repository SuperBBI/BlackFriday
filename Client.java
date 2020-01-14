import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Client {
	
	public static void main(String[] args) {
		try {
			Socket cl = new Socket("localhost", 9999);
			Scanner scanCL = new Scanner(System.in);
			Scanner scanSV = new Scanner(cl.getInputStream());
			PrintStream printout = new PrintStream(cl.getOutputStream());
			
			Thread t = new Thread() {
				@Override
				public void run() {
					while(cl.isConnected() && !cl.isClosed()) {
						try {
							System.out.println(scanSV.nextLine());
						} catch (NoSuchElementException e) {
							try {
								cl.close();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			};
			
			t.start();
			
			while(cl.isConnected()) {
				try {
					printout.println(scanCL.nextLine());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
