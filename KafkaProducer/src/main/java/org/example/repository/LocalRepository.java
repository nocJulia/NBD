package org.example.repository;

import org.example.model.Local;

import java.util.List;
import java.util.UUID;

public interface LocalRepository {
    Local saveLocal(Local local);
    boolean deleteLocal(UUID id);
    Local findLocalById(UUID id);
    List<Local> findAllLocals();
}
