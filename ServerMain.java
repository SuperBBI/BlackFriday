import java.io.FileNotFoundException;

public abstract class ServerMain {
	private static int LISTENING_PORT;

	public static void main(String[] args) {
		LISTENING_PORT = 9999;

		try {
			ImportDataFromFile.ImportAdminsFromFile();
			System.out.println("Admins loaded successfully!");

			ImportDataFromFile.ImportUsersFromFile();
			System.out.println("Users loaded successfully!");
			
			ImportDataFromFile.ImportProductsFromFile();
			System.out.println("Products loaded successfully!");
			System.out.println();

			Server server = new Server(LISTENING_PORT);

			server.start();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
