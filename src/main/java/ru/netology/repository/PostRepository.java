package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

// Stub
@Repository
public class PostRepository {
    private final ConcurrentMap<Long, Post> map = new ConcurrentHashMap<>();
    private final AtomicLong counter = new AtomicLong(0);

    public List<Post> all() {
        return new ArrayList<>(map.values());
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(map.get(id));
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            post.setId(counter.getAndIncrement());
            map.put(post.getId(), post);
        } else if (map.containsKey(post.getId())) {
          map.replace(post.getId(),post);
        } else {
            throw new NotFoundException("dont have post with id");
        }
        return post;
    }

    public void removeById(long id) {
        if (map.containsKey(id)) {
            map.remove(id);
        } else {
            throw new NotFoundException("dont have post with id");
        }
    }
}
