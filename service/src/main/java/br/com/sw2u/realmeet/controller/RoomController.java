package br.com.sw2u.realmeet.controller;

import br.com.sw2u.realmeet.api.facade.RoomsApi;
import br.com.sw2u.realmeet.api.model.RoomDTO;
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
}
