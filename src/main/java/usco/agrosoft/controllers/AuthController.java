package usco.agrosoft.controllers;
import usco.agrosoft.utils.JWTUtil;
import usco.agrosoft.models.User;
import usco.agrosoft.dao.UserDao;
import org.springframework.web.bind.annotation.RestController;

import net.minidev.json.JSONObject;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RestController
public class AuthController {
    @Autowired
    private UserDao userDao;
    @Autowired
    private JWTUtil jwtUtil;

    @CrossOrigin
    @RequestMapping(value = "api/login", method = RequestMethod.POST)
    public JSONObject login(@RequestBody User user) throws UnsupportedEncodingException {
        User userLogged = userDao.login(user);
        if (userLogged != null) {
            if(userLogged.isActive()){
                JSONObject obj = new JSONObject();
                obj.put("error", false);
                obj.put("response", jwtUtil.create(String.valueOf(userLogged.getIdUser()), userLogged.getEmail()));
                obj.put("userName", userLogged.getName() + " " + userLogged.getLastName());
                return obj;
            }else{
                JSONObject obj = new JSONObject();
                obj.put("error", true);
                obj.put("response", "Su cuenta no ha sido activada, revise su email");
                return obj;
            } 
        }
        JSONObject obj = new JSONObject();
        obj.put("error", true);
        obj.put("response", "Email o Contraseña Incorrecto");
        return obj;
    }
}