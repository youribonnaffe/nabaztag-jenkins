import org.eclipse.jetty.server.Server

import groovy.servlet.GroovyServlet
import org.eclipse.jetty.servlet.Holder
import org.eclipse.jetty.servlet.ServletContextHandler.Context
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder

@Grab(group = 'org.eclipse.jetty.aggregate', module = 'jetty-servlet', version = '8.0.4.v20111024')

Server server = new Server(8080);

ServletContextHandler context = new ServletContextHandler(server, '/', ServletContextHandler.SESSIONS);
context.resourceBase = '.'

addRoute context, 'vl/bc.jsp', 'bootcode.groovy'
addRoute context, 'vl/locate.jsp', 'locate.groovy'
addRoute context, 'vl/p4.jsp', 'ping.groovy'

server.start()
println "Starting Jetty, press Ctrl+C to stop."
server.join()

def addRoute(ServletContextHandler context, String path, String script) {
    def holder = new ServletHolder(new GroovyServlet())
    context.addServlet(holder, '/' + path)
    holder.setInitParameter('resource.name.replacement', script)
    holder.setInitParameter('resource.name.regex', path)
}