package kim.restful.api.controller;

import kim.restful.api.common.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private static final Map<String, User> userMap = new ConcurrentHashMap<>();

    static {
        IntStream.range(1, 5).forEach(i -> {
            String uuid = UUID.randomUUID().toString();
            User user = new User(uuid, "name" + i, "email%s@test.com".formatted(i));
            userMap.put(uuid, user);
        });
    }

    @GetMapping(path = "")
    public List<User> getAll() {
        return userMap.values().stream().toList();
    }

    @GetMapping(path = "/{uuid}")
    public User getById(@PathVariable String uuid) {
        return userMap.getOrDefault(uuid, null);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "")
    public User createUser(@RequestBody User user) {
        String uuid = UUID.randomUUID().toString();
        user.setUuid(uuid);
        return userMap.putIfAbsent(uuid, user);
    }

    @PutMapping(path = "/{uuid}")
    public User editUser(@RequestBody User user, @PathVariable String uuid) {
        User existUser = userMap.get(uuid);
        if (null == existUser) {
            throw new ResourceNotFoundException("User with uuid: %s not found".formatted(uuid));
        }
        userMap.put(uuid, user);
        return user;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{uuid}")
    public void deleteUser(@PathVariable String uuid) {
        User existUser = userMap.get(uuid);
        if (null == existUser) {
            throw new ResourceNotFoundException("User with uuid: %s not found".formatted(uuid));
        }
        userMap.remove(uuid);
    }

}
