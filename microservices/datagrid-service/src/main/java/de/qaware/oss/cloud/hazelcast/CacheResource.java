package de.qaware.oss.cloud.hazelcast;

import org.eclipse.microprofile.opentracing.Traced;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.CompleteConfiguration;
import javax.cache.configuration.MutableConfiguration;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Optional;

/**
 * The REST resource to do stuff with the cache.
 */
@Path("/hazelcast")
@Produces(MediaType.APPLICATION_JSON)
@Traced
public class CacheResource {

    @Inject
    private CacheManager cacheManager;

    @Context
    private UriInfo uriInfo;

    @GET
    @Path("/{name}")
    public Response getCacheByName(@PathParam("name") String name) {
        Cache<Object, Object> cache = cacheManager.getCache(name);
        if (cache == null) {
            throw new NotFoundException("No cache with name " + name);
        } else {
            return Response.noContent().build();
        }
    }

    @PUT
    @Path("/{name}")
    public Response createCacheByName(@PathParam("name") String name) {
        Cache<Object, Object> cache = getOrCreateCache(name);
        URI uri = uriInfo.getBaseUriBuilder()
                .path(CacheResource.class)
                .path(CacheResource.class, "createCacheByName")
                .build(cache.getName());
        return Response.created(uri).build();
    }

    @GET
    @Path("/{name}/{key}")
    public JsonObject getCacheEntry(@PathParam("name") String name, @PathParam("key") String key) {
        Cache<Object, Object> cache = getOrCreateCache(name);

        Object value = Optional.ofNullable(cache.get(key))
                .orElseThrow(() -> new NotFoundException("No cache entry for key " + key));

        return Json.createObjectBuilder()
                .add("key", key)
                .add("value", value.toString()).build();
    }

    @POST
    @Path("/{name}/{key}")
    @Consumes({MediaType.TEXT_PLAIN})
    public Response putCacheEntry(@PathParam("name") String name, @PathParam("key") String key, String value) {
        Cache<Object, Object> cache = getOrCreateCache(name);
        cache.put(key, value);
        return Response.noContent().build();
    }

    private Cache<Object, Object> getOrCreateCache(String name) {
        Cache<Object, Object> cache = cacheManager.getCache(name, Object.class, Object.class);
        if (cache == null) {
            CompleteConfiguration<Object, Object> config = new MutableConfiguration<Object, Object>().setTypes(Object.class, Object.class);
            cache = cacheManager.createCache(name, config);
        }
        return cache;
    }
}
