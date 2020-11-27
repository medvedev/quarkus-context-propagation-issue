package com.github.medvedev;

import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.eclipse.microprofile.context.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;


@Path("/")
@Produces(MediaType.TEXT_PLAIN)
@ApplicationScoped
public class DemoResource {

    private static final Logger log = LoggerFactory.getLogger(DemoResource.class);

    @Inject
    ManagedExecutor managedExecutor;

    @Inject
    ThreadContext threadContext;

    private final Supplier<String> mdcValueSupplier =
            () -> "MDC value:  " + MDC.get("foo") + "\n";

    @GET
    @Path("thread-context")
    public String get() throws ExecutionException, InterruptedException {
        MDC.put("foo", "from-thread-context");
        Supplier<String> ctxSupplier = threadContext.contextualSupplier(mdcValueSupplier);
        return managedExecutor.supplyAsync(ctxSupplier).get();
    }

    @GET
    @Path("uni")
    public Uni<String> getUni() {
        MDC.put("foo", "from-uni");
        return Uni.createFrom().item(mdcValueSupplier);
    }

}
