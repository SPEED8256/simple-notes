package dk.kea.tech.project.simplenotes.Notes;

import dk.kea.tech.project.simplenotes.User.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface NoteRepository extends Repository<Note, Integer> {
    void deleteById(int id);
    void save(Note note);
    Note findById(int id);
    List<Note> findAllByUserOrderByCreationTimeDesc(User user);
    Page<Note> findAllByUserOrderByCreationTimeDesc(User user, Pageable pageable);
}
