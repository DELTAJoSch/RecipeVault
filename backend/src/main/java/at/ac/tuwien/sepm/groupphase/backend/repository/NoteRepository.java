package at.ac.tuwien.sepm.groupphase.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Note;
import at.ac.tuwien.sepm.groupphase.backend.entity.NoteCompositeKey;

@Repository
public interface NoteRepository extends JpaRepository<Note, NoteCompositeKey> {
}