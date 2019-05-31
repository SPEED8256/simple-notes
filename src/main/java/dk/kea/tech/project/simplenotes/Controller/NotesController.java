package dk.kea.tech.project.simplenotes.Controller;

import dk.kea.tech.project.simplenotes.Model.Note;
import dk.kea.tech.project.simplenotes.Repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
public class NotesController {


    @Autowired
    private NoteRepository noteRepository;


    //View note
    @GetMapping("/")
    public String showNotes(Model model) {
        List<Note> allNotes = noteRepository.findAll();

        model.addAttribute("allNotes", allNotes);

        model.addAttribute("note", new Note());

        return "index";
    }

    //Add note
    @PostMapping("/")
    public String saveScreening(@ModelAttribute Note note) {
        note.setContent(note.getContent().replaceAll("\n","<br />"));
        note.setCreationTime(new Date());
        noteRepository.save(note);
        return "redirect:/";
    }

    //Delete note
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") int id) {
        noteRepository.delete(noteRepository.findById(id).get());
        return "redirect:/";
    }


    //Update note
    @GetMapping("/update/{id}")
    public String update(@PathVariable(name = "id") int id, Model model) {
        Note note = noteRepository.findById(id).get();

        note.setContent(note.getContent().replaceAll("<br />", "\n"));
        model.addAttribute("noteToEdit", note);
        return "edit";
    }


    @PostMapping("/update")
    public String saveEditTheater(@ModelAttribute Note note) {

        note.setContent(note.getContent().replaceAll("\n","<br />"));
        note.setCreationTime(new Date());
        noteRepository.save(note);
        return "redirect:/";
    }
}
