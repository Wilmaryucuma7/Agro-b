package usco.agrosoft.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import usco.agrosoft.dao.IdentifierDao;
import usco.agrosoft.models.Identifier;
import usco.agrosoft.models.User;

import java.util.List;

@RestController
@Configuration
public class IdentifierController {
    @Autowired
    private IdentifierDao identifierDao;
    
    @CrossOrigin
    @RequestMapping(value = "api/identifiers", method = RequestMethod.GET)
    public List<Identifier> getIdentifier() {
        return identifierDao.getIdentifier();
    }
}
