package com.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.admin.AdminDBConnection;
import com.entity.User;
import com.security.NewPasswordHashing;
import com.security.PasswordHashing;

@EnableWebMvc
@Controller
@SessionAttributes("sessionId")
@RequestMapping(value = "login")
public class LoginController {

	private Connection con;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView loginIndex() {
		System.out.println("loginIndex() starts.");
		System.out.println("loginIndex() ends.");
		return new ModelAndView("index", "command", new User());
	}

	@RequestMapping(value = "/attempt", params ="login", method = RequestMethod.POST)
	public String loginValidCheck(@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "password", required = false) String password, HttpSession session,
			HttpServletRequest request,ModelMap model) {
		System.out.println("loginValidCheck() starts.");
		String s = null;
		String salt = "";
		try {
			con = AdminDBConnection.DBC();
			PreparedStatement getSalt = con.prepareStatement("select * from admin where username = ?");
			getSalt.setString(1, username);
			ResultSet rsSalt = getSalt.executeQuery();
			if(rsSalt.next()){
			salt = rsSalt.getString("salt");
			}
			PasswordHashing hashing = new PasswordHashing();
			String hashedPassword = hashing.passwordHash(password, salt);
			System.out.println(hashedPassword);
			PreparedStatement pstmt = con.prepareStatement("select * from admin where username = ? and password = ?");
			pstmt.setString(1, username);
			pstmt.setString(2, hashedPassword);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				System.out.println("User " + username + " Login Successful.");
				PreparedStatement pstmt2 = con.prepareStatement("Update admin SET loginAttempt = ?, loginAttemptTime = now()Where username = ?");
				pstmt2.setInt(1, 0);
				pstmt2.setString(2, username);
				pstmt2.executeUpdate();
				session.invalidate();
				HttpSession newSession = request.getSession();
				String sessionId = newSession.getId();
			    newSession.setAttribute("username", username);
				System.out.println(sessionId);
				
				model.addAttribute("sessionId", sessionId);
				s = "redirect:/admin/redirecting";
			} else {
				PreparedStatement pstmt3 = con.prepareStatement("select * from admin where username = ?");
				pstmt3.setString(1, username);
				ResultSet rs2 = pstmt3.executeQuery();
				if(rs2.next()){
					int loginAttempt = rs2.getInt("loginAttempt");
				int newLoginAttempt = loginAttempt + 1;
				if (newLoginAttempt < 5){
				PreparedStatement pstmt4 = con.prepareStatement("Update admin SET loginAttempt = ?, loginAttemptTime = now()Where username = ?");
				pstmt4.setInt(1, newLoginAttempt);
				pstmt4.setString(2, username);;
				pstmt4.executeUpdate();
				s = "index";
				String error = "Invalid username or password!You can try " + (4 - loginAttempt) + " more times.";
				model.addAttribute("error", error );
				}else if(newLoginAttempt == 5){
					PreparedStatement accessDenied = con.prepareStatement("Update admin SET loginAttempt = ?, loginAttemptTime = now()Where username = ?");
					accessDenied.setInt(1, 5);
					accessDenied.setString(2, username);;
					accessDenied.executeUpdate();
				    s = "index";
					String error = "You can try again after 5 minutes!";
					model.addAttribute("error", error );
				}else{
					LocalDateTime lastAttempt = rs2.getTimestamp("loginAttemptTime").toLocalDateTime();
					LocalDateTime currentAttempt = LocalDateTime.now();
					System.out.println(currentAttempt);
					System.out.println(lastAttempt);
					long time = 5 - lastAttempt.until(currentAttempt, ChronoUnit.MINUTES);
					if(time <= 0){
						PreparedStatement pstmt5 = con.prepareStatement("Update admin SET loginAttempt = ?, loginAttemptTime = now()Where username = ?");
						pstmt5.setInt(1, 1);
						pstmt5.setString(2, username);;
						pstmt5.executeUpdate();
						s = "index";
						String error = "Invalid username or password!You can try 4 more times.";
						model.addAttribute("error", error );
					}else{
						s = "index";
						String error = "You can try again after " + time + " minutes.";
						model.addAttribute("error", error );
					}
				}
				}else{
				s = "index";
					model.addAttribute("error", "Invalid username or password!");
				}
			}

		} catch (Exception e) {
			System.out.println("Error:" + e);
		}
		System.out.println("loginValidCheck() ends.");
		return s;
	}

	@RequestMapping(value = "/attempt", params ="register", method = RequestMethod.POST)
	public ModelAndView registerInput(){
		System.out.println("registerInput() starts.");
		System.out.println("registerInput() ends.");
		return new ModelAndView("register", "command", new User());
	}
	
	@RequestMapping(value = "/register", params ="register", method = RequestMethod.POST)
	public String register(@ModelAttribute("MovieRental") User registerInput,
			@RequestParam (value = "confirmPassword") String confirm, ModelMap model){
		System.out.println("register() starts.");
		String s = null;
		if (registerInput.getUsername() == null || registerInput.getUsername().trim().isEmpty()
			||  registerInput.getPassword() == null || registerInput.getPassword().trim().isEmpty()){
			model.addAttribute("error", "Invalid username or password");
			s = "register";
		}else{
		if (registerInput.getPassword().equals(confirm)){
			try {
				con = AdminDBConnection.DBC();
				PreparedStatement pstmt = con.prepareStatement("select * from admin where username = ?");
				pstmt.setString(1, registerInput.getUsername());
				ResultSet rs = pstmt.executeQuery();
				if(rs.next()){
					model.addAttribute("error", "Input username has been used");
					s = "register";
				}else{
					NewPasswordHashing newHashing = new NewPasswordHashing();
					String salt = newHashing.getSalt();
					String newPassword = newHashing.passwordHash(registerInput.getPassword(), salt);
					PreparedStatement pstmt2 = con.prepareStatement(
							"insert into admin (username, password, salt) values(?,?,?)");
					pstmt2.setString(1, registerInput.getUsername());
					pstmt2.setString(2, newPassword);
					pstmt2.setString(3, salt);
					int result = pstmt2.executeUpdate();
					if(result == 0){
						model.addAttribute("error", "Cannot register,please try again or contact support");
						s ="register";
					}else{
						model.addAttribute("username", registerInput.getUsername());
						s = "registerSuccess";
					}
				}
			}catch (Exception e) {
				System.out.println("Error:" + e);
				model.addAttribute("error", "Cannot register,please try again or contact support");
				s ="register";
			}
		}else{
			model.addAttribute("error", "Passwords do not match!");
			s ="register";
		}
		}
		System.out.println("register() ends.");
	return s;
	}
	@RequestMapping(value = "/register", params ="cancel", method = RequestMethod.POST)
	public String cancelRegister(){
		System.out.println("cancelRegister() starts.");
		System.out.println("cancelRegister() ends.");
		return "redirect:index";
	}
	@RequestMapping(value = "/timeout", method = RequestMethod.GET)
	public ModelAndView timeout(HttpSession session) {
		System.out.println("timeout() starts.");
		ModelAndView model = new ModelAndView("index");
		try {
			con.close();
		} catch (SQLException e) {
			System.out.println("Error:" + e);
		};
		session.invalidate();
		model.addObject("error", "Session expired.Please login again");
		System.out.println("timeout() ends.");
		return model;
	}
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public ModelAndView logout(HttpSession session) {
		System.out.println("logout() starts.");
		ModelAndView model = new ModelAndView("index");
		try {
			con.close();
		} catch (SQLException e) {
			System.out.println("Error:" + e);
		};
		session.invalidate();
		model.addObject("msg", "You've been logged out successfully.");
		System.out.println("logout() ends.");
		return model;
	}

}
