import java.io.PrintStream;
import java.util.Scanner;

public interface Administrator {
	public abstract void createProduct(PrintStream out, Scanner scanCL);

	public abstract void editProduct(Product p, PrintStream out, Scanner scanCL);

}
