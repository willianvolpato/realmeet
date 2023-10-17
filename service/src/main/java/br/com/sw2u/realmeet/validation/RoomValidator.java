package br.com.sw2u.realmeet.validation;

import br.com.sw2u.realmeet.api.model.CreateRoomDTO;
import br.com.sw2u.realmeet.api.model.UpdateRoomDTO;
import br.com.sw2u.realmeet.domain.repository.RoomRepository;

import java.util.Objects;

import org.springframework.stereotype.Component;

import static br.com.sw2u.realmeet.validation.ValidationUtils.thrownOnError;
import static br.com.sw2u.realmeet.validation.ValidationUtils.validateMaxLength;
import static br.com.sw2u.realmeet.validation.ValidationUtils.validateRequired;
import static br.com.sw2u.realmeet.validation.ValidationUtils.validateValue;

@Component
public class RoomValidator {
    private final RoomRepository roomRepository;

    public RoomValidator(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public void validate(CreateRoomDTO dto) {
        ValidationErrors validationErrors = new ValidationErrors();

        if (validateRoomName(dto.getName(), validationErrors) && validateRoomSeats(dto.getSeats(), validationErrors)) {
            validateDuplicatedRoom(null, dto.getName(), validationErrors);
        }

        thrownOnError(validationErrors);
    }

    public void validate(Long id, UpdateRoomDTO dto) {
        ValidationErrors validationErrors = new ValidationErrors();

        if (validateRequired("id", id, validationErrors) &&
                validateRoomName(dto.getName(), validationErrors) &&
                validateRoomSeats(dto.getSeats(), validationErrors)) {
            validateDuplicatedRoom(id, dto.getName(), validationErrors);
        }

        thrownOnError(validationErrors);
    }

    private boolean validateRoomName(String roomName, ValidationErrors validationErrors) {
        return (
            validateRequired("name", roomName, validationErrors) &&
            validateMaxLength("name", roomName, 20, validationErrors)
        );
    }

    private boolean validateRoomSeats(Integer roomSeats, ValidationErrors validationErrors) {
        return (
            validateRequired("seats", roomSeats, validationErrors) &&
            validateValue("seats", roomSeats, 50, 2, validationErrors)
        );
    }

    private void validateDuplicatedRoom(Long roomId, String name, ValidationErrors validationErrors) {
        roomRepository
            .findByNameAndActive(name, true)
            .ifPresent(room -> {
                if(Objects.isNull(roomId) || !Objects.equals(room.getId(), roomId))
                    validationErrors.add("name", "Already exists a room with name " + room.getName());
            });
    }
}
