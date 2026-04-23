package com.example.protetion_logiciels.Service;

import com.example.protetion_logiciels.DTO.LicenceDTO;
import com.example.protetion_logiciels.Entity.Client;
import com.example.protetion_logiciels.Entity.ContratAssistance;
import com.example.protetion_logiciels.Entity.Licence;
import com.example.protetion_logiciels.Entity.Logiciel;
import com.example.protetion_logiciels.Repository.ClientRepository;
import com.example.protetion_logiciels.Repository.ContratAssistanceRepository;
import com.example.protetion_logiciels.Repository.LicenceRepository;
import com.example.protetion_logiciels.Repository.LogicielRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LicenceServiceTest {

    @Mock
    private LicenceRepository licenceRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private LogicielRepository logicielRepository;

    @Mock
    private ContratAssistanceRepository contratRepository;

    @Mock
    private LicenseNotificationService notificationService;

    @InjectMocks
    private LicenceService licenceService;

    private Client client;
    private Logiciel logiciel;
    private ContratAssistance contrat;
    private Licence licence;
    private LicenceDTO.Request request;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId(1);
        client.setRaisonSociale("Test Client");

        logiciel = new Logiciel();
        logiciel.setId(1);
        logiciel.setNom("Test Logiciel");

        contrat = new ContratAssistance();
        contrat.setId(1);

        licence = new Licence();
        licence.setId(1);
        licence.setClient(client);
        licence.setLogiciel(logiciel);
        licence.setContrat(contrat);
        licence.setCleLicence("TEST-KEY-123");
        licence.setCodeDebridage("DEBRID-456");
        licence.setNbPostesAutorises(5);
        licence.setNbPostesActuels(2);
        licence.setDateDebut(LocalDate.now());
        licence.setDateFin(LocalDate.now().plusYears(1));
        licence.setStatut("actif");

        request = new LicenceDTO.Request();
        request.setClientId(1);
        request.setLogicielId(1);
        request.setContratId(1);
        request.setCleLicence("TEST-KEY-123");
        request.setCodeDebridage("DEBRID-456");
        request.setNbPostesAutorises(5);
        request.setNbPostesActuels(2);
        request.setDateDebut(LocalDate.now());
        request.setDateFin(LocalDate.now().plusYears(1));
        request.setStatut("actif");
    }

    @Test
    void testFindAll() {
        List<Licence> licences = Arrays.asList(licence);
        when(licenceRepository.findAll()).thenReturn(licences);

        List<LicenceDTO.Response> result = licenceService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("TEST-KEY-123", result.get(0).getCleLicence());
        verify(licenceRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(licenceRepository.findById(1)).thenReturn(Optional.of(licence));

        LicenceDTO.Response result = licenceService.findById(1);

        assertNotNull(result);
        assertEquals("TEST-KEY-123", result.getCleLicence());
        assertEquals("Test Client", result.getClientNom());
        assertEquals("Test Logiciel", result.getLogicielNom());
        verify(licenceRepository, times(1)).findById(1);
    }

    @Test
    void testFindById_NotFound() {
        when(licenceRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> licenceService.findById(999));
        verify(licenceRepository, times(1)).findById(999);
    }

    @Test
    void testFindByClientId() {
        List<Licence> licences = Arrays.asList(licence);
        when(licenceRepository.findByClientId(1)).thenReturn(licences);

        List<LicenceDTO.Response> result = licenceService.findByClientId(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getClientId());
        verify(licenceRepository, times(1)).findByClientId(1);
    }

    @Test
    void testFindByLogicielId() {
        List<Licence> licences = Arrays.asList(licence);
        when(licenceRepository.findByLogicielId(1)).thenReturn(licences);

        List<LicenceDTO.Response> result = licenceService.findByLogicielId(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getLogicielId());
        verify(licenceRepository, times(1)).findByLogicielId(1);
    }

    @Test
    void testFindByStatut() {
        List<Licence> licences = Arrays.asList(licence);
        when(licenceRepository.findByStatut("actif")).thenReturn(licences);

        List<LicenceDTO.Response> result = licenceService.findByStatut("actif");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("actif", result.get(0).getStatut());
        verify(licenceRepository, times(1)).findByStatut("actif");
    }

    @Test
    void testCreate() {
        when(clientRepository.findById(1)).thenReturn(Optional.of(client));
        when(logicielRepository.findById(1)).thenReturn(Optional.of(logiciel));
        when(contratRepository.findById(1)).thenReturn(Optional.of(contrat));
        when(licenceRepository.save(any(Licence.class))).thenReturn(licence);

        LicenceDTO.Response result = licenceService.create(request);

        assertNotNull(result);
        assertEquals("TEST-KEY-123", result.getCleLicence());
        verify(licenceRepository, times(1)).save(any(Licence.class));
    }

    @Test
    void testCreate_ClientNotFound() {
        when(clientRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> licenceService.create(request));
        verify(licenceRepository, never()).save(any());
    }

    @Test
    void testCreate_LogicielNotFound() {
        when(clientRepository.findById(1)).thenReturn(Optional.of(client));
        when(logicielRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> licenceService.create(request));
        verify(licenceRepository, never()).save(any());
    }

    @Test
    void testCreate_ContratNotFound() {
        when(clientRepository.findById(1)).thenReturn(Optional.of(client));
        when(logicielRepository.findById(1)).thenReturn(Optional.of(logiciel));
        when(contratRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> licenceService.create(request));
        verify(licenceRepository, never()).save(any());
    }

    @Test
    void testUpdate() {
        when(licenceRepository.findById(1)).thenReturn(Optional.of(licence));
        when(clientRepository.findById(1)).thenReturn(Optional.of(client));
        when(logicielRepository.findById(1)).thenReturn(Optional.of(logiciel));
        when(contratRepository.findById(1)).thenReturn(Optional.of(contrat));
        when(licenceRepository.save(any(Licence.class))).thenReturn(licence);

        LicenceDTO.Response result = licenceService.update(1, request);

        assertNotNull(result);
        verify(notificationService, times(1)).notifyLicenseStatusChange(any(Licence.class), eq("actif"), eq("actif"));
        verify(licenceRepository, times(1)).save(any(Licence.class));
    }

    @Test
    void testUpdate_NotFound() {
        when(licenceRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> licenceService.update(999, request));
        verify(licenceRepository, never()).save(any());
    }

    @Test
    void testActiver() {
        when(licenceRepository.findById(1)).thenReturn(Optional.of(licence));
        when(licenceRepository.save(any(Licence.class))).thenReturn(licence);

        LicenceDTO.Response result = licenceService.activer(1);

        assertNotNull(result);
        assertEquals("actif", licence.getStatut());
        verify(licenceRepository, times(1)).save(licence);
        verify(notificationService, times(1)).notifyLicenseStatusChange(any(Licence.class), anyString(), eq("actif"));
    }

    @Test
    void testDesactiver() {
        when(licenceRepository.findById(1)).thenReturn(Optional.of(licence));
        when(licenceRepository.save(any(Licence.class))).thenReturn(licence);

        LicenceDTO.Response result = licenceService.desactiver(1);

        assertNotNull(result);
        assertEquals("inactif", licence.getStatut());
        verify(licenceRepository, times(1)).save(licence);
        verify(notificationService, times(1)).notifyLicenseStatusChange(any(Licence.class), anyString(), eq("inactif"));
    }

    @Test
    void testSuspendre() {
        when(licenceRepository.findById(1)).thenReturn(Optional.of(licence));
        when(licenceRepository.save(any(Licence.class))).thenReturn(licence);

        LicenceDTO.Response result = licenceService.suspendre(1, "Test motif");

        assertNotNull(result);
        assertEquals("suspendu", licence.getStatut());
        verify(licenceRepository, times(1)).save(licence);
        verify(notificationService, times(1)).notifyLicenseStatusChange(any(Licence.class), anyString(), eq("suspendu"));
    }

    @Test
    void testReactiver() {
        licence.setStatut("suspendu");
        when(licenceRepository.findById(1)).thenReturn(Optional.of(licence));
        when(licenceRepository.save(any(Licence.class))).thenReturn(licence);

        LicenceDTO.Response result = licenceService.reactiver(1);

        assertNotNull(result);
        assertEquals("actif", licence.getStatut());
        verify(licenceRepository, times(1)).save(licence);
        verify(notificationService, times(1)).notifyLicenseStatusChange(any(Licence.class), eq("suspendu"), eq("actif"));
    }

    @Test
    void testDelete() {
        when(licenceRepository.findById(1)).thenReturn(Optional.of(licence));
        doNothing().when(licenceRepository).deleteById(1);

        licenceService.delete(1);

        verify(licenceRepository, times(1)).deleteById(1);
    }

    @Test
    void testDelete_NotFound() {
        when(licenceRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> licenceService.delete(999));
        verify(licenceRepository, never()).deleteById(any());
    }

    @Test
    void testFindByContratId() {
        List<Licence> licences = Arrays.asList(licence);
        when(licenceRepository.findByContratId(1)).thenReturn(licences);

        List<LicenceDTO.Response> result = licenceService.findByContratId(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getContratId());
        verify(licenceRepository, times(1)).findByContratId(1);
    }
}
