package AccountMng;

import java.util.Scanner;

import API.ApiConnection;
import API.Connection;

public class AccountMng {
	
	ApiConnection apiConnection;
	Connection connection;
	
	
	public AccountMng(){
		
	}
	
	public ApiConnection getApiConnection() {
		boolean successfull = false;
		boolean yes = false;
		boolean no = false;
		Scanner scanner = new Scanner("System.in");	
		
		while(!successfull) {
		System.out.println("Mit standart Account anmelden? (Ja)  (Nein)");
		String input = scanner.nextLine();
		input = input.toLowerCase();
		yes = input.contains("ja");
		no = input.contains("nein");
		if((yes&&no)||(!yes&&!no)) {}
		else {successfull = true;}
		}
		
		if(yes&&!no) {
			connection = new Connection();
			apiConnection = new ApiConnection(connection);
		}else if(no&&!yes){
			System.out.print("Geben sie den ihren Account Token ein:");
			String input = scanner.nextLine();			
			connection = new Connection(input);
			
			connection.getAccountIDs();
			
		}
		return apiConnection;
	}

}
