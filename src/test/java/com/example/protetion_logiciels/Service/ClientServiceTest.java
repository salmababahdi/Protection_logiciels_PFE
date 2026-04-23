package com.example.protetion_logiciels.Service;

import com.example.protetion_logiciels.DTO.ClientDTO;
import com.example.protetion_logiciels.Entity.Client;
import com.example.protetion_logiciels.Entity.User;
import com.example.protetion_logiciels.Repository.ClientRepository;
import com.example.protetion_logiciels.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private ClientService clientService;

    private Client client;
    private ClientDTO clientDTO;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId(1);
        client.setCodeClient("CLI-001");
        client.setRaisonSociale("Test Client SAS");
        client.setSiret("123456789000123");
        client.setEmail("contact@testclient.fr");
        client.setTelephone("0123456789");
        client.setAdresse("123 Rue Test");
        client.setVille("Paris");
        client.setStatut("actif");

        clientDTO = new ClientDTO(
                1, "CLI-001", "Test Client SAS",
                "123456789000123", "contact@testclient.fr",
                "0123456789", "123 Rue Test", "Paris", "actif"
        );
    }

    @Test
    void testFindAll() {
        List<Client> clients = Arrays.asList(client);
        when(clientRepository.findAll()).thenReturn(clients);

        List<ClientDTO> result = clientService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Client SAS", result.get(0).getRaisonSociale());
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(clientRepository.findById(1)).thenReturn(Optional.of(client));

        ClientDTO result = clientService.findById(1);

        assertNotNull(result);
        assertEquals("Test Client SAS", result.getRaisonSociale());
        assertEquals("CLI-001", result.getCodeClient());
        verify(clientRepository, times(1)).findById(1);
    }

    @Test
    void testFindById_NotFound() {
        when(clientRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> clientService.findById(999));
        verify(clientRepository, times(1)).findById(999);
    }

    @Test
    void testFindByStatut() {
        List<Client> clients = Arrays.asList(client);
        when(clientRepository.findByStatut("actif")).thenReturn(clients);

        List<ClientDTO> result = clientService.findByStatut("actif");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("actif", result.get(0).getStatut());
        verify(clientRepository, times(1)).findByStatut("actif");
    }

    @Test
    void testCreate() {
        when(clientRepository.save(any(Client.class))).thenReturn(client);
        when(userRepository.findAll()).thenReturn(Arrays.asList());

        ClientDTO result = clientService.create(clientDTO);

        assertNotNull(result);
        assertEquals("Test Client SAS", result.getRaisonSociale());
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    void testCreate_WithNotifications() {
        User user = new User();
        user.setId(1);
        user.setEmail("admin@test.com");

        when(clientRepository.save(any(Client.class))).thenReturn(client);
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));

        ClientDTO result = clientService.create(clientDTO);

        assertNotNull(result);
        verify(notificationService, times(1)).createNewClientNotification(eq(1), eq("Test Client SAS"));
    }

    @Test
    void testUpdate() {
        when(clientRepository.findById(1)).thenReturn(Optional.of(client));
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        ClientDTO updatedDTO = new ClientDTO(
                1, "CLI-001", "Updated Client SAS",
                "123456789000123", "updated@testclient.fr",
                "0123456789", "456 New Street", "Lyon", "actif"
        );

        ClientDTO result = clientService.update(1, updatedDTO);

        assertNotNull(result);
        assertEquals("Updated Client SAS", result.getRaisonSociale());
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    void testUpdate_NotFound() {
        when(clientRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> clientService.update(999, clientDTO));
        verify(clientRepository, never()).save(any());
    }

    @Test
    void testDelete() {
        when(clientRepository.existsById(1)).thenReturn(true);
        doNothing().when(clientRepository).deleteById(1);

        clientService.delete(1);

        verify(clientRepository, times(1)).deleteById(1);
    }

    @Test
    void testDelete_NotFound() {
        when(clientRepository.existsById(999)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> clientService.delete(999));
        verify(clientRepository, never()).deleteById(any());
    }

    @Test
    void testCreate_NullStatut() {
        ClientDTO dtoWithoutStatut = new ClientDTO(
                null, "CLI-002", "New Client",
                null, null, null, null, null, null
        );

        when(clientRepository.save(any(Client.class))).thenAnswer(invocation -> {
            Client saved = invocation.getArgument(0);
            saved.setId(2);
            return saved;
        });
        when(userRepository.findAll()).thenReturn(Arrays.asList());

        ClientDTO result = clientService.create(dtoWithoutStatut);

        assertNotNull(result);
        verify(clientRepository, times(1)).save(any(Client.class));
    }
}
