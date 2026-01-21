
# distance-api (WAR)

Servlet-based REST API that returns great-circle distance (Haversine) in kilometers.

## Endpoint
`POST /distance`

### Request (JSON)
```json
{ "lat1": 28.6139, "lon1": 77.2090, "lat2": 19.0760, "lon2": 72.8777 }
```

### Response (JSON)
```json
{ "distance_km": 1141.27 }
```

## Build (Maven)
```bash
mvn -v            # ensure Maven is installed
mvn clean package # produces target/distance-api.war
```

## Deploy (Tomcat)
Copy `target/distance-api.war` to `${TOMCAT_HOME}/webapps/` and start Tomcat.
Then call `http://localhost:8080/distance-api/distance`.

## Build without local Java (GitHub Actions)
1. Push this folder to a new GitHub repo.
2. Enable Actions (public repos are free).
3. The included workflow will build and publish the WAR artifact.

