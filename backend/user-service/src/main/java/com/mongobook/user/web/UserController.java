package com.mongobook.user.web;

import com.mongobook.user.domain.AppUser;
import com.mongobook.user.repository.UserRepository;
import jakarta.validation.Valid;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    private static final Log logger = LogFactory.getLog(UserController.class);
    private final UserRepository users;

    public UserController(UserRepository users) {
        this.users = users;
    }

    @GetMapping
    public List<AppUser> list() {
        logger.info("UserController.list called");
        return users.findAll();
    }

    @GetMapping("/{id}")
    public AppUser get(@PathVariable String id) {
        logger.info("UserController.get called with id=" + id);
        return users.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @PostMapping
    public ResponseEntity<AppUser> create(@Valid @RequestBody AppUser user) {
        logger.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        logger.info("UserController.create called for username=" + user.getFullName());
        user.setId(null);
        logger.debug("DEBUGDEBUGDEBUG" + user);
        return ResponseEntity.status(HttpStatus.CREATED).body(users.save(user));
    }

    @PutMapping("/{id}")
    public AppUser update(@PathVariable String id, @Valid @RequestBody AppUser user) {
        logger.info("UserController.update called with id=" + id);
        if (!users.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        user.setId(id);
        return users.save(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        logger.info("UserController.delete called with id=" + id);
        if (!users.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        users.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

