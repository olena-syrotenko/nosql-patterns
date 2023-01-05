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
import org.springframework.web.bind.annotation.RequestParam;
import ua.nure.knt.coworking.entity.User;
import ua.nure.knt.coworking.service.UserService;

import java.util.List;

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
	public String registerUser(@ModelAttribute User user) {
		userService.saveUser(user);
		return "redirect:/login";
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
	String updateUser(@PathVariable Integer id, @ModelAttribute User userForm) {
		userService.updateUser(userForm);
		return "redirect:/users";
	}
}
