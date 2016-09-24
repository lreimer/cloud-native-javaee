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
package de.qaware.cloud.nativ.javaee.device.api;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

/**
 * The device resource provides the REST APIs of the device service.
 */
@ApplicationScoped
@Path("/devices")
@Produces(MediaType.APPLICATION_JSON)
public class DeviceResource {

    @Inject
    private DeviceRepository repository;

    @Inject
    private DeviceEventTransmitter transmitter;

    @Resource
    private ManagedExecutorService executor;

    /**
     * Get the list of all known devices.
     *
     * @param asyncResponse all known devices
     */
    @GET
    public void devices(@Suspended final AsyncResponse asyncResponse) {
        executor.execute(() -> asyncResponse.resume(repository.all()));
    }

    /**
     * Called when the device registers a given card.
     *
     * @param deviceId      the device ID
     * @param asyncResponse a successful response or a 404
     */
    @GET
    @Path("/{deviceId}")
    public void device(@PathParam("deviceId") String deviceId,
                       @Suspended final AsyncResponse asyncResponse) {
        executor.execute(() -> {
            Optional<Device> device = repository.byDeviceId(deviceId);

            Response response;
            if (device.isPresent()) {
                response = Response.ok(device.get()).build();
            } else {
                response = Response.status(Response.Status.NOT_FOUND).build();
            }

            asyncResponse.resume(response);
        });
    }

    /**
     * Called when the device registers a given card.
     *
     * @param deviceId      the device ID
     * @param cardId        the scanned card ID
     * @param asyncResponse in success 204 or 404 if not found
     */
    @POST
    @Path("/{deviceId}")
    public void device(@PathParam("deviceId") String deviceId,
                       @QueryParam("cardId") @NotNull String cardId,
                       @Suspended final AsyncResponse asyncResponse) {
        executor.execute(() -> {
            Optional<Device> device = repository.byDeviceId(deviceId);

            Response response;
            if (device.isPresent()) {
                // then send a device event with the cardId and roomNr
                transmitter.fire(device.get().getRoomNr(), cardId);
                response = Response.noContent().build();
            } else {
                response = Response.status(Response.Status.NOT_FOUND).build();
            }

            asyncResponse.resume(response);
        });
    }
}
