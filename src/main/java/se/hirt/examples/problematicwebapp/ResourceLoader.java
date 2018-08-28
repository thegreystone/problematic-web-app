package se.hirt.examples.problematicwebapp;

	import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import se.hirt.examples.problematicwebapp.rest.HelloRest;
	 
	public class ResourceLoader extends Application{
	 
	    @Override
	    public Set<Class<?>> getClasses() {
	        final Set<Class<?>> classes = new HashSet<Class<?>>();
	        
	        // register root resource
	        classes.add(HelloRest.class);
	        return classes;
	}
}
