package br.com.sw2u.realmeet.integration;

import static br.com.sw2u.realmeet.util.TestConstants.DEFAULT_ROOM_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.com.sw2u.realmeet.api.facade.RoomApi;
import br.com.sw2u.realmeet.api.model.CreateRoomDTO;
import br.com.sw2u.realmeet.api.model.RoomDTO;
import br.com.sw2u.realmeet.core.BaseIntegrationTest;
import br.com.sw2u.realmeet.domain.entity.Room;
import br.com.sw2u.realmeet.domain.repository.RoomRepository;
import br.com.sw2u.realmeet.util.TestDataCreator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

class RoomApiIntegrationTest extends BaseIntegrationTest {
    @Autowired
    private RoomApi roomApi;

    @Autowired
    private RoomRepository roomRepository;

    @Override
    protected void setupEach() throws Exception {
        setLocalhostBasePath(roomApi.getApiClient(), "/v1");
    }

    @Test
    void testGetRoomSuccess() {
        var room = TestDataCreator.newRoomBuilder().build();
        roomRepository.saveAndFlush(room);

        assertTrue(room.isActive());

        var dto = roomApi.getRoom(room.getId());

        assertEquals(room.getId(), dto.getId());
        assertEquals(room.getName(), dto.getName());
        assertEquals(room.getSeats(), dto.getSeats());
    }

    @Test
    void testGetInactiveRoom() {
        var room = TestDataCreator.newRoomBuilder().active(false).build();
        roomRepository.saveAndFlush(room);

        assertFalse(room.isActive());

        assertThrows(HttpClientErrorException.NotFound.class, () -> roomApi.getRoom(room.getId()));
    }

    @Test
    void testGetRoomThatDoesntExist() {
        assertThrows(HttpClientErrorException.NotFound.class, () -> roomApi.getRoom(DEFAULT_ROOM_ID));
    }

    @Test
    void testCreateRoomSuccess() {
        CreateRoomDTO dto = TestDataCreator.newCreateRoomDTO();
        RoomDTO roomDTO = roomApi.createRoom(dto);

        assertEquals(roomDTO.getName(), dto.getName());
        assertEquals(roomDTO.getSeats(), dto.getSeats());
        assertNotNull(roomDTO.getId());

        Room room = roomRepository.findByIdAndActive(roomDTO.getId(), true).orElseThrow();

        assertEquals(roomDTO.getName(), room.getName());
        assertEquals(roomDTO.getSeats(), room.getSeats());
        assertEquals(roomDTO.getId(), room.getId());
    }

    @Test
    void testCreateRoomFail() {
        CreateRoomDTO dto = TestDataCreator.newCreateRoomDTO().name(null);

        assertThrows(HttpClientErrorException.UnprocessableEntity.class, () -> roomApi.createRoom(dto));
    }
}
