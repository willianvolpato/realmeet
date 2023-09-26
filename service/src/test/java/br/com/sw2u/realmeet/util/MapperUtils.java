package br.com.sw2u.realmeet.util;

import br.com.sw2u.realmeet.mapper.RoomMapper;
import org.mapstruct.factory.Mappers;

public class MapperUtils {

    private MapperUtils() {}

    public static RoomMapper roomMapper() {
        return Mappers.getMapper(RoomMapper.class);
    }
}
