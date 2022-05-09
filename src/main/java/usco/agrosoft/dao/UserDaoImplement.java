package usco.agrosoft.dao;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import it.ozimov.springboot.mail.configuration.EnableEmailTools;
import net.minidev.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import usco.agrosoft.models.User;
import org.springframework.stereotype.Repository;
import usco.agrosoft.utils.TestService;

import java.io.UnsupportedEncodingException;
import javax.persistence.*;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.*;

@EnableEmailTools
@Repository
@Transactional
public class UserDaoImplement implements UserDao {
    String activeLink = "https://agrosoft.herokuapp.com/activate/";
    @PersistenceContext
    EntityManager entityManager;

    @Override
    @Transactional
    public List<User> getUsers() {
        String query = "FROM User u";
        List<User> result = entityManager.createQuery(query).getResultList();
        System.out.println(result.size());
        return result;
    }

    @Autowired
    private TestService testService;

    @Override
    public String register(User user) throws UnsupportedEncodingException {
        String query = "FROM User WHERE email = :email";
        List<User> list = entityManager.createQuery(query)
                .setParameter("email", user.getEmail())
                .getResultList();
        if (list.isEmpty()) {
            entityManager.merge(user);
            String body = "Activa tu cuenta dando click aqui: " + activeLink
                    + user.getTokenUser();
            testService.sendEmail(user.getEmail(), user.getName() + user.getLastName(), "Activa tu cuenta Agrosoft",
                    body);
            return "1";
        }
        return "0";
    }

    @Override
    public User login(User user) throws UnsupportedEncodingException {
        String query = "FROM User WHERE email = :email";
        List<User> list = entityManager.createQuery(query)
                .setParameter("email", user.getEmail())
                .getResultList();
        if (list.isEmpty()) {
            return null;
        }
        if (!list.get(0).isActive()) {
            if(list.get(0).getTokenUser() == null || list.get(0).getTokenUser().equals("")){
                //generar token y reenviar email.
                String token = UUID.randomUUID().toString();
                User userFound = list.get(0);
                userFound.setTokenUser(token);
                entityManager.merge(userFound);
                String body = "Activa tu cuenta dando click aqui: " + activeLink + token;
                testService.sendEmail(user.getEmail(), user.getName() + user.getLastName(), "Activa tu cuenta Agrosoft", body);
            }else{
                //reenviar email con el token
                String body = "Activa tu cuenta dando click aqui: " + activeLink + list.get(0).getTokenUser();
                testService.sendEmail(user.getEmail(), user.getName() + user.getLastName(), "Activa tu cuenta Agrosoft", body);
            }
            
        }
        String passwordHashed = list.get(0).getPassword();

        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        if (argon2.verify(passwordHashed, user.getPassword())) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public String activate(String token) {

        String query = "FROM User WHERE token_user = :token";

        List<User> users = entityManager.createQuery(query).setParameter("token", token)
                .getResultList();

        if (users.isEmpty()) {
            return "There was an error activating the account, please try again";
        }

        User userFound = users.get(0);

        userFound.setActive(true);
        userFound.setTokenUser("");
        entityManager.merge(userFound);

        return "Account activated successfully!";
    }

    @Override
    public String generateToken(JSONObject data) throws UnsupportedEncodingException{
        //2. validate if email exist
        String query = "FROM User WHERE email = :email";
        List<User> list = entityManager.createQuery(query)
                .setParameter("email", data.get("email"))
                .getResultList();
        if(!list.isEmpty()){
            //3. validate if account is activated
            if(list.get(0).isActive()){
                //The account is activated, create token and send email
                User userFound = list.get(0);
                if(list.get(0).getTokenUser() == null || list.get(0).getTokenUser().equals("")){
                    //generar token y reenviar email.
                    String token = UUID.randomUUID().toString();
                    userFound.setTokenUser(token);
                    entityManager.merge(userFound);
                    String body = "Actualiza tu contraseña dando click aqui: "+ data.get("link") + token;
                    testService.sendEmail(userFound.getEmail(), userFound.getName() + userFound.getLastName(), "Cambio de contraseña Agrosoft", body);
                }else{
                    //reenviar email con el token
                    String body = "Actualiza tu contraseña dando click aqui: " + data.get("link") + userFound.getTokenUser();
                    testService.sendEmail(userFound.getEmail(), userFound.getName() + userFound.getLastName(), "Cambio de contraseña Agrosoft", body);
                }
                return userFound.getTokenUser();
            }else{
                //return 1, the account isn't activated and send the activation email
                User userFound = list.get(0);
                if(list.get(0).getTokenUser() == null || list.get(0).getTokenUser().equals("")){
                    //generar token y reenviar email.
                    String token = UUID.randomUUID().toString();
                    userFound.setTokenUser(token);
                    entityManager.merge(userFound);
                    String body = "Activa tu cuenta dando click aqui: " + activeLink + token;
                    testService.sendEmail(userFound.getEmail(), userFound.getName() + userFound.getLastName(), "Activa tu cuenta Agrosoft", body);
                }else{
                    //reenviar email con el token
                    String body = "Activa tu cuenta dando click aqui: " + activeLink + list.get(0).getTokenUser();
                    testService.sendEmail(userFound.getEmail(), userFound.getName() + userFound.getLastName(), "Activa tu cuenta Agrosoft", body);
                }
                return "1";
            }
        }else{
            //return 0, the email is not registered
            return "0";
        }
    }

    @Override
    public String changePassword (JSONObject data){
        //Validate if token exist
        String query = "FROM User WHERE token_user = :tokenUser";
        List<User> list = entityManager.createQuery(query)
                .setParameter("tokenUser", data.get("tokenUser"))
                .getResultList();
        if(!list.isEmpty()){
            User userFound = list.get(0);
            String hash = (String) data.get("hash");
            userFound.setPassword(hash);
            userFound.setTokenUser("");
            entityManager.merge(userFound);
            return "0";
        }else{
            return "1";
        }
        
    }
}
