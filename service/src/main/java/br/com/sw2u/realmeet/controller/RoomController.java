package br.com.sw2u.realmeet.controller;

import br.com.sw2u.realmeet.api.facade.RoomsApi;
import br.com.sw2u.realmeet.api.model.CreateRoomDTO;
import br.com.sw2u.realmeet.api.model.RoomDTO;
import br.com.sw2u.realmeet.api.model.UpdateRoomDTO;
import br.com.sw2u.realmeet.service.RoomService;
import br.com.sw2u.realmeet.util.ResponseEntityUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoomController implements RoomsApi {
    private final Executor controllerExecutor;

    private final RoomService roomService;

    public RoomController(Executor controllerExecutor, RoomService roomService) {
        this.controllerExecutor = controllerExecutor;
        this.roomService = roomService;
    }

    @Override
    public CompletableFuture<ResponseEntity<RoomDTO>> getRoom(Long id) {
        return CompletableFuture
            .supplyAsync(() -> roomService.getRoom(id), controllerExecutor)
            .thenApply(ResponseEntityUtils::ok);
    }

    @Override
    public CompletableFuture<ResponseEntity<RoomDTO>> createRoom(CreateRoomDTO createRoomDTO) {
        return CompletableFuture
            .supplyAsync(() -> roomService.createRoom(createRoomDTO), controllerExecutor)
            .thenApply(ResponseEntityUtils::created);
    }

    @Override
    public CompletableFuture<ResponseEntity<Void>> deleteRoom(Long id) {
        return CompletableFuture
            .runAsync(() -> roomService.deleteRoom(id), controllerExecutor)
            .thenApply(ResponseEntityUtils::noContent);
    }

    @Override
    public CompletableFuture<ResponseEntity<Void>> updateRoom(Long id, UpdateRoomDTO updateRoomDTO) {
        return CompletableFuture
                .runAsync(() -> roomService.updateRoom(id, updateRoomDTO), controllerExecutor)
                .thenApply(ResponseEntityUtils::noContent);
    }
}
