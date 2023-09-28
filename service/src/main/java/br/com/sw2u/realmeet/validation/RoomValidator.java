package br.com.sw2u.realmeet.validation;

import static br.com.sw2u.realmeet.validation.ValidationUtils.thrownOnError;
import static br.com.sw2u.realmeet.validation.ValidationUtils.validateMaxLength;
import static br.com.sw2u.realmeet.validation.ValidationUtils.validateRequired;
import static br.com.sw2u.realmeet.validation.ValidationUtils.validateValue;

import br.com.sw2u.realmeet.api.model.CreateRoomDTO;
import br.com.sw2u.realmeet.domain.repository.RoomRepository;
import org.springframework.stereotype.Component;

@Component
public class RoomValidator {
    private final RoomRepository roomRepository;

    public RoomValidator(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public void validate(CreateRoomDTO dto) {
        ValidationErrors validationErrors = new ValidationErrors();

        if (validateRoomName(dto.getName(), validationErrors) && validateRoomSeats(dto.getSeats(), validationErrors)) {
            validateDuplicatedRoom(dto.getName(), validationErrors);
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

    private void validateDuplicatedRoom(String name, ValidationErrors validationErrors) {
        roomRepository
            .findByNameAndActive(name, true)
            .ifPresent(room -> validationErrors.add("name", "Already exists a room with name " + room.getName()));
    }
}
