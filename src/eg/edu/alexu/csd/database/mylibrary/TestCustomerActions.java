package eg.edu.alexu.csd.database.mylibrary;

import eg.edu.alexu.csd.database.mylibrary.models.User;
import eg.edu.alexu.csd.database.mylibrary.utils.Constants;

public class TestCustomerActions {
	
	public static void main(String[] args) {
		try {
			User user = new User();
			user.setUsername("ahmedemasry");
			user.setPassword("1234");
			user.setLastName("Elmasry");
			user.setFirstName("Ahmed");
			user.setEmail("ahmedemasry@gmail.com");
			user.setPhone("01013689959");
			user.setShippingAddress("Alexandria, Egypt");
			user.setAdministration(1);
			MainLibraryFrame frame = new MainLibraryFrame(user);
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
