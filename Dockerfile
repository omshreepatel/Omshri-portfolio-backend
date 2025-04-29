# Use official Tomcat base image
FROM tomcat:9.0

# Remove default webapps to avoid conflicts
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy your WAR file into Tomcat's webapps folder
COPY target/portfolio-backend1-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Expose port 8090 (you can change to 8080 if preferred)
EXPOSE 8090

# Tomcat will automatically start with default CMD
