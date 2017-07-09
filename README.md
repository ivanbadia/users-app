
Application implemented following these [requirements](requirements.md)

# Design

I have designed the application trying to manage boundaries with com.sun.net.httpserver.HttpServer and having very few places in the code that refer to HttpServer classes. 
In my opinion good software design accommodates change without huge investments of time and this should be kept in mind considering that there are better alternatives to HttpServer.  For this reason, I have defined the interfaces Server, HttpContext, HttpRequest and HttpResponse that are simple interfaces and tailored to meet the needs of the application.
Another thing that I have taken into consideration it's the possible evolution of the application and readability of the code. For this reason, I have created a simple api that let us define the REST api like this:

        put("/api/users/{id}")
                .addAuthority(Role.ADMIN)
                .handler(new UpdateUser(userRepository));
                
With this approach you can immediately see what services are provided by the application, what path parameters they need (parameters are between {}), what authority is required to execute the action and which class is responsible for processing the request. It especially facilitates the evolution of the api, because you have a good overview and it's very easy to add new services.
 
# Generate jar

Running `mvn clean package` the tests are executed and the binaries generated.


# Run server

Running `java -jar users-app-1.0-SNAPSHOT-jar-with-dependencies.jar` the server is started on port 8080.

Once the server is started the application is running on <http://localhost:8080/>

When the application is started the following users are created in order to be able to test the application:

- page1/123 with PAGE_1 role
- page2/123 with PAGE_2 role
- page3/123 with PAGE_3 role
- admin/admin with ADMIN role

# REST api

The REST api supports content negotiation. The supported content types are application/json and application/xml. The Accept header should be used to specify the desired format. 

Here is a sample to get a user with the JSON format:

    GET /users/1 HTTP/1.1
    Accept: application/json
    
    HTTP/1.1 200 OK
    Content-Type: application/json
    
    {
      "id": 1,
      "username": "admin",
      "roles": [
        "ADMIN"
      ]
    }
    
And with XML format:


    GET /users/1 HTTP/1.1
    Accept: application/xml
    
    HTTP/1.1 200 OK
    Content-Type: application/xml
    
    <user>
        <id>1</id>
        <username>admin</username>
        <roles>
            <role>ADMIN</role>
        </roles>
    </user>
    
The REST api provides the following endpoints:
    
    - GET /api/users - Retrieves all the users
    - POST /api/users - Creates a user
    - GET /api/users/{id} - Retrieves the user
    - PUT /api/users/{id} - Updates the user
    - DELETE /api/users/{id} - Deletes the user
