package com.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.admin.AdminDBConnection;
import com.entity.Movie;
import com.security.SpringException;

@EnableWebMvc
@Controller

@RequestMapping(value = "admin")
public class MovieAdminController {

	private Connection con;
	
	@RequestMapping(value = "/redirecting", method = RequestMethod.GET)
	public String getUsername(){
		System.out.println("getUsername() starts.");
		System.out.println("getUsername() ends.");
		return "redirect:listMovie";
	}
	@RequestMapping(value = "/listMovie")
	public ModelAndView listMovie(HttpSession session) {
		System.out.println("listMovie() starts.");
		ModelAndView model = new ModelAndView("movieList");
        List<Movie> movies = new ArrayList<Movie>();
		try {
			con = AdminDBConnection.DBC();
			PreparedStatement pstmt = con.prepareStatement("SELECT * FROM movie");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Movie movie = new Movie();
				movie.setId(rs.getInt("id"));
				movie.setName(rs.getString("name"));
				movie.setYear(rs.getInt("year"));
				movie.setCountry(rs.getString("country"));
				movie.setType(rs.getString("type"));
				movie.setRent(rs.getDouble("rent"));
				movie.setRating(rs.getDouble("rating"));
				movies.add(movie);

			}
			con.close();
		} catch (Exception e) {
			System.out.println("Error:" + e);
		}
		model.addObject("movieList", movies);
		System.out.println("listMovie() ends.");
		return model;
	}

	@RequestMapping(value = "/modify", params = "create", method = RequestMethod.POST)
	public String createInput(ModelMap model,HttpSession session) {
		System.out.println("createInput() starts.");
		String sess = session.getId();
		System.out.println(sess);
		System.out.println("createInput() ends.");
		return "movieCreate";
	}

	@RequestMapping(value = "/createMovie", params ="create",  method = RequestMethod.POST)
	@ExceptionHandler({SpringException.class})
	public String createMovie(@RequestParam(value = "name", required = false) String[] name,
			@RequestParam(value = "year", required = false) String[] year,
			@RequestParam(value = "country", required = false) String[] country,
			@RequestParam(value = "type", required = false) String[] type,
			@RequestParam(value = "rent", required = false) String[] rent,
			@RequestParam(value = "rating", required = false) String[] rating,
			HttpSession session,ModelMap model) {
		System.out.println("createMovie() starts.");
		String doubleCheck = "[\\x00-\\x20]*[+-]?(((((\\p{Digit}+)(\\.)?((\\p{Digit}+)?)([eE][+-]?(\\p{Digit}+))?)|(\\.((\\p{Digit}+))([eE][+-]?(\\p{Digit}+))?)|(((0[xX](\\p{XDigit}+)(\\.)?)|(0[xX](\\p{XDigit}+)?(\\.)(\\p{XDigit}+)))[pP][+-]?(\\p{Digit}+)))[fFdD]?))[\\x00-\\x20]*";
		int number = 0;
		String s = null;
		boolean dataError = false;
		boolean databaseError = false;
		Integer[] yearS = new Integer[year.length];
		Double[] rentS = new Double[rent.length];
		Double[] ratingS = new Double[rating.length];
		for (int i = 0; i < name.length; i++) {
			if (year[i] == null || rent[i] == null || rating[i] == null || year[i].matches("^[0-9]*$") == false || 
					rent[i].matches(doubleCheck) == false || rating[i].matches(doubleCheck) == false){
				dataError = true;
				break;
			}else{
			yearS[i] = Integer.parseInt(year[i]);
			rentS[i] = Double.parseDouble(rent[i]);
			ratingS[i] = Double.parseDouble(rating[i]);
			}
			if (name[i] == null || name[i].trim().isEmpty() || country[i] == null || country[i].trim().isEmpty()
					|| type[i] == null || type[i].trim().isEmpty() ) {
				dataError = true;
			}
		}
		if (dataError) {
			model.addAttribute("error", "Input data error,please try again.");
			s = "movieCreate";
		} else {
			List<Movie> creations = new ArrayList<Movie>();
			for (int i = 0; i < name.length; i++) {
				Movie creation = new Movie();
				creation.setName(name[i]);
				creation.setYear(yearS[i]);
				creation.setCountry(country[i]);
				creation.setType(type[i]);
				creation.setRating(rentS[i]);
				creation.setRent(ratingS[i]);
				creations.add(creation);

			}
			try {
				con = AdminDBConnection.DBC();
				PreparedStatement pstmt = con.prepareStatement(
						"insert into movie (name, year, country, type, rent, rating) values(?,?,?,?,?,?)");
				for (Movie c : creations) {
					pstmt.setString(1, c.getName());
					pstmt.setInt(2, c.getYear());
					pstmt.setString(3, c.getCountry());
					pstmt.setString(4, c.getType());
					pstmt.setDouble(5, c.getRent());
					pstmt.setDouble(6, c.getRating());
					int result = pstmt.executeUpdate();
					if (result == 0) {
						databaseError = true;
					} else {
						number += result;
					}

				}
				con.close();
			} catch (Exception e) {
				System.out.println("Error:" + e);
				databaseError = true;
			}
			if (databaseError) {
				s = "movieCreate";
				model.addAttribute("error", "Cannot create new product,please try again or contact support.");
			} else {
				model.addAttribute("number", number);
				model.addAttribute("action", "created");
				s = "modifyResult";
			}
		}
		System.out.println("createMovie() ends.");
		return s;

	}

	@RequestMapping(value = "/createMovie", params ="cancel",  method = RequestMethod.POST)
	public String cancelCreate(){
		System.out.println("cancelCreate() ends.");
		System.out.println("cancelCreate() ends.");
		return "redirect:listMovie";
	}
	@RequestMapping(value = "/modify", params = "update", method = RequestMethod.POST)
	public ModelAndView updateList(@RequestParam(value = "movieId", required = false) List<Integer> id) {
		System.out.println("updateList() starts.");
        boolean dataError = false;
        ModelAndView model = null;
		for (Integer i : id) {
			if(i == null ){
				dataError = true;
			}
		}
		if(dataError){
			model = new ModelAndView("listMovie");
			model.addObject("error", "Please select at least one product.");
		}else{
		    model = new ModelAndView("movieUpdate", "command", new Movie());
		List<Movie> movieList = new ArrayList<Movie>();
		try {
			con = AdminDBConnection.DBC();
			PreparedStatement pstmt = con.prepareStatement("select * from movie where id = ?");
			for (Integer i : id) {
				pstmt.setInt(1, i);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					Movie movie = new Movie();
					movie.setId(rs.getInt("id"));
					movie.setName(rs.getString("name"));
					movie.setYear(rs.getInt("year"));
					movie.setCountry(rs.getString("country"));
					movie.setType(rs.getString("type"));
					movie.setRating(rs.getDouble("rating"));
					movie.setRent(rs.getDouble("rent"));
					movieList.add(movie);

				}
			}
			con.close();
		} catch (Exception e) {
			System.out.println("Error:" + e);
		}
		model.addObject("updateList", movieList);
		}
		System.out.println("updateList() ends.");
		return model;
	}
	
	@RequestMapping(value = "/updateMovie", params = "update", method = RequestMethod.POST)
	public ModelAndView updateMovie(@RequestParam(value = "id", required = false) Integer[] id,
			@RequestParam(value = "year", required = false) String[] year,
			@RequestParam(value = "country", required = false) String[] country,
			@RequestParam(value = "type", required = false) String[] type,
			@RequestParam(value = "rent", required = false) String[] rent,
			@RequestParam(value = "rating", required = false) String[] rating) {
		System.out.println("updateMovie() starts.");
		String doubleCheck = "[\\x00-\\x20]*[+-]?(((((\\p{Digit}+)(\\.)?((\\p{Digit}+)?)([eE][+-]?(\\p{Digit}+))?)|(\\.((\\p{Digit}+))([eE][+-]?(\\p{Digit}+))?)|(((0[xX](\\p{XDigit}+)(\\.)?)|(0[xX](\\p{XDigit}+)?(\\.)(\\p{XDigit}+)))[pP][+-]?(\\p{Digit}+)))[fFdD]?))[\\x00-\\x20]*";
		int number = 0;
		boolean dataError = false;
		boolean databaseError = false;
		ModelAndView model = null;
		List<Movie> movieList = new ArrayList<Movie>();
		Integer[] yearS = new Integer[year.length];
		Double[] rentS = new Double[rent.length];
		Double[] ratingS = new Double[rating.length];
		for (int i = 0; i < id.length; i++) {
			String[] idS = new String[id.length];
			if (id[i] == null || year[i] == null || rent[i] == null || rating[i] == null || year[i].matches("^[0-9]*$") == false || 
					rent[i].matches(doubleCheck) == false || rating[i].matches(doubleCheck) == false){
				dataError = true;
				break;
			}else{
			idS[i] = id[i].toString();
			yearS[i] = Integer.parseInt(year[i]);
			rentS[i] = Double.parseDouble(rent[i]);
			ratingS[i] = Double.parseDouble(rating[i]);
			}
			if ( idS[i].trim().isEmpty() ||  country[i] == null || country[i].trim().isEmpty()
					|| type[i] == null || type[i].trim().isEmpty() ) {
				dataError = true;
			}
		}
		if(dataError){
			model = new ModelAndView("movieUpdate");
			try {
				con = AdminDBConnection.DBC();
				PreparedStatement pstmt = con.prepareStatement("select * from movie where id = ?");
				for (Integer i : id) {
					pstmt.setInt(1, i);
					ResultSet rs = pstmt.executeQuery();
					while (rs.next()) {
						Movie movie = new Movie();
						movie.setId(rs.getInt("id"));
						movie.setName(rs.getString("name"));
						movie.setYear(rs.getInt("year"));
						movie.setCountry(rs.getString("country"));
						movie.setType(rs.getString("type"));
						movie.setRating(rs.getDouble("rating"));
						movie.setRent(rs.getDouble("rent"));
						movieList.add(movie);

					}
				}
			} catch (Exception e) {
				System.out.println("Error:" + e);
			}
			model.addObject("updateList", movieList);
			model.addObject("error", "Input data error,please try again");
		} else {
			List<Movie> updates = new ArrayList<Movie>();
			for (int i = 0; i < year.length; i++) {
				Movie update = new Movie();
				update.setId(id[i]);
				update.setYear(yearS[i]);
				update.setCountry(country[i]);
				update.setType(type[i]);
				update.setRating(rentS[i]);
				update.setRent(ratingS[i]);
				updates.add(update);
				System.out.println(i);
			}
			try {
				PreparedStatement pstmt = con.prepareStatement(
						"Update movie SET year = ?, country = ?, type = ?, rent = ?, rating = ?WHERE id = ?");
				for (Movie u : updates) {
					pstmt.setInt(1, u.getYear());
					pstmt.setString(2, u.getCountry());
					pstmt.setString(3, u.getType());
					pstmt.setDouble(4, u.getRent());
					pstmt.setDouble(5, u.getRating());
					pstmt.setInt(6, u.getId());
					int i = pstmt.executeUpdate();
					if (i == 0) {
						databaseError = true;
					} else {
						number += i;
					}
				}

			} catch (Exception e) {
				System.out.println("Error:" + e);
				databaseError = true;
			}
			if (databaseError) {
				model = new ModelAndView("movieUpdate");
				model.addObject("error", "Cannot update selected record, please try again or contact support.");
				try {
					PreparedStatement pstmt = con.prepareStatement("select * from movie where id = ?");
					for (Integer i : id) {
						pstmt.setInt(1, i);
						ResultSet rs = pstmt.executeQuery();
						while (rs.next()) {
							Movie movie = new Movie();
							movie.setId(rs.getInt("id"));
							movie.setName(rs.getString("name"));
							movie.setYear(rs.getInt("year"));
							movie.setCountry(rs.getString("country"));
							movie.setType(rs.getString("type"));
							movie.setRating(rs.getDouble("rating"));
							movie.setRent(rs.getDouble("rent"));
							movieList.add(movie);

						}
					}
				} catch (Exception e) {
					System.out.println("Error:" + e);
				}
			model.addObject("updateList", movieList);
			} else {
				model = new ModelAndView("modifyResult");
				model.addObject("number", number);
				model.addObject("action", "updated");
			}
		}
		System.out.println("updateMovie() ends.");
		return model;
	}

	@RequestMapping(value = "/updateMovie", params = "cancel", method = RequestMethod.POST)
	public String cancelUpdate() {
		System.out.println("cancelUpdate() starts.");
		System.out.println("cancelUpdate() ends.");
		return "redirect:listMovie";
	}


	@RequestMapping(value = "/modify", params = "delete", method = RequestMethod.POST)
	public ModelAndView deleteList(@RequestParam(value = "movieId", required = false) List<Integer> id) {
		System.out.println("deleteList() starts.");
		 boolean dataError = false;
	        ModelAndView model = null;
			for (Integer i : id) {
				if(i == null ){
					dataError = true;
				}
			}
			if(dataError){
				model = new ModelAndView("listMovie");
				model.addObject("error", "Please select at least one product.");
			}else{
		model = new ModelAndView("movieDelete", "command", new Movie());
		List<Movie> movieList = new ArrayList<Movie>();
		try {
			con = AdminDBConnection.DBC();
			PreparedStatement pstmt = con.prepareStatement("select * from movie where id = ?");
			for (Integer i : id) {
				pstmt.setInt(1, i);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					Movie movie = new Movie();
					movie.setId(rs.getInt("id"));
					movie.setName(rs.getString("name"));
					movie.setYear(rs.getInt("year"));
					movie.setCountry(rs.getString("country"));
					movie.setType(rs.getString("type"));
					movie.setRating(rs.getDouble("rating"));
					movie.setRent(rs.getDouble("rent"));
					movieList.add(movie);

				}
			}
			con.close();
		} catch (Exception e) {
			System.out.println("Error:" + e);
		}
		model.addObject("deteleList", movieList);
			}
		System.out.println("deleteList() ends.");
		return model;
	}
	
	@RequestMapping(value = "/deleteMovie", params = "cancel", method = RequestMethod.POST)
	public String cancelDelete() {
		System.out.println("cancelDelete() starts.");
		System.out.println("cancelDelete() ends.");
		return "redirect:listMovie";
	}
	
	@RequestMapping(value = "/deleteMovie", params = "delete", method = RequestMethod.POST)
	public String deleteMovie(@RequestParam(value = "id", required = false) List<Integer> id,
			ModelMap model) {
		System.out.println("deleteMovie() starts.");
		String s = null;
		boolean databaseError = false;
		int number = 0;
		try {
			con = AdminDBConnection.DBC();
			PreparedStatement pstmt = con.prepareStatement("DELETE FROM movie WHERE id = ?");
			for (Integer i : id) {
				pstmt.setInt(1, i);
				int j = pstmt.executeUpdate();
				if (j == 0) {
					databaseError = true;
				}else{
					number += j;
				}
                con.close();
			}

		} catch (Exception e) {
			System.out.println("Error:" + e);
			databaseError = true;
		}
		if (databaseError) {
			model.addAttribute("error", "Cannot delete selected record, please try again or contact support.");
			s = "movieDelete";
		} else {
			s = "modifyResult";
		model.addAttribute("number", number);
		model.addAttribute("action", "deleted");
		}
		System.out.println("deleteMovie() ends.");
		return s;
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public String logoutRedirect(){
		
		System.out.println("redirect to loginController.");
		return "redirect:/login/logout";
	}

	
}
