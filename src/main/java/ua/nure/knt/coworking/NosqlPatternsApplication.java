package ua.nure.knt.coworking;

import ua.nure.knt.coworking.dao.DaoFactory;
import ua.nure.knt.coworking.dao.DaoType;
import ua.nure.knt.coworking.entity.User;
import ua.nure.knt.coworking.util.RoleBuilder;
import ua.nure.knt.coworking.util.UserBuilder;

import java.sql.SQLException;

//@SpringBootApplication
//public class NosqlPatternsApplication {
//
//	public static void main(String[] args) {
//		SpringApplication.run(NosqlPatternsApplication.class, args);
//	}
//
//}

public class NosqlPatternsApplication {

	public static void main(String[] args) throws SQLException {
		DaoFactory daoFactory = DaoFactory.getDaoFactory(DaoType.MySQL);
		//		List<User> users = daoFactory.getUserDao()
		//				.readAllUsers();
		//		users.forEach(System.out::println);
		//		System.out.println(daoFactory.getUserDao().readUserById(2));
		//		System.out.println(daoFactory.getUserDao().readUserByEmail("test@gmail.com"));
//		User user = new UserBuilder().setEmail("test3@gmail.com")
//				.setPassword("12345")
//				.setLastName("Black")
//				.setFirstName("Mary")
//				.setPassportId("236841589")
//				.setRole(new RoleBuilder().setName("ROLE_USER").build())
//				.build();
//		System.out.println(daoFactory.getUserDao().createUser(user));
		User user = daoFactory.getUserDao().readUserByEmail("test@gmail.com");
		System.out.println(user);
		user.setFirstName("Alex");
		daoFactory.getUserDao().updateUser(user);
		System.out.println(daoFactory.getUserDao().readUserByEmail("test@gmail.com"));
	}

}
