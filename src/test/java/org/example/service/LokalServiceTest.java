package org.example.service;

import org.example.model.Lokal;
import org.example.repository.LokalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LokalServiceTest {

    @Mock
    private LokalRepository lokalRepository;

    @InjectMocks
    private LokalService lokalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveLokalCzyDaSie() {
        Lokal lokal = mock(Lokal.class);

        lokalService.saveLokal(lokal);

        verify(lokalRepository).save(lokal);
    }

    @Test
    void saveLokalPustyTest() {
        assertThrows(IllegalArgumentException.class, () -> lokalService.saveLokal(null));
    }

    @Test
    void findLokalByIdCzyDaSie() {
        Long id = 1L;
        Lokal expectedLokal = mock(Lokal.class);
        when(lokalRepository.findById(id)).thenReturn(expectedLokal);

        Lokal result = lokalService.findLokalById(id);

        assertEquals(expectedLokal, result);
    }

    @Test
    void findAllLokaleCzyDaSie() {
        List<Lokal> expectedLokale = Arrays.asList(mock(Lokal.class), mock(Lokal.class));
        when(lokalRepository.findAll()).thenReturn(expectedLokale);

        List<Lokal> result = lokalService.findAllLokale();

        assertEquals(expectedLokale, result);
    }

    @Test
    void deleteLokalCzyDasie() {
        Lokal lokal = mock(Lokal.class);

        lokalService.deleteLokal(lokal);

        verify(lokalRepository).delete(lokal);
    }

    @Test
    void deleteLokalCzyNieDaSie() {
        assertThrows(IllegalArgumentException.class, () -> lokalService.deleteLokal(null));
    }
}