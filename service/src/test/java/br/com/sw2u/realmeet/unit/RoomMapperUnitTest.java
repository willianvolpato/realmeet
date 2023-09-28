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
        var entity = TestDataCreator.newRoomBuilder().build();

        var dto = victim.fromEntityToDto(entity);

        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
        assertEquals(entity.getSeats(), dto.getSeats());
    }

    @Test
    void testFromDtoToEntity() {
        var dto = TestDataCreator.newCreateRoomDTO();

        var entity = victim.fromDtoToEntity(dto);

        assertEquals(dto.getName(), entity.getName());
        assertEquals(dto.getSeats(), entity.getSeats());
    }
}
