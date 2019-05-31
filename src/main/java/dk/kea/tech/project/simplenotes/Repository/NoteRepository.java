package dk.kea.tech.project.simplenotes.Repository;

import dk.kea.tech.project.simplenotes.Model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Integer> {
}
