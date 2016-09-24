/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 QAware GmbH, Munich, Germany
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package de.qaware.cloud.nativ.javaee.room.api;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

/**
 * The rooms resource provides the REST APIs of the room service.
 */
@ApplicationScoped
@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
public class RoomResource {

    @Inject
    private RoomRepository repository;

    @Resource
    private ManagedExecutorService executor;

    @GET
    public void rooms(@Suspended final AsyncResponse asyncResponse) {
        executor.execute(() -> asyncResponse.resume(repository.all()));
    }

    @GET
    @Path("/{roomNr}")
    public void room(@PathParam("roomNr") Integer roomNr,
                     @Suspended final AsyncResponse asyncResponse) {
        executor.execute(() -> {
            Optional<Room> room = repository.byRoomNr(roomNr);

            Response response;
            if (room.isPresent()) {
                response = Response.ok(room.get()).build();
            } else {
                response = Response.status(Response.Status.NOT_FOUND).build();
            }

            asyncResponse.resume(response);
        });
    }
}
