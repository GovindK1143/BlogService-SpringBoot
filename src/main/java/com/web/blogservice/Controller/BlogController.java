package com.web.blogservice.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.web.blogservice.DTO.BlogDTO;
import com.web.blogservice.Model.Blog;
import com.web.blogservice.Model.User;
import com.web.blogservice.Service.BlogService;
import com.web.blogservice.Service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class BlogController {
	
	@Autowired
	private BlogService service;
	
	@Autowired
    private UserService userService;
	
	@GetMapping("/")
    public String redirectToHomePage() {
        return "redirect:/home";
    }

	@GetMapping("/home")
    public String home(Model model) {
        ResponseEntity<List<Blog>> response = service.latestBlogs();
        if (response.getStatusCode() == HttpStatus.OK) {
            List<Blog> blogs = response.getBody();
            model.addAttribute("blogs", blogs);
        }
        return "home"; // Renders home.html
    }
    
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String processRegistration(@ModelAttribute("user") User user) {
        userService.saveUser(user);
        return "redirect:/";
    }
    
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "login"; // Renders login.html
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute("user") User user, Model model, HttpSession session) {
        User existingUser = userService.findByUsername(user.getUsername());
        if (existingUser != null && existingUser.getPassword().equals(user.getPassword())) {
            // Add user details to session
            session.setAttribute("loggedInUser", existingUser);
            session.setAttribute("loginSuccess", "Login successful"); // Store success message in session
            return "redirect:/blogs/new";
        } else {
            model.addAttribute("error", "Invalid Username or Password");
            return "login";
        }
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/blogs/new")
    public String createBlogForm(HttpSession session, Model model) {
        if (session.getAttribute("loggedInUser") == null) {
            model.addAttribute("loginMessage", "Please login first to create a new blog.");
            return "redirect:/login";
            
        }
        model.addAttribute("blog", new Blog());
        return "create-blog";
    }

    @PostMapping("/blogs")
    public String createBlog(@ModelAttribute Blog blog, Model model) {
        ResponseEntity<Long> response = service.createBlogPost(blog);
        if (response.getStatusCode() == HttpStatus.CREATED) {
            return "redirect:/blogs";
        } else {
            model.addAttribute("error", "Failed to create blog");
            return "create-blog";
        }
    }

    @GetMapping("/blogs/{id}")
    public String getById(@PathVariable("id") long id, Model model) {
        Blog blog = service.getBlogById(id).getBody();
        model.addAttribute("blog", blog);
        return "view-blog";
    }

    @GetMapping("/blogs/{id}/edit")
    public String showEditForm(@PathVariable("id") long id, Model model) {
        Blog blog = service.getBlogById(id).getBody();
        model.addAttribute("blog", blog);
        return "edit-blog";
    }

    @PostMapping("/blogs/{id}/edit")
    public String edit(@PathVariable("id") Long id, @ModelAttribute BlogDTO dto) {
        service.editBlogPost(id, dto);
        return "redirect:/blogs/" + id;
    }

    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable("id") long id) {
        service.deleteBlog(id);
        return "redirect:/blogs";
    }

    @GetMapping("/blogs")
    public String latestBlogs(Model model) {
        ResponseEntity<List<Blog>> response = service.latestBlogs();
        if (response.getStatusCode() == HttpStatus.OK) {
            List<Blog> blogs = response.getBody();
            model.addAttribute("blogs", blogs);
        }
        return "home"; // Ensure you're returning the correct view name
    }

    @GetMapping("/blogs/tags")
    public String getBlogsByTags(@RequestParam List<String> tags, Model model) {
        List<Blog> blogs = service.getBlogsByTags(tags).getBody();
        model.addAttribute("blogs", blogs);
        return "blog-list";
    }

    @GetMapping("/blogs/creator/{creatorId}")
    public String CreatorBlogs(@PathVariable("creatorId") long id, Model model) {
        List<Blog> blogs = service.CreatorBlogs(id).getBody();
        model.addAttribute("blogs", blogs);
        return "blog-list";
    }

    @GetMapping("/blogs/search")
    public String searchBlogs(@RequestParam String query, Model model) {
        List<Blog> blogs = service.searchBlogs(query).getBody();
        model.addAttribute("blogs", blogs);
        return "blog-list";
    }

    @PostMapping("/blogs/{id}/comment")
    public String addComment(@PathVariable("id") Long id, @RequestParam String comment) {
        service.addComment(id, comment);
        return "redirect:/blogs/" + id;
    }

    @GetMapping("/blogs/{id}/comments")
    public String getComments(@PathVariable("id") Long id, Model model) {
        List<String> comments = service.getComments(id).getBody();
        model.addAttribute("comments", comments);
        return "view-comments";
    }    
}
	
