package br.com.sw2u.realmeet.util;

import static br.com.sw2u.realmeet.util.TestConstants.DEFAULT_ROOM_ID;
import static br.com.sw2u.realmeet.util.TestConstants.DEFAULT_ROOM_NAME;
import static br.com.sw2u.realmeet.util.TestConstants.DEFAULT_ROOM_SEATS;

import br.com.sw2u.realmeet.api.model.CreateRoomDTO;
import br.com.sw2u.realmeet.domain.entity.Room;

public final class TestDataCreator {

    private TestDataCreator() {}

    public static Room.Builder newRoomBuilder() {
        return Room.newBuilder().id(DEFAULT_ROOM_ID).name(DEFAULT_ROOM_NAME).seats(DEFAULT_ROOM_SEATS);
    }

    public static CreateRoomDTO newCreateRoomDTO() {
        return new CreateRoomDTO().name(DEFAULT_ROOM_NAME).seats(DEFAULT_ROOM_SEATS);
    }
}
