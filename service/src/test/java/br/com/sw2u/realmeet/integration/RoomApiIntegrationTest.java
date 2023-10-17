package br.com.sw2u.realmeet.integration;

import br.com.sw2u.realmeet.api.facade.RoomApi;
import br.com.sw2u.realmeet.api.model.CreateRoomDTO;
import br.com.sw2u.realmeet.api.model.RoomDTO;
import br.com.sw2u.realmeet.api.model.UpdateRoomDTO;
import br.com.sw2u.realmeet.core.BaseIntegrationTest;
import br.com.sw2u.realmeet.domain.entity.Room;
import br.com.sw2u.realmeet.domain.repository.RoomRepository;
import br.com.sw2u.realmeet.util.TestDataCreator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import static br.com.sw2u.realmeet.util.TestConstants.DEFAULT_ROOM_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    void testDeleteRoomSuccess() {
        Long roomId = roomRepository.saveAndFlush(TestDataCreator.newRoomBuilder().build()).getId();

        roomApi.deleteRoom(roomId);

        assertFalse(roomRepository.findById(roomId).orElseThrow().isActive());
    }

    @Test
    void testDeleteRoomDoesntExist() {
        assertThrows(HttpClientErrorException.NotFound.class, () -> roomApi.deleteRoom(100L));
    }

    @Test
    void testUpdateRoomSuccess() {
        Room room = roomRepository.saveAndFlush(TestDataCreator.newRoomBuilder().build());
        UpdateRoomDTO roomUpdated = (UpdateRoomDTO) new UpdateRoomDTO().name(room.getName() + " TEST").seats(room.getSeats() + 1);

        roomApi.updateRoom(room.getId(), roomUpdated);

        Room roomUpdatedOnRepository = roomRepository.findById(room.getId()).orElseThrow();

        assertEquals(roomUpdated.getName(), roomUpdatedOnRepository.getName());
        assertEquals(roomUpdated.getSeats(), roomUpdatedOnRepository.getSeats());
        assertTrue(roomUpdatedOnRepository.isActive());
    }

    @Test
    void testUpdateRoomDoesntExistsFail() {
        assertThrows(
            HttpClientErrorException.NotFound.class,
            () -> roomApi.updateRoom(1L, (UpdateRoomDTO) new UpdateRoomDTO().name("NAME").seats(10))
        );
    }

    @Test
    void testUpdateRoomWithoutNameFail() {
        Room room = roomRepository.saveAndFlush(TestDataCreator.newRoomBuilder().build());
        assertThrows(
                HttpClientErrorException.UnprocessableEntity.class,
                () -> roomApi.updateRoom(room.getId(), new UpdateRoomDTO())
        );
    }
}
