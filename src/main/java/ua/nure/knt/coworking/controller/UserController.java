package ua.nure.knt.coworking.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ua.nure.knt.coworking.entity.Role;
import ua.nure.knt.coworking.entity.User;
import ua.nure.knt.coworking.observers.ContentObserver;
import ua.nure.knt.coworking.service.UserService;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {
	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping(value = "/registration")
	public String registerUser(Model model) {
		User userForm = new User();
		model.addAttribute("userForm", userForm);
		return "registrationPage";
	}

	@PostMapping(value = "/registration")
	public String registerUser(@ModelAttribute User user, Model model) {
		userService.saveUser(user, new ContentObserver(model));
		return "messagePage";
	}

	@GetMapping("/users")
	String getAllUsers(Model model, @Param("fullName") String fullName) {
		List<User> users = StringUtils.isBlank(fullName) ? userService.readAllUsers() : userService.readUserByFullName(fullName);
		model.addAttribute("users", users);
		model.addAttribute("fullName", fullName);
		return "usersPage";
	}

	@GetMapping("/user/{id}")
	String getUserById(@PathVariable Integer id, Model model) {
		User userForm = userService.readUserById(id);
		model.addAttribute("userForm", userForm);
		return "userUpdatePage";
	}

	@PostMapping("/user/{id}")
	String updateUser(@PathVariable Integer id, @ModelAttribute User userForm, Model model) {
		userService.updateUser(userForm, new ContentObserver(model));
		return "messagePage";
	}

	@GetMapping(value = "/login")
	public String loginUser(Model model) {
		User userForm = new User();
		model.addAttribute("userForm", userForm);
		return "loginPage";
	}

	@PostMapping(value = "/login")
	public String loginUser(@ModelAttribute User userForm, HttpSession session, Model model) {
		Optional<User> user = Optional.ofNullable(userService.readUserByEmail(userForm.getEmail()));
		if (userForm.getPassword()
				.equals(user.map(User::getPassword)
						.orElse(null))) {
			session.setAttribute("userRole", user.map(User::getRole)
					.map(Role::getName)
					.orElse("No role"));
			model.addAttribute("content", "Success authorization");
		} else {
			model.addAttribute("content", "Fail authorization. Try it again");
		}
		return "messagePage";
	}
}
