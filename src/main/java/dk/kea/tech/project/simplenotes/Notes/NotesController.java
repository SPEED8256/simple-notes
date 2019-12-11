package dk.kea.tech.project.simplenotes.Notes;

import dk.kea.tech.project.simplenotes.User.Security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class NotesController {

    @Autowired
    private HibernateSearchService searchservice;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private NoteService noteService;

    @Autowired
    private UserService userService;


    //View note
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String showNotes(Model model, Principal principal, Pageable pageable, @RequestParam("page") Optional<Integer> page,
                            @RequestParam("size") Optional<Integer> size) {
        if(principal==null){
            return "index-anon";}
        else {
            int currentPage = page.orElse(1);
            int pageSize = size.orElse(7);

            Page<Note> notePage = noteService.findPaginated(PageRequest.of(currentPage - 1, pageSize), userService.findByUsername(principal.getName()));
            if (notePage.getContent().isEmpty()){
                model.addAttribute("msg", "No notes found.");
            }else {
                model.addAttribute("msg", "");
            }
            model.addAttribute("notePage", notePage);

            int totalPages = notePage.getTotalPages();
            if (totalPages > 0) {
                List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                        .boxed()
                        .collect(Collectors.toList());
                model.addAttribute("pageNumbers", pageNumbers);
            }
            model.addAttribute("note", new Note());

            return "index";
        }


    }


    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(@RequestParam(value = "search", required = false) String q, Model model, Principal principal) {
        List<Note> searchResults = null;
        System.out.println("test");
        try {
            searchResults = searchservice.fuzzySearch(q, userService.findByUsername(principal.getName()));


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        model.addAttribute("search", searchResults);
        return "search";

    }

    //Add note
    @PostMapping("/")
    public String saveNote(@ModelAttribute Note note, Principal principal) {
        note.setContent(note.getContent().replaceAll("\n","<br />"));
        note.setCreationTime(new Date());
        note.setUser(userService.findByUsername(principal.getName()));
        noteRepository.save(note);
        return "redirect:/";
    }

    //Delete note
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") int id) {
        noteRepository.deleteById(id);
        return "redirect:/";
    }


    //Update note
    @GetMapping("/update/{id}")
    public String update(@PathVariable(name = "id") int id, Model model) {
        Note note = noteRepository.findById(id);

        note.setContent(note.getContent().replaceAll("<br />", "\n"));
        model.addAttribute("noteToEdit", note);
        return "edit";
    }


    @PostMapping("/update")
    public String saveEditTheater(@ModelAttribute Note note, Principal principal) {
        note.setUser(userService.findByUsername(principal.getName()));
        note.setContent(note.getContent().replaceAll("\n","<br />"));
        note.setCreationTime(new Date());
        noteRepository.save(note);
        return "redirect:/";
    }
}
