package com.example.authentication.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.authentication.models.Login;
import com.example.authentication.models.User;
import com.example.authentication.services.MainService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class MainController {

    @Autowired
    private MainService serv;

    @GetMapping("/")
	public String index(Model viewModel) {
		viewModel.addAttribute("user", new User());
		viewModel.addAttribute("login", new Login());
		return "loginreg.jsp";
	}
	
	@PostMapping("/registration")
	public String register(@Valid @ModelAttribute("user") User usuario, 
			BindingResult resultado, Model viewModel) {
		if(resultado.hasErrors()) {
			viewModel.addAttribute("login", new Login());
			return "loginreg.jsp";
		}
		
		User usuarioRegistrado = serv.registerUser(usuario, resultado);
		viewModel.addAttribute("login", new Login());
		if(usuarioRegistrado != null) {
			viewModel.addAttribute("succesRegister", "Gracias por registrarte, por favor login"); 	
		}
		return "loginreg.jsp";
	}
	
	@PostMapping("/login")
	public String login(@Valid @ModelAttribute("login") Login loginuser, 
			BindingResult resultado, Model viewModel, HttpSession sesion) {
		if(resultado.hasErrors()) {
			viewModel.addAttribute("user", new User());
			return "loginreg.jsp";
		}
		
		if(serv.authenticateUser(loginuser.getEmail(), 
				loginuser.getPassword(), resultado)) {
			User usuarioLog = serv.findByEmail(loginuser.getEmail());
			sesion.setAttribute("userID",  usuarioLog.getId());
//			System.out.println(sesion.getAttribute("userID") + "atributo ");
			return "redirect:/dashboard";
			
		}else {
			viewModel.addAttribute("user", new User());
			return "loginreg.jsp";
		}
	}
	
	@GetMapping("/dashboard")
	public String welcome(HttpSession sesion, Model viewModel) {
		Long userId = (Long) sesion.getAttribute("userID");
		if(userId == null) {
			return "redirect:/"; 
		}
		User usuario = serv.findUserById(userId);
		viewModel.addAttribute("usuario", usuario);
		return "home.jsp";
	}
	
	@GetMapping("/logout")
	 public String logout(HttpSession session) {
		 session.setAttribute("userID", null);
		 return "redirect:/";
	 }


}
