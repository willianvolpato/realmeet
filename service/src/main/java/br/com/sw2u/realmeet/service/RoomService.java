package br.com.sw2u.realmeet.service;

import br.com.sw2u.realmeet.api.model.CreateRoomDTO;
import br.com.sw2u.realmeet.api.model.RoomDTO;
import br.com.sw2u.realmeet.api.model.UpdateRoomDTO;
import br.com.sw2u.realmeet.domain.entity.Room;
import br.com.sw2u.realmeet.domain.repository.RoomRepository;
import br.com.sw2u.realmeet.exception.RoomNotFoundException;
import br.com.sw2u.realmeet.mapper.RoomMapper;
import br.com.sw2u.realmeet.validation.RoomValidator;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Objects.requireNonNull;

@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final RoomValidator roomValidator;

    public RoomService(RoomRepository roomRepository, RoomMapper roomMapper, RoomValidator roomValidator) {
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
        this.roomValidator = roomValidator;
    }

    public RoomDTO getRoom(Long id) {
        Room room = getRoomByIdOrThrow(id);

        return roomMapper.fromEntityToDto(room);
    }

    public RoomDTO createRoom(CreateRoomDTO dto) {
        roomValidator.validate(dto);

        Room room = roomMapper.fromDtoToEntity(dto);
        roomRepository.save(room);

        return roomMapper.fromEntityToDto(room);
    }

    @Transactional
    public void deleteRoom(Long id) {
        getRoomByIdOrThrow(id);
        roomRepository.disable(id);
    }

    @Transactional
    public void updateRoom(Long id, UpdateRoomDTO updateRoomDTO) {
        getRoomByIdOrThrow(id);
        roomValidator.validate(id, updateRoomDTO);
        roomRepository.updateRoom(id, updateRoomDTO.getName(), updateRoomDTO.getSeats());
    }

    private Room getRoomByIdOrThrow(Long id) {
        requireNonNull(id);
        return roomRepository
            .findByIdAndActive(id, true)
            .orElseThrow(() -> new RoomNotFoundException("Room " + id + " not found"));
    }
}
