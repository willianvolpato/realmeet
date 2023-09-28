package br.com.sw2u.realmeet.unit;

import static br.com.sw2u.realmeet.util.TestDataCreator.newCreateRoomDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import br.com.sw2u.realmeet.core.BaseUnitTest;
import br.com.sw2u.realmeet.domain.entity.Room;
import br.com.sw2u.realmeet.domain.repository.RoomRepository;
import br.com.sw2u.realmeet.exception.InvalidRequestException;
import br.com.sw2u.realmeet.util.TestConstants;
import br.com.sw2u.realmeet.validation.RoomValidator;
import br.com.sw2u.realmeet.validation.ValidationError;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class RoomValidatorUnitTest extends BaseUnitTest {
    private RoomValidator victim;

    @Mock
    private RoomRepository roomRepository;

    @BeforeEach
    void setupEach() {
        victim = new RoomValidator(roomRepository);
    }

    @Test
    void testValidateWhenRoomIsValid() {
        victim.validate(newCreateRoomDTO());
    }

    @Test
    void testValidateWhenRoomNameHasMoreThanMaxValue() {
        InvalidRequestException exception = assertThrows(
            InvalidRequestException.class,
            () -> victim.validate(newCreateRoomDTO().name("===Test Test Test Test==="))
        );

        assertEquals(1, exception.getValidationErrors().getErrorsCount());
        assertEquals(
            new ValidationError("name", "Exceeded max length on field: name"),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testValidateWhenRoomNameIsNull() {
        InvalidRequestException exception = assertThrows(
            InvalidRequestException.class,
            () -> victim.validate(newCreateRoomDTO().name(null))
        );

        assertEquals(1, exception.getValidationErrors().getErrorsCount());
        assertEquals(
            new ValidationError("name", "Missing value on field: name"),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testValidateWhenRoomSeatsAreNull() {
        InvalidRequestException exception = assertThrows(
            InvalidRequestException.class,
            () -> victim.validate(newCreateRoomDTO().seats(null))
        );

        assertEquals(1, exception.getValidationErrors().getErrorsCount());
        assertEquals(
            new ValidationError("seats", "Missing value on field: seats"),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testValidateWhenRoomSeatsHasLessThanMinValue() {
        InvalidRequestException exception = assertThrows(
            InvalidRequestException.class,
            () -> victim.validate(newCreateRoomDTO().seats(1))
        );

        assertEquals(1, exception.getValidationErrors().getErrorsCount());
        assertEquals(
            new ValidationError("seats", "The field seats has value 1, but the field allows max 50 and min 2"),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testValidateWhenRoomSeatsHasMoreThanMinValue() {
        InvalidRequestException exception = assertThrows(
            InvalidRequestException.class,
            () -> victim.validate(newCreateRoomDTO().seats(51))
        );

        assertEquals(1, exception.getValidationErrors().getErrorsCount());
        assertEquals(
            new ValidationError("seats", "The field seats has value 51, but the field allows max 50 and min 2"),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testValidateWhenRoomNameAlreadyExistsInrepository() {
        when(roomRepository.findByNameAndActive(TestConstants.DEFAULT_ROOM_NAME, true))
            .thenReturn(Optional.of(Room.newBuilder().name(TestConstants.DEFAULT_ROOM_NAME).build()));

        InvalidRequestException exception = assertThrows(
            InvalidRequestException.class,
            () -> victim.validate(newCreateRoomDTO())
        );

        assertEquals(1, exception.getValidationErrors().getErrorsCount());
        assertEquals(
            new ValidationError("name", "Already exists a room with name " + TestConstants.DEFAULT_ROOM_NAME),
            exception.getValidationErrors().getError(0)
        );
    }
}
