package br.com.sw2u.realmeet.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import br.com.sw2u.realmeet.core.BaseUnitTest;
import br.com.sw2u.realmeet.mapper.RoomMapper;
import br.com.sw2u.realmeet.util.MapperUtils;
import br.com.sw2u.realmeet.util.TestDataCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoomMapperUnitTest extends BaseUnitTest {
    private RoomMapper victim;

    @BeforeEach
    void beforeEach() {
        victim = MapperUtils.roomMapper();
    }

    @Test
    void testFromEntityToDto() {
        var room = TestDataCreator.newRoomBuilder().build();

        var dto = victim.toDto(room);

        assertEquals(room.getId(), dto.getId());
        assertEquals(room.getName(), dto.getName());
        assertEquals(room.getSeats(), dto.getSeats());
    }
}
