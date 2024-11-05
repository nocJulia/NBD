//package org.example.repository;
//
//import org.example.model.Budynek;
//import org.example.repository.impl.BudynekRepositoryImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.data.mongodb.repository.MongoRepository;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//class BudynekRepositoryImplTest {
//
//    private MongoRepository<Budynek, String> budynekRepository;
//    private BudynekRepositoryImpl budynekRepositoryImpl;
//
//    @BeforeEach
//    void setUp() {
//        budynekRepository = Mockito.mock(MongoRepository.class);
//        budynekRepositoryImpl = new BudynekRepositoryImpl(budynekRepository);  // Wstrzykujemy mockowane repozytorium
//    }
//
//    @Test
//    void testSave() {
//        Budynek budynek = new Budynek("Budynek A");
//
//        budynekRepositoryImpl.save(budynek);
//
//        verify(budynekRepository).save(budynek);  // Weryfikujemy, że save() został wywołany
//    }
//
//    @Test
//    void testFindById() {
//        Budynek budynek = new Budynek("Budynek A");
//        budynek.setId("1");
//        when(budynekRepository.findById("1")).thenReturn(Optional.of(budynek));
//
//        Budynek foundBudynek = budynekRepositoryImpl.findById("1");
//
//        assertEquals(budynek, foundBudynek);
//    }
//
//    @Test
//    void testFindAll() {
//        List<Budynek> budynki = List.of(new Budynek("Budynek A"), new Budynek("Budynek B"));
//        when(budynekRepository.findAll()).thenReturn(budynki);
//
//        List<Budynek> result = budynekRepositoryImpl.findAll();
//
//        assertEquals(2, result.size());
//        assertEquals(budynki, result);
//    }
//
//    @Test
//    void testDelete() {
//        Budynek budynek = new Budynek("Budynek A");
//        budynek.setId("1");
//
//        budynekRepositoryImpl.delete(budynek);
//
//        verify(budynekRepository).delete(budynek);  // Weryfikujemy, że delete() został wywołany
//    }
//
//    @Test
//    void testDeleteNonExisting() {
//        Budynek budynek = new Budynek("Budynek A");
//        budynek.setId("1");
//
//        when(budynekRepository.findById("1")).thenReturn(Optional.empty());
//
//        budynekRepositoryImpl.delete(budynek);
//
//        verify(budynekRepository, times(1)).delete(budynek);  // Weryfikujemy, że delete() został wywołany, nawet jeśli nie istnieje
//    }
//}
