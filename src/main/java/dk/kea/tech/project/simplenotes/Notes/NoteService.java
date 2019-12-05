package dk.kea.tech.project.simplenotes.Notes;

import dk.kea.tech.project.simplenotes.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@Service
public class NoteService {

    @Autowired
    NoteRepository noteRepository;



    public Page<Note> findPaginated(Pageable pageable, User user) {
        List<Note> notes = noteRepository.findAllByUserOrderByCreationTimeDesc(user);
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Note> list;

        if (notes.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, notes.size());
            list = notes.subList(startItem, toIndex);
        }

        Page<Note> notePage
                = new PageImpl<Note>(list, PageRequest.of(currentPage, pageSize), notes.size());

        return notePage;
    }

}
