package com.example.projet.projet.Controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.projet.projet.Model.CategorieEntity;
import com.example.projet.projet.Model.RoleEntity;
import com.example.projet.projet.Model.TechnicienEntity;
import com.example.projet.projet.Model.UserEntity;
import com.example.projet.projet.Service.CategorieService;
import com.example.projet.projet.Service.UserService;
import com.example.projet.projet.Service.technicienService;

@Controller
public class UserController {

    private UserService userService;
    @Autowired
    private CategorieService categorieService;
    @Autowired
    private technicienService servicetech;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/singup")
    public String singup(Model model) {
        UserEntity user = new UserEntity();
        model.addAttribute("user", user);
        CategorieEntity categorie = new CategorieEntity();
        model.addAttribute("categorie", categorie);
        model.addAttribute("allusers", userService.selectAll());
        model.addAttribute("allcategorie", categorieService.findAll());

        return "singup";
    }

    @GetMapping(value = "/")
    public String singin(Model model) {
        UserEntity user = new UserEntity();
        model.addAttribute("user", user);
        return "index";
    }

    @PostMapping("/singup")
    public String saveUser(@ModelAttribute("user") UserEntity user,
            @ModelAttribute("categorie") CategorieEntity categorie, HttpServletRequest request) {
        user.setId(0);
        // System.out.println("post \n" + user);
        // System.out.println("post achraf test  \n" + user.getRole());
        // System.out.println("categorie est " + categorie.getId());
        if (user.getRole().getId() == 3) {
            userService.addUser(user);
            UserEntity userentity = userService.findByName(user.getName());
            TechnicienEntity technicienEntity = new TechnicienEntity();
            technicienEntity.setCategorie(categorie);
            technicienEntity.setUserEntity(userentity);
            technicienEntity.setNote(0);
            servicetech.save(technicienEntity);
            request.getSession().setAttribute("technicien", technicienEntity);
            request.getSession().setAttribute("client", userentity);

        } else {
            // System.out.println("hello user !");
            userService.addUser(user);
            UserEntity userentity = userService.findByName(user.getName());
            request.getSession().setAttribute("client", userentity);
        }
        return "redirect:/services/all";

    }

    @PostMapping("/logOut")
    public String logOut(HttpServletRequest request) {
        request.getSession().removeAttribute("client");
        return "redirect:/";

    }

    @PostMapping("/")
    public String getUser(@ModelAttribute("user") UserEntity user,
            HttpServletRequest request) {
        UserEntity userEntity = userService.findByEmail(user.getEmail());
        if (user.getPassword().equals(userEntity.getPassword())) {
            // System.out.println("yes");
            if (userEntity.getRole().getId() == 3) {
                // System.out.println("yes tech");
                request.getSession().setAttribute("client", userEntity);
                TechnicienEntity technicienEntity = userEntity.getTechnicienEntity();
                request.getSession().setAttribute("technicien", technicienEntity);
            } else {
                request.getSession().setAttribute("client", userEntity);
            }
            return "redirect:/services/all";
        } else {
            // System.out.println("no");
        }
        return "redirect:/";
    }
   

    @GetMapping("/users/{id}")
    @ResponseBody
    public String getUserById(@PathVariable("id") int userId) {
        return userService.getUserById(userId).toString();
    }

    @RequestMapping(path = "/users/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteService(@PathVariable("id") int userId) {
        return "deleted";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public String handleIllegalArgsException(IllegalArgumentException e) {
        return "Error in search :" + e.getMessage();
    }

    @GetMapping("/users/allRole")
    @ResponseBody
    public List<RoleEntity> getAllRole() {
        return userService.getAllRole();
    }

    @GetMapping("/users/allTechnicien")
    @ResponseBody
    public List<TechnicienEntity> getAllTechnicien() {
        return (List<TechnicienEntity>) servicetech.findAll();
    }

}
