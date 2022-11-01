package ru.practicum.ewmMain.compilations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmMain.compilations.model.Compilation;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation,Long> {
    List<Compilation> getCompilationsByPinned(boolean pinned, Pageable pageRequest);
}
