package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.NoteDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Note;
import at.ac.tuwien.sepm.groupphase.backend.entity.NoteCompositeKey;
import at.ac.tuwien.sepm.groupphase.backend.exception.InternalServerException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UserPermissionException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.NoteRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.NoteService;
import jakarta.transaction.Transactional;

@Service
public class DefaultNoteService implements NoteService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    NoteRepository noteRepository;
    @Autowired
    RecipeRepository recipeRepository;

    public Note findNote(String ownerEmail, Long recipeId, String issuerEmail)
            throws NotFoundException {

        var issuer = this.userRepository.findByEmail(issuerEmail);
        if (issuer == null) {
            throw new InternalServerException("Ausstellender Benutzer konnte nicht in der Datenbank gefunden werden.");
        }

        var user = this.userRepository.findByEmail(ownerEmail);
        if (user == null) {
            throw new NotFoundException("Benutzer nicht gefunden");
        }

        var recipeEntity = this.recipeRepository.findById(recipeId);
        if (recipeEntity.isEmpty()) {
            throw new NotFoundException("Rezept nicht gefunden");
        }

        var recipe = recipeEntity.get();
        if (!issuer.getAdmin() && !issuer.getId().equals(user.getId())) {
            throw new NotFoundException("Notiz nicht gefunden");
        }

        NoteCompositeKey noteCompositeKey = new NoteCompositeKey(user, recipe);

        var recipeOpt = this.noteRepository.findById(noteCompositeKey);
        if (recipeOpt.isEmpty()) {
            return new Note.NoteBuilder()
                .setContent("")
                .build();
        }
        return recipeOpt.get();

    }

    @Transactional
    public void deleteNote(Long recipeId, String email, String issuerEmail)
            throws NotFoundException, UserPermissionException {

        var issuer = this.userRepository.findByEmail(issuerEmail);
        if (issuer == null) {
            throw new InternalServerException("Ausstellender Benutzer konnte nicht in der Datenbank gefunden werden.");
        }

        var user = this.userRepository.findByEmail(email);
        if (user == null) {
            throw new NotFoundException("Benutzer nicht gefunden");
        }

        var recipeOpt = this.recipeRepository.findById(recipeId);
        if (!recipeOpt.isPresent()) {
            throw new NotFoundException("Rezept nicht gefunden");
        }

        var recipe = recipeOpt.get();
        if (!issuer.getAdmin() && !issuer.getId().equals(user.getId())) {
            throw new UserPermissionException("Sie können die Notiz nicht löschen");
        }

        NoteCompositeKey noteCompositeKey = new NoteCompositeKey(user, recipe);
        if (!this.noteRepository.existsById(noteCompositeKey)) {
            throw new NotFoundException("Notiz nicht gefunden");
        }

        this.noteRepository.deleteById(noteCompositeKey);
    }

    @Transactional
    public Note updateOrCreateNote(NoteDto noteDto, String issuerEmail) throws UserPermissionException,
            ValidationException {

        var issuer = this.userRepository.findByEmail(issuerEmail);
        if (issuer == null) {
            throw new InternalServerException("Ausstellender Benutzer konnte nicht in der Datenbank gefunden werden.");
        }

        if (noteDto.getOwnerId() == null) {
            noteDto.setOwnerId(this.userRepository.findByEmail(issuerEmail).getId());
        }

        var userOpt = this.userRepository.findById(noteDto.getOwnerId());
        if (userOpt.isEmpty()) {
            throw new NotFoundException("Benutzer existiert nicht");
        }
        var user = userOpt.get();

        var recipeOpt = this.recipeRepository.findById(noteDto.getRecipeId());
        if (recipeOpt.isEmpty()) {
            throw new NotFoundException("Rezept existiert nicht");
        }
        var recipe = recipeOpt.get();

        if (!issuer.getAdmin() && !issuer.getId().equals(user.getId())) {
            throw new UserPermissionException("Sie können die Notiz nicht aktualisieren oder erstellen");
        }

        if (noteDto.getContent() == null || noteDto.getContent().isBlank() || noteDto.getContent().length() >= 2047) {
            throw new ValidationException("Inhalt zu lang");
        }
        NoteCompositeKey noteCompositeKey = new NoteCompositeKey(user, recipe);
        Note entity;
        if (this.noteRepository.existsById(noteCompositeKey)) {
            entity = this.noteRepository.getReferenceById(noteCompositeKey);
            entity.setContent(noteDto.getContent());
        } else {
            entity = new Note(user, recipe, noteDto.getContent());
        }
        noteRepository.saveAndFlush(entity); // saves the new entity or overwrites the old one
        return entity;

    }

}