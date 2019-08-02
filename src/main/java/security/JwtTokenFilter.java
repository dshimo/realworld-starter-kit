//package security;
//
//import java.io.IOException;
//import java.security.Principal;
//import java.util.Optional;
//
//import javax.ws.rs.WebApplicationException;
//import javax.ws.rs.container.ContainerRequestContext;
//import javax.ws.rs.container.ContainerRequestFilter;
//import javax.ws.rs.core.HttpHeaders;
//import javax.ws.rs.core.Response;
//import javax.ws.rs.core.Response.Status;
//import javax.ws.rs.core.SecurityContext;
//
//public class JwtTokenFilter implements ContainerRequestFilter  {
//
////    @Override
////    public void filter(ContainerRequestContext containerRequest) throws WebApplicationException {
////
////        // Get the authentication passed in HTTP headers parameters
////        String auth = containerRequest.getHeaderString(HttpHeaders.AUTHORIZATION);
////        Optional<String> subject = getTokenString(auth);
////        if (auth != null) {
////            final SecurityContext securityContext = containerRequest.getSecurityContext();
////            containerRequest.setSecurityContext(new SecurityContext() {
////            	
////	        @Override
////	        public Principal getUserPrincipal() {
////	            return new Principal() {
////	                @Override
////	                public String getName() {
////	                    return subject;
////	                }
////	            };
////	        }
////
////	        @Override
////	        public boolean isSecure() {
////	            return uriInfo.getAbsolutePath().toString().startsWith("https");
////	        }
////	        @Override
////	        public String getAuthenticationScheme() {
////	            return "Token-Based-Auth-Scheme";
////	        }
////	    });
////	        }
////    }
//    
//    public static SecurityContext makeSecurityContext (final String name, final String... roles) {
//        return new SecurityContext() {
//            @Override
//            public Principal getUserPrincipal() {
//                return new Principal() {
//                    @Override
//                    public String getName() {
//                        return name;
//                    }
//                };
//            }
//
//            @Override
//            public boolean isUserInRole(String role) {
//                for (String r : roles) { // TODO make a real class with a Set
//                    if (r.equals(role)) {
//                        return true;
//                    }
//                }
//                return false;
//            }
//
//            @Override
//            public String getAuthenticationScheme() { return SecurityContext.BASIC_AUTH; }
//
//            @Override
//            public boolean isSecure() {
//                // Is this happening over a secure channel like HTTPS?
//                // Yes, we already checked in the filter before creating a security context.
//                return true;
//            }
//        };
//    }
//    
//    /* Throw an exception if a user is unauthenticated. requestContext.abortWith()? */
//    private static void unauthenticated (String user) {
//        String message = String.format("Incorrect password for OpenTripPlanner user '%s'", user);
//        throw new WebApplicationException(Response.status(Status.UNAUTHORIZED)
//            .header(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=\"OpenTripPlanner\"")
//            .entity(message)
//            .build());
//    }
//
//    /* Throw an exception if user attempts to do basic auth over an unencrypted connection. */
//    private static void unencrypted () {
//        throw new WebApplicationException(Response.status(Status.UNAUTHORIZED)
//                .header(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=\"OpenTripPlanner\"")
//                .entity("OpenTripPlanner refuses to do basic auth without transport layer security (HTTPS).")
//                .build());
//    }
//    
//    private Optional<String> getTokenString(String header) {
//        if (header == null) {
//            return Optional.empty();
//        } else {
//            String[] split = header.split(" ");
//            if (split.length < 2) {
//                return Optional.empty();
//            } else {
//                return Optional.ofNullable(split[1]);
//            }
//        }
//    }
//
//
//}
